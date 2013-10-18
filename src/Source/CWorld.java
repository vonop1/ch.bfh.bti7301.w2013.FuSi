package Source;

import java.io.File;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import Util.CDijkstra;
import Util.CGraph;
import Util.CPosition;
import Util.CVertex;
import org.w3c.dom.*;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:30
 */
public class CWorld {
    private Map<Integer, CWalker> aoWalkers = new HashMap<Integer, CWalker>();

    //private CGraph oGraph;
    private Vector<CObstacle> aoObstacles = new Vector<CObstacle>();
    private CGrid grid = new CGrid();

    public CWorld ()
    {

    }

    public Map<Integer, CWalker> getWalkers() {
        return aoWalkers;
    }

    public Vector<CObstacle> getObstacles() {
        return aoObstacles;
    }

    public void addWalker(CWalker walker) {
        this.aoWalkers.put(walker.getId(), walker);
    }

    public void addObstacle(CObstacle obstacle) {
        this.aoObstacles.add(obstacle);
    }

    public Vector<CObstacle> loadConfig ()
    {
        this.aoObstacles = new Vector<CObstacle>();
        try
        {
            // load Config from File Config.xml
            File oConfigFile = new File ("./XML/Config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document oConfigDoc = dBuilder.parse(oConfigFile);

            // Get a List off all Obstacles
            NodeList aoObj = oConfigDoc.getElementsByTagName("obj");

            for (int i = 0; i < aoObj.getLength(); i++)
            {
               Node oObj = aoObj.item(i);
               Vector<CPosition> aoPosition = new Vector<CPosition>();

               if (oObj.hasChildNodes())
               {
                   // Get a List off all Points of a Obstacle
                   NodeList aoPoint = oObj.getChildNodes();
                   for (int j = 0; j < aoPoint.getLength(); j++)
                   {
                       Node oPoint = aoPoint.item(j);

                       if (oPoint.getNodeType() == Node.ELEMENT_NODE)
                       {
                           // Get Edge-points of the Obstacle
                           NamedNodeMap oPointAttributes = oPoint.getAttributes();

                           double dX = Integer.parseInt(oPointAttributes.getNamedItem("x").getNodeValue());
                           double dY = Integer.parseInt(oPointAttributes.getNamedItem("y").getNodeValue());
                           aoPosition.add(new CPosition(dX, dY));
                       }


                   }

                   addObstacle(new CObstacle(aoPosition));
               }

            }

            // Get a List off all Walkers
            NodeList walkers = oConfigDoc.getElementsByTagName("walker");

            for (int i = 0; i < walkers.getLength(); i++)
            {
                Node walker = walkers.item(i);

                if (walker.hasChildNodes())
                {
                    // Get a List off all Points of a Obstacle
                    NodeList points = walker.getChildNodes();

                    Node point = points.item(0);
                    while (point.getNodeType() != Node.ELEMENT_NODE)
                    {
                        //remove empty elements
                        point = point.getNextSibling();
                    }

                    // Get Source Point with X-Coordinate and Y-Coordinate
                    NamedNodeMap pointAttributes = point.getAttributes();

                    double dX = Integer.parseInt(pointAttributes.getNamedItem("x").getNodeValue());
                    double dY = Integer.parseInt(pointAttributes.getNamedItem("y").getNodeValue());

                    CPosition source = new CPosition(dX, dY);

                    point = point.getNextSibling();

                    while (point.getNodeType() != Node.ELEMENT_NODE)
                    {
                        //remove empty elements
                        point = point.getNextSibling();
                    }

                    // Get Destination Point with X-Coordinate and Y-Coordinate
                    pointAttributes = point.getAttributes();

                    dX = Integer.parseInt(pointAttributes.getNamedItem("x").getNodeValue());
                    dY = Integer.parseInt(pointAttributes.getNamedItem("y").getNodeValue());

                    CPosition destination = new CPosition(dX, dY);

                    addWalker(new CWalker(source, destination));
                }

            }

        }
        catch (Exception e)
        {
          System.out.print(e);
        }
        return this.aoObstacles;
    }

    private CGraph oGraph = null;

    public CGraph getGraph() {
        return oGraph;
    }

    public void buildGraph() {
        this.oGraph = new CGraph();

        // add obstacle lines
        for(CObstacle obstacle : this.aoObstacles) {
            Vector<CPosition> positions = obstacle.getPositions();
            for(int i = 0; i < positions.size(); i++) {
                for (int j = 0; j< positions.size(); j++) {
                    this.oGraph.addObstacleEdge(positions.elementAt(j), positions.elementAt(i));
                }
            }
        }

        // add basic lines
        for(CObstacle obstacle : this.aoObstacles) {
            Vector<CPosition> wayPoints = obstacle.getWaypoints();
            for(int i = 0; i < wayPoints.size(); i++) {
                for (int j = 0; j< wayPoints.size(); j++) {
                    this.oGraph.addWayPointEdge(wayPoints.elementAt(j), wayPoints.elementAt(i));
                    //this.oGraph.addWayPointEdge(wayPoints.elementAt(i), wayPoints.elementAt(j));
                }
            }
        }

        // connect obstacles with others
        for(CObstacle obstacle : this.aoObstacles) {
            Vector<CPosition> wayPoints = obstacle.getWaypoints();
            for(CPosition position : wayPoints) {
                for(CObstacle innerObstacle : this.aoObstacles) {
                    if(obstacle.compareTo(innerObstacle) != 0) { // if it is not the same obstacle

                         for(CPosition innerPosition : innerObstacle.getWaypoints()) {
                            this.oGraph.addWayPointEdge(position, innerPosition);
                         }
                    }
                }
            }
        }

        for(CWalker walker : this.aoWalkers.values()) {
            this.oGraph.addWayPointEdge(walker.getPosition(), walker.getTarget());

            for(CObstacle obstacle : this.aoObstacles) {
                for(CPosition position : obstacle.getWaypoints()) {
                    this.oGraph.addWayPointEdge(walker.getPosition(), position);
                    this.oGraph.addWayPointEdge(position, walker.getTarget());
                }
            }

            CDijkstra dijkstra = new CDijkstra(this.oGraph);

            CVertex position = this.oGraph.getVertexByPosition(walker.getPosition());
            CVertex target = this.oGraph.getVertexByPosition(walker.getTarget());
            walker.setDesiredPath(dijkstra.getShortestPath(position, target));
        }


    }


    /**
     * calculates the next simulation step
     */
    public void stepSimulation() {
        // let the people move their bodies ^^
        Iterator<CWalker> iter = this.aoWalkers.values().iterator();
        while(iter.hasNext()){
            CWalker walker = iter.next();
            grid.unsubscribeWalker(walker);
            if(walker.walkAStep()){
                // Walker has reached target, remove the guy
                iter.remove();
            }
            else
            {
                grid.subscribeWalker(walker);
            }

            grid.getNeighbours(walker);

        }

//        for(CWalker walker : this.aoWalkers) {
//            if( walker.walkAStep()) {
//                // Walker has reached target, remove the guy
//                this.aoWalkers.removeElement(walker) // fires a ConcurrentModificationException
//            }
//        }
    }


}
