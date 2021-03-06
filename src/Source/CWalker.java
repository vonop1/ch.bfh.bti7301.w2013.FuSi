package Source;

import Util.CDijkstra;
import Util.CEdge;
import Util.CGraph;
import Util.CPosition;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA
 * author: bohnp1, jaggr2
 * Date: 27.09.13
 * Time: 14:09
 */
public class CWalker {

    protected Integer id;
    protected CPosition currentPosition;
    protected CPosition targetPosition;
    protected CPosition startPosition;
    protected CPosition desiredNextPosition;
    protected boolean modusStriktNachDesiredPath = false;

    protected CWorld worldReference;

    protected Double halfWalkerSize = 5.0;
    protected Double stepSize = 2.0;

    protected LinkedList<CPosition> desiredPath = new LinkedList<CPosition>();
    protected LinkedList<CPosition> originalDesiredPath = new LinkedList<CPosition>();
    protected CGraph walkerGraph;

    protected CCollisionList collisionWith = null;
    protected boolean hasCollisionHandled = false;
    protected Double lastDirectionAngle = 0.0;


    //variables for stats
    protected int stepCountOpt = 0;
    protected int stepCountReal = 0;

    private IStrategie strategie = null;

    public CWalker(CPosition start, CPosition target, CWorld worldReference) {
        this.startPosition = start;
        this.currentPosition = start;
        this.targetPosition = target;
        this.worldReference = worldReference;
        if(worldReference != null) {
            this.id = worldReference.incrementWalkerId();
        }
        else {
            this.id = CWorld.incrementGlobalId();
        }
        this.strategie = new CStrategieRightLeft();

    }

    public CPosition getPosition() {
        return currentPosition;
    }

    public void setPosition(CPosition start) {
        this.currentPosition = start;
    }

    public CPosition getTarget() {
        return targetPosition;
    }

    public void setTarget(CPosition start) {
        this.targetPosition = start;
    }

    public CPosition getDesiredNextPosition() {
        if (desiredNextPosition == null) {
            return currentPosition;
        } else {
            return desiredNextPosition;
        }
    }

    public LinkedList<CPosition> getDesiredPath() {
        return desiredPath;
    }

    public Double getHalfWalkerSize() {
        return halfWalkerSize;
    }

    public Double getLastDirectionAngle() {
        return this.lastDirectionAngle;
    }

    public IStrategie getStrategie() {
        return this.strategie;
    }

    public int getStepCountReal() {return this.stepCountReal;}

    public int getStepCountOpt() {return this.stepCountOpt; }

    public Boolean hasCollisions() {
        return this.collisionWith != null && this.collisionWith.hasCollisions();
    }

    public void changeStrategie(IStrategie strategie) {
        if(strategie == null) {
            return;
        }

        this.strategie = strategie;
    }

    public void setDesiredPath(LinkedList<CPosition> vertexes) {
        if (vertexes == null || vertexes.size() == 0) {
            throw new IllegalArgumentException("Der desiredPath darf nicht Null oder leer sein --> vermutlich kam Dijsktra zu keinem Ergebnis!");
        }

        if(this.originalDesiredPath.size() == 0) {
            for(CPosition pos : vertexes) {
                this.originalDesiredPath.add(pos);
            }
        }

        if (currentPosition.isNearBy(vertexes.getFirst(), stepSize / 0.5)) {
            // the first checkpoint is the current position, remove it from our list
            vertexes.removeFirst();
        }

        this.desiredPath = vertexes;

        // Beim ersten Mal vom desired Path einen Graph für den Walker erstellen
        if (this.walkerGraph == null) {
            this.walkerGraph = new CGraph(worldReference);

            for (int i = 1; i < this.originalDesiredPath.size(); i++) {
                this.walkerGraph.addWayPointEdge(this.originalDesiredPath.get(i - 1), this.originalDesiredPath.get(i));
            }

            //berechnen der Anzahl Schritten auf dem kürzesten Weg
            stepCountOpt = 0;
            for (CEdge edge : walkerGraph.getEdges())
            {
                 stepCountOpt += edge.getWeight() / stepSize;
            }
        }
    }

    public void recalcDesiredPath() throws IllegalArgumentException {

        if (!modusStriktNachDesiredPath) {
            if (this.walkerGraph == null) {
                return;
            }

            for (CPosition anOriginalDesiredPath : this.originalDesiredPath) {
                this.walkerGraph.addWayPointEdge(this.currentPosition, anOriginalDesiredPath);
            }

            CDijkstra dijkstra = new CDijkstra(this.walkerGraph);

            CPosition position = this.walkerGraph.getVertexByPosition(this.currentPosition);
            CPosition target = this.walkerGraph.getVertexByPosition(this.targetPosition);

            if(position == null || target == null) {
                // in this case, position or target are out of the world or on an object
                throw new IllegalArgumentException("Huston, we have a problem! see CWalker.recalcDesiredPath()");
            }

            LinkedList<CPosition> newPath = dijkstra.getShortestPath(position, target);

            this.walkerGraph.removeVertex(this.currentPosition);


            if (newPath != null) {
                this.setDesiredPath(newPath);
                return;
            }
        }

        // bisheriges statement
        if (this.desiredPath.size() > 0) {
            if (this.getDesiredNextPosition().isNearBy(this.desiredPath.getFirst(), stepSize / 0.5)) {
                // Yes, we reached a checkpoint, remove it from our list
                this.desiredPath.removeFirst();
                System.out.println("Walker" + this + " changed the desired Path due to checkpoint match!");
            }
        }
    }

