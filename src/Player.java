
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * Player character in Final Project.
 * extends Flying in order to move in according 
 * to Newtonian Mechanics.  Image for player
 * is drawn using geometry package and the drawing
 * is rotated based on the angle.
 * 
 * @author Seth Tinglof
 * @version 1.0
 */
public class Player extends Flying {
	public static final int WIDTH = 150;
	public static final int HEIGHT = 150;

	public static final double TURN_ANGLE = Math.PI / 60;
	public static final double ACCELERATION_SPEED = .3;

	double drawAngle = 0;

	private BufferedImage image;
	private GeneralPath path;						//used to draw the image of the player
	
	/*x and y positions that make up the player's basic shape */
	static final int[] X_POINTS = new int[] {40, 48, 48, 53, 66, 71, 66, 53, 48, 48, 40};
	static final int[] Y_POINTS = new int[] {42, 43, 33, 33, 48, 50, 52, 67, 67, 57, 58};
	
	static final int NUMBER_OF_POINTS = X_POINTS.length;
	
	/*The location of the points that define the players shape after being rotated to the player's angle. */
	double[] displayX = new double[NUMBER_OF_POINTS];
	double[] displayY = new double[NUMBER_OF_POINTS]; 
	
	
	public Player(int xPos, int yPos) {
		super(xPos, yPos);
		dragAmount = .97;
		hitBox = new Line2D[NUMBER_OF_POINTS];
		mass = 1000;
		setHealth(100);
	}

	/**
	 * Draws the image for the player.
	 * Also updates the position of the player
	 * for a later call of updateHitBox().
	 */
	public void drawPlayer(){
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		path = new GeneralPath(GeneralPath.WIND_NON_ZERO, 3);
		for(int i = 0; i < NUMBER_OF_POINTS; i++){
			double r = Math.sqrt(Math.pow(X_POINTS[i] - 50, 2) + Math.pow(Y_POINTS[i] - 50, 2));    //Unscaled center is at (50, 50).
			double oldAngle = Math.atan2(-(Y_POINTS[i] - 50), X_POINTS[i] - 50);
			displayX[i] = 1.5 * (50 + r * Math.cos(drawAngle - oldAngle));
			displayY[i] = 1.5 * (50 - r * Math.sin(drawAngle - oldAngle));
			try{
				path.lineTo(displayX[i], displayY[i]);
			}catch (IllegalPathStateException e) {path.moveTo(displayX[0], displayY[0]);}
		}
		path.closePath();
		Graphics2D g = ((Graphics2D) image.getGraphics());
		g.setRenderingHints(ImageResources.AA);
		g.setColor(Color.ORANGE);
		g.fill(path);
		g.dispose();
			
	}
	
	@Override
	public Image getImage() {
		return image;
	}

	/**
	 * Updates the hit box to the player's current location and rotation.
	 */
	@Override
	public void updateHitBox() {
		for(int i = 0; i < hitBox.length - 1; i++)
			hitBox[i] = new Line2D.Double(getTrueX() + displayX[i], getTrueY() + displayY[i], getTrueX() + displayX[i + 1], getTrueY() + displayY[i + 1]);
		hitBox[hitBox.length - 1] = new Line2D.Double(getTrueX() + displayX[hitBox.length - 1], getTrueY() + displayY[hitBox.length - 1], getTrueX() + displayX[0], getTrueY() + displayY[0]);
	}

	@Override
	public int[] getCenter() {
		return new int[]{WIDTH / 2 + this.getXPos(), HEIGHT / 2 + this.getYPos()};
	}

	public double[] getTrueCenter(){
	    return new double[]{WIDTH / 2 + this.getTrueX(), HEIGHT / 2 + this.getTrueY()};
    }
}
