package GUI;

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
    final JMenuBar menuWorldEditor = new JMenuBar();
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

        // init Menus
        initWorldEditorMenu();
        initSimulationMenu();

        // show the UI
        this.setVisible(true);
    }

    public void initWorldEditorMenu() {
        menuWorldEditor.setBounds(0, 0, 800, 20);

        JMenu menuOption = new JMenu("Modus");
        menuWorldEditor.add(menuOption);

        menuOption.add(createMenuItem("Simulation (F2)", KeyEvent.VK_F2));
        menuOption.add(createMenuItem("World Editor (F3)", KeyEvent.VK_F3));

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
                int count;
                String input = JOptionPane.showInputDialog(CApplication.INSTANCE, "Wieviele Walker?" , "Anzahl Walker", JOptionPane.QUESTION_MESSAGE);
                try {
                    count = Integer.parseInt(input);
                    if (count >= 1 && count <= 100){
                        CWorldEditor.addWalker(count);
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CApplication.INSTANCE, "Bitte eine Zahl zwischen 1-100 eingeben", "Falsches Format", JOptionPane.ERROR_MESSAGE);
                }
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
        menuWorldEditor.add(menu);
    }

    public void initSimulationMenu() {
        menuSimulation.setBounds(0,0,800,20);

        JMenu menuOption = new JMenu("Modus");
        menuSimulation.add(menuOption);

        menuOption.add(createMenuItem("Simulation (F2)", KeyEvent.VK_F2));
        menuOption.add(createMenuItem("World Editor (F3)", KeyEvent.VK_F3));


        JMenu menuSimulationControll = new JMenu("Simulationsteuerung");
        menuSimulation.add(menuSimulationControll);

        menuSimulationControll.add(createMenuItem("Pausiere Simulation (P)", KeyEvent.VK_P));
        menuSimulationControll.add(createMenuItem("Markierten Fussgänger manuell steuern (M)", KeyEvent.VK_M));
        menuSimulationControll.add(createMenuItem("Mache 1 Simulationsschritt (N)", KeyEvent.VK_N));
        menuSimulationControll.add(createMenuItem("Vorherigen Walker anwählen (Page Up)", KeyEvent.VK_PAGE_UP));
        menuSimulationControll.add(createMenuItem("Nächsten Walker anwählen (Page Down)", KeyEvent.VK_PAGE_DOWN));
        menuSimulationControll.add(createMenuItem("Beende Simulation (Esc)", KeyEvent.VK_ESCAPE));


        JMenu menuDisplay = new JMenu("Ansicht");
        menuSimulation.add(menuDisplay);

        menuDisplay.add(createMenuItem("Zeige Hilfe an (F1)", KeyEvent.VK_F1));
        menuDisplay.add(createMenuItem("Zeige das Grid an (H)", KeyEvent.VK_H));
        menuDisplay.add(createMenuItem("Zeige Hindernisse an (O)", KeyEvent.VK_O));
        menuDisplay.add(createMenuItem("Zeige Waypoints an (W)", KeyEvent.VK_W));
        menuDisplay.add(createMenuItem("Zeige Graph-Kanten an (G)", KeyEvent.VK_G));
        menuDisplay.add(createMenuItem("Zeige gelöschte Graph-Kanten an (T)", KeyEvent.VK_T));
        menuDisplay.add(createMenuItem("Zeige Walkers an (L)", KeyEvent.VK_L));
        menuDisplay.add(createMenuItem("Zeige Walker Richtung an (D)", KeyEvent.VK_D));
        menuDisplay.add(createMenuItem("Zeige Walker ID an (I)", KeyEvent.VK_I));
        menuDisplay.add(createMenuItem("Zeige Walker Koordinaten an (J)", KeyEvent.VK_J));
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

        /*  - set the panel size
            - remove other content
            - add the simulationPanel to our application
            - revalidate the application
         */
        this.setJMenuBar(menuSimulation);

        simulationPanel.setBounds(0, 0, 800, 580);
        this.getContentPane().removeAll();
        this.getContentPane().add(simulationPanel);
        this.revalidate();
    }

    public void loadWorldEditor() {

        /*  - set the panel size
            - remove other content
            -  add the CWorldEditor to our application
            - revalidate the application
            - setup the default editor
         */
        this.setJMenuBar(menuWorldEditor);

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