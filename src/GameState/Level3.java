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
        MainEnemyList.add(new CircularList<>());
        MainEnemyList.add(new DoubleCircularList<>());

        createSubScene();

        MainEnemyList.get(0).setType("ClaseD");
        createAliens(MainEnemyList.get(0));

        MainEnemyList.get(1).setType("ClaseE");
        createAliens(MainEnemyList.get(1));

        createBackground();
    }

    private void createSubScene() {
        subScene = new LevelTransition("Nivel 3");
        Pane.getChildren().add(subScene);
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
        if (current == 0){
            if (current != MainEnemyList.length()) {
                for (int i = 0; i < MainEnemyList.get(current).length(); i++) {
                    MainEnemyList.get(current).get(i).update();
                }
            }
        }

        collisionController(MainEnemyList.get(current));

        if (MainEnemyList.get(current).length() == 0) {
            current++;
            if (MainEnemyList.get(current).getType().equals("ClaseE"))
                rotate(MainEnemyList.get(current));
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

    private void createAliens(ListModel<Enemy> EnemyList){
        String[][] images = { {"resources/Alien1.png", "resources/Alien2.png"},
                              {"resources/Alien1green.png", "resources/Alien2green.png"},
                              {"resources/Alien1blue.png", "resources/Alien2blue.png"},
                              {"resources/Alien1pink.png", "resources/Alien2pink.png"},
                              {"resources/Alien1orange.png", "resources/Alien2orange.png"},
                              {"resources/Alien1yellow.png", "resources/Alien2yellow.png"},
                              {"resources/Alien1turquesa.png", "resources/Alien2turquesa.png"} };

        String[] bossImages = {"resources/AlienBoss1.png", "resources/AlienBoss2.png"};

        int boss = 0;
        if (EnemyList.getType().equals("ClaseD"))
             boss = (int) (Math.random() * (7));

        int xPos = 7;

        if (EnemyList.getType().equals("ClaseE")){
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
        if (EnemyList.getType().equals("ClaseD")) {
            if (EnemyList.length() != 1)
                keepSorted(EnemyList);
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

                int bossNumber = 0;

                for (int i = 0; i < EnemyList.length(); i++) {
                    if (EnemyList.get(i).getID().equals("BOSS"))
                        bossNumber = i;
                }

                for (int i = 0; i < EnemyList.length(); i++) {
                    if (i < bossNumber) {
                        EnemyList.get(i).setXandY(
                                EnemyList.get(bossNumber).getX() + (60 + 55 * i) * Math.cos(t),
                                EnemyList.get(bossNumber).getY() + (60 + 55 * i) * Math.sin(t));
                    }if (i > bossNumber) {
                        EnemyList.get(i).setXandY(
                                EnemyList.get(bossNumber).getX() + (60 - (55 * (i - 1.75))) * Math.cos(t),
                                EnemyList.get(bossNumber).getY() + (60 - (55 * (i - 1.75))) * Math.sin(t));
                    }
                }
            }
        }
        ));

        timeline.playFromStart();
    }


    private void collisionController(ListModel<Enemy> EnemyList){
        for (int i = 0; i < bullets.length(); i++) {
            Bullet bullet = bullets.get(i);

            for (int j = 0; j < EnemyList.length(); j++) {
                if (bullet.isColliding(EnemyList.get(j))){
                    if (EnemyList.get(j).getLife() != 1)
                        EnemyList.get(j).setLife(EnemyList.get(j).getLife() - 1);
                    else {
                        if (EnemyList.getType().equals("ClaseD")){
                            boolean isBoss = EnemyList.get(j).getID().equals("BOSS");

                            EnemyList.remove(j);

                            if (isBoss) {
                                String[] bossImages = {"resources/AlienBoss1.png",
                                        "resources/AlienBoss2.png"};
                                EnemyList.get(0).setID("BOSS");
                                EnemyList.get(0).setImages(bossImages);
                            }
                        } else
                            EnemyList.remove(j);
                    }
                    bullets.remove(i);
                    if (EnemyList.getType().equals("ClaseD"))
                        keepSorted(EnemyList);
//                    if (EnemyList.getType().equals("ClaseE"))
//                        inMiddle(EnemyList);
                }
            }
        }
    }

    private void inMiddle(ListModel<Enemy> EnemyList){
        double lastX;
        int ind = (EnemyList.length() - (EnemyList.length() + 1) / 2);
        System.out.println(EnemyList.length());
        if (!EnemyList.get(ind).getID().equals("BOSS")){
            int tmpBoss = 0, change = (EnemyList.length() - (EnemyList.length() + 1) / 2);

            for (int i = 0; i < EnemyList.length(); i++) {
                if (EnemyList.get(i).getID().equals("BOSS"))
                    tmpBoss = i;
            }
            System.out.println(tmpBoss + "  Indice Boss");
            System.out.println(change + "   Indice Change");
            lastX = EnemyList.get(change).getX();
            System.out.println(lastX + "   LastX");

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

            for (int i = 0; i < EnemyList.length(); i++) {
                System.out.println(EnemyList.get(i).getID() + "   " + i);
            }
        }

    }

    private void keepSorted(ListModel<Enemy> EnemyList){
        double lastX = 0;

        if (EnemyList.length() > 0)
            lastX = EnemyList.get(0).getX();

        if (lastX >= 540 - 55 * EnemyList.length())
            lastX = (540 - 55 * EnemyList.length() - 1);

        bubbleSort(EnemyList);

        int xPos = EnemyList.length();
        for (int i = 0; i < EnemyList.length(); i++) {
            EnemyList.get(i).setPosition(lastX + 55 * i, EnemyList.get(i).getY(), xPos, i);
            xPos--;
        }
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
}
