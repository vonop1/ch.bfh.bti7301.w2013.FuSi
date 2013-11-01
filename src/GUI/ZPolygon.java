package GUI;

import java.awt.Polygon;

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

    public boolean isHit(float x, float y) {
        if (contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }
    public void addXY(int x, int y) {
        translate(x, y);
        /*
        for(int i=0; i<xpoints.length; i++){
            xpoints[i] += x;
            ypoints[i] += y;
        }
         */
    }
}
