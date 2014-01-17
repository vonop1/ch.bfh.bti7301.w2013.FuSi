package GUI;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created with IntelliJ IDEA.
 * author: vonop1
 * Date: 01.11.13
 * Time: 14:32
 * To change this template use File | Settings | File Templates.
 */
public class CPolygon extends Polygon {

    //position and size of a new regular polygon
    private static int X_POS = 75;
    private static int Y_POS = 75;
    private static int RADIUS = 50;

    /**
     * Constructor for a regular polygon
     * @param n: the total number of points
     */
    public CPolygon(int n){

        //calculate the points for the rectangle on a given radius
        for (int i=0; i<n; i++) {
            addPoint((int) (X_POS + RADIUS * Math.cos(i * 2 * Math.PI / n)), (int) (Y_POS + RADIUS * Math.sin(i * 2 * Math.PI / n)));
        }
    }

    /**
     * Constructor for a variable polygon
     * @param xpoints: the array of X coordinates
     * @param ypoints: the array of Y coordinates
     * @param npoints: the total number of points
     */
    public CPolygon(int[] xpoints, int[] ypoints, int npoints){
        this.xpoints = xpoints;
        this.ypoints = ypoints;
        this.npoints = npoints;
    }

    /**
     * Tests if the specified coordinates are inside the polygon
     * @param x: the X coordinate
     * @param y: the Y coordinate
     * @return: true if polygon is hit, false if not
     */
    public boolean isHit(double x, double y) {
        return contains(x, y);
    }

    /**
     * Adds value to a given point of the polygon
     * @param x: the value to add to the X coordinate
     * @param y: the value to add to the Y coordinate
     * @param npoint: the point to add the values to
     */
    public void addXY(int x, int y, int npoint) {
        for(int i=0; i<npoints; i++){
            if(i==npoint){
               xpoints[i]+= x;
               ypoints[i]+= y;
            }
        }
    }

    /**
     * Translates the vertices of the Polygon by deltaX along the x axis and by deltaY along the y axis.
     * @param x: the amount to translate along the X axis
     * @param y: the amount to translate along the Y axis
     */
    public void translateXY (int x, int y) {
        translate(x, y);
    }

    /**
     * finds the center of a polygon
     * @return Point with the center of the polygon
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

    /**
     * transforms the polygon along a given center point and an axis
     * @param deltaTheta
     * @param centerPt
     */
    public void transform (double deltaTheta, Point centerPt, CPolygon originalPoly) {
        AffineTransform rotateTransform = AffineTransform.getRotateInstance(deltaTheta, centerPt.x, centerPt.y);

        for(int i=0; i<npoints; i++){
            Point p = new Point(originalPoly.xpoints[i], originalPoly.ypoints[i]);
            rotateTransform.transform(p,p);
            xpoints[i]=p.x;
            ypoints[i]=p.y;
        }


    }

    @Override
    public CPolygon clone () throws CloneNotSupportedException
    {
        return new CPolygon(this.xpoints.clone(), this.ypoints.clone(), this.npoints);
    }

}
