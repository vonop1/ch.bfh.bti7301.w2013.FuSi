package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    //ArrayList with all resizable and movable polygons
    private ArrayList<ZPolygon> zpolys = new ArrayList<ZPolygon>();

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

        for(ZPolygon zpoly : zpolys){
            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(zpoly);

            g2d.setColor(Color.BLACK);
        }
        //create new white rectangles with small black sizing rectangle
        for(ZRectangle zrect : zrects){

            points = zrect.getPoints();
            zrect.setFrameFromDiagonal(points[0], points[1]);
            g2d.setColor(Color.WHITE);
            g2d.fillRect((int)zrect.getX(), (int)zrect.getY(), (int)zrect.getWidth(),(int)zrect.getHeight());
            //g2d.setColor(new Color(255, 255, 255));
            //g2d.fill(zrect);
            g2d.setColor(Color.BLACK);
            //g2.fill(new Rectangle2D.Double(x, y, SIZE, SIZE));
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
        zrects.add(new ZRectangle());
        repaint();
    }

    public void addPolygon(int edges){
        zpolys.add(new ZPolygon(edges));
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

                for(ZPolygon zpoly : zpolys){
                    if (zpoly.isHit(x, y)) {
                        zpoly.addXY(dx, dy);
                        repaint();
                    }
                }


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




