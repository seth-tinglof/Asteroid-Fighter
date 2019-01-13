
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;


/**
 * Instructions for Planes game.
 * @author Seth Tinglof
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Title extends ButtonPanel implements ActionListener{
	
	public Title(int width, int height){
		title.setBounds(0, 0, width, height);
		okayButton.setBounds(600, 600, 75, 50);
		
		try {
			SwingUtilities.invokeAndWait(new Runnable(){
				@Override
				public void run() {
					add(okayButton);
					add(title);
					setVisible(true);
				}	
			});
		} catch (InvocationTargetException | InterruptedException e) {}
		okayButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setOpen(false);
		synchronized(lock){
			lock.notifyAll();
		}
	}
}
