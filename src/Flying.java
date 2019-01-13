
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A flying entity with acceleration, drag, and gravity.
 * 
 * @author Seth Tinglof
 * @version 2.0
 */
public abstract class Flying extends TrueCoordinates{

    private double maxSpeed = 10;

	private double angle = 0;

	private double xVelocity;
	private double yVelocity;
	
	int mass;
	
	double dragAmount;						//The amount velocity is multiplied by because of drag.

    /**
     * Creates a new Flying.
     * @param xPos of this Flying.
     * @param yPos of this Flying.
     */
	public Flying(int xPos, int yPos) {
		super(xPos, yPos);
		resetVelocity();
	}
	
	/**
	 * Object changes position based on velocity.
	 */
	@Override
	public void move(){
		increaseTruePosition(xVelocity, yVelocity);
		setIntegerPosition();
	}
	
	public abstract int[] getCenter();
	
	/**
	 * Velocity is set to zero, object stops.
	 */
	public void resetVelocity(){
		xVelocity = 0;
		yVelocity = 0;
		angle = 0;
	}
	
	/**
	 * @return velocity rounded to an integer value.
	 */
	public int getIntegerVelocity(){
		return (int) Math.round(Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity));
	}

	public double getVelocity(){
		return Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
	}
	
	/**
	 * @return the angle this is flying in.
	 */
	public double getAngle(){
		return angle;
	}

    /**
     * @param xVelocity of the Flying object.
     * @param yVelocity of the Flying object.
     */
	private void setVelocity(double xVelocity, double yVelocity){
	    this.xVelocity = xVelocity;
	    this.yVelocity = yVelocity;
        restoreAngle();
    }

	/**
	 * @param xVelocityShift of the Flying object.
	 * @param yVelocityShift of the Flying object.
	 */
	private void shiftVelocity(double xVelocityShift, double yVelocityShift){
		this.xVelocity += xVelocityShift;
		this.yVelocity += yVelocityShift;
        restoreAngle();
	}
	
	/**
	 * Increases velocity of Object
	 * @param angle of velocity increase.
	 * @param force of velocity increase.
	 */
	public void accelerate(double angle, double force){
		xVelocity += Math.cos(angle) * force;
		yVelocity += Math.sin(angle) * force;
		restoreAngle();
	}

	/**
	 * Restores the value of angle to the current angle of this flying's velocity.
	 */
	private void restoreAngle(){
        this.angle = Math.atan2(yVelocity, xVelocity);
    }
	
	/**
	 * Decreases yVelocity to simulate effects of gravity. Not used in space game.
	 */
	public void gravity(){
		accelerate(-Math.PI / 2, .15);
	}
	
	/**
	 * Causes two flying objects to exchange momentum at the angle of their centers.
	 * @param flying object that collided with this one.
	 */
	public void collide(Flying flying){

		/*Take total momentum divided by total mass to find reference frame where center of mass does not change*/
		double xShift = -(this.xVelocity * this.mass + flying.xVelocity * flying.mass) / (this.mass + flying.mass);
		double yShift = -(this.yVelocity * this.mass + flying.yVelocity * flying.mass) / (this.mass + flying.mass);
		this.shiftVelocity(xShift, yShift);
		flying.shiftVelocity(xShift, yShift);

		boolean successful_collision = true;

		if((flying.getTrueCenter()[0] - this.getTrueCenter()[0]) * xVelocity - (flying.getTrueCenter()[1] - this.getTrueCenter()[1]) * yVelocity >= 0) {
        /*Reverse direction of movement in reference frame where center of mass does not change.*/
			this.setVelocity(-this.xVelocity, -this.yVelocity);
			flying.setVelocity(-flying.xVelocity, -flying.yVelocity);
			if (this instanceof Player || this instanceof Enemy) {
				this.accelerate(this.angle, .5);
				flying.accelerate(flying.angle, .5);
			}
		}
		else successful_collision = false;

        /*Reverse reference frame shift*/
		this.shiftVelocity(-xShift, -yShift);
		flying.shiftVelocity(-xShift, -yShift);

		if (this instanceof Player || this instanceof Enemy && !successful_collision) {
			double angle = Math.atan2(-flying.getTrueCenter()[1] + this.getTrueCenter()[1], flying.getTrueCenter()[0] - this.getTrueCenter()[0]);
			this.accelerate(angle + Math.PI, .5);
			flying.accelerate(angle, .5);
		}

		if (this.getVelocity() > maxSpeed) {
			this.setVelocity(maxSpeed * Math.cos(this.angle), maxSpeed * Math.sin(this.angle));
		}
		if (flying.getVelocity() > flying.maxSpeed) {
			flying.setVelocity(flying.maxSpeed * Math.cos(flying.angle), flying.maxSpeed * Math.sin(flying.angle));
		}
	}
	
	/**
	 * DragAmount must be given a value, this should usually
	 * be set in the sub-class.
	 */
	public void drag(){
		xVelocity *= dragAmount;
		yVelocity *= dragAmount;
	}
}
