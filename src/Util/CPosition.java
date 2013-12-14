package Util;

import com.sun.istack.internal.NotNull;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:14
 */
public class CPosition implements Comparable<CPosition> {
    private Double dX;
    private Double dY;

    public CPosition(Integer X, Integer Y)
    {
        this.dX = Double.valueOf(X);
        this.dY = Double.valueOf(Y);
    }

    public CPosition(Double X, Double Y)
    {
        this.dX = X;
        this.dY = Y;
    }

    public CPosition(CPosition pos)
    {
        this.dX = pos.getX();
        this.dY = pos.getY();
    }

    public CPosition(CPosition pointA, CPosition pointB) {
        this.dX = pointB.getX() - pointA.getX();
        this.dY = pointB.getY() - pointA.getY();
    }

    public Double getX() {
        return dX;
    }

    public Double getY() {
        return dY;
    }

    @Override
    public int compareTo(@NotNull CPosition o) {
        int returnValue = this.dX.compareTo(o.getX());

        if(returnValue == 0) {
            returnValue = this.dY.compareTo(o.getY());
        }

        return returnValue;
    }

    @Override
    public boolean equals (Object obj)
    {
        return  obj != null &&
                obj.getClass() == this.getClass() &&
                ((CPosition)obj).getX().equals(this.getX()) &&
                ((CPosition)obj).getY().equals(this.getY());
    }

    /**
     * calculates the the distance to another point
     * @param other the other Point
     * @return the distance as Double
     */
    public Double getDistanceTo(CPosition other) {
        return CMathFunctions.calcDistance(this, other);
    }

    /**
     * determines if the point is in the radius of another point
     * @param other the other point
     * @param radius the radius
     * @return true if the point is in the radius oder false if not
     */
    public boolean isNearBy(CPosition other, Double radius) {

        if( Math.abs(other.getX() - this.getX()) < radius) {
            if( Math.abs(other.getY() - this.getY()) < radius) {
                return true;
            }
        }
        return false;
    }

    /**
     * calculates the distance to a line
     * @param lineStartPos starting point of the line
     * @param lineEndPos ending point of the line
     * @param isInfiniteLine indicates if the lines is infinite or only between the two points
     * @return the distance as Double
     */
    public Double getDistanceToLine(CPosition lineStartPos, CPosition lineEndPos, Boolean isInfiniteLine) {
        return CMathFunctions.calcLineToPointDistance(lineStartPos, lineEndPos, this, isInfiniteLine);
    }

    /**
     * Intended only for debugging.
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.###");
        return "(" + df.format(this.getX()) + "|" + df.format(this.getY()) + ")";
    }

}
