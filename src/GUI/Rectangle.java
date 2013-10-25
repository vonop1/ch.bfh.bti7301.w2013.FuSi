package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created with IntelliJ IDEA.
 * User: pvonow
 * Date: 19.10.13
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */

public class Rectangle {
    private ZRectangle zrect;

    public Rectangle() {
        super();
        //setLayout(null);
    }

    public void newRectangle(){
        zrect = new ZRectangle(50, 50, 50, 50);
    }
    /*
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(200, 0, 0));
        g2d.fill(zrect);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
    */
    class ZRectangle extends Rectangle2D.Float {

        public ZRectangle(float x, float y, float width, float height) {

            setRect(x, y, width, height);
        }

        public boolean isHit(float x, float y) {

            if (getBounds2D().contains(x, y)) {

                return true;
            } else {

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
}
