
/**
 * A living character with health.
 * Has a position.
 * Can die.
 * 
 * @author Seth Tinglof
 * @version 1.0
 */
public abstract class Character extends Entity{
	
	private int health;
	
	public Character(int xPos, int yPos) {
		super(xPos, yPos);
	}
	
	/**
	 * Checks if character has 0 or less health.
	 * If it does, the character is killed.
	 */
	public void checkIfKilled(){
		if(health <= 0)
			kill();
	}
	
	/**
	 * Sets value of health
	 * @param health
	 */
	public void setHealth(int health){
		this.health = health;
	}
	
	/**
	 * @return value for health
	 */
	public int getHealth(){
		return health;
	}
	
	/**
	 * removes argument number of health.
	 * @param healthChange
	 */
	public void subtractHealth(int healthChange){
		health -= healthChange;
		checkIfKilled();
	}
	
	/**
	 * sets character to not be displayed.
	 */
	private void kill(){
		super.setShown(false);
		super.setActive(false);
	}
	
	/**
	 * moves character parameter distance right.
	 * negative values go left.
	 * @param distance
	 */
	public void moveX(int distance){
		setXPos(getXPos() + distance);
	}
	
	/**
	 * moves character parameter distance up.
	 * negative values go down.
	 * @param distance
	 */
	public void moveY(int distance){
		setYPos(getYPos() - distance);
	}
	
	/**
	 * @return String with character's position and health.
	 */
	@Override
	public String toString(){
		return super.toString() + "Health: " + health + "\n";
	}
}