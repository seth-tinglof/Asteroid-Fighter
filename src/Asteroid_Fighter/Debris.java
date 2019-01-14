package Asteroid_Fighter;

import Game_Objects.Flying;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Debris extends Flying {
	
	static final int IMAGE_SIZE = 200;
	public static final int SMALLEST_RADIUS = 20;
	public static final int LARGEST_RADIUS = 70;
	public static final int MAX_SIDES = 9;
    public static final int MIN_SIDES = 4;
	
	private BufferedImage image;
	private GeneralPath shape;
	
	/*X and Y coordinate points to witch the blocker displays */ 
	private double[] xPoints;
	private double[] yPoints;
	
	private int sides;			//number of sides shape has.
	private double r;
	private double angle;
	
	public Debris(int xPos, int yPos, double r) {
		super(xPos, yPos);
		this.r = r;
		mass = (int) (r * r);
		sides = (int) (Math.random() * (MAX_SIDES - MIN_SIDES) + MIN_SIDES);
		xPoints = new double[sides];
		yPoints = new double[sides];
		angle = 2 * Math.PI / sides;
		createBlockerShape();
		drawShape();
		dragAmount = .96;
	}
	
	/**
	 * Creates the semi-random shape for the blocker.
	 */
	private void createBlockerShape(){
		double angle;
		for(int i = 0; i < sides; i++){
			angle = this.angle * i;
			xPoints[i] =  (IMAGE_SIZE / 2 + r * Math.sin(angle)) + (Math.random() * 20 - 10);
			yPoints[i] =  (IMAGE_SIZE / 2 - r * Math.cos(angle)) + (Math.random() * 20 - 10);
		}
	}
	
	/**
	 * Draws the image for the blocker.
	 */
	private void drawShape(){
		image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
		shape = new GeneralPath(GeneralPath.WIND_NON_ZERO, sides);
		shape.moveTo(xPoints[0], yPoints[0]);
		for(int i = 1; i < sides; i++)
			shape.lineTo(xPoints[i], yPoints[i]);
		shape.closePath();
		Graphics2D g = (Graphics2D) (image.getGraphics());
		g.setRenderingHints(ImageResources.AA);
		g.setColor(Color.BLUE);
		g.fill(shape);
		g.dispose();
		
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public void updateHitBox() {
		hitBox = new Line2D[sides];
		for(int i = 0; i < sides - 1; i++)
			hitBox[i] = new Line2D.Double(getXPos() + xPoints[i], getYPos() + yPoints[i], getXPos() + xPoints[i + 1], getYPos() + yPoints[i + 1]);
		hitBox[sides - 1] = new Line2D.Double(getXPos() + xPoints[sides - 1], getYPos() + yPoints[sides - 1], getXPos() + xPoints[0], getYPos() + yPoints[0]);	
	}

	@Override
	public int[] getCenter() {
		return new int[]{getXPos() + IMAGE_SIZE / 2, getYPos() + IMAGE_SIZE / 2};
	}

	public double[] getTrueCenter(){
		return new double[]{getTrueX() + IMAGE_SIZE / 2, getTrueY() + IMAGE_SIZE / 2};
	}
}
