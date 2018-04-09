package GameState;

import Server.Server;
import character.Bullet;
import character.Enemy;
import character.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import model.*;
import javafx.util.Duration;

public class Level3 extends GameStateManager {
    private AnchorPane Pane;
    private LevelTransition subScene;

    private Player player;
    private LinkedList<Bullet> bullets;

    private LinkedList<ListModel<Enemy>> MainEnemyList;

    private Server server;
    private int current;
    private boolean firstRun = true;

    Level3(AnchorPane Pane){
        this.Pane = Pane;

        player = new Player(220, 655);
        bullets = new LinkedList<>();

        MainEnemyList = new LinkedList<>();

        MainEnemyList.add(new DoubleCircularList<>());
        MainEnemyList.add(new DoubleLinkedList<>());
        MainEnemyList.add(new CircularList<>());
        MainEnemyList.add(new CircularList<>());


        createSubScene();

        MainEnemyList.get(3).setType("ClaseD");
        createAliens(MainEnemyList.get(3));

        MainEnemyList.get(1).setType("ClaseB");
        createAliens(MainEnemyList.get(1));
        bossTransition(MainEnemyList.get(1));

        MainEnemyList.get(2).setType("ClaseC");
        createAliens(MainEnemyList.get(2));

        MainEnemyList.get(0).setType("ClaseE");
        createAliens(MainEnemyList.get(0));
        rotate(MainEnemyList.get(0));

        createBackground();
    }

    private void generateRows(){
        for (int i = 0; i < 6; i++) {
            int rowType = (int) (Math.random());
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void update() {
        if (firstRun){
            serverConnect();
            subScene.startSubScene();
            firstRun = false;
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
        if (current != MainEnemyList.length()) {
            for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
//                MainEnemyList.get(current).get(i).update();
            }
        }

        collisionController(MainEnemyList.get(current));

        if (MainEnemyList.get(current).length() == 0) {
            current++;
//            if (MainEnemyList.get(current).getType().equals("ClaseE"))
//                rotate(MainEnemyList.get(current));
        }
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

        if (EnemyList.getType().equals("ClaseE")) {
            for (int i = 0; i < 7; i++) {
                int life = (int) (Math.random() * 5) + 1;

                if (i == 3)
                    EnemyList.add(new Enemy(55 * i + 110, 150, xPos, i, bossImages, "BOSS"));
                else {
                    EnemyList.add(new Enemy(55 * i + 110, 150, xPos, i, images[i], "NORMAL"));
                    EnemyList.get(i).setLife(life);
                }
                xPos--;
            }
        } else {
            for (int i = 0; i < 7; i++) {
                int life = (int) (Math.random() * 6) + 1;
                if (i == boss)
                    EnemyList.add(new Enemy(55 * i, 1, xPos, i, bossImages, "BOSS"));
                else {
                    EnemyList.add(new Enemy(55 * i, 1, xPos, i, images[i], "NORMAL"));
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

                            EnemyList.remove(j);

                            if(EnemyList.getType().equals("ClaseE"))
                                toReplace = (EnemyList.length() - (EnemyList.length() + 1) / 2);
                            System.out.println(toReplace);
                            if (isBoss) {
                                String[] bossImages = {"resources/AlienBoss1.png",
                                        "resources/AlienBoss2.png"};
                                EnemyList.get(toReplace).setID("BOSS");
                                EnemyList.get(toReplace).setImages(bossImages);
                                System.out.println(EnemyList.get(toReplace).getID());
                            }
                        }
                        else if (EnemyList.getType().equals("ClaseC")){
                            boolean isBoss = EnemyList.get(j).getID().equals("BOSS");

                            EnemyList.remove(j);
                            if(isBoss)
                                positionChanger(EnemyList, EnemyList.getType());
                        }
                        else if (EnemyList.getType().equals("ClaseB") && EnemyList.get(j).getID().equals("BOSS")){
                            current++;
                        }
                        else{
                            EnemyList.remove(j);
                        }

                    }
                    bullets.remove(i);

                    if (!EnemyList.getType().equals("ClaseE"))
                        keepSorted(EnemyList);
//                    if (EnemyList.getType().equals("ClaseE"))
//                        inMiddle(EnemyList);
                }
            }
        }
    }

    private void rotate(ListModel<Enemy> EnemyList){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        final long timeStart = System.currentTimeMillis();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.017), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                double t = (System.currentTimeMillis() - timeStart) / 1000.0;
                double x, y, pos = 1.75;

                int bossNumber = EnemyList.length() / 2, leftSide = 0, rightSide = 0, xPos = EnemyList.length();
                Enemy.speed = 1;

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
                        if (EnemyList.length() < 5 && leftSide == 2)
                            pos = 0.25;
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
            }
        }
        ));
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
                    EnemyList.get(i).setXandY(55 * i + 1, EnemyList.get(i).getY());
                }
                if (EnemyList.get(i).getX() > 540 - 55 * xPos){
                    EnemyList.get(i).setXandY((540 - 55 * xPos) - 1, EnemyList.get(i).getY());
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
        subScene = new LevelTransition("Nivel 3");
        Pane.getChildren().add(subScene);
    }
}
