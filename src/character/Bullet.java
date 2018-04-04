package character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.geometry.Rectangle2D;

public class Bullet {
    private Image image;

    private double x;
    private double y;

    private double dy;

    private double width;
    private double height;


    public Bullet(double x, double y, Image image, double speed){
        this.x  = x + 37;
        this.y = y - 20;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();

        dy = -speed;
    }

    public void update(){
        y += dy;
    }

    public void render(GraphicsContext g){
        g.drawImage(image, x, y);
    }

    private Rectangle2D getFronter() {
        return new Rectangle2D(x, y, width, height);
    }

    public boolean isColliding(Enemy enemy){
        return enemy.getFronter().intersects(this.getFronter());
    }

    public double getY() {
        return y;
    }
}
