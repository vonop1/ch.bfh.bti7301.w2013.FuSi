package Util;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:14
 */
public class CPosition implements Comparable<CPosition> {
    private Double dX;
    private Double dY;

    public CPosition(int X, int Y)
    {
        this.dX = Double.valueOf(X);
        this.dY = Double.valueOf(Y);
    }

    public CPosition(Double X, Double Y)
    {
        this.dX = X;
        this.dY = Y;
    }

    public Double getX() {
        return dX;
    }

    public Double getY() {
        return dY;
    }

    @Override
    public int compareTo(CPosition o) {
        int returnValue = this.dX.compareTo(o.getX());

        if(returnValue == 0) {
            returnValue = this.dY.compareTo(o.getY());
        }

        return returnValue;
    }

    public Double getDistanceTo(CPosition other) {
        return calcDistance(this, other);
    }

    public boolean isNearBy(CPosition other, Double radius) {

        if( Math.abs(other.getX() - this.getX()) < radius) {
            if( Math.abs(other.getY() - this.getY()) < radius) {
                return true;
            }
        }
        return false;

    }

    public Double getDistanceToLine(CPosition lineStartPos, CPosition lineEndPos) {
        return calcLineToPointDistance(lineStartPos, lineEndPos, this, true);
    }

    /**
     * Computes the dot product AB . AC
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @return dot product as Double
     */
    public static Double calcDotProduct(CPosition pointA, CPosition pointB, CPosition pointC)
    {
        Double ab_x = pointB.getX() - pointA.getX();
        Double ab_y = pointB.getY() - pointA.getY();
        Double bc_x = pointC.getX() - pointB.getX();
        Double bc_y = pointC.getY() - pointB.getY();

        Double dot = ab_x * bc_x + ab_y * bc_y;

        return dot;
    }

    /**
     * Computes the cross product AB x AC
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @return the cross product as Double
     */
    public static Double calcCrossProduct(CPosition pointA, CPosition pointB, CPosition pointC)
    {
        Double ab_x = pointB.getX() - pointA.getX();
        Double ab_y = pointB.getY() - pointA.getY();
        Double ac_x = pointC.getX() - pointA.getX();
        Double ac_y = pointC.getY() - pointA.getY();

        Double cross = ab_x * ac_y - ab_y * ac_x;

        return cross;
    }

    /**
     * Compute the distance from A to B
     * @param pointA point A
     * @param pointB point B
     * @return the distance between the two points as Double
     */
    public static Double calcDistance(CPosition pointA, CPosition pointB)
    {
        Double deltaX = pointA.getX() - pointB.getX();
        Double deltaY = pointA.getY() - pointB.getY();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Compute the distance from the line AB to point C, if isInfiniteLine is true, line AB is infinite an not a segment
     * @param pointA point A
     * @param pointB point B
     * @param pointC point C
     * @param isInfiniteLine true if the line is infinite, false if the linie is a finite segment
     * @return the distance from point C to line AB as double
     */
    public static Double calcLineToPointDistance(CPosition pointA, CPosition pointB, CPosition pointC, Boolean isInfiniteLine)
    {
        Double dist = calcCrossProduct(pointA, pointB, pointC) / calcDistance(pointA, pointB);
        if (!isInfiniteLine) {
            double dot1 = calcDotProduct(pointA, pointB, pointC);
            if (dot1 > 0) {
                return calcDistance(pointB, pointC);
            }

            double dot2 = calcDotProduct(pointB, pointA, pointC);
            if (dot2 > 0) {
                return calcDistance(pointA, pointC);
            }
        }
        return Math.abs(dist);
    }

}
