package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import controller.Player;
import controller.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import model.Video; 


public class Miniature extends Button {
    private Video v;

    public Miniature(Video v){
        super();
        this.v = v;
        FXMLLoader loader = new FXMLLoader(View.class.getResource("/resources/fxml/videoCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }catch(IOException e){
            throw new RuntimeException("Failed to load a videoCard : " + v.path);
        }
    }

    @FXML
    private void launchVideo() throws IOException{
        //j'aimerais bien switchpage, mais c'est complexe (ici) de définir le contrôleur via SceneBuilder
        //du coup je peux pas faire un switchpage
        System.out.println("Path : " + v.path);
        Scene scene = new Scene(new Pane());
        State.getStage().setScene(scene);
        State.getStage().setTitle(v.name);
        
        Media media = new Media(new File(v.path).toURI().toURL().toString());
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new Player(media, scene));
        loader.setLocation(getClass().getResource(Page.VIDEO.getFilename()));
        Parent root = loader.load();
        scene.setRoot(root);
    }

    
    public void initialize() {
        this.setText(v.name);
        
    }
}
