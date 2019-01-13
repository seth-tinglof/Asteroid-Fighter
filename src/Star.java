import java.awt.Image;

/**
 * A star that can be drawn on the background.
 *
 * @author Seth Tinglof
 * @version 1.0
 */
public class Star extends Entity{

    public static int SIDE_LENGTH = 30;

    private Image image;
    private int type;

    private int originalX, originalY;

    private int width = 30;
    private int height = 30;

    public Star(int xPos, int yPos, int type){
        super(xPos, yPos);
        originalX = xPos;
        originalY = yPos;
        this.type = type;
        setImage();
    }

    /**
     * Creates a new star of a random type (higher probability for smaller stars).
     * @param xPos of the new Star
     * @param yPos of the new Star
     * @return a new Star of random type.
     */
    public static Star createRandomStar(int xPos, int yPos){
        int type;
        int rand = (int)(Math.random() * 8);
        if(Util.isBetween(rand, 0, 3)){
            type = 1;
        }
        else if(Util.isBetween(rand, 4, 6)){
            type = 2;
        }
        else {
            type = 3;
        }
        return new Star(xPos, yPos, type);
    }


    public void updateXPos(int playerX, int screenWidth){
        setXPos(Util.modulus(originalX - playerX, screenWidth + width) - width);
    }

    public void updateYPos(int playerY, int screenHeight){
        setYPos(Util.modulus(originalY - playerY, screenHeight + height) - height);
    }

    private void setImage(){
        switch (type){
            case 1:
                image = ImageResources.star_small;
                break;
            case 2:
                image = ImageResources.star_medium;
                break;
            case 3:
                image = ImageResources.star_large;
                break;
            default:
                image = ImageResources.star_small;
        }
    }

    @Override
    public Image getImage(){
        return image;
    }

    @Override
    public void move() {

    }

    @Override
    public void updateHitBox() {

    }
}
