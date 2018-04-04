package character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.geom.Rectangle2D;

public class Player
{

    private Image image = new Image("resources/player.png");
    private double x;
    private double y;

    private double dx;
    private int speed;

    private double width = image.getWidth();;
    private double height = image.getHeight();

    private boolean right;
    private boolean left;
    private boolean fire;

    public Player(double x, double y){
        this.x = x;
        this.y = y;
        speed = 7;
    }

    public void setRight(boolean b){
        right = b;
    }

    public void setLeft(boolean b){
        left = b;
    }

    public void setFire(boolean b){
        this.fire = b;
    }

    public boolean getFire(){
        return fire;
    }

    public void update(){
        if (right) {
            dx = speed;
        }
        if (left){
            dx = -speed;
        }

        x += dx;

        if (x < 0){
            x = 0;
        }

        if (x > 540 - width){
            x = 540 - width;
        }

        dx = 0;
    }

    public void render(GraphicsContext g){
        g.drawImage(image, x, y);
    }

    public Rectangle2D getFronter() {
        return new Rectangle2D.Double(x, 600.0, width, height);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
}
