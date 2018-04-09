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
import model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Level2 extends GameStateManager{
    private AnchorPane Pane;
    private Stage TheStage;
    private Scene Level3Scene;
    private LevelTransition subScene;

    private Server server;
    private Player player;
    private LinkedList<Bullet> bullets;

    private LinkedList<ListModel<Enemy>> MainEnemyList;

    private Label label;
    private Label subLabel;
    private Label scoreLabel;

    private int current;
    private boolean firstRun = true;

    public Level2(AnchorPane Pane, Stage TheStage, Scene Level3Scene){
        this.Pane = Pane;
        this.TheStage = TheStage;
        this.Level3Scene = Level3Scene;

        player = new Player(220, 655);
        bullets = new LinkedList<>();

        MainEnemyList = new LinkedList<>();
        createBackground();
        createLabels();
        createSubScene();
        generateRows();

    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update() {
        try{
            if (current == 6){
                TheStage.setScene(Level3Scene);
                GameState.index++;
            }

            if (firstRun){
                serverConnect();
                subScene.startSubScene();
                firstRun = false;
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

                        GameState.score += EnemyList.get(j).getScore();
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
                if (type.equals("ClaseB") || type.equals("ClaseA") || (type.equals("ClaseC") && EnemyList.length() == 1)){
                    GameState.score += EnemyList.get(i).getScore();
                    current++;
                }
                else if(type.equals("ClaseC"))
                    positionChanger(EnemyList, type);
        }
    }

    /**
     * Actualiza la informacion de los textos
     */
    private void showRow() {
        String text1 = "Actual: " + MainEnemyList.get(current).getType();
        String text2 = "Siguiente: ";

        if (current + 1 >= MainEnemyList.length())
            text2 += "";
        else
            text2 += MainEnemyList.get(current + 1).getType();

        label.setText(text1);
        subLabel.setText(text2);
        scoreLabel.setText("Score: " + GameState.score);

        server.setToSend(GameState.getNivel() + ", "+ label.getText() + ", " + subLabel.getText() + ", " + scoreLabel.getText());
    }

    private void createBackground() {
        Image backgroundImage = new Image("resources/background_game.png", true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        Pane.setBackground(new Background(background));
    }

    private void createLabels() {
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

            scoreLabel = new Label();
            scoreLabel.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 20));
            scoreLabel.setTextFill(Color.valueOf("FFFFFF"));
            scoreLabel.setTranslateX(10);
            scoreLabel.setTranslateY(60);
            Pane.getChildren().add(scoreLabel);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
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

    @SuppressWarnings("Duplicates")
    private void serverConnect(){
        server = Server.getServer();
        server.setPlayer(player);

        Thread thread = new Thread(new Runnable(){

            public void run(){
                server.run();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void createSubScene() {
        subScene = new LevelTransition("Nivel 2");
        Pane.getChildren().add(subScene);
    }

    /**
     * Crea las hileras de aliens aleatoriamente
     */
    private void generateRows(){
        for (int i = 0; i < 6; i++) {
            int rowType = (int) (Math.random() * 4);

            if (rowType == 0){
                MainEnemyList.add(new LinkedList<>());
                createAliens(MainEnemyList.get(i), 0);
            }
            if (rowType == 1){
                MainEnemyList.add(new LinkedList<>());
                createAliens(MainEnemyList.get(i));
                MainEnemyList.get(i).setType("ClaseA");
            }
            if (rowType == 2){
                MainEnemyList.add(new DoubleLinkedList<>());
                createAliens(MainEnemyList.get(i));
                MainEnemyList.get(i).setType("ClaseB");
                bossTransition(MainEnemyList.get(i));
            }
            if (rowType == 3){
                MainEnemyList.add(new CircularList<>());
                createAliens(MainEnemyList.get(i));
                MainEnemyList.get(i).setType("ClaseC");
            }
        }
        MainEnemyList.add(new LinkedList<>());
        createAliens(MainEnemyList.get(6), -150);
    }
}
