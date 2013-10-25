package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Created with IntelliJ IDEA.
 * User: pvonow
 * Date: 11.10.13
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */

public class WorldEditor extends JPanel{

    //ArrayList with all resizable and movable rectangles
    private ArrayList<ZRectangle> zrects = new ArrayList<ZRectangle>();

    //private Point2D[] points;
    private int SIZE = 8;
    private int pos;
    private ZEllipse zell;

    public WorldEditor() {
        super();
    }

    public void setupEditor() {
        MovingAdapter ma = new MovingAdapter();
        addMouseMotionListener(ma);
        addMouseListener(ma);
        //addMouseWheelListener(new ScaleHandler());

        setDoubleBuffered(true);
    }


    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        double x = 0;
        double y = 0;

        for(ZRectangle zrect : zrects){
            g2d.setColor(new Color(255, 255, 255));
            g2d.fill(zrect);
            x = zrect.getX() - SIZE / 2;
            y = zrect.getY() - SIZE / 2;
            g2d.setColor(new Color(0, 0, 0));
            g2d.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
            x = zrect.getX() + zrect.getWidth() - SIZE / 2;
            y = zrect.getY() + zrect.getHeight() - SIZE / 2;
            g2d.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    public void addRectangle(){
        zrects.add(new ZRectangle(50,50,50,50));
        repaint();
    }

    private class MovingAdapter extends MouseAdapter {

            private int x;
            private int y;
            private int i;

            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                x = e.getX();
                y = e.getY();
                i = 0;

                for (ZRectangle zrect : zrects) {
                    double x = zrect.getX() - SIZE / 2;
                    double y = zrect.getY() - SIZE / 2;
                    Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);

                    if (r.contains(p)) {
                        pos = i;
                        return;
                    }
                    i++;
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                pos = -1;
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                int dx = e.getX() - x;
                int dy = e.getY() - y;

                for (ZRectangle zrect : zrects){
                    if (zrect.isHit(x, y)) {

                        zrect.addX(dx);
                        zrect.addY(dy);
                        repaint();
                    }
                }

                if (zell.isHit(x, y)) {

                    zell.addX(dx);
                    zell.addY(dy);
                    repaint();
                }

                x += dx;
                y += dy;

                if (pos == -1) {
                    return;
                }

                ZRectangle zrect = zrects.get(i);

                //points[pos] = e.getPoint();
                repaint();
            }
        }
}

    class ZEllipse extends Ellipse2D.Float {

        public ZEllipse(float x, float y, float width, float height) {

            setFrame(x, y, width, height);
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


    /*
    class ScaleHandler implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            int x = e.getX();
            int y = e.getY();

            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                for (ZRectangle zrect : zrects){
                    if (zrect.isHit(x, y)) {

                        float amount =  e.getWheelRotation() * 5f;
                        zrect.addWidth(amount);
                        zrect.addHeight(amount);
                        repaint();
                    }
                }
                if (zell.isHit(x, y)) {

                    float amount =  e.getWheelRotation() * 5f;
                    zell.addWidth(amount);
                    zell.addHeight(amount);
                    repaint();
                }
            }
        }
    }
    */


