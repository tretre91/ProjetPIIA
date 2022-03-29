package controller;

import javafx.fxml.FXML;
import view.View;

public class ManageFilesController {
    
    @FXML
    public void goBack(){
        View.switchPage(view.Page.ACCOUNT_HOME_PRIVILEGED); //ou Home tout court selon le status
    }
}
