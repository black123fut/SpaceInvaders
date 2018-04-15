package model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.SubScene;
import javafx.scene.layout.*;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class LevelTransition extends SubScene {
    private LinkedList<String> animation;
    private AnchorPane subAnchor;

    private BackgroundImage image;
    private int currentIndex = 0;

    /**
     * Constructor
     * @param level El texto que debe ensenar la animacion.
     */
    public LevelTransition(String level){
        super(new AnchorPane(), 440, 80);
        prefWidth(80);
        prefHeight(440);

        //Agrega todas las imagenes de la animacion a la lista.
        animation = new LinkedList<>();
        for (int i = 1; i < 9; i++) {
            animation.add("resources/flag/flag" + i + ".png");
        }

        //Pane de la SubScene
        subAnchor = (AnchorPane) this.getRoot();
        createLabel(level);
        setLayoutX(-440);
        setLayoutY(320);
    }

    /**
     * Empieza la animacion del nivel nuevo.
     */
    public void startSubScene(){
        //Mueve a la SubScene
        TranslateTransition transtion = new TranslateTransition();
        transtion.setDuration(Duration.seconds(6));
        transtion.setNode(this);

        //Hace el cambio de la imagen del fondo.
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(100.0), e -> {
                    if (currentIndex == 8)
                        currentIndex = 0;
                    image = new BackgroundImage(new Image(animation.get(currentIndex), 440,
                            80, false, true),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.DEFAULT, null);
                    subAnchor.setBackground(new Background(image));

                    currentIndex++;
                }));
        timeline.playFromStart();

        transtion.setToX(1420);
        transtion.play();
    }

    /**
     * Crea el texto de la animacion.
     * @param level Texto que se quiere en la animacion.
     */
    private void createLabel(String level){
        try{
            Label label = new Label(level);
            label.setFont(Font.loadFont(new FileInputStream("src/resources/Future_thin.ttf"), 25));
            label.setTranslateX(121);
            label.setTranslateY(20);
            label.setTextFill(Color.valueOf("000000"));
            subAnchor.getChildren().add(label);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}