package controller;

import javafx.fxml.FXML;
import view.Page;
import view.View;

public class ManageFilesController {
    
    @FXML
    public void goBack(){
        if(State.getCurrentStatus().getValue() <= 1 ){
            View.switchPage(view.Page.ACCOUNT_HOME_PRIVILEGED);
        }else{
            View.switchPage(view.Page.ACCOUNT_HOME);
        }
    }

    @FXML
    public void goToAddVideo(){
        View.switchPage(Page.ADD_VIDEO);
    }

    @FXML
    public void goToRemoveVideo(){
        System.out.println("Remove");
    }

    @FXML
    public void goToExplore(){
        System.out.println("Explore");
    }

    @FXML
    public void goToManageCat(){
        System.out.println("Manage Categories");
    }
}
