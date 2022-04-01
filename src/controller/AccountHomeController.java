package controller;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import view.Page;
import view.View;
import javafx.scene.control.Button;
import model.Status;

public class AccountHomeController implements Initializable {
    
    @FXML
    private void disconnect() {
        View.switchPage(Page.USER_SELECTION);
    }

    @FXML
    private void goToCreate(){
        if(State.getCurrentStatus() == Status.ADMIN){
            View.switchPage(Page.ACCOUNT_CREATION_PRIVILEGED);
        }else{
            View.switchPage(Page.ACCOUNT_CREATION);
        }
    }

    @FXML
    private void goToManage(){
        if(State.getCurrentStatus().getValue() <= Status.PARENT.getValue()){
            View.switchPage(Page.MANAGE_FILES_PRIVILEGED);
        }else{
            System.out.println("Manage (low tier)");
        }
    }

    @FXML
    private void goToLib(){
        System.out.println("later");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
}
