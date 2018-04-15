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

    /**
     * Constructor
     * @param x posicion en X de la bala
     * @param y posicion en Y de la bala
     */
    public Bullet(double x, double y){
        this.x  = x + 37;
        this.y = y - 20;
        this.image = new Image("resources/bullet.png");
        this.width = image.getWidth();
        this.height = image.getHeight();

        //Reproduce el sonido de laser.
        AudioClip laserSound = new AudioClip(new File("src/resources/sound/laser.mp3").toURI().toString());
        laserSound.play();

        dy = -20;
    }

    /**
     * Actualiza la posicion de la bala
     */
    public void update(){
        y += dy;
    }

    /**
     * Dibuja a la bala en la nueva posicion
     * @param g Dibuja lo que esta en el canvas
     */
    public void render(GraphicsContext g){
        g.drawImage(image, x, y);
    }

    /**
     * Da a la imagen un rectangulo con su tamano
     * @return Un rectangulo del tamano de la imagen
     */
    private Rectangle2D getFronter() {
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * Indica que si el objeto esta colisionando con un enemigo
     * @param enemy Alien enemigo
     * @return El valor de la colision
     */
    public boolean isColliding(Enemy enemy){
        return enemy.getFronter().intersects(this.getFronter());
    }

    /**
     * El valor de su posicion en Y.
     * @return La coordenada en Y.
     */
    public double getY() {
        return y;
    }
}