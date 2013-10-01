package Source;

import Util.CPosition;

import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:37
 */
public class CObstacle {

    double dDistToEdgeC = 2.0;
    private Vector<CPosition> aoPosition;

    /**
     * Calculate the Waypoints for a the obstacle
     * @return vector with Points for travers the obstacle
     */
    public Vector<CPosition> getWaypoints()
    {
        Vector<CPosition> oResult = new Vector<CPosition>();

        for (int i = 0; i < aoPosition.size(); i++)
        {
           oResult.add(getWaypoint(i));
        }

        return oResult;
    }

    /**
     * Calculate the Waypoint for one Edge of the obstacle
     * @return Point for travers the obstacle
     */
    private CPosition getWaypoint(int iEdgeNummber)
    {
        double dXPoint = aoPosition.elementAt(iEdgeNummber).getX();
        double dYPoint = aoPosition.elementAt(iEdgeNummber).getY();
        double dXAverage = 0;
        double dYAverage = 0;
        double dXSum = 0;
        double dYSum = 0;

        for (CPosition oPos:aoPosition)
        {
            dXSum += oPos.getX();
            dYSum += oPos.getY();
        }

        if (aoPosition.size() != 1)
        {
            //Division with Zero not possible
            dXAverage = (dXSum - dXPoint) / (aoPosition.size() - 1);
            dYAverage = (dYSum - dYPoint) / (aoPosition.size() - 1);
        }

        double dXDiff = dXPoint - dXAverage;
        double dYDiff = dYPoint - dYAverage;

        if ( (dXDiff > 0) && (dYDiff > 0) )
        {
            // First Quadrant
            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiff, 2.0) + Math.pow(dYDiff, 2.0));
            double dAngle = Math.tan(dYDiff/dXDiff);

            double dXWaypoint = (Math.asin(dAngle) * (dLengthDiagonal + dDistToEdgeC)) + dXAverage;
            double dYWaypoint = (Math.acos(dAngle) * (dLengthDiagonal + dDistToEdgeC)) + dYAverage;

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff > 0) )
        {
            // Second Quadrant
            double dXDiffAbs = Math.abs(dXDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiff, 2.0));
            double dAngle = Math.tan(dYDiff/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.asin(dAngle) * (dLengthDiagonal + dDistToEdgeC));
            double dYWaypoint = dYAverage + (Math.acos(dAngle) * (dLengthDiagonal + dDistToEdgeC));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff <= 0) )
        {
            // Third Quadrant
            double dXDiffAbs = Math.abs(dXDiff);
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.tan(dYDiffAbs/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.asin(dAngle) * (dLengthDiagonal + dDistToEdgeC));
            double dYWaypoint = dYAverage - (Math.acos(dAngle) * (dLengthDiagonal + dDistToEdgeC));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else
        {
            // Fourth Quadrant
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiff, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.tan(dYDiffAbs/dXDiff);

            double dXWaypoint = dXAverage + (Math.asin(dAngle) * (dLengthDiagonal + dDistToEdgeC));
            double dYWaypoint = dYAverage - (Math.acos(dAngle) * (dLengthDiagonal + dDistToEdgeC));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
    }


}
