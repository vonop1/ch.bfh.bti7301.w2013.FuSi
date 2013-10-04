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

    private Vector<CObstacle> loadConfig ()
    {
        Vector<CObstacle> aoObstacles = new Vector<CObstacle>();


        return aoObstacles;
    }

    /**
     * calculates the next simulation step
     */
    public void stepSimulation() {

    }
}
