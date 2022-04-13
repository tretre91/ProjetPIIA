package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Player;
import controller.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.FloatMap;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import model.Category;
import model.Video;
import model.Videos;

public class VideoList extends GridPane {
    private String label;
    private Category categorie;

    @FXML
    private Label name = new Label();
    
    @FXML
    private HBox liste = new HBox();

    public VideoList(Category categorie){
        super();
        this.label = categorie.name;
        this.categorie = categorie;
        FXMLLoader loader = new FXMLLoader(VideoList.class.getResource("/resources/fxml/categoryPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }catch(IOException e){
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            // throw new RuntimeException("Failed to load category pane : " + categorie.name);
        }
        
    }

    public void initialize() {
        name.setText(this.label);
        for (Video v : Videos.getVideosByCategory(categorie.name)) {
            //TODO: link à la miniature de la vidéo, set un évènement sur le bouton qui lance la vidéo
            liste.getChildren().add(new Miniature(v)); //ajoute le bouton à la HBox
        }
    }
}
