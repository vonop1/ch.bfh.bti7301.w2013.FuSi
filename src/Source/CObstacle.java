package Source;

import Util.CGraph;
import Util.CMathFunctions;
import Util.CPosition;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:37
 */
public class CObstacle implements Comparable<CObstacle> {

    Integer iId;
    double dDistToEdgeC = 13.0;
    protected Vector<CPosition> aoPosition;

    /**
     * get edges of the obstacle
     * @return  edges as CPosition Vector
     */
    public Vector<CPosition> getPositions() {
        return  aoPosition;
    }


    public CObstacle (Vector<CPosition> aoPosition)
    {
        this.iId = CGraph.incrementId();
        this.aoPosition = aoPosition;
    }

    public Integer getId() {
        return this.iId;
    }

    /**
     * get the minimal distance of a point to the obstacle
     * @param point the point
     * @return the distance as double
     */
    public Double getDistanceTo(CPosition point) {

        Double minDistance = null;

        for(int i = 0; i < this.aoPosition.size(); i++) {
            Double distance = point.getDistanceToLine(this.aoPosition.get(i), this.aoPosition.get((i+1 >= this.aoPosition.size() ? 0 : i+1)), false);
            if(minDistance == null || distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    /**
     * Calculate the Waypoints for a the obstacle
     * @return vector with Points for travers the obstacle
     */
    public Vector<CPosition> getWaypoints()
    {
        //return getWayPoint4();

        Vector<CPosition> oResult = new Vector<CPosition>();

        for (int i = 0; i < aoPosition.size(); i++)
        {
           oResult.add(getWaypoint(i, dDistToEdgeC));
        }

        return oResult;
    }

    /**
     * Calculate the Waypoints for a the obstacle
     * @return vector with Points for travers the obstacle
     */
    public Vector<CPosition> getVertexPoints()
    {
        Vector<CPosition> oResult = new Vector<CPosition>();

        for (int i = 0; i < aoPosition.size(); i++)
        {
            oResult.add(getWaypoint(i, CWalker.halfWalkerSize));
        }

        return oResult;
    }

    /**
     * Calculate the Waypoint for one Edge of the obstacle
     * @return Point for travers the obstacle
     */
    private CPosition getWaypoint(int iEdgeNummber, double dDistToEdge)
    {
        CPosition pointA = aoPosition.elementAt(iEdgeNummber);
        double dXPoint = pointA.getX();
        double dYPoint = pointA.getY();

        CPosition pointB = (iEdgeNummber > 0) ? aoPosition.elementAt(iEdgeNummber - 1) : aoPosition.lastElement();

        CPosition pointC = ((iEdgeNummber + 1) < aoPosition.size()) ? aoPosition.elementAt(iEdgeNummber + 1): aoPosition.firstElement();

        double lengthA = pointB.getDistanceTo(pointC);
        double lengthB = pointA.getDistanceTo(pointC);
        double lengthC = pointA.getDistanceTo(pointB);
        double sumP = lengthA + lengthB + lengthC;

        double dXAverage = (lengthA * pointA.getX() + lengthB * pointB.getX() + lengthC * pointC.getX())/sumP;
        double dYAverage = (lengthA * pointA.getY() + lengthB * pointB.getY() + lengthC * pointC.getY())/sumP;

        double dXDiff = dXPoint - dXAverage;
        double dYDiff = dYPoint - dYAverage;

        if ( (dXDiff > 0) && (dYDiff > 0) )
        {
            // First Quadrant
            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiff, 2.0) + Math.pow(dYDiff, 2.0));
            double dAngle = Math.atan(dYDiff/dXDiff);

            double dXWaypoint = (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdge)) + dXAverage;
            double dYWaypoint = (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdge)) + dYAverage;

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff > 0) )
        {
            // Second Quadrant
            double dXDiffAbs = Math.abs(dXDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiff, 2.0));
            double dAngle = Math.atan(dYDiff/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdge));
            double dYWaypoint = dYAverage + (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdge));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff <= 0) )
        {
            // Third Quadrant
            double dXDiffAbs = Math.abs(dXDiff);
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.atan(dYDiffAbs/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdge));
            double dYWaypoint = dYAverage - (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdge));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else
        {
            // Fourth Quadrant
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiff, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.atan(dYDiffAbs/dXDiff);

            double dXWaypoint = dXAverage + (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdge));
            double dYWaypoint = dYAverage - (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdge));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
    }


    @Override
    public int compareTo(CObstacle o) {
        return this.iId.compareTo(o.getId());
    }

}
