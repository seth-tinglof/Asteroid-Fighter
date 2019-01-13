
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Enemy extends Flying {

	static final int[] X_POINTS = new int[]{20, 30, 40, 50, 80, 50, 40, 30};
	static final int[] Y_POINTS = new int[]{50, 40, 20, 40, 50, 60, 80, 60};
	static final int NUMBER_OF_POINTS = X_POINTS.length;

	public static final int WIDTH = 70;
	public static final int HEIGHT = 70;

	public static final double ACCELERATION = .25;
	
	private double[] displayX = new double[NUMBER_OF_POINTS];
	private double[] displayY = new double[NUMBER_OF_POINTS];
	private Color color;
	
	private Image image;
	
	public Enemy(int xPos, int yPos) {
		super(xPos, yPos);
		hitBox = new Line2D[NUMBER_OF_POINTS];
		mass = 500;
		setHealth(60);
		dragAmount = .97;
		color = ImageResources.randomColor();
	}

	@Override
	public Image getImage() {
		return image;
	}
	
	/**
	 * Draws the image for the Enemy.
	 * Also updates the position of the player
	 * for a later call of updateHitBox().
	 */
	public void drawImage(){
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO, 3);
		for(int i = 0; i < NUMBER_OF_POINTS; i++){
			double r = Math.sqrt(Math.pow(X_POINTS[i] - 50, 2) + Math.pow(Y_POINTS[i] - 50, 2));    //Unscaled center is at (50,50).
			double oldAngle = Math.atan2(-(Y_POINTS[i] - 50), X_POINTS[i] - 50);
			displayX[i] = .7 * (50 + r * Math.cos(getAngle() - oldAngle));
			displayY[i] = .7 * (50 - r * Math.sin(getAngle() - oldAngle));
			try{
				path.lineTo(displayX[i], displayY[i]);
			}catch (IllegalPathStateException e) {path.moveTo(displayX[0], displayY[0]);}
		}
		path.closePath();
		Graphics2D g = ((Graphics2D) image.getGraphics());
		g.setRenderingHints(ImageResources.AA);
		g.setColor(color);
		g.fill(path);
		g.dispose();
	}

	/**
	 * not exact center, flaws in implementation.
	 */
	@Override
	public int[] getCenter() {
		return new int[]{getXPos() + WIDTH / 2, getYPos() + HEIGHT / 2};
	}

	public double[] getTrueCenter(){
		return new double[]{getTrueX() + WIDTH / 2, getTrueY() + HEIGHT / 2};
	}

	@Override
	public void updateHitBox() {
		for(int i = 0; i < hitBox.length - 1; i++)
			hitBox[i] = new Line2D.Double(getTrueX() + displayX[i], getTrueY() + displayY[i], getTrueX() + displayX[i + 1], getTrueY() + displayY[i + 1]);
		hitBox[hitBox.length - 1] = new Line2D.Double(getTrueX() + displayX[hitBox.length - 1], getTrueY() + displayY[hitBox.length - 1], getTrueX() + displayX[0], getTrueY() + displayY[0]);
	}

}
