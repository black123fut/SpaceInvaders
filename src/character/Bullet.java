package character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.geometry.Rectangle2D;
import javafx.scene.media.AudioClip;

import java.io.File;


public class Bullet {
    private Image image;

    private double x;
    private double y;

    private double dy;

    private double width;
    private double height;

    public Bullet(double x, double y){
        this.x  = x + 37;
        this.y = y - 20;
        this.image = new Image("resources/bullet.png");
        this.width = image.getWidth();
        this.height = image.getHeight();

        AudioClip laserSound = new AudioClip(new File("src/resources/sound/laser.mp3").toURI().toString());
        laserSound.play();

        dy = -20;
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




















