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
        // a classic pythagoras
        return Math.sqrt(Math.pow(other.getX() - this.getX(),2) + Math.pow(other.getY() - this.getY(),2));
    }

    public boolean isPointInRectancleBetween(CPosition other, CPosition point) {

        if(other.getX() > this.getX()) {
            if(!(this.getX() < point.getX() && point.getX() < other.getX())) {
                return false;
            }
        }
        else {
            if(!(other.getX() < point.getX() && point.getX() < this.getX())) {
                return false;
            }
        }

        if(other.getY() > this.getY()) {
            if(!(this.getY() < point.getY() && point.getY() < other.getY())) {
                return false;
            }
        }
        else {
            if(!(other.getY() < point.getY() && point.getY() < this.getY())) {
                return false;
            }
        }

        return true;
    }
}
