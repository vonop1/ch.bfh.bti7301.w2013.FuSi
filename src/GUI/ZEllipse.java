package GUI;

import java.awt.geom.Ellipse2D;

/**
 * Created with IntelliJ IDEA.
 * User: pvonow
 * Date: 01.11.13
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class ZEllipse extends Ellipse2D.Float {

    public ZEllipse(float x, float y, float width, float height) {

        setFrame(x, y, width, height);
    }

    public boolean isHit(float x, float y) {

        if (getBounds2D().contains(x, y)) {
            System.out.println("true");
            return true;
        } else {
            System.out.println("false");
            return false;
        }
    }

    public void addX(float x) {

        this.x += x;
    }

    public void addY(float y) {

        this.y += y;
    }

    public void addWidth(float w) {

        this.width += w;
    }

    public void addHeight(float h) {

        this.height += h;
    }
}
