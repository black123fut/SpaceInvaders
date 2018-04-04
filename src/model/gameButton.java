package model;

import GameState.GameState;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class gameButton extends Button {

    private static final String IMAGEN_BOTON_PRESIONADO = "-fx-background-color:" +
            " transparent; -fx-background-image: url('/resources/buttonLong_blue_pressed.png')";

    private static final String IMAGEN_BOTON = "-fx-background-color: transparent;" +
            " -fx-background-image: url('/resources/buttonLong_blue.png')";

    public gameButton(String texto){
        setText(texto);
        setButtonFont();
        setPrefWidth(190);
        setPrefHeight(49);
        setStyle(IMAGEN_BOTON);
        initializeButtonListeners();
    }

    private void setButtonFont(){
        setFont(Font.font("Verdana", 23));
    }

    private void setBotonPresionadoStyle(){
        setStyle(IMAGEN_BOTON_PRESIONADO);
        setPrefHeight(45);
        setLayoutY(getLayoutY() + 4);
    }

    private void setBotonReleasedStyle(){
        setStyle(IMAGEN_BOTON);
        setPrefHeight(45);
        setLayoutY(getLayoutY() - 4);
    }

    private void initializeButtonListeners(){

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)){
                    setBotonPresionadoStyle();
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)){
                    setBotonReleasedStyle();
                }
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Bloom bloom = new Bloom();
                bloom.setThreshold(0.1);
                setEffect(bloom);
            }
        });


        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setEffect(null);
            }
        });
    }

}
