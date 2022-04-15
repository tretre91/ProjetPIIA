package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import model.Category;
import model.Status;
import model.Users;
import model.Video;
import model.Videos;
import view.Page;
import view.VideoList;
import view.View;

public class LibraryController implements Initializable{
    @FXML 
    private VBox mainVBox; //contient les labels et les catégories (les videoList)

    @FXML
    private void goBack(){
        //TODO: à maj plus tard
        if(State.getCurrentStatus().lessThan(Status.PARENT)){
            View.switchPage(Page.ACCOUNT_HOME);
        }else{
            View.switchPage(Page.ACCOUNT_HOME_PRIVILEGED);
        }
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
        Category.init();
        ArrayList<Category> categorylist = Users.getCategoriesbyUser(State.getCurrentUser().getName());
        for(int i = 0; i < categorylist.size(); i++){
            VideoList liste = new VideoList(categorylist.get(i));
            mainVBox.getChildren().add(liste);
        }
    }
}
