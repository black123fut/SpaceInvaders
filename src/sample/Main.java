package sample;
import Server.Server;
import GameState.GameState;
import GameState.Level1;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import GameState.MenuState;
import model.LinkedList;

import java.awt.*;


public class Main extends Application
{
    @Override
    public void start(Stage theStage){
        try{
            GameState gameState = new GameState();
            theStage = gameState.getTheStage();
            Image icon = new Image(getClass().getResourceAsStream("/resources/icon.png"));
            theStage.getIcons().add(icon);
            theStage.setTitle("Space Invaders");

            theStage.addEventFilter(KeyEvent.KEY_PRESSED, k -> {
                if ( k.getCode() == KeyCode.SPACE)
                    k.consume();
            });

            theStage.show();
            new AnimationTimer(){
                @Override
                public void handle(long currentNanoTime){
                    double elapsedTime = (currentNanoTime - System.nanoTime()) / 1000.0;
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
