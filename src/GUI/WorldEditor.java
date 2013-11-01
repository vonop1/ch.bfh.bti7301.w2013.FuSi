package GUI;

import com.sun.org.apache.xpath.internal.operations.Or;

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

    private int SIZE = 8;

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
        Point2D points[];
        double x;
        double y;

        //create new white rectangles with small black sizing rectangle
        for(ZRectangle zrect : zrects){

            points = zrect.getPoints();
            zrect.setFrameFromDiagonal(points[0], points[1]);
            g2d.setColor(new Color(255, 255, 255));
            g2d.fillRect((int)zrect.getX(), (int)zrect.getY(), (int)zrect.getWidth(),(int)zrect.getHeight());
            //g2d.setColor(new Color(255, 255, 255));
            //g2d.fill(zrect);
            g2d.setColor(new Color(0, 0, 0));
            //g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
            x = zrect.getX() + zrect.getWidth() - SIZE / 2;
            y = zrect.getY() + zrect.getHeight() - SIZE / 2;
            g2d.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
            //System.out.println(zrect.toString());
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    public void addRectangle(){
        zrects.add(new ZRectangle());
        repaint();
    }

    private class MovingAdapter extends MouseAdapter {

            private int x;
            private int y;
            private boolean resize;
            private int i, j;

            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                x = e.getX();
                y = e.getY();
                int i = 0;

                for (ZRectangle zrect : zrects) {

                    double rx = zrect.getX() + zrect.getWidth() - SIZE / 2;
                    double ry = zrect.getY() + zrect.getHeight() - SIZE / 2;
                    Rectangle2D r = new Rectangle2D.Double(rx, ry, SIZE, SIZE);

                    if (r.contains(p)) {
                        resize = true;
                        j = i;
                        return;
                    }
                    System.out.println(i);
                    i++;

                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                resize = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                int dx = e.getX() - x;
                int dy = e.getY() - y;
                Point2D points[];

                i = 0;
                for (ZRectangle zrect : zrects){
                    if (zrect.isHit(x, y)) {
                        zrect.addXY(dx, dy);
                        repaint();
                    }

                    if(resize && i == j){

                        points = zrect.getPoints();
                        points[1] = e.getPoint();
                        zrect.setPoints(points);
                        repaint();
                    }
                    i++;
                }
                /*
                if (zell.isHit(x, y)) {

                    zell.addX(dx);
                    zell.addY(dy);
                    repaint();
                }
                */

                x += dx;
                y += dy;

            }
        }
}

    class ZEllipse extends Ellipse2D.Float {

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

    class ZRectangle extends Rectangle2D.Float {

        private Point2D[] points;

        public ZRectangle() {  //float x, float y, float width, float height
            //setRect(x, y, width, height);
            points = new Point2D[2];
            points[0] = new Point2D.Double(50, 50);
            points[1] = new Point2D.Double(100, 100);

        }

        public Point2D[] getPoints(){
            return this.points;
        }

        public void setPoints(Point2D[] points){
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
            for(int i = 0; i < points.length; i++) {
                points[i] = new Point2D.Double(points[i].getX() + x, points[i].getY() + y);
            }
        }
    }


