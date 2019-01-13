
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Loads images into memory then allows them
 * to be accessed for future use.
 * 
 * @author Seth Tinglof
 * @version 1.0
 */
public class ImageResources {
	
	/* Images for different entities.
	 * Created here instead of in each entities class
	 * because only one of each Image is needed in memory.
	 */
	 public static final ImageIcon instructions = new ImageIcon("Instructions.png");
	 public static final ImageIcon highscores   = new ImageIcon("HighScores.png");
	 public static final ImageIcon title        = new ImageIcon("Title.png");

	 public static Image star_small;
	 public static Image star_medium;
	 public static Image star_large;

	 private ImageResources(){} //Class should not be initialized.
	
	/**
	 * Loads images into memory.
	 * All images are BufferedImage objects.
	 * Once this method is called, these images
	 * may be accessed from a static context:
	 * 
	 */
	public static void loadImages(){
		try {
			star_small = ImageIO.read(new File("small_star.png"));
			star_medium = ImageIO.read(new File("medium_star.png"));
			star_large = ImageIO.read(new File("large_star.png"));
		} catch (IOException e) {
			System.out.println("Unable to load image file: " + e.getMessage());
		}
	}
	
	/**
	 * Takes an argument BufferedImage then returns a new BufferedImage
	 * that has been rotated the argument amount.
	 * @param image to be rotated
	 * @param angle of rotation in radians
	 * @return image rotated angle radians.
	 */
	public static BufferedImage rotateImage(BufferedImage image, double angle){
		BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = rotatedImage.createGraphics();
        g.rotate(-angle, image.getWidth() /2, image.getHeight() /2);				
        g.drawRenderedImage(image, null);
        g.dispose();
		return rotatedImage;
	}
	
	public static void setColorRandom(Graphics2D g){
		g.setColor(new Color((int)(256 * Math.random()), (int) (256 * Math.random()), (int) (256 * Math.random())));
	}
	
	/**
	 * @return A new color with random values.
	 */
	public static Color randomColor(){
		return new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256));
	}

	static final RenderingHints AA = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
}
