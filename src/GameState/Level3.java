package GameState;

import Server.Server;
import character.Bullet;
import character.Enemy;
import character.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.*;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Level3 extends GameStateManager {
    private AnchorPane Pane;
    private LevelTransition subScene;

    private Player player;
    private LinkedList<Bullet> bullets;

    private LinkedList<ListModel<Enemy>> MainEnemyList;

    private Label label;
    private Label subLabel;
    private Label score;

    private Server server;
    private int current;

    private boolean firstRun = true;
    private boolean eRun = true;
    private boolean sceneRun = true;

    Level3(AnchorPane Pane){
        this.Pane = Pane;

        player = new Player(220, 655);
        bullets = new LinkedList<>();
        server = Server.getServer();

        MainEnemyList = new LinkedList<>();

        createSubScene();
        createLabels();
        generateRows();
        createBackground();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update() {
        if(sceneRun){
            subScene.startSubScene();
            sceneRun = false;
        }

        if (server.getConnected()){
            if (firstRun){
                serverConnect();
                firstRun = false;
            }
        }
        //Player
        Pane.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT)
                player.setRight(true);
            if (e.getCode() == KeyCode.LEFT)
                player.setLeft(true);
            if (e.getCode() == KeyCode.SPACE)
                bullets.add(new Bullet(player.getX(), player.getY(),
                        new Image("resources/bullet.png"), 20));
        });

        Pane.getScene().setOnKeyReleased(e ->{
            if (e.getCode() == KeyCode.RIGHT)
                player.setRight(false);
            if (e.getCode() == KeyCode.LEFT)
                player.setLeft(false);
        });

        player.update();

        //bullets
        for (int i = 0; i < bullets.length(); i++) {
            Bullet shoot = bullets.get(i);
            if ( shoot.getY() < -15)
                bullets.remove(i);
            shoot.update();
        }

        //Enemy update
        if (current != MainEnemyList.length() - 1) {
            if (!MainEnemyList.get(current).getType().equals("ClaseE")){
                for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
                    MainEnemyList.get(current).get(i).update();
                }
            } else{
                if (eRun){
                    rotate(MainEnemyList.get(current));
                    eRun = false;
                }
            }
        }
        collisionController(MainEnemyList.get(current));

        if (MainEnemyList.get(current).length() == 0) {
            current++;
            Enemy.speed += 0.15;
            eRun = true;
        }
        updateLabels();
    }

    @Override
    public void render(GraphicsContext g) {
        g.clearRect(0 , 0, 540, 720);

        player.render(g);

        for (int i = 0; i < bullets.length(); i++) {
            bullets.get(i).render(g);
        }

        for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
            MainEnemyList.get(current).get(i).render(g);
        }
    }

    private void createAliens(ListModel<Enemy> EnemyList) {
        String[][] images = {{"resources/Alien1.png", "resources/Alien2.png"},
                {"resources/Alien1green.png", "resources/Alien2green.png"},
                {"resources/Alien1blue.png", "resources/Alien2blue.png"},
                {"resources/Alien1pink.png", "resources/Alien2pink.png"},
                {"resources/Alien1orange.png", "resources/Alien2orange.png"},
                {"resources/Alien1yellow.png", "resources/Alien2yellow.png"},
                {"resources/Alien1turquesa.png", "resources/Alien2turquesa.png"}};

        String[] bossImages = {"resources/AlienBoss1.png", "resources/AlienBoss2.png"};

        int boss = 0;
        if (!EnemyList.getType().equals("ClaseE"))
            boss = (int) (Math.random() * (7));

        int xPos = 7;
        int yPos = 1;
        if (EnemyList.getType().equals(""))
            yPos = -150;


        if (EnemyList.getType().equals("ClaseE")) {
            for (int i = 0; i < 7; i++) {
                int life = (int) (Math.random() * 5) + 1;

                if (i == 3)
                    EnemyList.add(new Enemy(55 * i + 110, 1, xPos, i, bossImages, "BOSS"));
                else {
                    EnemyList.add(new Enemy(55 * i + 110, 1, xPos, i, images[i], "NORMAL"));
                    EnemyList.get(i).setLife(life);
                }
                xPos--;
            }
        } else {
            for (int i = 0; i < 7; i++) {
                int life = (int) (Math.random() * 6) + 1;
                if (i == boss)
                    EnemyList.add(new Enemy(55 * i, yPos, xPos, i, bossImages, "BOSS"));
                else {
                    EnemyList.add(new Enemy(55 * i, yPos, xPos, i, images[i], "NORMAL"));
                    EnemyList.get(i).setLife(life);
                }
                xPos--;
            }
        }
        if (!EnemyList.getType().equals("ClaseE")) {
            if (EnemyList.length() != 1)
                keepSorted(EnemyList);
        }
    }

    private void collisionController(ListModel<Enemy> EnemyList){
        for (int i = 0; i < bullets.length(); i++) {
            Bullet bullet = bullets.get(i);

            for (int j = 0; j < EnemyList.length(); j++) {
                if (bullet.isColliding(EnemyList.get(j))){
                    if (EnemyList.get(j).getLife() != 1)
                        EnemyList.get(j).setLife(EnemyList.get(j).getLife() - 1);
                    else {
                        if (EnemyList.getType().equals("ClaseD") || EnemyList.getType().equals("ClaseE")){
                            boolean isBoss = EnemyList.get(j).getID().equals("BOSS");
                            int toReplace = 0;

                            GameState.score += EnemyList.get(j).getScore();
                            EnemyList.remove(j);
                            if(EnemyList.getType().equals("ClaseE"))
                                toReplace = (EnemyList.length() - (EnemyList.length() + 1) / 2);

                            if (isBoss) {
                                String[] bossImages = {"resources/AlienBoss1.png",
                                        "resources/AlienBoss2.png"};
                                EnemyList.get(toReplace).setID("BOSS");
                                EnemyList.get(toReplace).setImages(bossImages);
                            }
                        }
                        else if (EnemyList.getType().equals("ClaseC")){
                            boolean isBoss = EnemyList.get(j).getID().equals("BOSS");

                            GameState.score += EnemyList.get(j).getScore();
                            EnemyList.remove(j);
                            if(isBoss)
                                positionChanger(EnemyList, "ClaseC");
                        }
                        else if (EnemyList.getType().equals("ClaseB") && EnemyList.get(j).getID().equals("BOSS")){
                            GameState.score += EnemyList.get(j).getScore();
                            current++;
                        }
                        else{
                            GameState.score += EnemyList.get(j).getScore();
                            EnemyList.remove(j);
                        }

                    }
                    bullets.remove(i);

                    if (!EnemyList.getType().equals("ClaseE"))
                        keepSorted(EnemyList);
                }
            }
        }
    }

    private void rotate(ListModel<Enemy> EnemyList){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        final long timeStart = System.currentTimeMillis();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.017), ae -> {
            double t = (System.currentTimeMillis() - timeStart) / 1000.0;
            double x, y, pos = 1.75;
            int bossNumber = EnemyList.length() / 2, leftSide = 0, rightSide = 0, xPos = EnemyList.length();

            for (int i = 0; i < EnemyList.length(); i++) {
                if (EnemyList.get(i).getID().equals("BOSS")){
                    bossNumber = i;
                    break;
                }
                xPos--;
            }

            EnemyList.get(bossNumber).update(bossNumber, xPos);
            x = EnemyList.get(bossNumber).getX();
            y = EnemyList.get(bossNumber).getY();

            if (EnemyList.length() < 6)
                pos = 0.60;
            if(EnemyList.length() < 4)
                pos = 0;
            if(EnemyList.length() < 3)
                pos = -1;

            for (int i = 0; i < EnemyList.length(); i++) {
                if (i < bossNumber) {
                    EnemyList.get(i).setXandY(
                            x + (55 + 55 * i) * Math.cos(t),
                            y + (55 + 55 * i) * Math.sin(t));
                    leftSide++;
                } if (i > bossNumber) {
                    if (EnemyList.length() < 5 && leftSide == 1)
                        pos = 0;
                    if (EnemyList.length() < 7 && leftSide == 2)
                        pos = 1.15;

                    EnemyList.get(i).setXandY(
                            x + (55 - (55 * (i - pos))) * Math.cos(t),
                            y + (55 - (55 * (i - pos))) * Math.sin(t));
                    rightSide++;
                }
            }
            if((leftSide == 1 && rightSide == 3) || (rightSide == 1 && leftSide == 3) ||
                    (leftSide == 0 && rightSide == 2) || (rightSide == 0 && leftSide == 2)){
                inMiddle(EnemyList);
            }
        }));
        timeline.playFromStart();
    }

    private void inMiddle(ListModel<Enemy> EnemyList){
        int tmpBoss = 0, change = (EnemyList.length() - (EnemyList.length() + 1) / 2);

        for (int i = 0; i < EnemyList.length(); i++) {
            if (EnemyList.get(i).getID().equals("BOSS"))
                tmpBoss = i;
        }

        double Xchange = EnemyList.get(change).getX(),
                Ychange = EnemyList.get(change).getY(),
                iRchange = EnemyList.get(change).getiR(),
                iLchange = EnemyList.get(change).getiL();

        double Xboss = EnemyList.get(tmpBoss).getX()
                , Yboss = EnemyList.get(tmpBoss).getY()
                , iRboss = EnemyList.get(tmpBoss).getiR()
                , iLboss = EnemyList.get(tmpBoss).getiL();

        EnemyList.get(tmpBoss).setPosition(
                Xchange, Ychange,
                iRchange, iLchange
        );

        EnemyList.get(change).setPosition(
                Xboss, Yboss,
                iRboss, iLboss
        );

        NodeModel<Enemy> node1 = EnemyList.getNode(change);
        NodeModel<Enemy> node2 = EnemyList.getNode(tmpBoss);
        CircularNode<Enemy> tmp = new CircularNode<>(node1.getData());

        node1.setData(node2.getData());
        node2.setData(tmp.getData());

        int xPos = EnemyList.length();
        for (int i = 0; i < EnemyList.length(); i++) {
            if(EnemyList.get(i).getID().equals("BOSS")){
                if (EnemyList.get(i).getX() < 55 * i){
                    EnemyList.get(i).setXandY(55 * i + 1, Ychange);
                }
                if (EnemyList.get(i).getX() > 540 - 55 * xPos){
                    EnemyList.get(i).setXandY((540 - 55 * xPos) - 1, Ychange);
                }
            }
            xPos--;
        }
    }

    private void keepSorted(ListModel<Enemy> EnemyList){
        double lastX = 0;

        if (EnemyList.length() > 0)
            lastX = EnemyList.get(0).getX();

        if (lastX >= 540 - 55 * EnemyList.length())
            lastX = (540 - 55 * EnemyList.length() - 1);

        if (EnemyList.getType().equals("ClaseD"))
            bubbleSort(EnemyList);

        int xPos = EnemyList.length();
        for (int i = 0; i < EnemyList.length(); i++) {
            EnemyList.get(i).setPosition(lastX + 55 * i, EnemyList.get(i).getY(), xPos, i);
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

    private void createBackground() {
        Image backgroundImage = new Image("resources/background_game.png", true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        Pane.setBackground(new Background(background));
    }

    @SuppressWarnings("Duplicates")
    private void serverConnect() {
        server.setPlayer(player);
        Thread thread = new Thread(() -> server.run());
        thread.setDaemon(true);
        thread.start();
    }

    private void createSubScene() {
        subScene = new LevelTransition("Nivel 3");
        Pane.getChildren().add(subScene);
    }

    @SuppressWarnings("Duplicates")
    private void updateLabels(){
        String text1 = "Actual: " + MainEnemyList.get(current).getType();
        String text2 = "Siguiente: ";

        if (current + 1 >= MainEnemyList.length())
            text2 += "";
        else
            text2 += MainEnemyList.get(current + 1).getType();

        label.setText(text1);
        subLabel.setText(text2);
        score.setText("Score: " + GameState.score);

        server.setToSend(GameState.getNivel() + ", "+ label.getText() + ", " + subLabel.getText() + ", " + score.getText());
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

            score = new Label();
            score.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 20));
            score.setTranslateX(10);
            score.setTranslateY(60);
            score.setTextFill(Color.valueOf("FFFFFF"));
            Pane.getChildren().add(score);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void generateRows(){
        for (int i = 0; i < 6; i++) {
            int rowType = (int) (Math.random() * 4);

            if(rowType == 0){
                MainEnemyList.add(new DoubleLinkedList<>());
                MainEnemyList.get(i).setType("ClaseB");
                createAliens(MainEnemyList.get(i));
                bossTransition(MainEnemyList.get(i));
            }
            if (rowType == 1){
                MainEnemyList.add(new CircularList<>());
                MainEnemyList.get(i).setType("ClaseC");
                createAliens(MainEnemyList.get(i));
            }
            if (rowType == 2){
                MainEnemyList.add(new CircularList<>());
                MainEnemyList.get(i).setType("ClaseD");
                createAliens(MainEnemyList.get(i));
            }
            if (rowType == 3){
                MainEnemyList.add(new DoubleCircularList<>());
                MainEnemyList.get(i).setType("ClaseE");
                createAliens(MainEnemyList.get(i));
            }
        }

        MainEnemyList.add(new LinkedList<>());
        MainEnemyList.get(6).setType("");
        createAliens(MainEnemyList.get(6));
    }
}