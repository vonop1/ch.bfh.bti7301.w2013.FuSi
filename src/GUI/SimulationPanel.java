package GUI;

import Source.CObstacle;
import Source.CObstacleFactory;
import Source.CWalker;
import Source.CWorld;
import Util.CPosition;

import javax.swing.*;
import java.awt.*;
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

        this.oWorld.addWalker(CObstacleFactory.createWalter(50,50));

        this.oWorld.addObstacle(CObstacleFactory.createRectangle(150, 150, 250, 250));
        this.oWorld.addObstacle(CObstacleFactory.createTriangle(300, 300, 350, 300, 300, 350));
        this.oWorld.addObstacle(CObstacleFactory.createFuenfeck(50, 300, 40, 400, 80, 410, 120, 400, 120, 300));

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
                   xCoordinates[i] = (int)position.getX();
                   yCoordinates[i] = (int)position.getY();
                   i += 1;
               }

               g.setColor(Color.WHITE);
               g.fillPolygon(xCoordinates, yCoordinates, positions.size());

               Vector<CPosition> positions2 = obstacle.getWaypoints();
               int[] xCoordinates2 = new int[positions.size()];
               int[] yCoordinates2 = new int[positions.size()];

               int j = 0;
               for(CPosition position : positions2) {
                   xCoordinates2[j] = (int)position.getX();
                   yCoordinates2[j] = (int)position.getY();
                   j += 1;
               }

               g.setColor(Color.GREEN);
               g.drawPolygon(xCoordinates2, yCoordinates2, positions2.size());
           }

            for(CWalker walker : oWorld.getWalkers()) {
                CPosition position = walker.getPosition();

                int size = 5;
                g.setColor(Color.ORANGE);
                g.fillOval(((int) position.getX()) - size, ((int) position.getY()) - size, size * 2, size * 2);

                //g.fillOval();
            }
        }
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
                java.lang.Thread.sleep(5);
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
