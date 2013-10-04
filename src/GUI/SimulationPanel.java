package GUI;

import javax.swing.*;
import java.awt.*;

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

    public SimulationPanel() {
        super();

        // We want to positionate our Elements with x und y coordinates
        setLayout(null);

        // set the Panel size to the Window size
        this.setBounds(0,0,Application.INSTANCE.getWidth(), Application.INSTANCE.getHeight());
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

        if(this.isRunning) {
            g.drawString("Passenger Simulation is running!", 200, 50);
        }
        else {
            g.drawString("Passenger Simulation standby", 200, 50);
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

                // do the Tick stuff here
            }
        } while (true);
    }

    public void toggleRunningState() {
        this.isRunning = !this.isRunning;
    }
}
