package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import view.Page;
import view.View;
import javafx.scene.control.Button;

public class AccountHomeController {
    
    @FXML
    private void disconnect() {
        View.switchPage(Page.USER_SELECTION);
    }

    @FXML
    private void goToCreate(){ //j'aurais bien fais une disjonction de cas mais j'y arrive pas
        View.switchPage(Page.ACCOUNT_CREATION_PRIVILEGED); //ou ACCOUNT_CREATION si l'user n'est pas un admin
    }

    @FXML
    private void goToManage(){
        View.switchPage(Page.MANAGE_FILES);
    }

    @FXML
    private void goToLib(){
        System.out.println("later");
    }
}
