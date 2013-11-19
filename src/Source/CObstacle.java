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


    /**
     * get Waypoint of the obstacle
     * @param edgeNumber
     * @return
     */
    private CPosition getWaypoint5(int edgeNumber) {

        CPosition edgePoint = aoPosition.elementAt(edgeNumber);
        CPosition prePoint = aoPosition.elementAt((edgeNumber - 1 < 0 ? aoPosition.size() - 1 : edgeNumber - 1));
        CPosition postPoint = aoPosition.elementAt((edgeNumber + 1 >= aoPosition.size() ? 0 : edgeNumber + 1));

        assert(edgePoint != null);
        assert(prePoint != null);
        assert(postPoint != null);

        // calculate vector size
        CPosition vector1 = new CPosition(prePoint, edgePoint);
        CPosition vector2 = new CPosition(postPoint, edgePoint);

        CPosition vector3 = new CPosition(vector2);
        vector3.resizeToAbsoluteValue(vector1.calcAbsoluteValue()); // resize vector 3 to the absolute value of vector 1

        assert(vector1.calcAbsoluteValue() == vector3.calcAbsoluteValue());

        CPosition vector4 = new CPosition(0,0);
        vector4.add(vector1);
        vector4.add(vector3);

        CPosition vector5 = new CPosition(vector4);
        vector5.resizeToAbsoluteValue(dDistToEdgeC);

        CPosition result1 = (new CPosition(edgePoint)).add(vector5);
        CPosition result2 = (new CPosition(edgePoint)).subtract(vector5);

        CPosition result;
        CPosition center = this.getCenter();

        if( result2.getDistanceTo(center) > result1.getDistanceTo(center)) {
            result = result2;
        }
        else {
            result = result1;
        }

        System.out.println("prePoint="+prePoint+", edgePoint=" + edgePoint + ", postPoint="+postPoint+" -> vector1=" + vector1 + ", vector2=" + vector2 + ", vector3=" + vector3 + ", vector4=" + vector4 + ", vector5=" + vector5 + ", -> result1=" + result1 + ", result2=" + result2 + ", result=" + result );

        return result;
    }


    /**
     * get the Waypoints of the obstacle
     * @param edgeNumber
     * @return
     */
    private CPosition getWaypoint2(int edgeNumber) {

        CPosition edgePoint = aoPosition.elementAt(edgeNumber);
        CPosition prePoint = aoPosition.elementAt((edgeNumber - 1 < 0 ? aoPosition.size() - 1 : edgeNumber - 1));
        CPosition postPoint = aoPosition.elementAt((edgeNumber + 1 >= aoPosition.size() ? 0 : edgeNumber + 1));

        assert(edgePoint != null);
        assert(prePoint != null);
        assert(postPoint != null);

        Double alpha = CMathFunctions.angleBetween2Lines(prePoint, edgePoint, edgePoint, postPoint);
        Double beta = CMathFunctions.angleBetween2Lines(prePoint, edgePoint, edgePoint, new CPosition(edgePoint.getX() + 10, edgePoint.getY()));

        //beta = (beta > Math.PI ?  Math.PI * 2 - beta : beta);


        Double gamma = (alpha / 2) + beta; // * (beta <= Math.PI/4 || beta >= Math.PI*(3/4) ? -1 :  1 ); // beta + (alpha / 2) + Math.PI / 2;



        Double delta = gamma + Math.PI; // (Math.PI - gamma);

        //CPosition directionVector = this.getDirectionVectorOfCenter(edgePoint);
        CPosition center = this.getCenter();
        /*


        Double xDiff = edgePoint.getX() - center.getX();
        Double yDiff = edgePoint.getY() - center.getY();

        if(xDiff >= 0  && yDiff >= 0) {
            //delta += Math.PI;
        }
        if(xDiff >= 0  && yDiff < 0) {
            delta -= beta; // + Math.PI / 2;+
            gamma -= beta;
        }
        if(xDiff < 0  && yDiff >= 0) {
            delta += beta; //+ Math.PI / 2;
            gamma += beta;
        }
        else {

        }
        */
        //if(delta > Math.PI) {
        //    delta = (Math.PI * 2) - delta;
        //}

        /*
        Double distanceToLine = center.getDistanceToLine(prePoint, postPoint, true);
        Double distanceToEdge = center.getDistanceTo(edgePoint);

        boolean isStumpf = distanceToEdge < distanceToLine;
        */



        Double xDelta1 = Math.cos(delta) * this.dDistToEdgeC; //* (xDiff > 0 ? 1 : -1);
        Double yDelta1 = Math.sin(delta) * this.dDistToEdgeC; //* (yDiff > 0 ? 1 : -1);
        Double xDelta2 = Math.cos(gamma) * this.dDistToEdgeC; //* (xDiff > 0 ? 1 : -1);
        Double yDelta2 = Math.sin(gamma) * this.dDistToEdgeC; //* (yDiff > 0 ? 1 : -1);

        CPosition result1 = new CPosition(edgePoint.getX() + xDelta1, edgePoint.getY() + yDelta1);
        CPosition result2 = new CPosition(edgePoint.getX() + xDelta2, edgePoint.getY() + yDelta2);
        CPosition result;

        if( result2.getDistanceTo(center) > result1.getDistanceTo(center)) {
            result = result2;
        }
        else {
            result = result1;
        }

        DecimalFormat df = new DecimalFormat("#.####");
        System.out.println("prePoint="+prePoint+", edgePoint=" + edgePoint + ", postPoint="+postPoint+" -> alpha=" + df.format(Math.toDegrees(alpha)) + ", beta=" + df.format(Math.toDegrees(beta)) + ", gamma=" + df.format(Math.toDegrees(gamma)) + ", delta=" + df.format(Math.toDegrees(delta)) + " -> results=" + result1 );

        return result;
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

    /**
     * see http://stackoverflow.com/questions/2014859/scaling-a-2d-polygon-with-the-mouse
     * http://algs4.cs.princeton.edu/91primitives/Polygon.java.html
     * http://harmoniccode.blogspot.ch/2011/06/just-scale-it.html
     * @return
     */
    private CPosition getWayPoint3(int edgeNumber) {

        CPosition edgePoint = aoPosition.elementAt(edgeNumber);
        CPosition center = this.getCenter();

        assert(edgePoint != null);
        assert(center != null);

        double scaleFactor = 1.2;

        CPosition distance = new CPosition(edgePoint.getX() - center.getX(), edgePoint.getY() - center.getY());

        CPosition result = new CPosition(scaleFactor * distance.getX() + center.getX(), scaleFactor * distance.getY() + center.getY());

        return result;
    }

    private Vector<CPosition> getWayPoint4() {


        final double scaleFactor = 1.2;
        final CPosition centroid = calcCenterOfMass();
        //final AffineTransform scaleTransform = AffineTransform.getTranslateInstance((1.0 - scaleFactor) * centroid.getX(), (1.0 - scaleFactor) * centroid.getY());

        int[] xPoints = new int[this.aoPosition.size()];
        int[] yPoints = new int[this.aoPosition.size()];

        for(int i=0; i<this.aoPosition.size(); i++) {
            xPoints[i] = this.aoPosition.elementAt(i).getX().intValue();
            yPoints[i] = this.aoPosition.elementAt(i).getY().intValue();
        }


        AffineTransform rotateTransform = AffineTransform.getTranslateInstance((1.0 - scaleFactor) * centroid.getX(), (1.0 - scaleFactor) * centroid.getY());
        Vector<CPosition> result = new Vector<CPosition>();

        for(int i=0; i<this.aoPosition.size(); i++){
            Point p = new Point(xPoints[i], yPoints[i]);
            rotateTransform.transform(p,p);

            result.add(new CPosition(p.x, p.y));
        }

        return result;
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

    /**
     * Returns a double that represents the area of the given point array of a polygon
     * @return a double that represents the area of the given point array of a polygon
     */
    private double calcSignedPolygonArea()
    {
        int i;
        int j;
        double area = 0;

        for (i = 0; i < this.aoPosition.size(); i++)
        {
            j = (i + 1) % this.aoPosition.size();
            area += this.aoPosition.elementAt(i).getX() * this.aoPosition.elementAt(j).getY();
            area -= this.aoPosition.elementAt(i).getY() * this.aoPosition.elementAt(j).getX();
        }
        area /= 2.0;

        return (area);
        //return(area < 0 ? -area : area); for unsigned
    }

    /**
     * Returns a Point2D object that represents the center of mass of the given point array which represents a
     * polygon.
     * @return a Point2D object that represents the center of mass of the given point array
     */
    public CPosition calcCenterOfMass()
    {
        double cx = 0;
        double cy = 0;
        double area = calcSignedPolygonArea();
        int i;
        int j;

        double factor = 0;
        for (i = 0; i < this.aoPosition.size(); i++)
        {
            j = (i + 1) % this.aoPosition.size();
            factor = (this.aoPosition.elementAt(i).getX() * this.aoPosition.elementAt(j).getY() - this.aoPosition.elementAt(j).getX() * this.aoPosition.elementAt(i).getY());
            cx += (this.aoPosition.elementAt(i).getX() + this.aoPosition.elementAt(j).getX()) * factor;
            cy += (this.aoPosition.elementAt(i).getY() + this.aoPosition.elementAt(j).getY()) * factor;
        }
        area *= 6.0f;
        factor = 1 / area;
        cx *= factor;
        cy *= factor;

        return new CPosition(cx, cy);
    }

    /**
     * Returns a scaled version of the given shape, calculated by the given scale factor.
     * The scaling will be calculated around the centroid of the shape.
     * @param SHAPE
     * @param SCALE_FACTOR
     * @return a scaled version of the given shape, calculated around the centroid by the given scale factor.

    public Shape scale(final double SCALE_FACTOR)
    {
        final CPosition CENTROID = calcCenterOfMass();
        final AffineTransform TRANSFORM = AffineTransform.getTranslateInstance((1.0 - SCALE_FACTOR) * CENTROID.getX(), (1.0 - SCALE_FACTOR) * CENTROID.getY());
        TRANSFORM.scale(SCALE_FACTOR, SCALE_FACTOR);
        return TRANSFORM.createTransformedShape(SHAPE);
    }

    /**
     * Returns a scaled version of the given shape, calculated by the given scale factor.
     * The scaling will be calculated around the given point.
     * @param SHAPE
     * @param SCALE_FACTOR
     * @param SCALE_CENTER
     * @return a scaled version of the given shape, calculated around the given point with the given scale factor.

    public Shape scale(final Shape SHAPE, final double SCALE_FACTOR, final Point2D SCALE_CENTER)
    {
        final AffineTransform TRANSFORM = AffineTransform.getTranslateInstance((1.0 - SCALE_FACTOR) * SCALE_CENTER.getX(), (1.0 - SCALE_FACTOR) * SCALE_CENTER.getY());
        TRANSFORM.scale(SCALE_FACTOR, SCALE_FACTOR);
        return TRANSFORM.createTransformedShape(SHAPE);
    }
     */
}
