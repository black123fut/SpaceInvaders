package GameState;

import Server.Server;
import character.Bullet;
import character.Enemy;
import character.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.CircularList;
import model.DoubleLinkedList;
import model.LinkedList;
import model.ListModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Level2 extends GameStateManager{
    private AnchorPane Pane;
    private Stage TheStage;
    private Scene Level3Scene;

    private Server server;
    private Player player;
    private LinkedList<Bullet> bullets;

    private LinkedList<ListModel<Enemy>> MainEnemyList;

    private Label label;
    private Label subLabel;
    private int current;
    private int ActivateSocket;

    public Level2(AnchorPane Pane, Stage TheStage, Scene Level3Scene){
        this.Pane = Pane;
        this.TheStage = TheStage;
        this.Level3Scene = Level3Scene;

        player = new Player(220, 655);
        bullets = new LinkedList<>();


        try {
            label = new Label();
            label.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 20));
            label.setTranslateX(10);
            label.setTranslateY(20);
            label.setTextFill(Color.valueOf("FFFFFF"));
            Pane.getChildren().add(label);

            subLabel = new Label();
            subLabel.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 20));
            subLabel.setTranslateX(10);
            subLabel.setTranslateY(40);
            subLabel.setTextFill(Color.valueOf("FFFFFF"));
            Pane.getChildren().add(subLabel);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        MainEnemyList = new LinkedList<>();
        //SubListas
        MainEnemyList.add(new DoubleLinkedList<>());
        MainEnemyList.add(new LinkedList<>());
        MainEnemyList.add(new LinkedList<>());
        MainEnemyList.add(new CircularList<>());
        MainEnemyList.add(new LinkedList<>());

        createBackground();

        createAliens(MainEnemyList.get(0));
        MainEnemyList.get(0).setType("ClaseB");
        bossTransition(MainEnemyList.get(0));

        createAliens(MainEnemyList.get(1), 0);

        createAliens(MainEnemyList.get(2));
        MainEnemyList.get(2).setType("ClaseA");

        createAliens(MainEnemyList.get(3));
        MainEnemyList.get(3).setType("ClaseC");

        createAliens(MainEnemyList.get(4), -150);
    }

    private void createAliens(ListModel<Enemy> enemyList, int basicY){
        String[] images = {"resources/Alien1.png", "resources/Alien2.png"};

        int xPos = 7;

        if (basicY != -150)
            enemyList.setType("Basic");
        else enemyList.setType("");

        for (int i = 0; i < 7; i++) {
            enemyList.add(new Enemy(55 * i, basicY, xPos, i, images,"NORMAL"));
            xPos--;
        }
    }


    private void createAliens(ListModel<Enemy> EnemyList){
        String[] images = {"resources/Alien1.png", "resources/Alien2.png"};
        String[] bossImages = {"resources/AlienBoss1.png", "resources/AlienBoss2.png"};

        int boss = (int) (Math.random() * (7));

        int xPos = 7;

        for (int i = 0; i < 7; i++) {
            if (i == boss)
                EnemyList.add(new Enemy(55 * i, 0, xPos, i, bossImages,"BOSS"));
            else
                EnemyList.add(new Enemy(55 * i, 0, xPos, i, images,"NORMAL"));
            xPos--;
        }
    }

    private void bossTransition(ListModel<Enemy> EnemyList){
            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(4),
                            e -> {
                                if (EnemyList.length() != 1)
                                    positionChanger(EnemyList, EnemyList.getType());
                            }));
            timeline.playFromStart();
    }

    public void serverConnect(){
        server = Server.getServer();
        server.setPlayer(player);

        Thread thread = new Thread(new Runnable(){

            public void run(){
                server.run();
            }
        });
        thread.start();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update() {
        try{
            if (current == 4){
                TheStage.setScene(Level3Scene);
                GameState.index++;
            }

            if (ActivateSocket == 0){
                serverConnect();
                ActivateSocket++;
            }

            //player
            Pane.getScene().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.LEFT)
                    player.setLeft(true);
                if (e.getCode() == KeyCode.RIGHT)
                    player.setRight(true);
                if (e.getCode() == KeyCode.SPACE)
                    bullets.add(new Bullet(player.getX(), player.getY(),
                            new Image("resources/bullet.png"), 20));
            });

            Pane.getScene().setOnKeyReleased(e -> {
                if (e.getCode() == KeyCode.LEFT)
                    player.setLeft(false);
                if (e.getCode() == KeyCode.RIGHT)
                    player.setRight(false);
            });

            if (player.getFire()){
                bullets.add(new Bullet(player.getX(), player.getY(),
                        new Image("resources/bullet.png"), 20));
                player.setFire(false);
            }

            player.update();

            //Colision Enemy-Bullet
            collisionController(MainEnemyList.get(current));

            //bullets update
            for (int i = 0; i < bullets.length(); i++) {
                Bullet shoot = bullets.get(i);

                if (shoot.getY() < -15)
                    bullets.remove(i);
                shoot.update();
            }

            //Enemy update
            if (current != MainEnemyList.length() - 1){
                for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
                    MainEnemyList.get(current).get(i).update();
                }
            }

            if (MainEnemyList.get(current).length() == 0) {
                current++;
                Enemy.speed += 0.25;
            }

            showRow();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void render(GraphicsContext g) {
        g.clearRect(0 , 0, 540, 720);

        player.render(g);

        for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
            MainEnemyList.get(current).get(i).render(g);
        }

        for (int i = 0; i < bullets.length(); i++) {
            bullets.get(i).render(g);
        }
    }

    private void collisionController(ListModel<Enemy> EnemyList){
        for (int i = 0; i < bullets.length(); i++) {

            Bullet bullet = bullets.get(i);

            for (int j = 0; j < EnemyList.length(); j++) {
                
                if (bullet.isColliding(EnemyList.get(j))){

                    if (EnemyList.get(j).getID().equals("BOSS")){

                        if (!EnemyList.getType().equals("Basic"))
                            collisionForBoss(EnemyList, EnemyList.getType());

                        bullets.remove(i);

                    } else{
                        double lastX = 0;

                        if (EnemyList.length() > 0)
                            lastX = EnemyList.get(0).getX();

                        if (lastX >= 540 - 55 * EnemyList.length())
                            lastX = (540 - 55 * EnemyList.length()) - 1;

                        EnemyList.remove(j);
                        bullets.remove(i);

                        int count = EnemyList.length();

                        for (int k = 0; k < EnemyList.length(); k++) {
                            EnemyList.get(k).setPosition(
                                    lastX + 55 * k,
                                    EnemyList.get(k).getY(),
                                    count, k);
                            count--;
                        }
                    }
                }
            }
        }
    }

    private void collisionForBoss(ListModel<Enemy> EnemyList, String type){
        for (int i = 0; i < EnemyList.length(); i++) {
            if (EnemyList.get(i).getLife() != 1)
                EnemyList.get(i).setLife(EnemyList.get(i).getLife() - 1);
            else
                if (type.equals("ClaseB") || type.equals("ClaseA") || (type.equals("ClaseC") && EnemyList.length() == 1))
                    current++;
                else if(type.equals("ClaseC")){
                    positionChanger(EnemyList, type);
                }
        }
    }

    private void positionChanger(ListModel<Enemy> EnemyList, String type){
        int change = (int) (Math.random() * EnemyList.length()), indBoss = 0;

        for (int i = 0; i < EnemyList.length(); i++) {
            if (EnemyList.get(i).getID().equals("BOSS")){
                indBoss = i;
            }
        }

        if (EnemyList.length() != 0){
            while (change == indBoss){
                change = (int) (Math.random() * EnemyList.length());
            }
        }
        double Xchange = EnemyList.get(change).getX(),
                Ychange = EnemyList.get(change).getY(),
                iRchange = EnemyList.get(change).getiR(),
                iLchange = EnemyList.get(change).getiL();

        double Xboss = EnemyList.get(indBoss).getX()
                , Yboss = EnemyList.get(indBoss).getY()
                , iRboss = EnemyList.get(indBoss).getiR()
                , iLboss = EnemyList.get(indBoss).getiL();

        if (type.equals("ClaseC")){
            String[] bossImages = {"resources/AlienBoss1.png", "resources/AlienBoss2.png"};

            double lastX = EnemyList.get(0).getX();

            if (lastX >= 540 - 55 * EnemyList.length())
                lastX = (540 - 55 * EnemyList.length()) - 1;

            if (EnemyList.length() == 1)
                EnemyList.remove(indBoss);

            else{
                EnemyList.remove(indBoss);
                EnemyList.get(change).setImages(bossImages);
                EnemyList.get(change).setID("BOSS");

                int count = EnemyList.length();

                for (int k = 0; k < EnemyList.length(); k++) {
                    EnemyList.get(k).setPosition(
                            lastX + 55 * k,
                            EnemyList.get(k).getY(),
                            count, k);
                    count--;
                }
            }

        }
        else {
            EnemyList.get(indBoss).setPosition(Xchange
                    ,Ychange
                    ,iRchange
                    ,iLchange
            );

            EnemyList.get(change).setPosition(Xboss
                    , Yboss
                    , iRboss
                    ,iLboss
            );
        }
    }


    private void showRow() {
        String text1 = "Actual: " + MainEnemyList.get(current).getType();
        String text2 = "Siguiente: ";

        if (current + 1 >= MainEnemyList.length())
            text2 += "";
        else
            text2 += MainEnemyList.get(current + 1).getType();

        label.setText(text1);
        subLabel.setText(text2);
    }

    private void createBackground() {
        Image backgroundImage = new Image("resources/background_game.png", true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        Pane.setBackground(new Background(background));
    }
}
