package GameState;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.LinkedList;

public class GameState
{
    private Stage TheStage;
    static int index;
    private static String nivel = " ";
    static int score;

    private LinkedList<GameStateManager> currentState;

    private static final int HEIGHT = 720;
    private static final int WIDTH = 540;

    Level1 lvl1State;
    Level2 Level2State;
    Level3 Level3State;


    private Canvas canvas1;
    private Canvas canvas2;
    private Canvas canvas3;

    private GraphicsContext g;

    public GameState() {
        //Panes y Scenes
        AnchorPane menuPane = new AnchorPane();
        Scene menuScene = new Scene(menuPane, WIDTH, HEIGHT);

        AnchorPane lvl1Pane = new AnchorPane();
        Scene lvl1Scene = new Scene(lvl1Pane, WIDTH, HEIGHT);

        AnchorPane Level2Pane = new AnchorPane();
        Scene Level2Scene = new Scene(Level2Pane, WIDTH, HEIGHT);

        AnchorPane Level3Pane = new AnchorPane();
        Scene Level3Scene = new Scene(Level3Pane, WIDTH, HEIGHT);

        //
        TheStage = new Stage();

        canvas1 = new Canvas(WIDTH, HEIGHT);
        canvas2 = new Canvas(WIDTH, HEIGHT);
        canvas3 = new Canvas(WIDTH, HEIGHT);

        lvl1Pane.getChildren().add(canvas1);
        Level2Pane.getChildren().add(canvas2);
        Level3Pane.getChildren().add(canvas3);

        //Niveles
        MenuState menuState = new MenuState(menuPane, TheStage, lvl1Scene);
        lvl1State = new Level1(lvl1Pane, TheStage, Level2Scene);
        Level2State = new Level2(Level2Pane, TheStage, Level3Scene);
        Level3State = new Level3(Level3Pane);


        currentState = new LinkedList<>();
        currentState.add(menuState);
        currentState.add(lvl1State);
        currentState.add(Level2State);
        currentState.add(Level3State);


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

    public static String getNivel(){
        return nivel;
    }

    private void setNivel(int dato){
        if (dato == 1)
            nivel = "Nivel 1";
        else if (dato == 2)
            nivel = "Nivel 2";
        else if (dato == 3)
            nivel = "Nivel 3";
    }

}
