package controller;

import javafx.fxml.FXML;
import view.Page;
import view.View;

public class AddVideoController {
    
    @FXML
    private void goBack(){
        View.switchPage(Page.MANAGE_FILES_PRIVILEGED);
    }
}
