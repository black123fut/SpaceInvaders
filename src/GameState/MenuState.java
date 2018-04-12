package GameState;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.effect.DropShadow;
import javafx.event.*;
import javafx.stage.Stage;
import model.gameButton;

public class MenuState extends GameStateManager{
    private AnchorPane MenuPane;
    private Stage TheStage;
    private Scene Lvl1Scene;

    public MenuState(AnchorPane MenuPane, Stage TheStage, Scene Lvl1Scene){
        this.MenuPane = MenuPane;
        this.TheStage = TheStage;
        this.Lvl1Scene = Lvl1Scene;

        createButtons();
        createBackground();
        createTitule();
        }

    private void createButtons(){
        gameButton boton = new gameButton("Jugar");
        boton.setLayoutX(270 - 190 / 2);
        boton.setLayoutY(250);
        boton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TheStage.setScene(Lvl1Scene);
                GameState.index++;
            }
        });
        MenuPane.getChildren().add(boton);

        gameButton exit = new gameButton("Salir");
        exit.setTranslateX(270 - 190 / 2);
        exit.setTranslateY(350);
        exit.setOnAction(e -> TheStage.close());
        MenuPane.getChildren().add(exit);
    }

    private void createBackground(){
        Image backgroundImage = new Image("resources/space.jpg");

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);


        MenuPane.setBackground(new Background(background));

    }

    private void createTitule(){
        ImageView titule = new ImageView("resources/space_invaders_titulo.png");
        titule.setLayoutX(540 / 2 - 200);
        titule.setLayoutY(20);

        titule.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                titule.setEffect(new DropShadow());
            }
        });

        titule.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                titule.setEffect(null);
            }
        });

        MenuPane.getChildren().add(titule);
    }

    public void update() {

    }

    public  void render(GraphicsContext g){

    }

}
