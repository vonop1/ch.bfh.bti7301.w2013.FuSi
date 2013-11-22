package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Created with IntelliJ IDEA.
 * User: vonop1
 * Date: 11.10.13
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */

public class WorldEditor extends JPanel{

    //ArrayList with all resizable and movable rectangles
    private ArrayList<ZRectangle> zrects = new ArrayList<ZRectangle>();

    //ArrayList with all resizable and movable polygons
    private ArrayList<ZPolygon> zpolys = new ArrayList<ZPolygon>();
    private ZPolygon activePolygon = null;
    private int polygonIndex = 0;
    private int npoint = 0;
    private int SIZE = 8;

    public WorldEditor() {
        super();
    }

    public void setupEditor() {
        MovingAdapter ma = new MovingAdapter();
        addMouseMotionListener(ma);
        addMouseListener(ma);

        setDoubleBuffered(true);
    }


    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Point2D points[];
        double x;
        double y;

        //create new white polygons with small black translate/resizing ellipses
        //paint the active polygon grey
        for(ZPolygon zpoly : zpolys){
            if (!zpoly.equals(activePolygon)) {
                g2d.setColor(Color.WHITE);
                g2d.fill(zpoly);
            }
            else {
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill(zpoly);
            }
            g2d.setColor(Color.BLACK);
            Ellipse2D.Double ellipse = new Ellipse2D.Double((int)zpoly.getCenter().getX() - SIZE / 2, (int) zpoly.getCenter().getY()- SIZE / 2, SIZE, SIZE);
            g2d.fill(ellipse);
            for(int i=0; i<zpoly.npoints; i++){
                ellipse = new Ellipse2D.Double(zpoly.xpoints[i] - SIZE / 2, zpoly.ypoints[i] - SIZE / 2, SIZE, SIZE);
                g2d.fill(ellipse);
            }
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

    public void saveWorld(){
        CXMLFunctions.saveXMLFile(zpolys);
    }

    public void loadWorld(){
        CXMLFunctions.loadXMLFile();
    }

    private class MovingAdapter extends MouseAdapter {

        private Point pressPt, centerPt;
        private int pressPtX, pressPtY;
        private double pressTheta;
        private boolean resize, translate;
        private int currentShape, clickedShape;
        private Ellipse2D.Double ellipse;

        //temporary polygon
        private Polygon polygon = null;

        @Override
        public void mousePressed(MouseEvent e) {
            pressPt = e.getPoint();
            pressPtX = e.getX();
            pressPtY = e.getY();
            translate = false;
            resize = false;
            polygonIndex = 0;
            int currentShape = 0;

            for (int k = zpolys.size() - 1; k >= 0; k--) {
                for (int i = 0; i < zpolys.get(k).npoints; i++) {
                    ellipse = new Ellipse2D.Double(zpolys.get(k).xpoints[i] - SIZE / 2, zpolys.get(k).ypoints[i] - SIZE / 2, SIZE, SIZE);
                    if (ellipse.contains(pressPt)) {
                        resize = true;
                        npoint = i;
                        polygonIndex = k;
                        break;
                    }
                }
                if(resize){
                    break;
                }
                ellipse = new Ellipse2D.Double((int) zpolys.get(k).getCenter().getX() - SIZE / 2, (int) zpolys.get(k).getCenter().getY() - SIZE / 2, SIZE, SIZE);
                if (ellipse.contains(pressPt)) {
                    translate = true;
                } else if (zpolys.get(k).contains(e.getPoint())) {
                    polygonIndex = k;
                    polygon = zpolys.get(k);
                    activePolygon = new ZPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
                    zpolys.remove(k);
                    zpolys.add(k, activePolygon);
                    repaint();
                    centerPt = activePolygon.getCenter();
                    pressTheta = Math.atan2(pressPt.y - centerPt.y, pressPt.x - centerPt.x);
                    break;
                }
            }
            activePolygon = null;
            repaint();

            for (ZRectangle zrect : zrects) {

                double rx = zrect.getX() + zrect.getWidth() - SIZE / 2;
                double ry = zrect.getY() + zrect.getHeight() - SIZE / 2;
                Rectangle2D r = new Rectangle2D.Double(rx, ry, SIZE, SIZE);

                if (r.contains(pressPt)) {
                    resize = true;
                    clickedShape = currentShape;
                    return;
                }
                currentShape++;

            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            translate = false;
            resize = false;
            activePolygon = null;
            polygon = null;
            npoint = 0;
            polygonIndex=0;
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            Point dragPt = e.getPoint();
            int dx = e.getX() - pressPtX;
            int dy = e.getY() - pressPtY;
            Point2D points[];

            polygon = zpolys.get(polygonIndex);
            for (ZPolygon zpoly : zpolys){
                if(resize && zpoly == polygon){
                    zpoly.addXY(dx, dy, npoint);
                    repaint();
                    break;
                }else if(zpoly.isHit(pressPtX, pressPtY)){
                    if(translate){
                        zpoly.translateXY(dx, dy);
                        repaint();
                        break;
                    } else {
                        double dragTheta = Math.atan2(dragPt.y - centerPt.y, dragPt.x - centerPt.x);
                        double deltaTheta = dragTheta - pressTheta;
                        activePolygon = new ZPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
                        activePolygon.transform(deltaTheta, centerPt);
                        zpolys.remove(polygonIndex);
                        zpolys.add(polygonIndex, activePolygon);
                        repaint();
                        break;
                    }
                }
            }



            for (ZRectangle zrect : zrects) {
                if (zrect.isHit(pressPtX, pressPtY)) {
                    zrect.addXY(dx, dy);
                    repaint();
                }

                if (resize && currentShape == clickedShape) {

                    points = zrect.getPoints();
                    points[1] = e.getPoint();
                    zrect.setPoints(points);
                    repaint();
                }
                currentShape++;
            }

            pressPtX += dx;
            pressPtY += dy;

        }
    }
}




