package sample;
import Server.Server;
import GameState.GameState;
import GameState.Level1;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import GameState.MenuState;
import model.LinkedList;


public class Main extends Application
{

//    public GameStateManager gsm;
    @Override
    public void start(Stage theStage){
        try{
            GameState gameState = new GameState();
            theStage = gameState.getTheStage();

            theStage.setTitle("Space Invaders");

            theStage.show();

            new AnimationTimer(){
                @Override
                public void handle(long currentNanoTime){
                    double elapsedTime = (currentNanoTime - System.nanoTime()) / 1000000000.0;
                    gameState.init();
                }
            }.start();


        } catch (Throwable e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}
