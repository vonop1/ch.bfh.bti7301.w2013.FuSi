package Source;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import Util.CDijkstra;
import Util.CGraph;
import Util.CPosition;
import Util.CVertex;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import org.w3c.dom.*;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:30
 */
public class CWorld {
    private Vector<CWalker> aoWalkers = new Vector<CWalker>();

    //private CGraph oGraph;
    private Vector<CObstacle> aoObstacles = new Vector<CObstacle>();

    public CWorld ()
    {

    }

    public Vector<CWalker> getWalkers() {
        return aoWalkers;
    }

    public Vector<CObstacle> getObstacles() {
        return aoObstacles;
    }

    public void addWalker(CWalker walker) {
        this.aoWalkers.add(walker);
    }

    public void addObstacle(CObstacle obstacle) {
        this.aoObstacles.add(obstacle);
    }

    public Vector<CObstacle> loadConfig ()
    {
        this.aoObstacles = new Vector<CObstacle>();
        try
        {
            File oConfigFile = new File ("C:/java/ch.bfh.bti7301.w2013.FuSi/XML/Config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document oConfigDoc = dBuilder.parse(oConfigFile);

            NodeList aoObj = oConfigDoc.getElementsByTagName("obj");

            for (int i = 0; i < aoObj.getLength(); i++)
            {
               Node oObj = aoObj.item(i);
               Vector<CPosition> aoPosition = new Vector<CPosition>();

               if (oObj.hasChildNodes())
               {
                   NodeList aoPoint = oObj.getChildNodes();
                   for (int j = 0; j < aoPoint.getLength(); j++)
                   {
                       Node oPoint = aoPoint.item(j);

                       if (oPoint.getNodeType() == Node.ELEMENT_NODE)
                       {
                           NamedNodeMap oPointAttributes = oPoint.getAttributes();

                           double dX = Integer.parseInt(oPointAttributes.getNamedItem("x").getNodeValue());
                           double dY = Integer.parseInt(oPointAttributes.getNamedItem("y").getNodeValue());
                           aoPosition.add(new CPosition(dX, dY));
                       }


                   }

                   this.aoObstacles.add(new CObstacle(aoPosition));
               }

            }


        }
        catch (Exception e)
        {

        }
        return this.aoObstacles;
    }

    private CGraph oGraph = null;

    public CGraph getGraph() {
        return oGraph;
    }

    public void buildGraph() {
        this.oGraph = new CGraph();

        // add basic lines
        for(CObstacle obstacle : this.aoObstacles) {
            int j = 0;
            Vector<CPosition> wayPoints = obstacle.getWaypoints();
            for(int i = 0; i < wayPoints.size(); i++) {
                if(i+1 >= wayPoints.size()) {
                    j = 0;
                }
                else {
                    j = i + 1;
                }

                if( i > (int)(wayPoints.size() / 2)){
                    this.oGraph.addEdge(wayPoints.elementAt(j), wayPoints.elementAt(i), true);
                }
                else
                {
                    this.oGraph.addEdge(wayPoints.elementAt(i), wayPoints.elementAt(j), true);
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
                            this.oGraph.addEdge(position, innerPosition, false);
                         }
                    }
                }
            }
        }

        for(CWalker walker : this.aoWalkers) {
            this.oGraph.addEdge(walker.getPosition(), walker.getTarget(), false);

            for(CObstacle obstacle : this.aoObstacles) {
                for(CPosition position : obstacle.getWaypoints()) {
                    this.oGraph.addEdge(walker.getPosition(), position, false);
                    this.oGraph.addEdge(position, walker.getTarget(), false);
                }
            }

            CDijkstra dijkstra = new CDijkstra(this.oGraph);

            CVertex position = this.oGraph.getVertexByPosition(walker.getPosition());
            CVertex target = this.oGraph.getVertexByPosition(walker.getTarget());
            walker.setDesiredPath(dijkstra.getPath(position, target));
        }


    }


    /**
     * calculates the next simulation step
     */
    public void stepSimulation() {
        // let the people move their bodies ^^
        for(CWalker walker : this.aoWalkers) {
            if( walker.walkAStep() == true ) {
                // Walker has reached target, remove the guy
                // TODO
            }
        }
    }
}
