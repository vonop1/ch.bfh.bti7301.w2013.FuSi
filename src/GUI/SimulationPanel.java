package GUI;

import Source.CObstacle;
import Source.CObstacleFactory;
import Source.CWalker;
import Source.CWorld;
import Util.CEdge;
import Util.CPosition;
import Util.CVertex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Vector;

/**
 * Copyright 2013 blastbeat syndicate gmbh
 * Author: Roger Jaggi <roger.jaggi@blastbeatsyndicate.com>
 * Date: 04.10.13
 * Time: 10:30
 */
public class SimulationPanel extends JPanel {

    /**
     * Holds the informaticon if the simulation is running
     */
    protected boolean isRunning = false;

    protected CWorld oWorld = null;

    public SimulationPanel() {
        super();

        // We want to positionate our Elements with x und y coordinates
        setLayout(null);

        // set the Panel size to the Window size
        this.setBounds(0,0,Application.INSTANCE.getWidth(), Application.INSTANCE.getHeight());
    }


    /**
     * creates a random world
     */
    public void setupDummyWorld() {
        this.isRunning = false;

        this.oWorld = new CWorld();

        this.oWorld.addWalker(CObstacleFactory.createWalker(20, 20, 520, 300));

//        this.oWorld.addObstacle(CObstacleFactory.createRectangle(150, 150, 250, 250));
//        this.oWorld.addObstacle(CObstacleFactory.createTriangle(300, 300, 350, 300, 300, 350));
//        this.oWorld.addObstacle(CObstacleFactory.createFuenfeck(50, 300, 40, 400, 80, 410, 120, 400, 120, 300));

        this.oWorld.loadConfig();

        this.oWorld.buildGraph();

        this.isRunning = true;

    }

    /**
     * paintComponent is called, wenn the window get's drawed by the application
     *
     * @param g contains the reference to the Java2D graphics object
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        g.setColor(Color.WHITE);

        if(oWorld == null) {
            g.drawString("Passenger Simulation - please load a world or Press F2 for Dummyworld", 200, 50);
        }
        else {
           for(CObstacle obstacle : oWorld.getObstacles()) {
               Vector<CPosition> positions = obstacle.getPositions();
               int[] xCoordinates = new int[positions.size()];
               int[] yCoordinates = new int[positions.size()];

               int i = 0;
               for(CPosition position : positions) {
                   xCoordinates[i] = position.getX().intValue();
                   yCoordinates[i] = position.getY().intValue();
                   i += 1;
               }

               g.setColor(Color.WHITE);
               g.fillPolygon(xCoordinates, yCoordinates, positions.size());

//               Vector<CPosition> positions2 = obstacle.getWaypoints();
//               int[] xCoordinates2 = new int[positions.size()];
//               int[] yCoordinates2 = new int[positions.size()];
//
//               int j = 0;
//               for(CPosition position : positions2) {
//                   xCoordinates2[j] = position.getX().intValue();
//                   yCoordinates2[j] = position.getY().intValue();
//                   j += 1;
//               }
//
//               g.setColor(Color.GREEN);
//               g.drawPolygon(xCoordinates2, yCoordinates2, positions2.size());
           }


            g.setColor(Color.GREEN);
            for(CEdge edge : this.oWorld.getGraph().getEdges()) {
                g.drawLine(edge.getSource().getPos().getX().intValue(), edge.getSource().getPos().getY().intValue(), edge.getDestination().getPos().getX().intValue(), edge.getDestination().getPos().getY().intValue());
                this.drawArrowLine((Graphics2D)g, edge.getSource().getPos().getX().intValue(), edge.getSource().getPos().getY().intValue(), edge.getDestination().getPos().getX().intValue(), edge.getDestination().getPos().getY().intValue());

            }

            for(CWalker walker : oWorld.getWalkers()) {
                CPosition position = walker.getPosition();

                g.setColor(Color.ORANGE);
                g.fillOval(position.getX().intValue() - walker.getSize(), position.getY().intValue() - walker.getSize(), walker.getSize() * 2, walker.getSize() * 2);

                // draw the target as x, because the X marks the point =)
                int upperleftX = walker.getTarget().getX().intValue() - walker.getSize();
                int upperleftY = walker.getTarget().getY().intValue() - walker.getSize();
                int width = walker.getSize() * 2;
                int height = walker.getSize() * 2;
                g.drawLine(upperleftX, upperleftY, upperleftX + width, upperleftY + height);
                g.drawLine(upperleftX + width, upperleftY, upperleftX, upperleftY + height);

            }
        }
    }

    private void drawArrowLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {

        AffineTransform tx = new AffineTransform();
        Line2D.Double line = new Line2D.Double(x1,y1,x2,y2);

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint( 0,5);
        arrowHead.addPoint( -5, -5);
        arrowHead.addPoint( 5,-5);

        tx.setToIdentity();
        double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
        tx.translate(line.x2, line.y2);
        tx.rotate((angle-Math.PI/2d));

        Graphics2D g = (Graphics2D) g2d.create();
        g.setTransform(tx);
        g.fill(arrowHead);
        g.dispose();
        //g.drawLine(x1,y1,x2,y2);
    }

    /**
     * the simulation loop itself
     */
    public void runSimulationLoop() {
        do {
            // redraw window
            this.repaint();

            // sleep 5 miliseconds
            try	{
                java.lang.Thread.sleep(100);
            } catch (InterruptedException e) {

            }

            // when the simulation is running....
            if(this.isRunning ) {
                  this.oWorld.stepSimulation();

            }
        } while (true);
    }

    public void toggleRunningState() {
        this.isRunning = !this.isRunning;
    }
}
