package character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.geometry.Rectangle2D;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;


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

        AudioClip sound = new AudioClip(new File("src/resources/sound/laser.mp3").toURI().toString());
        sound.play();

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




















