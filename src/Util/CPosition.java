package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:14
 */
public class CPosition {
    private double dX;
    private double dY;

    public CPosition(double X, double Y)
    {
        this.dX = X;
        this.dY = Y;
    }

    public double getX() {
        return dX;
    }

    public void setX(float fX) {
        this.dX = fX;
    }

    public double getY() {
        return dY;
    }

    public void setY(float fY) {
        this.dY = fY;
    }
}
