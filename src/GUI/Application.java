package GUI;

import javax.swing.*;
import java.awt.event.*;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;

/**
 * The application is the main class of the passenger simulation.
 * 
 * @author vonop1, jaggr2
 * @version 1.0
 */
public class Application extends JFrame implements WindowListener, KeyListener {

    // Singleton Pattern for our application
    public static Application INSTANCE = new Application();

    // the main components
    final SimulationPanel simulationPanel = new SimulationPanel();
    final WorldEditor worldEditor = new WorldEditor();
    final JMenuBar menuBar = new JMenuBar();

    /**
     * Startup the application and shows the window
     * @param args application arguments
     */
    public static void main(String[] args) {
        // Let's go
        Application.INSTANCE.setVisible(true);
    }

    /**
     * constructor for the application with the simulation panel
     */
    private Application() {
        super("Passenger Simulation");
        initUI();

        loadSimulationPanel();
    }

    /**
     * initializes the GUI components
     */
    private void initUI() {

        // set default parameters
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        // add listener for key events
        this.addKeyListener(this);

        // add menuBar
        menuBar.setBounds(0,0,800,20);
        JMenu menu = new JMenu("Options");
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        // JMenuItem for the SimulationPanel
        JMenuItem itemSimulation = new JMenuItem("Start Simulation (F2)");
        itemSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSimulationPanel();
            }
        });
        menu.add(itemSimulation);

        // JMenuItem for the WorldEditor
        JMenuItem itemWorldEditor = new JMenuItem("World Editor");
        itemWorldEditor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadWorldEditor();
            }
        });
        menu.add(itemWorldEditor);

        // show the UI
        this.setVisible(true);
    }

    /**
     *  loads the simulation panel for the Passenger Simulation
     */
    public void loadSimulationPanel() {

        // check if JMenus from WorldEditor are present and if yes remove them
        for(int m = 0; m < menuBar.getMenuCount(); m++) {
            String accessibleName = menuBar.getMenu(m).getAccessibleContext().getAccessibleName();
            if(accessibleName.equals("Add") || accessibleName.equals("Load File") || accessibleName.equals("Save File")) {
                menuBar.remove(menuBar.getMenu(m));
            }
        }

        /*  - set the panel size
            - remove other content
            - add the simulationPanel to our application
            - revalidate the application
         */
        simulationPanel.setBounds(0, 0, 800, 580);
        this.getContentPane().removeAll();
        this.getContentPane().add(simulationPanel);
        this.revalidate();
    }

    public void loadWorldEditor() {

        // ArrayList with all menu items for the world editor
        ArrayList<JMenuItem> items = new ArrayList<JMenuItem>();

        // JMenuItem Rectangle
        JMenuItem itemRectangle = new JMenuItem("Rectangle");
        items.add(itemRectangle);
        itemRectangle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                worldEditor.addRectangle();
            }
        });

        // JMenuItem Triangle
        JMenuItem itemTriangle = new JMenuItem("Triangle");
        items.add(itemTriangle);
        itemTriangle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        // JMenuItem Ellipse
        JMenuItem itemEllipse = new JMenuItem("Ellipse");
        items.add(itemEllipse);
        itemEllipse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        // JMenuItem Polygon
        JMenuItem itemPolygon = new JMenuItem("Polygon");
        items.add(itemPolygon);
        itemPolygon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int edges;
                String input = JOptionPane.showInputDialog(Application.INSTANCE, "Wieviele Ecken?" , "Anzahl Polygon-Ecken", JOptionPane.QUESTION_MESSAGE);
                try {
                    edges = Integer.parseInt(input);
                    if (edges >= 3 && edges <= 10){
                        worldEditor.addPolygon(edges);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Application.INSTANCE, "Bitte eine Zahl zwischen 3-10 eingeben", "Falsches Format", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // JMenuItem Walker
        JMenuItem itemWalker = new JMenuItem("Walker");
        items.add(itemWalker);
        itemWalker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        // JMenu Add
        JMenu menuAdd = new JMenu("Add");
        for(JMenuItem item : items) {
            menuAdd.add(item);
        }

        // JMenu Load File
        JMenu menuLoad = new JMenu("Load File");
        menuLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        // JMenu Save File
        JMenu menuSave = new JMenu("Save File");
        menuSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        //check if menus already are present, if not add it to the menuBar
        boolean add = false, save = false, load = false;
        for(int m = 0; m < menuBar.getMenuCount(); ++m) {
            if(menuBar.getMenu(m).getAccessibleContext().getAccessibleName().equals("Add")){
                add = true;
            }else if(menuBar.getMenu(m).getAccessibleContext().getAccessibleName().equals("Load File")){
                load = true;
            }else if(menuBar.getMenu(m).getAccessibleContext().getAccessibleName().equals("Save File")){
                save = true;
            }
        }
        if(!load){
            menuBar.add(menuLoad);
        }
        if(!save){
            menuBar.add(menuSave);
        }
        if(!add) {
            menuBar.add(menuAdd);
        }

        /*  - set the panel size
            - remove other content
            -  add the worldEditor to our application
            - revalidate the application
            - setup the default editor
         */
        worldEditor.setBounds(0, 0, 800, 580);
        this.getContentPane().removeAll();
        this.getContentPane().add(worldEditor);
        this.revalidate();
        worldEditor.setupEditor();

    }

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent e) { }
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent e) { }
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) { }
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) { }
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) { }
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) { }

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		// Event an den aktuellen Component 0 weiter geben
		if( getContentPane().getComponentCount() > 0 && getContentPane().getComponent(0) instanceof KeyListener)
        {
            ((KeyListener)(getContentPane().getComponent(0))).keyPressed(e);
        }

        // F2-Key starts the simulation
        if ( e.getKeyCode() == KeyEvent.VK_F2) {
            loadSimulationPanel();
        }

        // F3-Key starts the world editor
        if ( e.getKeyCode() == KeyEvent.VK_F3) {
            loadWorldEditor();
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
        // Event an den aktuellen Component 0 weiter geben
        if( getContentPane().getComponentCount() > 0 && getContentPane().getComponent(0) instanceof KeyListener)
        {
            ((KeyListener)(getContentPane().getComponent(0))).keyReleased(e);
        }
    }
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
        // Event an den aktuellen Component 0 weiter geben
        if( getContentPane().getComponentCount() > 0 && getContentPane().getComponent(0) instanceof KeyListener)
        {
            ((KeyListener)(getContentPane().getComponent(0))).keyTyped(e);
        }
    }

}