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

public class VideoList extends VBox{
    private Label name;
    private HBox liste = new HBox();

    public VideoList(Category categorie){
        this.setSpacing(5);
        liste.setMaxWidth(State.getStage().getWidth() - 100);
        
        //liste.setPrefHeight(110);
        liste.setSpacing(10);
        liste.setFillHeight(true);
        name = new Label(categorie.name);
        this.getChildren().add(name);
        ScrollPane scrollpane = new ScrollPane();
        for (Video v : Videos.getVideosByCategory(categorie.name)) {
            //TODO: link à la miniature de la vidéo, set un évènement sur le bouton qui lance la vidéo
            Miniature thumbnail = new Miniature(v);
            liste.getChildren().add(thumbnail); //ajoute le bouton à la HBox
        }
        //scrollpane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollpane.setContent(liste);
        scrollpane.setPrefHeight(Miniature.HEIGHT);
        //scrollpane.setVmax(arg0);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.prefHeight(Miniature.HEIGHT);
        this.getChildren().add(scrollpane);
    }
}
