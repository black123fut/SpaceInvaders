package character;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player
{

    private Image image = new Image("resources/player.png");
    private double x;
    private double y;

    private double dx;
    private int speed;

    private double width;

    private boolean right;
    private boolean left;

    /**
     * Contructor
     * @param x posicion en x de la nave
     * @param y posicion en y de la nave
     */
    public Player(double x, double y){
        this.x = x;
        this.y = y;
        speed = 7;
        width = image.getWidth();
    }

    /**
     * Indica cuando el jugador se debe mover a la derecha
     * @param b Valor booleano de la direccion del movimiento
     */
    public void setRight(boolean b){
        right = b;
    }

    /**
     * Indica cuando el jugador se debe mover a la izquierda
     * @param b Valor booleano de la direccion del movimiento
     */
    public void setLeft(boolean b){
        left = b;
    }

    /**
     * Actualiza la posicion del jugador
     */
    public void update(){
        if (right) {
            dx = speed;
        }
        if (left){
            dx = -speed;
        }

        x += dx;

        //Hace que el jugador no se salga de la pantalla
        if (x < 0){
            x = 0;
        }

        if (x > 540 - width){
            x = 540 - width;
        }

        dx = 0;
    }

    /**
     * Dibuja a la nave en la posicion actualizada
     * @param g Dibuja a la nave en el Canvas
     */
    public void render(GraphicsContext g){
        g.drawImage(image, x, y);
    }

    /**
     * Da el valor de la posicion en X del objeto
     * @return Da el valor de la posicion en X
     */
    public double getX(){
        return x;
    }

    /**
     * Da el valor de la posicion en Y del objeto
     * @return Da el valor de la posicion en Y
     */
    public double getY(){
        return y;
    }
}
