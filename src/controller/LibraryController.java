package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;
import view.Page;
import view.View;
import model.Category;

public class LibraryController implements Initializable{
    @FXML private VBox frame;

    //TODO: add as many scrollPanes as there are category for this account,
    //in each scrollPane show the videos from the corresponding category

    @FXML
    private void goBack(){
        //TODO: à maj plus tard
        View.switchPage(Page.ACCOUNT_HOME_PRIVILEGED);
    }

    @FXML
    private void launchVideo(){
        //TODO:
    }

    @FXML
    private void debugBD(){
        ArrayList<Category> categorylist = Users.getCategoriesbyUser(State.getCurrentUser());
        for(int i = 0; i < categorylist.size(); i++){
            System.out.println("\nCatégorie : " + categorylist.get(i).name + "\nVidéos :");
            for (Video v : Videos.getVideosByCategory(categorylist.get(i).name)) {
                System.out.println(v.name);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resources){
        try {
            ArrayList<Category> categorylist = Users.getCategoriesbyUser(State.getCurrentUser());
            for(int i = 0; i < categorylist.size(); i++){
                HBox category = new HBox();
                
                URL videoCard= getClass().getResource("/resources/fxml/videoCard.fxml");
                for (Video v : Videos.getVideosByCategory(categorylist.get(i).name)) {
                    //TODO: link à la miniature de la vidéo, set un évènement sur le bouton qui lance le la vidéo
                    Button thumbnail = FXMLLoader.load(videoCard);
                    //thumbnail.setGraphic(new ImageView("src/resources/icons/baseline_arrow_back_black_48dp.png"));
                    thumbnail.setText(v.name);
                    thumbnail.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            //TODO:
                            System.out.println(thumbnail.getText());
                        }
                    });
                    category.getChildren().add(thumbnail);
                }
                frame.getChildren().add(category);
            }
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
