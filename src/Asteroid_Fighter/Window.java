package Asteroid_Fighter;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * Frame for Space Asteroid_Fighter Game.
 * @author Seth Tinglof
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Window extends JFrame implements Runnable{
    public static int SCREEN_WIDTH = 1280;
    public static int SCREEN_HEIGHT = 720;

	JPanel panel;
	ButtonPanel buttonPanel;
	Container content;
	String name;
	int score;
	
	public Window(){
		ImageResources.loadImages(); 			//loads image files that will be used in the game
		/*Sets up graphics using EDT */
		try {
			SwingUtilities.invokeAndWait(this);
		} catch (InvocationTargetException | InterruptedException e) {}
		title();
		instructions();
		while(true){
			panel =  new GamePanel(SCREEN_WIDTH, SCREEN_HEIGHT);
			addPanel();
			Game game = new Game(this, (GamePanel) panel);
			score = game.score;
			removePanel();
			scoreScreen();
		}
	}
	
	/**
	 * sets the window on close operation to call System.exit().
	 */
	private void windowCloseOperation(){
		addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            	System.exit(0);
            }
        });
	}

	/**
	 * runs on EDT to set up graphical components.
	 */
	@Override
	public void run() {
		windowCloseOperation();
		content = getContentPane();
		setLayout(new BorderLayout());
		content.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	/**
	 * Repaints frame
	 */
	public void updateFrame(){
	    repaint();
	}
	
	/**
	 * Removes the current ButtonPanel.
	 */
	private void removeButtonPanel(){
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				@Override
				public void run() {
					remove(buttonPanel);
				}
			});
		} catch (InvocationTargetException | InterruptedException e){e.printStackTrace();}
	}
	
	/**
	 * adds current ButtonPanel to JFrame.
	 */
	private void addButtonPanel(){
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
                @Override
                public void run() {
                    add(buttonPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                }
            });
		}  catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}

		while(buttonPanel.isOpen()){
			synchronized(buttonPanel.lock){
				try {
					buttonPanel.lock.wait();
				} catch (InterruptedException e) {}
			}
		}
	}
	
	/**
	 * Removes the current panel from this.
	 */
	private void removePanel(){
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				@Override
				public void run() {
					remove(panel);
				}
			});
		} catch (InvocationTargetException | InterruptedException e){e.printStackTrace();}
	}
	
	/**
	 * adds current panel to this.
	 */
	private void addPanel(){
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
                @Override
                public void run() {
                    add(panel, BorderLayout.CENTER);
                    revalidate();
                }
            });
		} catch (InterruptedException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens instruction screen.
	 */
	private void instructions(){
		buttonPanel = new Instructions(SCREEN_WIDTH, SCREEN_HEIGHT);
		addButtonPanel();
		name = ((Instructions) buttonPanel).name;
		removeButtonPanel();
	}
	
	/**
	 * Opens title screen.
	 */
	private void title(){
		buttonPanel = new Title(SCREEN_WIDTH, SCREEN_HEIGHT);
		addButtonPanel();
		removeButtonPanel();
	}
	
	private void scoreScreen(){
		buttonPanel = new ScoreButtonPanel(name, score, SCREEN_WIDTH, SCREEN_HEIGHT);
		addButtonPanel();
		removeButtonPanel();
	}
}