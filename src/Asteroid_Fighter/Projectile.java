package Asteroid_Fighter;

import Game_Objects.Flying;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * A projectile that can bounce off of debris and damage enemies.
 * 
 * @author Seth Tinglof
 * @version 1.0
 */
public class Projectile extends Flying {
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	public static final double SPEED = 3.;
	public static final int DAMAGE = 10;
	private static final int HIT_BOX_ACCURACY = 20;  //number of points which define the hitbox.
	long frame;								//the frame where this was created
	
	private Image image;
	private double[] xPoints;
	private double[] yPoints;
	public Projectile(int xPos, int yPos, long frame) {
		super(xPos - WIDTH / 2, yPos - HEIGHT / 2);
		mass = 1000;
		this.frame = frame;
	}

	public void drawProjectile(){
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHints(ImageResources.AA);
		ImageResources.setColorRandom(g);
		g.drawOval(12, 12, 25, 25);
		ImageResources.setColorRandom(g);
		g.drawOval(15, 15, 19, 19);
		ImageResources.setColorRandom(g);
		g.drawOval(18, 18, 13, 13);
		ImageResources.setColorRandom(g);
		g.drawOval(21, 21, 7, 7);
		g.dispose();
	}
	
	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public int[] getCenter() {
		return null;
	}

	public double[] getTrueCenter(){
		return new double[]{getTrueX() + WIDTH / 2, getTrueY() + HEIGHT / 2};
	}

	@Override
	public void updateHitBox() {
		getPoints();
		hitBox = new Line2D[HIT_BOX_ACCURACY];
		for(int i = 0; i < hitBox.length - 1; i++){
			hitBox[i] = new Line2D.Double(xPoints[i] + getTrueX(), yPoints[i] + getTrueY(), xPoints[i + 1] + getTrueX(), yPoints[i + 1] + getTrueY());
		}
		hitBox[HIT_BOX_ACCURACY - 1] = new Line2D.Double(xPoints[HIT_BOX_ACCURACY - 1] + getTrueX(), yPoints[HIT_BOX_ACCURACY - 1] + getTrueY(), xPoints[0] + getTrueX(), yPoints[0] + getTrueY());
	}
	
	/**
	 * sets values for xPoints and yPoints array so a hitbox can be made.
	 */
	private void getPoints(){
		xPoints = new double[HIT_BOX_ACCURACY];
		yPoints = new double[HIT_BOX_ACCURACY];
		double r = 12.5;
		for(int i = 0; i < HIT_BOX_ACCURACY; i++){
			xPoints[i] = WIDTH / 2 + Math.cos(i * Math.PI * 2 / HIT_BOX_ACCURACY) * r;
			yPoints[i] = HEIGHT / 2 + Math.sin(i * Math.PI * 2 / HIT_BOX_ACCURACY) * r;
		}
	}

}
