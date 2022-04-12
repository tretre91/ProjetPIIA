package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import model.*;
import view.Page;
import view.VideoList;
import view.View;
import model.Category;

public class LibraryController implements Initializable{
    @FXML private ScrollPane frame;
    @FXML private VBox mainVBox; //contient les labels et les catégories (les videoList)

    @FXML
    private void goBack(){
        //TODO: à maj plus tard
        View.switchPage(Page.ACCOUNT_HOME_PRIVILEGED);
    }

    @FXML
    private void debugBD(){
        ArrayList<Category> categorylist = Users.getCategoriesbyUser(State.getCurrentUser().getName());
        for(int i = 0; i < categorylist.size(); i++){
            System.out.println("\nCatégorie : " + categorylist.get(i).name + "\nVidéos :");
            for (Video v : Videos.getVideosByCategory(categorylist.get(i).name)) {
                System.out.println(v.name);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resources){
        ArrayList<Category> categorylist = Users.getCategoriesbyUser(State.getCurrentUser().getName());
        mainVBox.setPrefWidth(State.getStage().getWidth() - 100);
        for(int i = 0; i < categorylist.size(); i++){
            VideoList liste = new VideoList(categorylist.get(i));
            mainVBox.getChildren().add(liste);
            VBox.setVgrow(mainVBox.getChildren().get(i), Priority.ALWAYS);
        }
    }
}
