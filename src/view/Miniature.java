package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import controller.Player;
import controller.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import model.Video; 


public class Miniature extends Button{
    public static final int HEIGHT = 180;
    public static final int WIDTH = 100;
    public BufferedImage thumbnail;

    public Miniature(Video v){
        VBox box = new VBox();
        box.setPrefSize(WIDTH, HEIGHT);
        box.setFillWidth(true);
        box.getChildren().add(0, new Label(v.name));

        //box.getChildren().add(new ImageView(v.path.get));
        this.getChildren().add(box);
        this.setPrefSize(WIDTH, HEIGHT);
        this.setText(v.name); //set le nom de la vidéo
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) { //défini l'event on click
                try {
                    launchVideo(v.path, v.name);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    private void launchVideo(String path, String name) throws IOException{
        //j'aimerais bien switchpage, mais c'est complexe (ici) de définir le contrôleur via SceneBuilder
        //du coup je peux pas faire un switchpage
        System.out.println("Path : " + path);
        Scene scene = new Scene(new Pane());
        State.getStage().setScene(scene);
        State.getStage().setTitle(name);
        
        Media media = new Media(new File(path).toURI().toURL().toString());
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Player(media, scene));
        loader.setLocation(getClass().getResource(Page.VIDEO.getFilename()));
        Parent root = loader.load();
        scene.setRoot(root);
    }
}
