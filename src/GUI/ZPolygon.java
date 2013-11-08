package GUI;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: pvonow
 * Date: 01.11.13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class ZPolygon extends Polygon {

    private static int X_POS = 75;
    private static int Y_POS = 75;
    private static int RADIUS = 50;

    public ZPolygon(int n){

        for (int i=0; i<n; i++) {

            addPoint((int) (X_POS + RADIUS * Math.cos(i * 2 * Math.PI / n)), (int) (Y_POS + RADIUS * Math.sin(i * 2 * Math.PI / n)));
        }
    }

    public ZPolygon(int[] xp, int[] yp, int np){

        xpoints = xp;
        ypoints = yp;
        npoints = np;
    }

    public boolean isHit(float x, float y) {
        if (contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }
    public void translateXY (int x, int y) {
        translate(x, y);
    }

    /**
     * finds the center of a polygon
     * @return the center of the polygon
     */
    public Point getCenter() {

        Point[] polygon = new Point[npoints];

        for (int i = 0; i < npoints; i++){
            polygon[i] = new Point(this.xpoints[i], this.ypoints[i]);
        }

        double cx = 0, cy = 0, area = 0, factor = 0;
        int j = 0;
        for (int i = 0; i < npoints; i++) {
            j = (i + 1) % npoints;
            area += polygon[i].x * polygon[j].y;
            area -= polygon[i].y * polygon[j].x;
        }
        area /= 2.0;
        area = (Math.abs(area));

        for (int i = 0; i < npoints; i++) {
            j = (i + 1) % npoints;
            factor = (polygon[i].x * polygon[j].y - polygon[j].x * polygon[i].y);
            cx += (polygon[i].x + polygon[j].x) * factor;
            cy += (polygon[i].y + polygon[j].y) * factor;
        }
        factor = 1.0 / (6.0 * area);
        cx *= factor;
        cy *= factor;
        return new Point((int) Math.abs(Math.round(cx)), (int) Math.abs(Math.round(cy)));
    }

    public void transform (double deltaTheta, Point centerPt) {
        AffineTransform rotateTransform = AffineTransform.getRotateInstance(deltaTheta, centerPt.x, centerPt.y);

        int[] rx = new int[xpoints.length];
        int[] ry = new int[ypoints.length];

        for(int i=0; i<npoints; i++){
            Point p = new Point(xpoints[i], ypoints[i]);
            rotateTransform.transform(p,p);
            rx[i]=p.x;
            ry[i]=p.y;
        }
        xpoints = rx;
        ypoints = ry;

    }
}