    public void resetCollisions() {
        if (this.collisionWith != null) {
            this.collisionWith.clear();
        }
        hasCollisionHandled = false;
    }

    public CCollisionList getCollisionWith() {
        return this.collisionWith;
    }

    public void setCollisionWith(CCollisionList list) {
        this.collisionWith = list;
    }

    /**
     * checks if the walker has a collision with the desiredNextPosition of another walker and remembers the collision
     *
     * @param other             the walker
     * @param rememberCollision true if the walker should remember this collision
     * @return true if we have a collision or false if not
     */
    public boolean checkCollisionWith(CWalker other, boolean rememberCollision) {

        if (this.equals(other)) {
            return false;
        }

        boolean hasCollision = this.getDesiredNextPosition().getDistanceTo(other.getDesiredNextPosition()) < this.getHalfWalkerSize() + other.getHalfWalkerSize() + 1;

        if (hasCollision && rememberCollision) {

            CCollisionList newList;
            if (!this.hasCollisions() && !other.hasCollisions()) { // case 1: none of both walker have a collision until now
                newList = new CCollisionList();
            } else if (this.hasCollisions() && !other.hasCollisions()) { // case 2: walker A has a collision, walker B not
                newList = this.getCollisionWith();
            } else if (!this.hasCollisions() && other.hasCollisions()) { // case 3: walker B has a collision, walker A not
                newList = other.getCollisionWith();
            } else { // case 4: both walkers have a collision until now
                newList = this.getCollisionWith();
                newList.merge(other.getCollisionWith());
            }

            newList.addCollision(this, other);

            this.setCollisionWith(newList);
            other.setCollisionWith(newList);
        }

        return hasCollision;
    }

    /**
     * checks if the walker has a collision with a position
     *
     * @param position             the position
     * @return true if we have a collision or false if not
     */
    public boolean checkCollisionWith(CPosition position) {

        return position != null && this.getDesiredNextPosition().getDistanceTo(position) < this.getHalfWalkerSize() + 1;

    }

    /**
     * checks if the walker has a collision with an obstacle and remembers the collision
     *
     * @param obstacle          the obstacle
     * @param rememberCollision true if the walker should remember this collision
     * @return true if we have a collision or false if not
     */
    public boolean checkCollisionWith(CObstacle obstacle, boolean rememberCollision) {

        boolean hasCollision = obstacle.getDistanceTo(this.getDesiredNextPosition()) < this.getHalfWalkerSize() + 0.1;

        if (hasCollision && rememberCollision) {

            if (this.collisionWith == null) {
                this.collisionWith = new CCollisionList();
            }

            this.collisionWith.addCollision(obstacle, this);
        }

        return hasCollision;
    }

    /**
     * checks if the walker has a collision with the world and remembers the collision
     *
     * @param world          the world
     * @param rememberCollision true if the walker should remember this collision
     * @return true if we have a collision or false if not
     */
    public boolean checkCollisionWith(CWorld world, boolean rememberCollision) {

        boolean hasCollision = !world.isPointInWorld(this.getDesiredNextPosition());

        if (hasCollision && rememberCollision) {

            if (this.collisionWith == null) {
                this.collisionWith = new CCollisionList();
            }

            this.collisionWith.addCollision(world, this);
        }

        return hasCollision;
    }

    /**
     * calculates the next position and saves the result to the nextDesiredPosition member var
     *
     * @param roundCount the calculation round count
     */
    public void calcNextDesiredPosition(Integer roundCount)
    {
        desiredNextPosition = strategie.calcNextDesiredPosition(roundCount, this);
    }

    /**
     * walks a step to the next desired Position and resets desiredNextPosition member variable to NULL
     *
     * @return true if ok or false if the walker is on the target
     */
    public boolean walkToNextDesiredPosition() {

        if (this.hasCollisions()) {
            throw new IllegalArgumentException("The Walker must no be blocked!");
        }

        if (this.desiredNextPosition == null) {
            throw new IllegalArgumentException("this.desiredNextPosition must not be NULL!");
        }

        Double xDelta = this.desiredNextPosition.getX() - this.currentPosition.getX();
        Double yDelta = this.desiredNextPosition.getY() - this.currentPosition.getY();

        this.lastDirectionAngle = Math.atan2( yDelta, xDelta );





        this.currentPosition = this.desiredNextPosition;

        this.desiredNextPosition = null;

        this.stepCountReal ++;

        return this.targetPosition.isNearBy(this.currentPosition, stepSize / 0.5);
    }

    /**
     * Get the unique Identifier of the Walker
     *
     * @return unique Identifier
     */
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && ((CWalker) obj).getId().equals(this.getId());
    }

    /**
     * Intended only for debugging.
     */
    @Override
    public String toString() {

        return "Walker" + id + currentPosition + (desiredNextPosition != null ? "->" + desiredNextPosition : "<>");
    }
}
