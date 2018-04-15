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
import model.*;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Level3 extends GameStateManager {
    private Stage TheStage;
    private AnchorPane Pane;
    private LevelTransition subScene;
    private Scene FinalScene;

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
    private Label level;

    /**
     * Constructor
     * @param Pane AnchorPane del nivel 3
     * @param TheStage Stage principal de la aplicacion
     * @param FinalScene Scene a cambiar si el jugador gana o pierde
     */
    Level3(AnchorPane Pane, Stage TheStage, Scene FinalScene){
        this.Pane = Pane;
        this.TheStage = TheStage;
        this.FinalScene = FinalScene;

        player = new Player(220, 655);
        bullets = new LinkedList<>();
        server = Server.getServer();

        MainEnemyList = new LinkedList<>();

        //Crea varios elementos del juego
        createSubScene();
        createLabels();
        generateRows();
        createBackground();
    }

    /**
     * Actualiza a los objetos
     */
    @SuppressWarnings("Duplicates")
    @Override
    public void update() {
        //Hace la animacion que muestra el nivel
        if(sceneRun){
            subScene.startSubScene();
            sceneRun = false;
        }

        //Conecta el servidor si se le esta dando uso
        if (server.getConnected()){
            if (firstRun){
                serverConnect();
                firstRun = false;
            }
        }

        //Muestra al jugador
        Pane.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.RIGHT)
                player.setRight(true);
            if (e.getCode() == KeyCode.LEFT)
                player.setLeft(true);
        });

        Pane.getScene().setOnKeyReleased(e ->{
            if (e.getCode() == KeyCode.RIGHT)
                player.setRight(false);
            if (e.getCode() == KeyCode.LEFT)
                player.setLeft(false);
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.Z)
                bullets.add(new Bullet(player.getX(), player.getY()));
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

                    //Si algun enemigo llega a la posicion de la nave, la aplicacion cambia a la escena de derrota
                    if (MainEnemyList.get(current).get(i).getY() > 620){
                        GameState.index = 4;
                        FinalState.condition = "Has Perdido";
                        TheStage.setScene(FinalScene);
                    }
                }
            } else{
                if (eRun){
                    rotate(MainEnemyList.get(current));
                    eRun = false;
                }
            }
        } else{
            GameState.index = 4;
            FinalState.condition = "      Victoria";
            TheStage.setScene(FinalScene);
        }

        collisionController(MainEnemyList.get(current));

        //Cambia a la siguiente hilera, cuando la actual se queda sin enemigos
        if (MainEnemyList.get(current).length() == 0) {
            current++;
            Enemy.speed += 0.15;
            eRun = true;
        }
        //Actualiza los label
        updateLabels();
    }

    /**
     * Dibuja los objetos que estan en uso
     * @param g Dibuja en el canvas los objetos
     */
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

    /**
     * Crea a los objetos de enemigo en la lista
     * @param EnemyList Lista de enemigos
     */
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

        //Si la hilera es de clase E, crea al jefe en el centro
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
        }
        //Si no es Clase E, crea al jefe en una posicion random
        else {
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
        //Ordena a los enemigos de mayor a menor resistencia
        if (!EnemyList.getType().equals("ClaseE")) {
            if (EnemyList.length() != 1)
                keepSorted(EnemyList);
        }
    }

    /**
     * Verifica las colisiones entre balas y enemigos
     * @param EnemyList Lista de enemigos
     */
    private void collisionController(ListModel<Enemy> EnemyList){
        for (int i = 0; i < bullets.length(); i++) {
            Bullet bullet = bullets.get(i);

            for (int j = 0; j < EnemyList.length(); j++) {
                if (bullet.isColliding(EnemyList.get(j))){
                    //Baja la vida a los enemigos
                    if (EnemyList.get(j).getLife() != 1)
                        EnemyList.get(j).setLife(EnemyList.get(j).getLife() - 1);
                    else {
                        //Si es hilera D o E, el jefe toma el control de algun otro alien.
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
                        //Si es hilera C cambia al jefe de posicion
                        else if (EnemyList.getType().equals("ClaseC")){
                            boolean isBoss = EnemyList.get(j).getID().equals("BOSS");

                            GameState.score += EnemyList.get(j).getScore();

                            if (!isBoss || EnemyList.length() == 1)
                                EnemyList.remove(j);
                            else
                                positionChanger(EnemyList, "ClaseC");
                        }
                        //Si es hilera B pasa a la siguiente hilera
                        else if (EnemyList.getType().equals("ClaseB") && EnemyList.get(j).getID().equals("BOSS")){
                            GameState.score += EnemyList.get(j).getScore();
                            current++;
                        }
                        //Si no solo elimina al alien
                        else{
                            GameState.score += EnemyList.get(j).getScore();
                            EnemyList.remove(j);
                        }

                    }
                    bullets.remove(i);
                    //Mantiene ordenados a los aliens
                    if (!EnemyList.getType().equals("ClaseE"))
                        keepSorted(EnemyList);
                }
            }
        }
    }

    /**
     * Hace que la hilera de aliens rote en la direccion de las manecillas del reloj.
     * @param EnemyList Lista de enemigos
     */
    private void rotate(ListModel<Enemy> EnemyList){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        final long timeStart = System.currentTimeMillis();
        //Hace que actualizen su posicion 60 veces por segundo
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.017), ae -> {
            if (EnemyList == MainEnemyList.get(current)){
                double t = (System.currentTimeMillis() - timeStart) / 1000.0;
                double x, y, pos = 1.75;
                int bossNumber = EnemyList.length() / 2, leftSide = 0, rightSide = 0, xPos = EnemyList.length();

                //Busca el indice del jefe.
                for (int i = 0; i < EnemyList.length(); i++) {
                    if (EnemyList.get(i).getID().equals("BOSS")){
                        bossNumber = i;
                        break;
                    }
                    xPos--;
                }

                //Solo actualiza la posicion del jefe.
                EnemyList.get(bossNumber).update(bossNumber, xPos);

                //Cuando el jefe llega a la posicion de la nave, el jugador pierde.
                if (EnemyList.get(bossNumber).getY() > 620){
                    GameState.index = 4;
                    FinalState.condition = "Has Perdido";
                    TheStage.setScene(FinalScene);
                }

                //Toma la posicion en X y Y del jefe
                x = EnemyList.get(bossNumber).getX();
                y = EnemyList.get(bossNumber).getY();

                //Condiciones que ayudan a que los aliens permanescan unidos.
                if (EnemyList.length() < 6)
                    pos = 0.60;
                if(EnemyList.length() < 4)
                    pos = 0;
                if(EnemyList.length() < 3)
                    pos = -1;

                for (int i = 0; i < EnemyList.length(); i++) {
                    //Hace que los aliens roten al rededor de la posicion en X y Y del jefe.
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
            }
        }));
        timeline.playFromStart();
    }

    /**
     * Hace que el jefe permanezca en el centro.
     * @param EnemyList Lista de enemigos.
     */
    private void inMiddle(ListModel<Enemy> EnemyList){
        int tmpBoss = 0, change = (EnemyList.length() - (EnemyList.length() + 1) / 2);

        //Busca la posicion del jefe
        for (int i = 0; i < EnemyList.length(); i++) {
            if (EnemyList.get(i).getID().equals("BOSS"))
                tmpBoss = i;
        }

        //Toma la posicion del jefe y del alien que esta en el centro.
        double Xchange = EnemyList.get(change).getX(),
                Ychange = EnemyList.get(change).getY(),
                iRchange = EnemyList.get(change).getiR(),
                iLchange = EnemyList.get(change).getiL();

        double Xboss = EnemyList.get(tmpBoss).getX()
                , Yboss = EnemyList.get(tmpBoss).getY()
                , iRboss = EnemyList.get(tmpBoss).getiR()
                , iLboss = EnemyList.get(tmpBoss).getiL();

        //Los cambia la posicion del alien que esta en el centro, con el jefe
        EnemyList.get(tmpBoss).setPosition(
                Xchange, Ychange,
                iRchange, iLchange
        );

        EnemyList.get(change).setPosition(
                Xboss, Yboss,
                iRboss, iLboss
        );

        //Cambia los nodos del jefe y el otro alien
        NodeModel<Enemy> node1 = EnemyList.getNode(change);
        NodeModel<Enemy> node2 = EnemyList.getNode(tmpBoss);
        CircularNode<Enemy> tmp = new CircularNode<>(node1.getData());

        node1.setData(node2.getData());
        node2.setData(tmp.getData());

        int xPos = EnemyList.length();
        //Permite que los enemigos permanescan unidos.
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

    /**
     * La hilera de aliens permanece unida, y si es clase D los ordena de mayor a menor segun su vida.
     * @param EnemyList Lsita de enemigos.
     */
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

    /**
     * Cambia al jefe de posicion cada 3 segundos.
     * @param EnemyList Lista de enemigos.
     */
    private void bossTransition(ListModel<Enemy> EnemyList){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(3),
                        e -> {
                            if (EnemyList.length() != 1)
                                positionChanger(EnemyList, EnemyList.getType());
                        }));
        timeline.playFromStart();
    }

    /**
     * Crea el fondo del nivel.
     */
    private void createBackground() {
        Image backgroundImage = new Image("resources/background_game.png", true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        Pane.setBackground(new Background(background));
    }

    /**
     * Da al servidor la lista de balas y el jugador que se estan usando, para que el control funcione con ellos.
     */
    @SuppressWarnings("Duplicates")
    private void serverConnect() {
        server.setPlayer(player);
        server.setBullets(bullets);

        Thread thread = new Thread(() -> server.run());
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Crea la clase de las animaciones.
     */
    private void createSubScene() {
        subScene = new LevelTransition("Nivel 3");
        Pane.getChildren().add(subScene);
    }

    /**
     * Actualiza la informacion de los label.
     */
    @SuppressWarnings("Duplicates")
    private void updateLabels(){
        String text1 = "Actual: " + MainEnemyList.get(current).getType();
        String text2 = "Siguiente: ";

        if (current + 1 >= MainEnemyList.length())
            text2 += "";
        else
            text2 += MainEnemyList.get(current + 1).getType();

        //Actualiza con los textos nuevos.
        level.setText(GameState.getNivel());
        label.setText(text1);
        subLabel.setText(text2);
        score.setText("Score: " + GameState.score);

        //Manda la informacion de los label al control de android.
        server.setToSend(GameState.getNivel() + ", "+ label.getText() + ", " + subLabel.getText() + ", " + score.getText());
    }

    /**
     * Crea los label.
     */
    private void createLabels() {
        try {
            level = new Label();
            level.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 20));
            level.setTranslateX(10);
            level.setTranslateY(0);
            level.setTextFill(Color.valueOf("FFFFFF"));
            Pane.getChildren().add(level);

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

    /**
     * Crea las hileras de aliens de diferente tipo aleatoriamente.
     */
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