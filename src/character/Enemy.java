package character;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import java.util.Random;


public class Enemy
{
    private Image[] images;
    private Image currentImage;
    private int currentNum;

    private double x;
    private double y;

    private double dx;
    public static double speed;

    private double width;
    private double height;

    private boolean right = true;
    private boolean left;

    private double iR;
    private double iL;
    private String ID;
    private int life;

    public Enemy(double x, double y, double iR, double iL, String[] aliens, String ID){
        setPosition(x, y, iR, iL);
        this.ID = ID;

        images = new Image[2];

        for (int i = 0; i < aliens.length; i++) {
            images[i] = new Image(aliens[i]);
        }

        width = images[0].getWidth();
        height = images[0].getHeight();

        Random rnd = new Random(System.nanoTime());

        Timeline timeline = new Timeline();

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(500),
                        e -> {
                    if (currentNum == 0)
                        currentNum = 1;
                    else if (currentNum == 1)
                        currentNum = 0;

                    currentImage = images[currentNum];
                    }));
        timeline.playFromStart();



        if (ID.equals("BOSS"))
            life = 7;

        speed = 1.5;

    }

    public void update(){

        if (55 * iL > x) {
            setRight(true);
            y += 40;
        }
        if (x > 540 -  55 * iR){
            setLeft(true);
            y += 40;
        }

        if (right){
            dx = speed;
        }

        if (left){
            dx = -speed;
        }

        x += dx;
}

    public void render(GraphicsContext g){
        g.drawImage(currentImage, x, y);

    }

    public Rectangle2D getFronter(){
        return new Rectangle2D(x, y, width, height);
    }

    public void setRight(boolean right) {
        this.right = right;
        left = false;
    }

    public void setLeft(boolean left) {
        this.left = left;
        right = false;
    }

    public void setPosition(double x, double y, double iR, double iL){
        this.x = x;
        this.y = y;
        this.iR = iR;
        this.iL = iL;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setID(String ID){
        this.ID = ID;

        if (getID().equals("BOSS"))
            setLife(7);
    }

    public String getID(){
        return ID;
    }

    public void setLife(int life){
        this.life = life;
    }

    public int getLife(){
        return life;
    }

    public void setImages(String[] aliens){
        Image[] newImages = new Image[2];
        for (int i = 0; i < aliens.length; i++) {
            newImages[i] = new Image(aliens[i]);
        }

        images = newImages;
    }

    public void setXandY(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getiR() {
        return iR;
    }

    public double getiL() {
        return iL;
    }

}
