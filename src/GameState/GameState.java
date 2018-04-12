package GameState;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import model.LinkedList;

import java.io.File;

public class GameState
{
    private Stage TheStage;
    static int index;
    private static String nivel = " ";
    static int score;

    private LinkedList<GameStateManager> currentState;

    private static final int HEIGHT = 720;
    private static final int WIDTH = 540;

    private Canvas canvas1;
    private Canvas canvas2;
    private Canvas canvas3;

    private AudioClip theme;

    private GraphicsContext g;

    public GameState() {
        TheStage = new Stage();

        //Panes y Scenes
        AnchorPane menuPane = new AnchorPane();
        Scene menuScene = new Scene(menuPane, WIDTH, HEIGHT);

        AnchorPane lvl1Pane = new AnchorPane();
        Scene lvl1Scene = new Scene(lvl1Pane, WIDTH, HEIGHT);

        AnchorPane Level2Pane = new AnchorPane();
        Scene Level2Scene = new Scene(Level2Pane, WIDTH, HEIGHT);

        AnchorPane Level3Pane = new AnchorPane();
        Scene Level3Scene = new Scene(Level3Pane, WIDTH, HEIGHT);

        AnchorPane FinalPane = new AnchorPane();
        Scene FinalScene = new Scene(FinalPane, WIDTH, HEIGHT);

        canvas1 = new Canvas(WIDTH, HEIGHT);
        canvas2 = new Canvas(WIDTH, HEIGHT);
        canvas3 = new Canvas(WIDTH, HEIGHT);

        lvl1Pane.getChildren().add(canvas1);
        Level2Pane.getChildren().add(canvas2);
        Level3Pane.getChildren().add(canvas3);

        //Niveles
        MenuState menuState = new MenuState(menuPane, TheStage, lvl1Scene);
        Level1 lvl1State = new Level1(lvl1Pane, TheStage, Level2Scene, FinalScene);
        Level2 level2State = new Level2(Level2Pane, TheStage, Level3Scene, FinalScene);
        Level3 level3State = new Level3(Level3Pane, TheStage, FinalScene);
        FinalState finalState = new FinalState(FinalPane, TheStage);

        currentState = new LinkedList<>();
        currentState.add(menuState);
        currentState.add(lvl1State);
        currentState.add(level2State);
        currentState.add(level3State);
        currentState.add(finalState);

        TheStage.setScene(menuScene);
    }

    public Stage getTheStage(){
        return TheStage;
    }

    public void init(){
        if (index == 1)
            g = canvas1.getGraphicsContext2D();
        if (index == 2)
            g = canvas2.getGraphicsContext2D();
        if (index == 3)
            g = canvas3.getGraphicsContext2D();
        setNivel(index);
        currentState.get(index).update();
        currentState.get(index).render(g);
    }

    static String getNivel(){
        return nivel;
    }

    private void setNivel(int dato){
        if(!nivel.equals("Nivel " + dato)){
            if (dato == 0){
                theme = new AudioClip(new File("src/resources/sound/menuTheme.mp3").toURI().toString());
                nivel = "Nivel 0";
            }
            else if (dato == 1){
                theme.stop();
                theme = new AudioClip(new File("src/resources/sound/theme.mp3").toURI().toString());
                nivel = "Nivel 1";
            }
            else if (dato == 2)
                nivel = "Nivel 2";
            else if (dato == 3)
                nivel = "Nivel 3";
            else if (dato == 4){
                if (FinalState.condition.equals("Has Perdido")){
                    AudioClip media = new AudioClip(new File("src/resources/sound/you_lose.mp3").toURI().toString());
                    media.play();
                }else{
                    AudioClip media = new AudioClip(new File("src/resources/sound/you_win.mp3").toURI().toString());
                    media.play();
                }
                nivel = "Nivel 4";
            }


            if (theme != null && (nivel.equals("Nivel 1") || nivel.equals("Nivel 0"))) {
                theme.setCycleCount(AudioClip.INDEFINITE);
                theme.play(0.3);
            }
        }
    }
}
