package GUI;

import Source.*;
import Util.CEdge;
import Util.CPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Copyright 2013
 * Author: Roger Jaggi <roger.jaggi@blastbeatsyndicate.com>
 * Date: 04.10.13
 * Time: 10:30
 */
public class CSimulationPanel extends JPanel implements ActionListener, KeyListener, MouseListener {

    protected Vector<CDrawObject> drawSimulationObjects = new Vector<CDrawObject>();

    /**
     * the timer triggers the simulation steps
     */
    protected Timer timer = null;

    protected Vector<File> files = new Vector<File>();

    protected CWorld simulationWorld = null;
    protected CWalker selectedWalker = null;
    protected CStrategieManual manualStrategie = new CStrategieManual();

    public CSimulationPanel() {
        super();

        // We want to positionate our Elements with x und y coordinates
        setLayout(null);

        initDrawingObjects();

        reloadConfigfiles();

        this.timer = new Timer(20, this);

        this.repaint();

        addMouseListener(this);
    }

    private void initDrawingObjects() {

        // draw the grid
        drawSimulationObjects.add(new CDrawObject(false, KeyEvent.VK_H, "H - Zeige das Grid an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                g2d.setColor(Color.LIGHT_GRAY);

                // draw vertical and horizontal lines
                int i = 1;
                int max = Math.max(getWidth(), getHeight());
                while (i < max) {
                    i += simulationWorld.getGridSize();

                    // the vertical line
                    g2d.drawLine(i, 0, i, getHeight());

                    // the horizontal line
                    g2d.drawLine(0, i, getWidth(), i);
                }

                int max_x = getWidth() / simulationWorld.getGridSize();
                int max_y = getHeight() / simulationWorld.getGridSize();
                for (int x = 0; x < max_x; x++) {
                    for (int y = 0; y < max_y; y++) {
                        if (simulationWorld.getGrid().hasCellObject(x, y)) {
                            g2d.fillRect(x * simulationWorld.getGridSize() + 3, y * simulationWorld.getGridSize() + 3, simulationWorld.getGridSize() - 3, simulationWorld.getGridSize() - 3);
                        }
                    }
                }
            }
        });

        // draw the objects
        drawSimulationObjects.add(new CDrawObject(true, KeyEvent.VK_O, "O - Zeige Obstacles an") {
            @Override
            public void doDrawing(Graphics2D g2d) {

                g2d.setColor(Color.WHITE);
                for (CObstacle obstacle : simulationWorld.getObstacles()) {

                    CDrawHelper.drawPolygon(g2d, obstacle.getPositions(), true);
                }
            }
        });

