package GUI;

import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
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
public class CApplication extends JFrame implements WindowListener, KeyListener {

    // Singleton Pattern for our application
    public static CApplication INSTANCE = new CApplication();
    public static Color BACKGROUND_COLOR = new Color(230, 230, 230);
    public static Color WALKER_COLOR = new Color(255, 154, 9);

    // the main components
    final CSimulationPanel simulationPanel = new CSimulationPanel();
    final CWorldEditor CWorldEditor = new CWorldEditor();
    final JMenuBar menuBar = new JMenuBar();
    final JMenuBar menuSimulation = new JMenuBar();

    /**
     * Startup the application and shows the window
     * @param args application arguments
     */
    public static void main(String[] args) {
        // Let's go
        CApplication.INSTANCE.setVisible(true);
    }

    /**
     * constructor for the application with the simulation panel
     */
    private CApplication() {
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
        this.setSize(800, 640);
        this.setLocationRelativeTo(null);

        // add listener for key events
        this.addKeyListener(this);

        // add menuBar
        menuBar.setBounds(0,0,800,20);
        this.setJMenuBar(menuBar);

        JMenu menuOption = new JMenu("Options");
        menuBar.add(menuOption);

        menuOption.add(createMenuItem("Zeige Hilfe an (F1)", KeyEvent.VK_F1));
        menuOption.add(createMenuItem("Simulation (F2)", KeyEvent.VK_F2));
        menuOption.add(createMenuItem("World Editor (F3)", KeyEvent.VK_F3));

        // show the UI
        this.setVisible(true);
    }

    public void initSimulationMenu() {

    }

    public JMenuItem createMenuItem(String text, final Integer keyCode) {
        JMenuItem newMenuItem = new JMenuItem(text);
        newMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CApplication.INSTANCE.dispatchEvent(new KeyEvent(CApplication.INSTANCE, KeyEvent.KEY_PRESSED, 0, 0, keyCode, KeyEvent.CHAR_UNDEFINED));
            }
        });
        return newMenuItem;
    }

    /**
     *  loads the simulation panel for the Passenger Simulation
     */
    public void loadSimulationPanel() {

        // check if JMenu from CWorldEditor is present and if yes remove it
        for(int m = 0; m < menuBar.getMenuCount(); m++) {
            String accessibleName = menuBar.getMenu(m).getAccessibleContext().getAccessibleName();
            if(accessibleName.equals("World Editor")) {
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
        ArrayList<JMenuItem> itemsAdd = new ArrayList<JMenuItem>();
        ArrayList<JMenuItem> items = new ArrayList<JMenuItem>();

        // JMenuItem Triangle
        JMenuItem itemTriangle = new JMenuItem("Triangle");
        itemsAdd.add(itemTriangle);
        itemTriangle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int edges = 3;
                CWorldEditor.addPolygon(edges);
            }
        });

        // JMenuItem Rectangle
        JMenuItem itemRectangle = new JMenuItem("Rectangle");
        itemsAdd.add(itemRectangle);
        itemRectangle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int edges = 4;
                CWorldEditor.addPolygon(edges);
            }
        });

        // JMenuItem Polygon
        JMenuItem itemPolygon = new JMenuItem("Polygon");
        itemsAdd.add(itemPolygon);
        itemPolygon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int edges;
                String input = JOptionPane.showInputDialog(CApplication.INSTANCE, "Wieviele Ecken?" , "Anzahl Polygon-Ecken", JOptionPane.QUESTION_MESSAGE);
                try {
                    edges = Integer.parseInt(input);
                    if (edges >= 3 && edges <= 10){
                        CWorldEditor.addPolygon(edges);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CApplication.INSTANCE, "Bitte eine Zahl zwischen 3-10 eingeben", "Falsches Format", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // JMenuItem Walker
        JMenuItem itemWalker = new JMenuItem("Walker");
        itemsAdd.add(itemWalker);
        itemWalker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CWorldEditor.addWalker();
            }
        });

        // JMenuItem Add
        JMenu menuAdd = new JMenu("Add");
        for(JMenuItem item : itemsAdd) {
            menuAdd.add(item);
        }

        // JMenuItem Load File
        JMenuItem menuLoad = new JMenuItem("Load File");
        menuLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CWorldEditor.loadWorld();
            }
        });
        items.add(menuLoad);

        // JMenu Save File
        JMenuItem menuSave = new JMenuItem("Save File");
        menuSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CWorldEditor.saveWorld();
            }
        });
        items.add(menuSave);

        // JMenu CWorldEditor
        JMenu menu = new JMenu("World Editor");
        for(JMenuItem item : items) {
            menu.add(item);
        }
        menu.add(menuAdd);

        //check if menus already are present, if not add it to the menuBar
        boolean world = false;
        for(int m = 0; m < menuBar.getMenuCount(); ++m) {
            if(menuBar.getMenu(m).getAccessibleContext().getAccessibleName().equals("World Editor")){
                world = true;
            }
        }
        if(!world){
            menuBar.add(menu);
        }

        /*  - set the panel size
            - remove other content
            -  add the CWorldEditor to our application
            - revalidate the application
            - setup the default editor
         */
        CWorldEditor.setBounds(0, 0, 800, 580);
        this.getContentPane().removeAll();
        this.getContentPane().add(CWorldEditor);
        this.revalidate();
        CWorldEditor.setupEditor();

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