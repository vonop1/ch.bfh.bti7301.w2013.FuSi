package GUI;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.String;
import java.lang.System;

/**
 * Die Application is the main class of the passenger simulation.
 * 
 * @author Roger Jaggi
 * @version 1.0
 */
public class Application extends JFrame implements WindowListener, KeyListener {

    /**
     * Startup the application
     * @param args
     */
    public static void main(String[] args) {
        // Let's go
        Application.INSTANCE.startup();
    }

    // Singleton Pattern
	public static Application INSTANCE = new Application();
	

	public Application() {
		// call the constructor of the JFrame and set title
		super("Passenger Simulation");
		
		// register Exit event
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addKeyListener(this);

        // set Background and Window Size
		this.setBackground(Color.BLACK);
		setSize(800, 500);

	}

	/**
	 * starts the simulation
	 */
	public void startup()
	{
        // Add Simulation panel
        SimulationPanel simulationPanel = new SimulationPanel();

        getContentPane().add(simulationPanel);

        setVisible(true);

        simulationPanel.runSimulationLoop();
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