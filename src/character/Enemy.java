package character;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

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
    private int score;

    /**
     * Constructor.
     * @param x Posicion en X del enemigo.
     * @param y Posicion en Y del enemigo.
     * @param iR Limitador cuando el alien se mueve a la derecha.
     * @param iL Limitador cuando el alien se mueve a la izquierda.
     * @param aliens Lista de imagenes.
     * @param ID Tipo de alien.
     */
    public Enemy(double x, double y, double iR, double iL, String[] aliens, String ID){
        setPosition(x, y, iR, iL);
        this.ID = ID;

        images = new Image[2];

        //Crea la lista de imagenes.
        for (int i = 0; i < aliens.length; i++) {
            images[i] = new Image(aliens[i]);
        }

        width = images[0].getWidth();
        height = images[0].getHeight();
        this.score = 10;

        //Empieza la animacion del alien.
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

        if (ID.equals("BOSS")) {
            this.score = Math.abs(50);
            life = 7;
        }

        speed = 1.5;
    }

    /**
     * Actualiza la posicion del enemigo.
     */
    @SuppressWarnings("Duplicates")
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

    /**
     * Actualiza la posicion del alien para la hilera de clase E.
     * @param iR Limitador cuando el alien se mueve a la derecha.
     * @param iL Limitador cuando el alien se mueve a la izquierda.
     */
    @SuppressWarnings("Duplicates")
    public void update(int iL, int iR){
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

    /**
     * Dibuja el enemigo en la posicion actualizada.
     * @param g Dibuja en el canvas.
     */
    public void render(GraphicsContext g){
        g.drawImage(currentImage, x, y);

    }

    /**
     * Crea un rectangulo del tamano del enemigo.
     * @return El rectangulo del tamano del alien.
     */
    public Rectangle2D getFronter(){
        return new Rectangle2D(x, y, width, height);
    }

    /**
     * Asigna un valor a right.
     * @param right Valor de right.
     */
    public void setRight(boolean right) {
        this.right = right;
        left = false;
    }

    /**
     * Asigna un valor a left.
     * @param left Valor de left.
     */
    public void setLeft(boolean left) {
        this.left = left;
        right = false;
    }

    /**
     * Asigna el valor de las variables de posicion y limitadores.
     * @param x Posicion en X del enemigo.
     * @param y Posicion en Y del enemigo.
     * @param iR Limitador cuando el alien se mueve a la derecha.
     * @param iL Limitador cuando el alien se mueve a la izquierda.
     */
    public void setPosition(double x, double y, double iR, double iL){
        this.x = x;
        this.y = y;
        this.iR = iR;
        this.iL = iL;
    }

    /**
     * Obtiene el valor de la posicion en X.
     * @return Posicion en X.
     */
    public double getX() {
        return x;
    }

    /**
     * Obtiene el valor de la posicion en Y.
     * @return Posicion en Y.
     */
    public double getY() {
        return y;
    }

    /**
     * Asigna el valor a la ID.
     * @param ID Valor de la ID.
     */
    public void setID(String ID){
        this.ID = ID;

        if (getID().equals("BOSS"))
            setLife(7);
    }

    /**
     * Obtiene el valor de la ID.
     * @return El valor de la ID.
     */
    public String getID(){
        return ID;
    }

    /**
     * Asigna un valor a la vida.
     * @param life El valor de la vida.
     */
    public void setLife(int life){
        this.life = life;
    }

    /**
     * Obtiene el valor de la vida.
     * @return El valor de la vida.
     */
    public int getLife(){
        return life;
    }

    /**
     * Obtiene el valor que fue asignado al enemigo.
     * @return Los puntos que vale el enemigo.
     */
    public int getScore(){
        return this.score;
    }

    /**
     * Asigna las imagenes del enemigo.
     * @param aliens Las direcciones de la imagenes del enemigo.
     */
    public void setImages(String[] aliens){
        Image[] newImages = new Image[2];
        for (int i = 0; i < aliens.length; i++) {
            newImages[i] = new Image(aliens[i]);
        }

        images = newImages;
    }

    /**
     * Asigna un valor a las posiciones en X y Y.
     * @param x Posicion en X del enemigo.
     * @param y Posicion en Y del enemigo.
     */
    public void setXandY(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Obtiene el valor del limitador derecho.
     * @return El valor del limitador derecho.
     */
    public double getiR() {
        return iR;
    }

    /**
     * Obtiene el valor del limitador izquierdo.
     * @return El valor del limitador izquierdo.
     */
    public double getiL() {
        return iL;
    }

}
