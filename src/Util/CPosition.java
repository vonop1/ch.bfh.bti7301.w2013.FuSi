package Util;

/**
 * Created with IntelliJ IDEA.
 * User: bohnp1
 * Date: 27.09.13
 * Time: 14:14
 */
public class CPosition {
    private float fX;
    private float fY;

    public CPosition(float X, float Y)
    {
        this.fX = X;
        this.fY = Y;
    }

    public float getX() {
        return fX;
    }

    public void setX(float fX) {
        this.fX = fX;
    }

    public float getY() {
        return fY;
    }

    public void setY(float fY) {
        this.fY = fY;
    }
}
