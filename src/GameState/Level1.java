package GameState;
import character.Bullet;
import character.Enemy;
import character.Player;
import Server.Server;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.LinkedList;

import java.io.*;

public class Level1 extends GameStateManager{
    private AnchorPane Lvl1Pane;
    private Stage TheStage;
    private Scene Level2Scene;

    private Label label;
    private Label subLabel;

    private Player player;
    private LinkedList<Bullet> bullets;
    private int current;

    private Server server;

    private LinkedList<LinkedList<Enemy>> MainEnemyList;

    public Level1(AnchorPane Lvl1Pane, Stage TheStage, Scene Level2Scene) {
        this.Lvl1Pane = Lvl1Pane;
        this.TheStage = TheStage;
        this.Level2Scene = Level2Scene;

        player = new Player(220, 655);
        bullets = new LinkedList<>();

        MainEnemyList = new LinkedList<>();

        MainEnemyList.add(new LinkedList<>());
        MainEnemyList.add(new LinkedList<>());
        MainEnemyList.add(new LinkedList<>());
        MainEnemyList.add(new LinkedList<>());
        MainEnemyList.add(new LinkedList<>());

        createBackground();

        server = Server.getServer();
        server.setPlayer(player);

        Thread thread = new Thread(new Runnable(){
            public void run(){
                server.run();
            }
        });
        thread.start();

        try {
            label = new Label();
            label.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 20));
            label.setTextFill(Color.valueOf("F8FFFD"));
            label.setTranslateX(10);
            label.setTranslateY(20);
            Lvl1Pane.getChildren().add(label);

            subLabel = new Label();
            subLabel.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 20));
            subLabel.setTextFill(Color.valueOf("F8FFFD"));
            subLabel.setTranslateX(10);
            subLabel.setTranslateY(40);
            Lvl1Pane.getChildren().add(subLabel);

        } catch(FileNotFoundException e){
            e.printStackTrace();
        }


        int rng = (int) (Math.random() * 7);

        createAliens(MainEnemyList.get(0), 0.0);
        createAliens(MainEnemyList.get(1), rng);
        createAliens(MainEnemyList.get(2) ,0.0);
        rng = (int) (Math.random() * 7);
        createAliens(MainEnemyList.get(3), rng);
        createAliens(MainEnemyList.get(4), -150.0);
    }


    private void createAliens(LinkedList<Enemy> enemyList, int bossNum){
        String[] images = {"resources/Alien1.png", "resources/Alien2.png"};
        String[] bossImages = {"resources/AlienBoss1.png", "resources/AlienBoss2.png"};

        int xPos = 7;
        enemyList.setType("ClaseA");

        for (int i = 0; i < 7; i++) {
            if (i == bossNum) {
                enemyList.add(new Enemy(55 * i, 0, xPos, i, bossImages, "BOSS"));
            }
            else{
                enemyList.add(new Enemy(55 * i, 0, xPos, i, images, "NORMAL"));
            }
            xPos--;
        }
    }

    private void createAliens(LinkedList<Enemy> enemyList, double posY){
        String[] images = {"resources/Alien1.png", "resources/Alien2.png"};

        int xPos = 7;
        if (posY != -150.00)
            enemyList.setType("Basic");
        else enemyList.setType("");

        for (int i = 0; i < 7; i++) {
            enemyList.add(new Enemy(55 * i, posY, xPos, i, images,"NORMAL"));
            xPos--;
        }
    }

    public void update() {
        try{
            if (current == 4){
                TheStage.setScene(Level2Scene);
                GameState.index++;
            }

            Lvl1Pane.getScene().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.LEFT) {
                    player.setLeft(true);
                }
                if (e.getCode() == KeyCode.RIGHT) {
                    player.setRight(true);
                }
                if (e.getCode() == KeyCode.SPACE)
                    bullets.add(new Bullet(player.getX(), player.getY(), new Image("resources/bullet.png"), 20));
            });

            Lvl1Pane.getScene().setOnKeyReleased(e -> {
                if (e.getCode() == KeyCode.RIGHT) {
                    player.setRight(false);
                }
                if (e.getCode() == KeyCode.LEFT) {
                    player.setLeft(false);
                }

            });

            if(player.getFire()) {
                bullets.add(new Bullet(player.getX(), player.getY(), new Image("resources/bullet.png"), 20));
                player.setFire(false);
            }

            //Player Bullets update
            for (int i = 0; i < bullets.length(); i++) {
                Bullet shoot = bullets.get(i);

                if (shoot.getY() < -15) {
                    bullets.remove(i);
                }
                shoot.update();
            }

            //collision
            collisionController();

            //Enemy update
            if (current != MainEnemyList.length() - 1){

            for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
                Enemy enemy = MainEnemyList.get(current).get(i);
                enemy.update();}
            }

            //Control de hileras
            if (MainEnemyList.get(current).length() == 0) {
                current++;
                Enemy.speed += 0.25;
            }

            player.update();
            currentRow();


        } catch( NullPointerException e){
            System.out.print("");
        }
    }

    //Enemy - Bullet collision
    private void collisionController() {
        try {
            for (int j = 0; j < bullets.length(); j++) {
                Bullet bullet = bullets.get(j);

                for (int k = 0; k < MainEnemyList.get(current).length(); k++) {

                    if (bullet.isColliding(MainEnemyList.get(current).get(k))) {
                        double lastX = 0;

                        if (MainEnemyList.get(current).get(k).getID().equals("BOSS")) {

                            if (MainEnemyList.get(current).getType().equals("ClaseA"))
                                collisionClassA(MainEnemyList.get(current));

                            bullets.remove(j);

                        } else {
                            if (MainEnemyList.get(current).length() > 0)
                                lastX = MainEnemyList.get(current).get(0).getX();

                            MainEnemyList.get(current).remove(k);
                            bullets.remove(j);

                            int count = MainEnemyList.get(current).length();

                            for (int l = 0; l < MainEnemyList.get(current).length(); l++) {
                                MainEnemyList.get(current).get(l).setPosition(
                                        lastX + 55 * l,
                                        MainEnemyList.get(current).get(l).getY(),
                                        count, l);
                                count--;
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println();
        }
    }

    public void collisionClassA(LinkedList<Enemy> EnemyList){
        for (int i = 0; i < EnemyList.length(); i++) {
            if (EnemyList.get(i).getLife() != 1)
                EnemyList.get(i).setLife(EnemyList.get(i).getLife() - 1);
            else
                current++;
        }
    }

    public void render(GraphicsContext g){
        try{
            g.clearRect(0 , 0, 540, 720);

            player.render(g);


            for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
                MainEnemyList.get(current).get(i).render(g);
            }

            for (int i = 0; i < bullets.length(); i++) {
                bullets.get(i).render(g);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void currentRow(){
        String hilera = "Actual: ";
        String next = "Siguiente: ";

        if (MainEnemyList.get(current + 1).getType().equals(""))
            next += "";

        else if (MainEnemyList.get(current + 1).getType().equals("ClaseA"))
            next += "Clase A";

        else if (MainEnemyList.get(current + 1).getType().equals("Basic"))
            next += "Basic";

        if (MainEnemyList.get(current).getType().equals("ClaseA"))
            hilera += "Clase A";

        else if (MainEnemyList.get(current).getType().equals("Basic"))
            hilera += "Basic";

        subLabel.setText(next);
        label.setText(hilera);
        }



    private void createBackground()
    {
        Image backgroundImage = new Image("resources/background_game.png", true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        Lvl1Pane.setBackground(new Background(background));
    }

}
