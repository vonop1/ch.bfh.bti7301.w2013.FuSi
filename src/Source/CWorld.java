package Source;

import java.util.*;

import Util.CDijkstra;
import Util.CGraph;
import Util.CPosition;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1, jaggr2
 * Date: 27.09.13
 * Time: 14:30
 */
public class CWorld {

    private static Integer globalId = 0;
    private Integer lastWalkerId = 0;
    private Integer lastObstacleId = 0;

    private Vector<CWalker> activeWalkers = new Vector<CWalker>();


    private LinkedList<CWalker> finishedWalkers = new LinkedList<CWalker>();

    public static final Random randomGenerator = new Random();

    private static final Integer maxCalculationCount = 10000;
    private Integer worldWidth = 800;
    private Integer worldHeight = 580;

    private CGraph globalGraph = null;

    private Vector<CObstacle> obstacles = new Vector<CObstacle>();
    private CGrid grid = new CGrid(worldWidth, worldHeight);

    private Double greatestHalfWalkerSize = 0.0;

    private Map<CPosition, LinkedList<CWalker>> walkerWaitingQueue = new HashMap<CPosition, LinkedList<CWalker>>();

    public CWorld() {

    }

    public static Integer incrementGlobalId() {
        globalId += 1;
        return globalId;
    }

    public Integer incrementWalkerId() {
        lastWalkerId += 1;
        return lastWalkerId;
    }

    public Integer incrementObstacleId() {
        lastObstacleId += 1;
        return lastObstacleId;
    }

    public LinkedList<CWalker> getFinishedWalkers() {
        return finishedWalkers;
    }

    public Integer getWorldWidth() {
        return worldWidth;
    }

    public Integer getWorldHeight() {
        return worldHeight;
    }

    public CGrid getGrid() {
        return this.grid;
    }

    public Vector<CWalker> getActiveWalkers() {
        return activeWalkers;
    }

    public Vector<CObstacle> getObstacles() {
        return obstacles;
    }

    public void addObstacle(CObstacle obstacle) {
        this.obstacles.add(obstacle);

        //subscribe obstacles in grid
        grid.subscribeObstacle(obstacle);
    }

    private void addWalkerWithoutCollsionCheck(CWalker walkerToAdd) {
        this.activeWalkers.add(walkerToAdd);
        if (walkerToAdd.getHalfWalkerSize() > greatestHalfWalkerSize) {
            greatestHalfWalkerSize = walkerToAdd.getHalfWalkerSize();
        }
    }

    public void addWalker(CPosition source, CPosition destination, Integer count) {

        CWalker walkerToAdd = new CWalker(source, destination, this);

        Boolean positionOccupied = false;
        for (CWalker existingWalker : this.activeWalkers) {
            if (existingWalker.checkCollisionWith(walkerToAdd, false)) {
                positionOccupied = true;
                break;
            }
        }

        if (positionOccupied) {
            addWalkerToWaitingQueue(walkerToAdd);
        } else {
            addWalkerWithoutCollsionCheck(walkerToAdd);
        }

        for(int i = 1; i < count; i++) {
            addWalkerToWaitingQueue(new CWalker(source, destination, this));
        }
    }

    public Double getGreatestHalfWalkerSize() {
        return greatestHalfWalkerSize;
    }

    public int getGridSize() {
        return this.grid.gridSizeC;
    }

    public void addWalkerToWaitingQueue(CWalker walker) {
        for ( Map.Entry<CPosition, LinkedList<CWalker>> entry : this.walkerWaitingQueue.entrySet() ) {
            if(walker.checkCollisionWith(entry.getKey())) {

                LinkedList<CWalker> list = entry.getValue();
                list.add(walker);
                entry.setValue(list);
                return;
            }
        }

        LinkedList<CWalker> newList = new LinkedList<CWalker>();
        newList.add(walker);
        this.walkerWaitingQueue.put(walker.getPosition(), newList);
    }

    public Boolean isPointInWorld(CPosition point) {
        return 0 <= point.getX() && point.getX() <= this.worldWidth && 0 <= point.getY() && point.getY() <= this.worldHeight;
    }

    public CGraph getGraph() {
        return globalGraph;
    }

    public void buildGraph() {

        this.globalGraph = new CGraph(this);

        // add basic lines
        for (CObstacle obstacle : this.obstacles) {
            Vector<CPosition> wayPoints = obstacle.getWaypoints();
            for (int i = 0; i < wayPoints.size(); i++) {
                for (int j = 0; j < wayPoints.size(); j++) {
                    this.globalGraph.addWayPointEdge(wayPoints.elementAt(j), wayPoints.elementAt(i));
                }
            }
        }

        // connect obstacles with others
        for (CObstacle obstacle : this.obstacles) {
            Vector<CPosition> wayPoints = obstacle.getWaypoints();
            for (CPosition position : wayPoints) {
                for (CObstacle innerObstacle : this.obstacles) {
                    if (obstacle.compareTo(innerObstacle) != 0) { // if it is not the same obstacle

                        for (CPosition innerPosition : innerObstacle.getWaypoints()) {
                            this.globalGraph.addWayPointEdge(position, innerPosition);
                        }
                    }
                }
            }
        }

        for (CWalker walker : this.activeWalkers) {
            this.globalGraph.addWayPointEdge(walker.getPosition(), walker.getTarget());

            for (CObstacle obstacle : this.obstacles) {
                for (CPosition position : obstacle.getWaypoints()) {
                    this.globalGraph.addWayPointEdge(walker.getPosition(), position);
                    this.globalGraph.addWayPointEdge(position, walker.getTarget());
                }
            }

            CDijkstra dijkstra = new CDijkstra(this.globalGraph);

            CPosition position = this.globalGraph.getVertexByPosition(walker.getPosition());
            CPosition target = this.globalGraph.getVertexByPosition(walker.getTarget());
            walker.setDesiredPath(dijkstra.getShortestPath(position, target));
        }


    }

