
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Game implements KeyListener{

	private static final int NUMBER_OF_DEBRIS = 30;
	private static final int NUMBER_OF_ENEMIES = 3;
	private static final int INVUL_AFTER_HIT_FRAMES = 30;
    private static final int FRAMES_BETWEEN_SHOTS = 10;
	private static final int MIN_FRAME_DELAY = 16;
	private static final int MAX_PLAYER_RADIUS = 2000;
	private static final int BULLET_FRAMES_TO_LIVE = 300;
	private static final int POINTS_PER_KILL = 10;
	
	int score = 0;
	private boolean alive = true;
	
	private long frames = 0;	//counts the total number of frames played.
	private long lastTime;
	private GamePanel panel;
	private Window   window;
	private Player   player;
	private ArrayList<Debris> debris;
	private ArrayList<Projectile> bullets;
	private ArrayList<Enemy>	enemies;
	private long lastShot = -100;		//Stores the frame of the last time the player fired a projectile
	private long lastHit = -100;		//Stores the frame of the last time the player was hit.
	
	private Thread musicThread;
	private Sound sound;
	
	private boolean playerAccelerating = false;
	private boolean turnLeft = false;
	private boolean turnRight = false;
	private boolean shooting = false;
	
	public Game(Window window, GamePanel panel){
		this.window = window;
		this.panel  =  panel;
		
		window.addKeyListener(this);
		window.requestFocus();
		lastTime = System.currentTimeMillis();
		player  =  this.panel.getPlayer();
		debris  = panel.debris;
		bullets = panel.bullets;
		enemies = panel.enemies;
		sound = new Sound();
		musicThread = new Thread(sound);
		musicThread.start();
		play();
		window.removeKeyListener(this);
	}
	
	/**
	 * Causes game to begin playing.
	 */
	private void play(){
		while(alive){
			mainLoop();
			pause();
        }
		sound.stopTrack();
	}
	
	/**
	 * Responds to user input every frame by changing the player's speed and angle
	 * according to the buttons pressed.
	 */
	private void mainLoop(){
        frames++;
		fillDebris();
		fillEnemies();
		
		movePlayer();
		moveBullets();
		moveEnemies();
		moveDebris();
		
		//panel.backgroundCoordinates();
		panel.starCoordinates(player.getXPos(), player.getYPos());
		player.drawPlayer();
		window.updateFrame();
	}
	
	/**
	 * Moves player for one frame.
	 */
	private void movePlayer(){
        if(player.getHealth() <= 0)
            alive = false;
        if(turnLeft)
            player.drawAngle += Player.TURN_ANGLE;
        if(turnRight)
            player.drawAngle -= Player.TURN_ANGLE;
        if(shooting && frames - lastShot > FRAMES_BETWEEN_SHOTS){
            bullets.add(new Projectile(player.getCenter()[0], player.getCenter()[1], frames));
            bullets.get(bullets.size() - 1).drawProjectile();
            bullets.get(bullets.size() - 1).accelerate(player.drawAngle, Projectile.SPEED + player.getIntegerVelocity());
            lastShot = frames;
            Sound.playerShot();
        }
        moveHelper(player, player.drawAngle, playerAccelerating ? Player.ACCELERATION_SPEED : 0);

        for (Debris debri : debris) {
            if (player.entityHit(debri)) {
                player.collide(debri);
            }
        }
        for (Enemy enemy : enemies) {
            if (player.entityHit(enemy)) {
                player.collide(enemy);
                if (frames - lastHit > INVUL_AFTER_HIT_FRAMES) {
                    player.subtractHealth(20);
                    lastHit = frames;
                }
            }
        }
	}
	
	/**
	 * Moves the player's bullets for one frame.
	 */
	private void moveBullets(){
		for(int i = 0; i < bullets.size(); i++){
            if(frames - bullets.get(i).frame > BULLET_FRAMES_TO_LIVE){
                Util.replaceWithLast(bullets, i);
                i--;
                continue;
            }
			bullets.get(i).move();
			bullets.get(i).updateHitBox();
            for (Debris debri : debris) {
                if (bullets.get(i).entityHit(debri)) {
                    bullets.get(i).collide(debri);
                }
            }
            for (Enemy enemy : enemies) {
                if (bullets.get(i).entityHit(enemy)) {
                    bullets.get(i).collide(enemy);
                    enemy.subtractHealth(Projectile.DAMAGE);
                    Util.replaceWithLast(bullets, i);
                    i--;
                    break;
                }
            }
		}
	}
	
	/**
	 * Moves Debris for one frame.
	 */
	private void moveDebris(){
		for(int i = 0; i < debris.size(); i++){
			moveHelper(debris.get(i));
			for(int k = i + 1; k < debris.size(); k++){
				if(debris.get(i).entityHit(debris.get(k))){
					debris.get(i).collide(debris.get(k));
				}
			}
		}
	}

	/**
	 * Moves Enemies for one frame
	 */
	private void moveEnemies(){
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).drawImage();
			double[] enemyCenter = enemies.get(i).getTrueCenter();
			double[] playerCenter = player.getTrueCenter();
			moveHelper(enemies.get(i), Math.atan2(enemyCenter[1] - playerCenter[1], playerCenter[0] - enemyCenter[0]), Enemy.ACCELERATION);
            for (Debris debri : debris) {
                if (enemies.get(i).entityHit(debri)) {
                    enemies.get(i).collide(debri);
                }
            }
			for(int k = i + 1; k < enemies.size(); k++){
				if(enemies.get(i).entityHit(enemies.get(k))){
					enemies.get(i).collide(enemies.get(k));
				}
			}
		}
	}

	private void moveHelper(Flying flying,double angle, double force){
	    flying.drag();
	    flying.accelerate(angle, force);
	    flying.move();
	    flying.updateHitBox();
    }

    private void moveHelper(Flying flying){
	    flying.drag();
	    flying.move();
	    flying.updateHitBox();
    }
	
	/**Not used, implemented for interface.*/
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_Z){
			playerAccelerating = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			turnLeft = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			turnRight = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_X){
			shooting = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_Z){
			playerAccelerating = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			turnLeft = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			turnRight = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_X){
			shooting = false;
		}
	}
	
	/**
	 * Pauses thread until frame has taken the minimum amount of time.
	 */
	private void pause(){
		long currentTime = System.currentTimeMillis();
		while( (currentTime - lastTime) < MIN_FRAME_DELAY){
			try {
				Thread.sleep(MIN_FRAME_DELAY - currentTime + lastTime);
			}catch (InterruptedException e){}
			currentTime = System.currentTimeMillis();
		}
		lastTime = currentTime;
	}
	
	/**
	 * Maintains a constant number of debris objects on the screen at all times. 
	 */
	private void fillDebris(){
		/*removes debris too far from player */
		for(int i = 0; i < debris.size(); i++){
			if(player.distanceTo(debris.get(i)) >= MAX_PLAYER_RADIUS){
				Util.replaceWithLast(debris, i);
				i--;
			}
		}
		
		/*creates new debris objects to maintain number of debris displayed on screen*/
		while(debris.size() < NUMBER_OF_DEBRIS){
			int[] coordinates = getRandomOffScreenCoordinates();
			boolean debris_added = true;
			
			debris.add(new Debris(coordinates[0], coordinates[1], Debris.SMALLEST_RADIUS + (int)(Math.random() * (Debris.LARGEST_RADIUS - Debris.SMALLEST_RADIUS))));  //creates new debris
			
			/*ensures that new debris does not overlap old debris. */
			for(int i = 0; i < debris.size() - 1; i++){
				if(debris.get(i).distanceTo(debris.get(debris.size() - 1)) < Debris.LARGEST_RADIUS * 1.5) {
                    debris.remove(debris.size() - 1);
                    debris_added = false;
                    break;
                }
			}

			if(debris_added)
			    debris.get(debris.size() - 1).updateHitBox();
		}
	}
	
	/**
	 * Maintains a constant number of enemies on the screen at all times
	 */
	private void fillEnemies(){
		/*removes enemies too far from player*/
		for(int i = 0; i < enemies.size(); i++){
			if(player.distanceTo(enemies.get(i)) >= MAX_PLAYER_RADIUS){
				Util.replaceWithLast(enemies, i);
				i--;
			}
		}
		
		/*removes dead enemies */
		for(int i = 0; i < enemies.size(); i++){
			if(enemies.get(i).getHealth() <= 0){
				score += POINTS_PER_KILL;
				Util.replaceWithLast(enemies, i);
				i--;
			}
		}
		
		/*creates new enemies objects to maintain number of enemies displayed on screen*/
		while(enemies.size() < NUMBER_OF_ENEMIES){
			int[] coordinates = getRandomOffScreenCoordinates();
			
			enemies.add(new Enemy(coordinates[0], coordinates[1]));  //creates new enemy

            enemies.get(enemies.size() - 1).drawImage();
            enemies.get(enemies.size() - 1).updateHitBox();
		}
	}

    /**
     * Gets some random coordinates that are off the screen. Useful for spawning objects out of player view.
     * @return An int[] with a random x and y coordinate not on the screen.
     */
	private int[] getRandomOffScreenCoordinates(){
	    double minRadius = Math.sqrt(panel.getScreenSize()[0] * panel.getScreenSize()[0] + panel.getScreenSize()[1] * panel.getScreenSize()[1]) / 2 + 200;
	    double randRad = Math.random() * (MAX_PLAYER_RADIUS - minRadius) + minRadius;
	    double angle = Math.random() * 2 * Math.PI;
	    int x = player.getCenter()[0] + (int)(randRad * Math.cos(angle));
        int y = player.getCenter()[1] + (int)(randRad * Math.sin(angle));
        return new int[]{x, y};
    }
}