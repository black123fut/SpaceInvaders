package GameState;

import character.Enemy;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.gameButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FinalState extends GameStateManager {
    private AnchorPane Pane;
    private Scene MenuScene;
    private Stage TheStage;

    private Label titule;
    private Label score;

    public FinalState(AnchorPane Pane, Stage TheStage, Scene MenuScene){
        this.Pane = Pane;
        this.MenuScene = MenuScene;
        this.TheStage = TheStage;

        createButtons();
        createLabels();
        createBackground();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext g) {

    }

    private void createLabels(){
        try{
            titule = new Label();
            titule.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 50));
            titule.setText("Felicidades");
            titule.setTranslateX(540 / 2 - 170);
            titule.setTranslateY(150);
            titule.setTextFill(Color.valueOf("FFFFFF"));
            Pane.getChildren().add(titule);

            score = new Label();
            score.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 30));
            score.setTranslateX(240 - 45);
            score.setTranslateY(225);
            score.setTextFill(Color.valueOf("FFFFFF"));
            score.setText("Score: " + GameState.score);
            Pane.getChildren().add(score);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void createButtons(){
        gameButton regretButton = new gameButton("Menu");
        regretButton.setTranslateX(270 - 190 / 2);
        regretButton.setTranslateY(350);
        regretButton.setOnAction(e -> {
            GameState.index = 0;
            Enemy.speed = 1.5;
            GameState.score = 0;
            TheStage.setScene(MenuScene);
        });
        Pane.getChildren().add(regretButton);

        gameButton exit = new gameButton("Salir");
        exit.setTranslateX(270 - 190 / 2);
        exit.setTranslateY(450);
        exit.setOnAction(e -> TheStage.close());
        Pane.getChildren().add(exit);
    }

    private void createBackground(){
        Image backgroundImage = new Image("resources/fondo1.jpg", true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);
        Pane.setBackground(new Background(background));
    }
}
