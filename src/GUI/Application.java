package GUI;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.lang.String;
import java.lang.System;

/**
 * Die Application is the main class of the passenger simulation.
 * 
 * @author Roger Jaggi
 * @version 1.0
 */
public class Application extends JFrame implements WindowListener, KeyListener {

    // Singleton Pattern
    public static Application INSTANCE = new Application();

    /**
     * Startup the application
     * @param args
     */
    public static void main(String[] args) {
        // Let's go
        Application.INSTANCE.setVisible(true);
        //Application app = new Application();
    }

    private Application() {
        super("Passenger Simulation");
        initUI();
    }

    SimulationPanel simulationPanel = new SimulationPanel();
    WorldEditor worldEditor = new WorldEditor();

    public final void initUI() {
        //final JFrame frame = new JFrame("Passenger Simulation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        //frame.pack();
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.YELLOW);
        this.getContentPane().add(mainPanel);

        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0,0,800,20);
        JMenu menu = new JMenu("Options");
        //menu.setMnemonic(KeyEvent.VK_A);
        //menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);
        JMenuItem itemSimulation = new JMenuItem("Start Simulation");
        itemSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Simulation");
                //Application
                // set the Panel size to the Window size
                simulationPanel.setBounds(0, 0, 800, 580);
                Application.INSTANCE.getContentPane().removeAll();
                Application.INSTANCE.getContentPane().add(simulationPanel);
                Application.INSTANCE.revalidate();
                simulationPanel.setupDummyWorld();
                //simulationPanel.repaint();
                //setVisible(true);
                //simulationPanel.runSimulationLoop();
            }
        });
        JMenuItem itemWorldEditor = new JMenuItem("World Editor");
        itemWorldEditor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenu menu = new JMenu("Add");
                JMenuItem Rectangle = new JMenuItem("Rectangle");
                JMenuItem Ellipse = new JMenuItem("Ellipse");
                menu.add(Rectangle);
                menu.add(Ellipse);
                menuBar.add(menu);
                worldEditor.setBounds(0, 0, 800, 580);
                Application.INSTANCE.getContentPane().removeAll();
                Application.INSTANCE.getContentPane().add(worldEditor);
                Application.INSTANCE.revalidate();
                worldEditor.setupEditor();
            }
        });
        menu.add(itemSimulation);
        menu.add(itemWorldEditor);
        this.setJMenuBar(menuBar);
        this.setVisible(true);
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
		// GamePanel vom Application-ContentPane holen
		SimulationPanel simulationPanel = (SimulationPanel) getContentPane().getComponent(0);

		// ESC-Key exits the simulation
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		
		// P-Key pauses, resumes simulation
		if ( e.getKeyCode() == KeyEvent.VK_P) {
			simulationPanel.toggleRunningState();
		}

        // P-Key pauses, resumes simulation
        if ( e.getKeyCode() == KeyEvent.VK_G) {
            simulationPanel.toggleShowGraph();
        }
		
		// F2-Key starts the simulation
		if ( e.getKeyCode() == KeyEvent.VK_F2) {
            simulationPanel.setupDummyWorld();
		}

        if ( e.getKeyCode() == KeyEvent.VK_RIGHT) {
            simulationPanel.runOneStep();
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) { }
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) { }

}