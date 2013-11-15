package Source;

import Util.CGraph;
import Util.CMathFunctions;
import Util.CPosition;

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
        Vector<CPosition> oResult = new Vector<CPosition>();

        for (int i = 0; i < aoPosition.size(); i++)
        {
           oResult.add(getWaypoint(i));
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
            oResult.add(getVertexpoint(i));
        }

        return oResult;
    }

    /**
     * Calculate the Waypoint for one Edge of the obstacle
     * @return Point for travers the obstacle
     */
    private CPosition getVertexpoint(int iEdgeNummber)
    {
        double distanceToVertex = 6;
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
            double dAngle = Math.atan(dYDiff/dXDiff);

            double dXWaypoint = (Math.cos(dAngle) * (dLengthDiagonal + distanceToVertex)) + dXAverage;
            double dYWaypoint = (Math.sin(dAngle) * (dLengthDiagonal + distanceToVertex)) + dYAverage;

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff > 0) )
        {
            // Second Quadrant
            double dXDiffAbs = Math.abs(dXDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiff, 2.0));
            double dAngle = Math.atan(dYDiff/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.cos(dAngle) * (dLengthDiagonal + distanceToVertex));
            double dYWaypoint = dYAverage + (Math.sin(dAngle) * (dLengthDiagonal + distanceToVertex));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff <= 0) )
        {
            // Third Quadrant
            double dXDiffAbs = Math.abs(dXDiff);
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.atan(dYDiffAbs/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.cos(dAngle) * (dLengthDiagonal + distanceToVertex));
            double dYWaypoint = dYAverage - (Math.sin(dAngle) * (dLengthDiagonal + distanceToVertex));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else
        {
            // Fourth Quadrant
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiff, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.atan(dYDiffAbs/dXDiff);

            double dXWaypoint = dXAverage + (Math.cos(dAngle) * (dLengthDiagonal + distanceToVertex));
            double dYWaypoint = dYAverage - (Math.sin(dAngle) * (dLengthDiagonal + distanceToVertex));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
    }


    private CPosition getWaypoint2(int edgeNumber) {

        CPosition edgePoint = aoPosition.elementAt(edgeNumber);
        CPosition prePoint = aoPosition.elementAt((edgeNumber - 1 < 0 ? aoPosition.size() - 1 : edgeNumber - 1));
        CPosition postPoint = aoPosition.elementAt((edgeNumber + 1 >= aoPosition.size() ? 0 : edgeNumber + 1));

        assert(edgePoint != null);
        assert(prePoint != null);
        assert(postPoint != null);

        Double alpha = CMathFunctions.angleBetween2Lines(prePoint, edgePoint, edgePoint, postPoint);
        Double beta = CMathFunctions.angleBetween2Lines(prePoint, edgePoint, edgePoint, new CPosition(edgePoint.getX() + 10, edgePoint.getY()));
        Double gamma = beta - alpha / 2;
        Double delta = Math.PI - gamma;

        CPosition directionVector = this.getDirectionVectorOfCenter(edgePoint);
        CPosition center = this.getCenter();

        Double xDiff = edgePoint.getX() - center.getX();
        Double yDiff = edgePoint.getY() - center.getY();

        //if(delta > Math.PI) {
        //    delta = (Math.PI * 2) - delta;
        //}

        Double distanceToLine = center.getDistanceToLine(prePoint, postPoint, true);
        Double distanceToEdge = center.getDistanceTo(edgePoint);

        boolean isStumpf = distanceToEdge < distanceToLine;


        Double xDelta = Math.cos(delta) * this.dDistToEdgeC; //* (xDiff > 0 ? 1 : -1);
        Double yDelta = Math.sin(delta) * this.dDistToEdgeC; //* (yDiff > 0 ? 1 : -1);

        return new CPosition(edgePoint.getX() + xDelta, edgePoint.getY() + yDelta);


        /*
        CPosition center = this.getCenter();

        CPosition pos1 = new CPosition(edgePoint.getX() + xDelta, edgePoint.getY() + yDelta);
        CPosition pos2 = new CPosition(edgePoint.getX() - xDelta, edgePoint.getY() - yDelta);
        CPosition pos3 = new CPosition(edgePoint.getX() + xDelta, edgePoint.getY() - yDelta);
        CPosition pos4 = new CPosition(edgePoint.getX() - xDelta, edgePoint.getY() + yDelta);

        Double distance1 = pos1.getDistanceTo(center);
        Double distance2 = pos2.getDistanceTo(center);
        Double distance3 = pos3.getDistanceTo(center);
        Double distance4 = pos4.getDistanceTo(center);

        CPosition resultPos = pos1;
        Double resultDistance = distance1;

        if(distance2 > resultDistance) {
            resultPos = pos2;
            resultDistance = distance2;
        }

        if(distance3 > resultDistance) {
            resultPos = pos3;
            resultDistance = distance3;
        }

        if(distance4 > resultDistance) {
            resultPos = pos4;
            resultDistance = distance4;
        }

        return resultPos;
        */




//        Double xDelta = Math.cos(delta) * this.dDistToEdgeC * (delta < Math.PI / 2 || delta > (Math.PI * 3) / 2 ? -1 : 1 );
//        Double yDelta = Math.sin(delta) * this.dDistToEdgeC * (delta > Math.PI ? 1 : -1 );

//        return new CPosition(edgePoint.getX() + xDelta, edgePoint.getY() + yDelta);

    }


    private CPosition getDirectionVectorOfCenter(CPosition edge) {
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
            dXAverage = (dXSum - edge.getX()) / (aoPosition.size() - 1);
            dYAverage = (dYSum - edge.getY()) / (aoPosition.size() - 1);
        }

        double dXDiff = edge.getX() - dXAverage;
        double dYDiff = edge.getY() - dYAverage;

        return new CPosition(dXDiff, dYDiff);
    }


    /**
     * finds the center of a polygon
     * @return the center of the polygon
     */
    public CPosition getCenter() {

        double cx = 0, cy = 0, area = 0, factor = 0;
        int j = 0;
        for (int i = 0; i < this.aoPosition.size(); i++) {
            j = (i + 1) % this.aoPosition.size();
            area += this.aoPosition.get(i).getX() * this.aoPosition.get(j).getY();
            area -= this.aoPosition.get(i).getY() * this.aoPosition.get(j).getX();

            factor = (this.aoPosition.get(i).getX() * this.aoPosition.get(j).getY() - this.aoPosition.get(j).getX() * this.aoPosition.get(i).getY());
            cx += (this.aoPosition.get(i).getX() + this.aoPosition.get(j).getX()) * factor;
            cy += (this.aoPosition.get(i).getY() + this.aoPosition.get(j).getY()) * factor;
        }

        area = Math.abs(area / 2);
        factor = 1.0 / (6.0 * area);

        cx *= factor;
        cy *= factor;

        return new CPosition(Math.abs(cx), Math.abs(cy));
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
            double dAngle = Math.atan(dYDiff/dXDiff);

            double dXWaypoint = (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdgeC)) + dXAverage;
            double dYWaypoint = (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdgeC)) + dYAverage;

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff > 0) )
        {
            // Second Quadrant
            double dXDiffAbs = Math.abs(dXDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiff, 2.0));
            double dAngle = Math.atan(dYDiff/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdgeC));
            double dYWaypoint = dYAverage + (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdgeC));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else if ( (dXDiff <= 0) && (dYDiff <= 0) )
        {
            // Third Quadrant
            double dXDiffAbs = Math.abs(dXDiff);
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiffAbs, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.atan(dYDiffAbs/dXDiffAbs);

            double dXWaypoint = dXAverage - (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdgeC));
            double dYWaypoint = dYAverage - (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdgeC));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
        else
        {
            // Fourth Quadrant
            double dYDiffAbs = Math.abs(dYDiff);

            double dLengthDiagonal = Math.sqrt(Math.pow(dXDiff, 2.0) + Math.pow(dYDiffAbs, 2.0));
            double dAngle = Math.atan(dYDiffAbs/dXDiff);

            double dXWaypoint = dXAverage + (Math.cos(dAngle) * (dLengthDiagonal + dDistToEdgeC));
            double dYWaypoint = dYAverage - (Math.sin(dAngle) * (dLengthDiagonal + dDistToEdgeC));

            return new CPosition(dXWaypoint, dYWaypoint);
        }
    }


    @Override
    public int compareTo(CObstacle o) {
        return this.iId.compareTo(o.getId());
    }
}
