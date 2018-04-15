package Server;

import character.Bullet;
import character.Player;
import model.LinkedList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static Server server = null;
    private boolean connected;
    private static Socket socket;
    private static ServerSocket serverSocket;
    private static InputStreamReader reader;
    private static BufferedReader bufferedReader;
    private static DataOutputStream dout;
    private static String message;
    private static String toSend = "";
    private Player player;
    private LinkedList<Bullet> bullets;

    /**
     * Constructor
     */
    private Server(){
    }

    /**
     * Una instancia del servidor.
     * @return El servidor que se esta usando y si no crea uno nuevo.
     */
    public static Server getServer(){
        if (server == null){
            server = new Server();
        }
        return server;
    }

    /**
     * Da un valor al jugador con el que el servidor trabaja.
     * @param player El jugador que se esta usando en el momento.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Da un valor a las balas con el que el servidor trabaja.
     * @param bullets La lista de balas que se estan usando.
     */
    public void setBullets(LinkedList<Bullet> bullets){
        this.bullets = bullets;
    }

    /**
     * Da el valor a la informacion que se va enviar.
     * @param info Es la informacion que se va enviar.
     */
    public void setToSend(String info){
        toSend = info;
    }

    /**
     * Da el valor de la conexion.
     * @return El valor de conexion.
     */
    public boolean getConnected(){
        return connected;
    }

    /**
     * Corre el loop del servidor.
     */
    public void run() {
        try {
            serverSocket = new ServerSocket(8000);

            while (true) {
                socket = serverSocket.accept();
                if (!connected)
                    connected = true;
                //Mada el mensaje al control.
                dout = new DataOutputStream(socket.getOutputStream());
                dout.writeUTF(toSend);

                //Lee el mensaje que mando la aplicacion de telefono
                reader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(reader);
                message = bufferedReader.readLine();

                // Lee el mensaje y da la accion que fue enviada.
                if (message.equals("Right")) {
                    player.setLeft(false);
                    player.setRight(true);
                }
                if (message.equals("Left")) {
                    player.setRight(false);
                    player.setLeft(true);
                }
                if (message.equals("  ")) {
                    player.setLeft(false);
                    player.setRight(false);
                }
                if (message.equals("Fire"))
                    bullets.add(new Bullet(player.getX(), player.getY()));
            }
        } catch (IOException e) {
            System.out.print("");
        }
    }
}