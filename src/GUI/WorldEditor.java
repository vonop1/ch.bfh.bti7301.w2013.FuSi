package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

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
    private ZPolygon rotatedPolygon = null;
    private int polygonIndex = 0;
    private int SIZE = 6;

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
            if (!zpoly.equals(rotatedPolygon)) {
                g2d.setColor(Color.WHITE);
                g2d.fill(zpoly);
            }
            else {
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(zpoly);
            }
            g2d.setColor(Color.BLACK);
            g2d.fillRect((int)zpoly.getCenter().getX() - SIZE / 2, (int) zpoly.getCenter().getY()- SIZE / 2, SIZE, SIZE);
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

            private int mx;
            private int my;
            private boolean resize;
            private int i, j;

        private Point centerPt;
        private Point pressPt;
        private Polygon polygon = null;
        private double pressTheta;

            @Override
            public void mousePressed(MouseEvent e) {
                pressPt = e.getPoint();
                mx = e.getX();
                my = e.getY();
                int i = 0;

                for (int k = zpolys.size() - 1; k >= 0; k--) {
                    if (zpolys.get(k).contains(e.getPoint())) {
                        polygonIndex = k;
                        polygon = zpolys.get(k);
                        rotatedPolygon = new ZPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
                        zpolys.remove(k);
                        zpolys.add(k, rotatedPolygon);
                        repaint();

                        Rectangle rect = rotatedPolygon.getBounds();
                        centerPt = new Point(rect.x + rect.width/2, rect.y + rect.height/2);
                        pressTheta = Math.atan2(pressPt.y - centerPt.y, pressPt.x - centerPt.x);

                        return;
                    }
                }
                rotatedPolygon = null;
                repaint();


                for (ZRectangle zrect : zrects) {

                    double rx = zrect.getX() + zrect.getWidth() - SIZE / 2;
                    double ry = zrect.getY() + zrect.getHeight() - SIZE / 2;
                    Rectangle2D r = new Rectangle2D.Double(rx, ry, SIZE, SIZE);

                    if (r.contains(pressPt)) {
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
                rotatedPolygon = null;
                polygon = null;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                Point dragPt = e.getPoint();


                for(ZPolygon zpoly : zpolys){
                    if (zpoly.isHit(mx, my)) {
                        double dragTheta = Math.atan2(dragPt.y - centerPt.y, dragPt.x - centerPt.x);
                        double deltaTheta = dragTheta - pressTheta;
                rotatedPolygon = new ZPolygon(polygon.npoints);
                rotatedPolygon.transform(deltaTheta, centerPt);
                zpolys.remove(polygonIndex);
                zpolys.add(polygonIndex, rotatedPolygon);
                    }
                }
                repaint();

                int dx = e.getX() - mx;
                int dy = e.getY() - my;
                Point2D points[];

//                for(ZPolygon zpoly : zpolys){
//                    if (zpoly.isHit(x, y)) {
//                        zpoly.translateXY(dx, dy);
//                        repaint();
//                    }
//                }


                i = 0;
                for (ZRectangle zrect : zrects){
                    if (zrect.isHit(mx, my)) {
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

                mx += dx;
                my += dy;

            }
        }
}