    /**
     * calculates the next simulation step
     */
    public void stepSimulation() {
        boolean hasBlockedWalkers = true;
        Integer calculationRoundCount = 0;
        // step 1: calculate the desired new position of each walker
        while (hasBlockedWalkers) {
            hasBlockedWalkers = false;
            calculationRoundCount += 1;

            if (calculationRoundCount > maxCalculationCount) {
                throw new IllegalArgumentException("Mischt, could not resolve blocked situation in CWorld.stepSimulation in " + maxCalculationCount.toString() + " calculation rounds");
            }

            for (CWalker walker : this.activeWalkers) {
                if (walker.hasCollisions() || (calculationRoundCount == 1)) {
                    // First round calc next Position for all Walkers,
                    // from second round calc only Walkers with collision
                    grid.unsubscribeWalker(walker, true);
                    walker.calcNextDesiredPosition(calculationRoundCount);
                    grid.subscribeWalker(walker, true);
                }
            }

            // reset all detected collsions
            for (CWalker walker : this.activeWalkers) {
                walker.resetCollisions();
            }

            // check all activeWalkers with neighbours for collisions
            for (CWalker walker : this.activeWalkers) {
                for (CWalker neighbourWalker : grid.getNeighbours(walker, true)) {
                    hasBlockedWalkers |= walker.checkCollisionWith(neighbourWalker, true); // if one collision return true, the the while loop must run once again
                }

                for (CObstacle neighbourObstacle : grid.getNearObstacles(walker, true)) {
                    hasBlockedWalkers |= walker.checkCollisionWith(neighbourObstacle, true);
                }

                hasBlockedWalkers |= walker.checkCollisionWith(this, true); // check if walker collide with world
            }

            if (hasBlockedWalkers) {
                Collections.shuffle(this.activeWalkers);
            }
        }

        // step 2: make the step for each walker and remove activeWalkers that reached their target
        // we have to use iterators because we modify the walker list while we iterate over it
        Iterator<CWalker> iter = this.activeWalkers.iterator();
        while (iter.hasNext()) {
            CWalker walker = iter.next();
            if (walker.walkToNextDesiredPosition()) {
                // Walker has reached target, remove the guy
                grid.unsubscribeWalker(walker, false);
                iter.remove();
                finishedWalkers.add(walker);
            } else {
                walker.recalcDesiredPath();
            }
        }

        // step 3: add a walker from the waiting queue if the position is free now
        for ( Map.Entry<CPosition, LinkedList<CWalker>> entry : this.walkerWaitingQueue.entrySet() ) {

            LinkedList<CWalker> list = entry.getValue();

            if(list != null && list.size() > 0) {
                Boolean isPositionBlocked = false;
                CWalker firstWalker = list.getFirst();
                for(CWalker neighbourWalker : grid.getNeighbours(list.getFirst(), false)) {
                    isPositionBlocked = firstWalker.checkCollisionWith(neighbourWalker, false);
                    if(isPositionBlocked) {
                        break;
                    }
                }

                if(!isPositionBlocked) {
                    list.removeFirst();
                    addWalkerWithoutCollsionCheck(firstWalker);
                    entry.setValue(list);

                    // add walker to the graph
                    this.globalGraph.addWayPointEdge(firstWalker.getPosition(), firstWalker.getTarget());

                    for (CObstacle obstacle : this.obstacles) {
                        for (CPosition position : obstacle.getWaypoints()) {
                            this.globalGraph.addWayPointEdge(firstWalker.getPosition(), position);
                            this.globalGraph.addWayPointEdge(position, firstWalker.getTarget());
                        }
                    }

                    CDijkstra dijkstra = new CDijkstra(this.globalGraph);

                    CPosition position = this.globalGraph.getVertexByPosition(firstWalker.getPosition());
                    CPosition target = this.globalGraph.getVertexByPosition(firstWalker.getTarget());
                    firstWalker.setDesiredPath(dijkstra.getShortestPath(position, target));

                }
            }
        }
    }

    /**
     * Get Walkers with given Id
     * @param id of the walker to return
     * @return walker with desired id or null if walker not exist
     */
    public CWalker getWalkerById(int id)
    {
        if ((id < 0) || (id > getWalkerCount()))
        {
            return null;
        }

        for (CWalker walker : this.activeWalkers)
        {
            if(walker.getId() == id)
            {
                return walker;
            }
        }
        for (CWalker walker : this.finishedWalkers)
        {
            if(walker.getId() == id)
            {
                return walker;
            }
        }
        for (LinkedList<CWalker> walkerList : this.walkerWaitingQueue.values())
        {
            for (CWalker walker : walkerList)
            {
                if(walker.getId() == id)
                {
                    return walker;
                }
            }
        }

        return null;
    }

    /**
     * Get Count of Walkers in this World
     * @return walker count
     */
    public int getWalkerCount()
    {
        return this.activeWalkers.size() + this.finishedWalkers.size() + this.walkerWaitingQueue.size();
    }
}
