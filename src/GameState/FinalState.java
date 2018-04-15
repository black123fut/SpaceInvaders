package GameState;

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
    private Stage TheStage;
    private Label titule;
    private Label score;
    static String condition;
    private boolean first = true;

    /**
     * Constructor
     * @param Pane AnchorPane de la pantalla actual.
     * @param TheStage Stage principal de la aplicacion.
     */
    public FinalState(AnchorPane Pane, Stage TheStage){
        this.Pane = Pane;
        this.TheStage = TheStage;

        createButtons();
        createLabels();
    }

    /**
     * Actualiza los elementos que se estan usando.
     */
    @Override
    public void update() {
        score.setText("Score: " + GameState.score);
        titule.setText(condition);

        //Pone el fondo de acuerdo a si gano o perdio.
        if (first){
            if (condition.equals("      Victoria")){
                Image backgroundImage = new Image("resources/fondoVictory.png", true);
                BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT, null);
                Pane.setBackground(new Background(background));
            } else if(condition.equals("Has Perdido")){
                Image backgroundImage = new Image("resources/fondoDefeat.png", true);
                BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT, null);
                Pane.setBackground(new Background(background));

            }
            first = false;
        }

    }

    @Override
    public void render(GraphicsContext g) {

    }

    /**
     * Crea los Label de esta pantalla.
     */
    private void createLabels(){
        try{
            titule = new Label();
            titule.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 50));
            titule.setTranslateX(540 / 2 - 170);
            titule.setTranslateY(150);
            titule.setTextFill(Color.valueOf("FFFFFF"));
            Pane.getChildren().add(titule);

            score = new Label();
            score.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 30));
            score.setTranslateX(240 - 45);
            score.setTranslateY(225);
            score.setTextFill(Color.valueOf("FFFFFF"));
            Pane.getChildren().add(score);

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Crea el boton de esta pantalla.
     */
    private void createButtons(){
        gameButton exit = new gameButton("Salir");
        exit.setTranslateX(270 - 190 / 2);
        exit.setTranslateY(300);
        exit.setOnAction(e -> TheStage.close());
        Pane.getChildren().add(exit);
    }
}
