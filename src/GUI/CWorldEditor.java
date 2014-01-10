package GUI;

import Source.CWalker;
import Util.CPosition;

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
 */
public class CWorldEditor extends JPanel{

    //size of the helper ellipses and walker points
    private int SIZE = 8;

    //ArrayList with all resizable and movable polygons, polygon helper variables
    private ArrayList<CPolygon> cPolys = new ArrayList<CPolygon>();
    private CPolygon activePolygon = null;
    private int npoint = 0;

    //ArrayList with all walkers, walker helper variable
    private ArrayList<CEditorWalker> walkers = new ArrayList<CEditorWalker>();
    private CWalker activeWalker = null;

    /**
     * constructor for the world editor
     */
    public CWorldEditor() {
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
            for (CEditorWalker walker : walkers) {
                g2d.setColor(Color.GREEN);
                CDrawHelper.drawPointAsCircle(g2d, walker.getPosition(), (double) SIZE, true);
                g2d.setColor(Color.RED);
                CDrawHelper.drawPointAsCircle(g2d, walker.getTarget(), (double) SIZE, true);
                g2d.setColor(CApplication.WALKER_COLOR);
                CDrawHelper.drawLine(g2d, walker.getPosition(), walker.getTarget());
                g2d.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(walker.getCount()), walker.getPosition().getX().intValue(), walker.getPosition().getY().intValue());
            }
        }

    }

    /**
     * draw the world editor objects
     * @param g the Graphics object
     */
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
     * add new walker with a start point and an end point
     */
    public void addWalker(int count){
        CPosition startPoint = new CPosition(25,25);
        CPosition endPoint = new CPosition(100,100);
        CEditorWalker walker = new CEditorWalker(startPoint, endPoint, count);
        walkers.add(walker);
        repaint();
    }

    /**
     * save the world into a xml file
     */
    public void saveWorld(){
        CXMLConfigFile xml = new CXMLConfigFile();
        xml.saveXMLFile(cPolys, walkers);
    }

    /**
     * load the world from a xml file
     */
    public void loadWorld(){
        CXMLConfigFile xml = new CXMLConfigFile();
        cPolys.clear();
        walkers.clear();
        xml.loadXMLFile(cPolys, walkers);
        repaint();
    }

    /**
     * class used to handle mouse events
     */
    private class MovingAdapter extends MouseAdapter {

        //Location of the pressed point and the center point of active polygon
        private Point pressPt, centerPt;

        //value for x and y location of the pressed point
        private int pressPtX, pressPtY;

        //Theta used to rotated polygons
        private double pressTheta;

        //used to determine what we want to do
        private boolean resizePolygon, translatePolygon, moveWalkerStart, moveWalkerEnd;

        //variable for helper ellipses
        private Ellipse2D.Double ellipse;

        //helper variable to save original poly
        private CPolygon originalPoly = null;

        /**
         * executed if a mouse click occurs
         * @param e the mouse event
         */
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

            //determine if we have some walkers and if the mouse was on a walker
            if(walkers.size() > 0){
                for(CEditorWalker walker : walkers){

                    //inside walker start point
                    ellipse = new Ellipse2D.Double(walker.getPosition().getX() - SIZE / 2, walker.getPosition().getY() - SIZE / 2, SIZE, SIZE);
                    if(ellipse.contains(pressPt)){
                        activeWalker = walker;

                        //handle double click on walker start
                        if (e.getClickCount() == 2 && !e.isConsumed()) {
                            e.consume();
                            int newCount;
                            String input = (String) (JOptionPane.showInputDialog(CApplication.INSTANCE, "Wieviele Walker?" , "Anzahl Walker", JOptionPane.QUESTION_MESSAGE,null,null, walker.getCount()));
                            try {
                                newCount = Integer.parseInt(input);
                                if (newCount >= 1 && newCount <= 100){
                                    walker.setCount(newCount);
                                } else {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(CApplication.INSTANCE, "Bitte eine Zahl zwischen 1-100 eingeben", "Falsches Format", JOptionPane.ERROR_MESSAGE);
                            }

                            //repaint world editor
                            repaint();
                            return;
                        }else{
                            moveWalkerStart = true;
                            break;
                        }
                    }

                    //inside walker end point
                    ellipse = new Ellipse2D.Double(walker.getTarget().getX() - SIZE / 2, walker.getTarget().getY() - SIZE / 2, SIZE, SIZE);
                    if(ellipse.contains(pressPt)){
                        activeWalker = walker;
                        moveWalkerEnd = true;
                        break;
                    }
                }
            }

            //determine if we have some polygons and if the mouse was inside a polygon
            for (CPolygon cPoly : cPolys) {
                //check if mouse was clicked on a small black resizing ellipse
                for (int i = 0; i < cPoly.npoints; i++) {
                    ellipse = new Ellipse2D.Double(cPoly.xpoints[i] - SIZE / 2, cPoly.ypoints[i] - SIZE / 2, SIZE, SIZE);
                    //mouse was inside our small black ellipse
                    //break because we don't need to search further
                    if (ellipse.contains(pressPt)) {
                        resizePolygon = true;
                        npoint = i;
                        activePolygon = cPoly;
                        break;
                    }
                }
                //break we have to resize the polygon with index k
                if(resizePolygon){
                    break;
                }

                //check if mouse was clicked on the small black center ellipse to translate the polygon
                ellipse = new Ellipse2D.Double((int) cPoly.getCenter().getX() - SIZE / 2, (int) cPoly.getCenter().getY() - SIZE / 2, SIZE, SIZE);
                if (ellipse.contains(pressPt)) {
                    activePolygon = cPoly;
                    translatePolygon = true;
                //else check if mouse was inside a polygon (needed to paint active polygon grey)
                //calculate the axis between press point and center point of the polygon
                } else if (cPoly.contains(e.getPoint())) {
                    activePolygon = cPoly;
                    try
                    {
                        originalPoly = cPoly.clone();
                    }
                    catch (CloneNotSupportedException ex)
                    {
                        System.out.println(ex.toString());
                    }
                    centerPt = activePolygon.getCenter();
                    pressTheta = Math.atan2(pressPt.y - centerPt.y, pressPt.x - centerPt.x);

                    //repaint the world editor
                    repaint();
                    break;
                }
            }
            //repaint the world editor
            repaint();
        }

        /**
         * executed if mouse is released
         * @param e the event
         */
        @Override
        public void mouseReleased(MouseEvent e) {

            //set variables to standard (nothing to do)
            translatePolygon = false;
            resizePolygon = false;
            activePolygon = null;
            npoint = 0;
            moveWalkerEnd = false;
            moveWalkerStart = false;
            activeWalker = null;

            //repaint the world editor
            repaint();
        }

        /**
         * executed if mouse is moving while clicked
         * @param e the event
         */
        @Override
        public void mouseDragged(MouseEvent e) {

            //save position of mouse after move
            Point dragPt = e.getPoint();

            //calculate and save the difference between initial mouse click and draged mouse position
            int dx = e.getX() - pressPtX;
            int dy = e.getY() - pressPtY;

            //if a walker is activ set walker start/end to new position
            if(activeWalker != null){
                if (moveWalkerStart){
                    activeWalker.setPosition(new CPosition(dragPt.x, dragPt.y));
                }else if(moveWalkerEnd){
                    activeWalker.setTarget(new CPosition(dragPt.x, dragPt.y));
                }
            }

            //if a polygon is active resize/translate it
            if (activePolygon != null){

                //resize the polygon
                if (resizePolygon){
                    activePolygon.xpoints[npoint] = dragPt.x;
                    activePolygon.ypoints[npoint] = dragPt.y;

                //if pressed point is inside active polygon...
                }else if (activePolygon.contains(pressPtX, pressPtY)){

                    //translate it
                    if (translatePolygon){
                        activePolygon.translateXY(dx, dy);

                    // or else rotate it
                    }else{
                        double dragTheta = Math.atan2(dragPt.y - centerPt.y, dragPt.x - centerPt.x);
                        double deltaTheta = dragTheta - pressTheta;
                        activePolygon.transform(deltaTheta, centerPt, originalPoly);
                    }
                }

                // update the boundary box
                activePolygon.invalidate();
            }

            //add dragged value to pressed point value
            pressPtX += dx;
            pressPtY += dy;

            //repaint the world editor
            repaint();
        }
    }
}