        // draw the shortcut help info
        drawSimulationObjects.add(new CDrawObject(false, KeyEvent.VK_F1, "F1 - Zeigt diese Hilfe an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                g2d.setColor(Color.BLACK);

                int x = simulationWorld.getWorldWidth() - 250;
                int y = 20;

                for(CDrawObject drawObject : drawSimulationObjects) {
                    if(drawObject.getVisibleShortcutKey() != null && drawObject.getDescription() != null && !drawObject.getDescription().isEmpty()) {
                        y += 20;
                        g2d.drawString(drawObject.getDescription(), x, y);
                    }
                }

                y += 20;
                g2d.drawString("P - Pausiere Simulation", x, y);
                y += 20;
                g2d.drawString("RECHTS - Mache 1 Simulationsschritt", x, y);
                y += 20;
                g2d.drawString("0-9 - Lade andere Simulation", x, y);
                y += 20;
                g2d.drawString("ESC - Beende Simulation", x, y);
            }
        });

        // draw the waypoints
        drawSimulationObjects.add(new CDrawObject(false, KeyEvent.VK_W, "W - Zeige Waypoints an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                //g2d.setColor(Color.YELLOW);
                g2d.setColor(Color.BLUE);
                for (CObstacle obstacle : simulationWorld.getObstacles()) {
                    CDrawHelper.drawPolygon(g2d, obstacle.getWaypoints(), false);
                }
            }
        });

        // draw the graph edges
        drawSimulationObjects.add(new CDrawObject(true, KeyEvent.VK_G, "G - Zeige Graph-Kanten an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                g2d.setColor(Color.BLUE);
                for (CEdge edge : simulationWorld.getGraph().getEdges()) {
                    CDrawHelper.drawLine(g2d, edge.getSource(), edge.getDestination());
                }
            }
        });

        // draw the graph trash edges
        drawSimulationObjects.add(new CDrawObject(false, KeyEvent.VK_T, "T - Zeige gelöschte Graph-Kanten an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                g2d.setColor(Color.RED);
                for (CEdge edge : simulationWorld.getGraph().getTrashEdges()) {
                    CDrawHelper.drawLine(g2d, edge.getSource(), edge.getDestination());
                    //g2d.drawLine(edge.getSource().getX().intValue(), edge.getSource().getY().intValue(), edge.getDestination().getX().intValue(), edge.getDestination().getY().intValue());
                }
            }
        });

        // draw the walkers
        drawSimulationObjects.add(new CDrawObject(true, KeyEvent.VK_L, "L - Zeige die Walkers an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                for (CWalker walker : simulationWorld.getWalkers()) {

                    if(selectedWalker != null && walker.equals(selectedWalker)) {
                        g2d.setColor(Color.GREEN);
                        CDrawHelper.drawPointAsCircle(g2d, walker.getPosition(), walker.getHalfWalkerSize() * 2 + 2 , true);
                    }

                    g2d.setColor(CApplication.WALKER_COLOR);
                    CDrawHelper.drawPointAsCircle(g2d, walker.getPosition(), walker.getHalfWalkerSize() * 2, true);

                    g2d.setColor(Color.WHITE);
                    Vector<CPosition> triangle = new Vector<CPosition>(3);
                    triangle.add(new CPosition(walker.getPosition(), walker.getLastDirectionAngle(), walker.getHalfWalkerSize()));
                    triangle.add(new CPosition(walker.getPosition(), walker.getLastDirectionAngle() + Math.PI * 4/5, walker.getHalfWalkerSize() ));
                    triangle.add(new CPosition(walker.getPosition(), walker.getLastDirectionAngle() - Math.PI * 4/5, walker.getHalfWalkerSize() ));

                    CDrawHelper.drawPolygon(g2d, triangle, true);


                    // draw the target as x, because the X marks the point =)
                    g2d.setColor(CApplication.WALKER_COLOR);
                    CDrawHelper.drawPointAsX(g2d, walker.getTarget(), walker.getHalfWalkerSize() * 2);
                }
            }
        });

        // draw the walker ids
        drawSimulationObjects.add(new CDrawObject(false, KeyEvent.VK_I, "I - Zeige Walker IDs an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                for (CWalker walker : simulationWorld.getWalkers()) {
                    CPosition position = walker.getPosition();

                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Serif", Font.BOLD, 9));
                    g2d.drawString(walker.getId().toString(), position.getX().intValue() - (walker.getHalfWalkerSize().intValue() / 2), position.getY().intValue() + (walker.getHalfWalkerSize().intValue()));
                }
            }
        });

        // draw the walker coordinates
        drawSimulationObjects.add(new CDrawObject(false, KeyEvent.VK_J, "J - Zeige Walker Koordinaten an") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                for (CWalker walker : simulationWorld.getWalkers()) {
                    CPosition position = walker.getPosition();

                    DecimalFormat df = new DecimalFormat("#.00");
                    g2d.setColor(Color.CYAN);
                    g2d.drawString("x" + df.format(walker.getPosition().getX()) + "/y" + df.format(walker.getPosition().getY()), position.getX().intValue() + ((Double) (walker.getHalfWalkerSize() * 2)).intValue() + 10, position.getY().intValue());
                }
            }
        });

        // draw the details of a walker
        drawSimulationObjects.add(new CDrawObject(true, null, "") {
            @Override
            public void doDrawing(Graphics2D g2d) {
                if(selectedWalker != null) {
                    g2d.setColor(Color.BLACK);

                    int x = simulationWorld.getWorldWidth() - 250;
                    int y = 20;
                    g2d.drawString("Details Walker Id " + selectedWalker.getId(), x, y);
                    y += 30;
                    g2d.drawString("CurrentPos: " + selectedWalker.getPosition(), x, y);
                    y += 20;

                    int i = 0;
                    g2d.setColor(Color.DARK_GRAY);
                    for(CPosition position : selectedWalker.getDesiredPath()) {

                        g2d.drawString("DesiredPath " + i + ": " + position, x, y);
                        i += 1;
                        y += 20;

                        // draw the position as x
                        int upperleftX = ((Double) (position.getX() - selectedWalker.getHalfWalkerSize())).intValue();
                        int upperleftY = ((Double) (position.getY() - selectedWalker.getHalfWalkerSize())).intValue();
                        int width = ((Double) (selectedWalker.getHalfWalkerSize() * 2)).intValue();
                        int height = ((Double) (selectedWalker.getHalfWalkerSize() * 2)).intValue();
                        g2d.drawLine(upperleftX, upperleftY, upperleftX + width, upperleftY + height);
                        g2d.drawLine(upperleftX + width, upperleftY, upperleftX, upperleftY + height);

                    }



                }
            }
        });
    }

    public void reloadConfigfiles() {
        File dir = new File("./XML");

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        };

        int i = 0;
        for(File file : dir.listFiles(filter)) {
            i += 1;
            if(i >= 10) {
                break;
            }

            this.files.add(file);
        }
    }

    public void setupWorld(File configFile) {
        if(configFile != null ) {

            this.selectedWalker = null;
            this.simulationWorld = new CWorld();

            this.simulationWorld.loadConfig(configFile);

            this.setBounds(0,0, this.simulationWorld.getWorldWidth(), this.simulationWorld.getWorldHeight());

            this.simulationWorld.buildGraph();

            this.timer.start();
        }
        else {
            this.timer.stop();
            this.simulationWorld = null;
            this.selectedWalker = null;
            this.setBounds(0,0, CApplication.INSTANCE.getWidth(),  CApplication.INSTANCE.getHeight());

            this.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.runOneStep();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // give the event to the manualstrategie
        this.manualStrategie.keyTyped(e);
    }

    /*
     * (non-Javadoc)
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {

        File fileToLoad = null;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_0:
                final JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(this);
                fileToLoad = fc.getSelectedFile();
                e.consume();
                break;

            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
            case KeyEvent.VK_5:
            case KeyEvent.VK_6:
            case KeyEvent.VK_7:
            case KeyEvent.VK_8:
            case KeyEvent.VK_9:
                try {
                    fileToLoad = this.files.elementAt(e.getKeyCode() - KeyEvent.VK_1);
                }
                catch(ArrayIndexOutOfBoundsException ex) {
                    //System.out.println(ex);
                }
                e.consume();
                break;

            case KeyEvent.VK_P:
                this.toggleRunningState();
                e.consume();
                break;

            case KeyEvent.VK_M:
                if(this.selectedWalker != null) {
                    this.selectedWalker.changeStrategie(this.manualStrategie);
                }

            case KeyEvent.VK_PAGE_DOWN:
                this.runOneStep();
                e.consume();
                break;

            case KeyEvent.VK_ESCAPE:
                if(this.simulationWorld == null) {
                    System.exit(0);
                }
                else {
                    this.setupWorld(null);
                }
                e.consume();
                break;
        }

        // give the event to the manualstrategie
        this.manualStrategie.keyPressed(e);

        // give the event to the drawobject
        for(CDrawObject drawObject : this.drawSimulationObjects) {
            drawObject.keyPressed(e);
        }

        if(fileToLoad != null ) {
            this.setupWorld(fileToLoad);
        }

        // if the timer is not running, fire a repaint event
        if(!this.timer.isRunning() && e.isConsumed()) {
            this.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // give the event to the manualstrategie
        this.manualStrategie.keyReleased(e);
    }

    /**
     * paintComponent is called, every time the window gets drawed by the application
     *
     * @param g contains the reference to the Java2D graphics object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing((Graphics2D)g);
    }

    /**
     * doDrawing does the actual drawing
     * @param g2d contains the reference to the Java2D graphics object
     */
    private void doDrawing(Graphics2D g2d) {

        //Customize the main world
        g2d.setColor(CApplication.BACKGROUND_COLOR);
        g2d.fillRect(getX(), getY(), getWidth(), getHeight());

        if(simulationWorld == null) {

            g2d.setColor(Color.BLACK);
            g2d.drawString("Bitte wähle eine Welt, die geladen werden soll:", 100, 50);
            Integer i = 0;
            for(File file : this.files) {
                i += 1;
                g2d.drawString(i.toString() + " - " + file.getName(), 100, 50 + (i * 30));
                if(i >= 9) {
                    break;
                }
            }

            i += 1;
            g2d.drawString("0 - XML-Datei selber wählen ...", 100, 50 + (i * 30));

        }
        else {
            for(CDrawObject drawObject : this.drawSimulationObjects) {
                drawObject.draw(g2d);
            }
        }
    }

    public void runOneStep() {
        if(this.simulationWorld != null) {
            this.simulationWorld.stepSimulation();
            this.repaint();
        }
    }

    /**
     * pauses/plays the simulation if a simulation is loaded
     */
    public void toggleRunningState() {
        if(this.simulationWorld != null) {
            if( this.timer.isRunning() ) {
                this.timer.stop();
            }
            else {
                this.timer.start();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(this.simulationWorld != null ) {
            CPosition clickedPos = new CPosition(e.getX(), e.getY());

            this.selectedWalker = null;
            for(CWalker walker : simulationWorld.getWalkers()) {
                if(walker.getPosition().getDistanceTo(clickedPos) < walker.getHalfWalkerSize()) {
                    this.selectedWalker = walker;
                    break;
                }
            }

            // if the timer is not running, fire a repaint event
            if(!this.timer.isRunning()) {
                this.repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
