package Source;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import Util.CPosition;
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
        aoObstacles = loadConfig();
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

    private Vector<CObstacle> loadConfig ()
    {
        Vector<CObstacle> aoObstacles = new Vector<CObstacle>();
        try
        {
            File oConfigFile = new File ("C:/develop/git/ch.bfh.bti7301.w2013.FuSi/XML/Config.xml");
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

                   aoObstacles.add(new CObstacle(aoPosition));
               }

            }


        }
        catch (Exception e)
        {

        }
        return aoObstacles;
    }

    /**
     * calculates the next simulation step
     */
    public void stepSimulation() {

    }
}
