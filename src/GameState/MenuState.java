package GameState;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.stage.Stage;
import model.gameButton;

public class MenuState extends GameStateManager{
    private AnchorPane MenuPane;
    private Stage TheStage;
    private Scene Lvl1Scene;

    /**
     * Constructor
     * @param MenuPane AnchorPane utilizado en el menu
     * @param TheStage Stage principal de la aplicacion
     * @param Lvl1Scene Scene del nivel 1
     */
    public MenuState(AnchorPane MenuPane, Stage TheStage, Scene Lvl1Scene){
        this.MenuPane = MenuPane;
        this.TheStage = TheStage;
        this.Lvl1Scene = Lvl1Scene;

        createButtons();
        createBackground();
        createTitule();
        }

    /**
     * Crea los botones del menu
     */
    private void createButtons(){
        gameButton boton = new gameButton("Jugar");
        boton.setLayoutX(270 - 190 / 2);
        boton.setLayoutY(250);
        //Se le asigna al boton la accion que debe realizar
        boton.setOnAction(event -> {
            TheStage.setScene(Lvl1Scene);
            GameState.index++;
        });
        MenuPane.getChildren().add(boton);

        gameButton exit = new gameButton("Salir");
        exit.setTranslateX(270 - 190 / 2);
        exit.setTranslateY(350);
        //El boton cierra la aplicacion
        exit.setOnAction(e -> TheStage.close());
        MenuPane.getChildren().add(exit);
    }

    /**
     * Crea el fondo del menu
     */
    private void createBackground(){
        Image backgroundImage = new Image("resources/space.jpg");

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        //Agrega el fondo al Pane del menu
        MenuPane.setBackground(new Background(background));

    }

    /**
     *Crea el titulo  del juego
     */
    private void createTitule(){
        ImageView titule = new ImageView("resources/space_invaders_titulo.png");
        titule.setLayoutX(540 / 2 - 200);
        titule.setLayoutY(20);
        //Agrega el titulo al MenuPane
        MenuPane.getChildren().add(titule);
    }

    public void update() {

    }

    public  void render(GraphicsContext g){

    }

}
