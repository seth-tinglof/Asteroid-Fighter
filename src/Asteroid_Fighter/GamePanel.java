package Asteroid_Fighter;

import Game_Objects.Entity;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel{
    private final int X_STAR_INTERVAL = 160;
    private final int Y_STAR_INTERVAL = 180;

    private Image drawBuffer;
    private int screenWidth, screenHeight;
	private Player player;
	
	ArrayList<Debris> debris      = new ArrayList<>();
	ArrayList<Projectile> bullets = new ArrayList<>();
	ArrayList<Enemy> enemies      = new ArrayList<>();
	ArrayList<Star> stars         = new ArrayList<>();
	
	public GamePanel(int screenWidth, int screenHeight){
		player = new Player(0, 0);
		player.drawPlayer();
		populateBackground(screenWidth, screenHeight);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(drawBuffer, 0, 0, this);
	}

	public void updateImage(){
	    Image drawBuffer = createImage(getWidth(), getHeight());
        Graphics g = drawBuffer.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        int minX = player.getXPos() - screenWidth / 2 + player.WIDTH / 2;
        int minY = player.getYPos() - screenHeight / 2 + player.HEIGHT / 2;
        drawEntityList(g, stars, 0, 0);
        g.drawImage(player.getImage(), screenWidth / 2 - player.WIDTH / 2, screenHeight / 2 - player.HEIGHT / 2, this);
        drawEntityList(g, debris, minX, minY);
        drawEntityList(g, enemies, minX, minY);
        drawEntityList(g, bullets, minX, minY);
        g.setColor(Color.RED);
        g.drawString("Health: " + player.getHealth(), 50, 30);
        g.dispose();
        this.drawBuffer = drawBuffer;
    }

    /**
     * Sets the coordinates where the stars will be drawn
     */
	public void starCoordinates(int playerX, int playerY){
	    for(Star star: stars){
	        star.updateXPos(playerX, screenWidth);
	        star.updateYPos(playerY, screenHeight);
        }
    }

    public int[] getScreenSize(){
		return new int[]{screenWidth, screenHeight};
	}

	private void populateBackground(int screenWidth, int screenHeight){
	    int randX, randY;
        for(int i = 0; i < screenHeight; i+=Y_STAR_INTERVAL){
            for(int j = 0; j < screenWidth; j+=X_STAR_INTERVAL){
                randX = j - (int)(Math.random() * (X_STAR_INTERVAL - Star.SIDE_LENGTH));
                randY = i - (int)(Math.random() * (Y_STAR_INTERVAL - Star.SIDE_LENGTH));
                stars.add(Star.createRandomStar(randX, randY));
            }
        }
    }


	private void drawEntityList(Graphics g, List<? extends Entity> list, int minX, int minY){
	    for(Entity entity: list){
            g.drawImage(entity.getImage(), entity.getXPos() - minX, entity.getYPos() - minY, this);
        }
    }
}
