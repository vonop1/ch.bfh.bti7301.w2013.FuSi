package Source;

import org.xml.sax.XMLReader;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:30
 */
public class CWorld {
    private Vector<CWalker> aoWalkers;

    //private CGraph oGraph;
    private Vector<CObstacle> aoObstacles;

    public CWorld ()
    {

    }

    private Vector<CObstacle> loadConfig ()
    {
        Vector<CObstacle> aoObstacles = new Vector<CObstacle>();


        return aoObstacles;
    }
}
