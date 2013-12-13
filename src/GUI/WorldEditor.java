package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: vonop1
 * Date: 11.10.13
 * Time: 14:28
 * To change this template use File | Settings | File Templates.
 */

public class WorldEditor extends JPanel{

    //ArrayList with all resizable and movable polygons
    private ArrayList<CPolygon> cPolys = new ArrayList<CPolygon>();
    private CPolygon activePolygon = null;
    private int npoint = 0;
    private int SIZE = 8;

    //ArrayList with all walkers
    private ArrayList<Point2D.Double[]> walkers = new ArrayList<Point2D.Double[]>();

    /**
     * constructor for the world editor
     */
    public WorldEditor() {
        super();
    }

    /**
     * setup the editor with mouse listener and initialize variables
     */
    public void setupEditor() {
        MovingAdapter ma = new MovingAdapter();
        addMouseMotionListener(ma);
        addMouseListener(ma);

        setDoubleBuffered(true);
        activePolygon = null;
        npoint = 0;
    }

    /**
     * draw the world editor objects
     * @param g the Graphics object
     */
    private void doDrawing(Graphics g) {

        //convert our graphics object to graphics2D
        Graphics2D g2d = (Graphics2D) g;

        //create new white polygons with small black translate/resizing ellipses
        //paint the active polygon grey
        if (cPolys.size() > 0){
            for(CPolygon cPoly : cPolys){
                if (!cPoly.equals(activePolygon)) {
                    g2d.setColor(Color.WHITE);
                    g2d.fill(cPoly);
                }
                else {
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fill(cPoly);
                }
                g2d.setColor(Color.BLACK);
                Ellipse2D.Double ellipse = new Ellipse2D.Double((int)cPoly.getCenter().getX() - SIZE / 2, (int) cPoly.getCenter().getY()- SIZE / 2, SIZE, SIZE);
                g2d.fill(ellipse);
                for(int i=0; i<cPoly.npoints; i++){
                    ellipse = new Ellipse2D.Double(cPoly.xpoints[i] - SIZE / 2, cPoly.ypoints[i] - SIZE / 2, SIZE, SIZE);
                    g2d.fill(ellipse);
                }
            }
        }

        //create new walkers
        if (walkers.size() > 0){
            for (Point2D.Double walker[] : walkers) {
                g2d.setColor(Color.ORANGE);
                g2d.fillOval((int) walker[0].getX(), (int) walker[0].getY(), SIZE, SIZE);
                g2d.fillOval((int) walker[1].getX(), (int) walker[1].getY(), SIZE, SIZE);
                g2d.drawLine((int) walker[0].getX(), (int) walker[0].getY(), (int) walker[1].getX(), (int) walker[1].getY());
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    /**
     * add a new polygon
     * @param edges number of edges
     */
    public void addPolygon(int edges){
        cPolys.add(new CPolygon(edges));
        repaint();
    }

    /**
     * add a new walker with a start point and an end point
     */
    public void addWalker(){
        Point2D.Double walker[] = new Point2D.Double[2];
        walker[0] = new Point2D.Double(25,25);
        walker[1] = new Point2D.Double(100,100);
        walkers.add(walker);
        repaint();
    }

    /**
     * save the world into a xml file
     */
    public void saveWorld(){
        CXMLFunctions xml = new CXMLFunctions();
        xml.saveXMLFile(cPolys);
    }

    /**
     * load the world from a xml file
     */
    public void loadWorld(){
        CXMLFunctions xml = new CXMLFunctions();
        cPolys = xml.loadXMLFile();
        repaint();
    }

    /**
     *
     */
    private class MovingAdapter extends MouseAdapter {

        //
        private Point pressPt, centerPt;
        private int pressPtX, pressPtY;
        private double lastTheta;
        private boolean resizePolygon, translatePolygon, moveWalkerStart, moveWalkerEnd;
        private Ellipse2D.Double ellipse;


        @Override
        public void mousePressed(MouseEvent e) {

            //save point of mouse pressed event into variable
            pressPt = e.getPoint();
            // get x and y coordinate of the pressed point
            pressPtX = e.getX();
            pressPtY = e.getY();

            //standard: don't move any walker points and no clicked walker at this moment
            moveWalkerEnd = false;
            moveWalkerStart = false;

            //standard: don't translate or resize and no clicked polygon at this moment
            translatePolygon = false;
            resizePolygon = false;

            if(walkers.size() > 0){
                for(Point2D.Double walker[] : walkers){
                    ellipse = new Ellipse2D.Double(walker[0].getX() - SIZE / 2, walker[0].getY() - SIZE / 2, SIZE, SIZE);
                    if(ellipse.contains(pressPt)){
                        break;
                    }
                    ellipse = new Ellipse2D.Double(walker[1].getX() - SIZE / 2, walker[1].getY() - SIZE / 2, SIZE, SIZE);
                    if(ellipse.contains(pressPt)){
                        break;
                    }
                }
            }

            //determine if we have some polygons and if the mouse was inside a polygon
            if(cPolys.size() > 0){
                for (int k = cPolys.size() - 1; k >= 0; k--) {
                    //check if mouse was clicked on a small black resizing ellipse
                    for (int i = 0; i < cPolys.get(k).npoints; i++) {
                        ellipse = new Ellipse2D.Double(cPolys.get(k).xpoints[i] - SIZE / 2, cPolys.get(k).ypoints[i] - SIZE / 2, SIZE, SIZE);
                        //mouse was inside our small black ellipse
                        //break because we don't need to search further
                        if (ellipse.contains(pressPt)) {
                            resizePolygon = true;
                            npoint = i;
                            activePolygon = cPolys.get(k);//polygonIndex = k;
                            break;
                        }
                    }
                    //break we have to resize the polygon with index k
                    if(resizePolygon){
                        break;
                    }

                    //check if mouse was clicked on the small black center ellipse to translatePolygon the polygon
                    ellipse = new Ellipse2D.Double((int) cPolys.get(k).getCenter().getX() - SIZE / 2, (int) cPolys.get(k).getCenter().getY() - SIZE / 2, SIZE, SIZE);
                    if (ellipse.contains(pressPt)) {
                        translatePolygon = true;
                    //else check if mouse was inside a polygon (needed to paint active polygon grey)
                    //calculate the axis between press point and center point of the polygon
                    } else if (cPolys.get(k).contains(e.getPoint())) {
                        activePolygon = cPolys.get(k);
                        cPolys.remove(k);
                        cPolys.add(k, activePolygon);
                        repaint();
                        centerPt = activePolygon.getCenter();
                        lastTheta = Math.atan2(pressPt.y - centerPt.y, pressPt.x - centerPt.x);
                        break;
                    }
                }
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            translatePolygon = false;
            resizePolygon = false;
            activePolygon = null;
            polygon = null;
            npoint = 0;
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            Point dragPt = e.getPoint();
            int dx = e.getX() - pressPtX;
            int dy = e.getY() - pressPtY;

            if(cPolys.size() > 0){
                for (CPolygon cPoly : cPolys){
                    if(resizePolygon && cPoly == activePolygon){
                        cPoly.addXY(dx, dy, npoint);
                        repaint();
                        break;
                    }else if(cPoly.isHit(pressPtX, pressPtY)){
                        if(translatePolygon){
                            cPoly.translateXY(dx, dy);
                            repaint();
                            break;
                        } else {
                            double dragTheta = Math.atan2(dragPt.y - centerPt.y, dragPt.x - centerPt.x);
                            double deltaTheta = dragTheta - lastTheta;
                            lastTheta = dragTheta;
                            activePolygon.transform(deltaTheta, centerPt);
                            repaint();
                            break;
                        }
                    }
                }
            }
            pressPtX += dx;
            pressPtY += dy;

        }
    }
}




