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

    public LevelTransition(String level){
        super(new AnchorPane(), 440, 80);
        prefWidth(80);
        prefHeight(440);

        animation = new LinkedList<>();
        for (int i = 1; i < 9; i++) {
            animation.add("resources/flag/flag" + i + ".png");
        }

        subAnchor = (AnchorPane) this.getRoot();
        createLabel(level);
        setLayoutX(-440);
        setLayoutY(320);
    }

    public void startSubScene(){
        TranslateTransition transtion = new TranslateTransition();
        transtion.setDuration(Duration.seconds(6));
        transtion.setNode(this);

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