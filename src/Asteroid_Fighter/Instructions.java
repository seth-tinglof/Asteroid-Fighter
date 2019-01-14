package Asteroid_Fighter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * Instructions for Planes game.
 * @author Seth Tinglof
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Instructions extends ButtonPanel implements ActionListener{

	private JTextField text = new JTextField(1000);
	String name;
	
	public Instructions(int width, int height){
		instructions.setBounds(0, 0, width, height);
		text.setBounds(350, 500, 300, 30);
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				@Override
				public void run() {
					add(text);
					add(instructions);
					setVisible(true);
				}	
			});
		} catch (InvocationTargetException | InterruptedException e) {}
		text.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		name = text.getText();
		if(!name.trim().equals("")){
			setOpen(false);
			synchronized(lock){
				lock.notifyAll();
			}
		}
	}
}
