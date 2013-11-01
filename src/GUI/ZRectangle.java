package GUI;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created with IntelliJ IDEA.
 * User: pvonow
 * Date: 01.11.13
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class ZRectangle extends Rectangle2D.Float {

    private Point2D[] points;

    public ZRectangle() {
        points = new Point2D[2];
        points[0] = new Point2D.Double(50, 50);
        points[1] = new Point2D.Double(100, 100);

    }

    public Point2D[] getPoints() {
        return this.points;
    }

    public void setPoints(Point2D[] points) {
        this.points = points;
    }

    public boolean isHit(float x, float y) {

        if (getBounds2D().contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    public void addXY(double x, double y) {
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point2D.Double(points[i].getX() + x, points[i].getY() + y);
        }
    }
}
