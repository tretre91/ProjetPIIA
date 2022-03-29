package controller;

import javafx.fxml.FXML;
import view.Page;
import view.View;

public class AccountCreationController {

    @FXML
    private void goBack() {
        View.switchPage(Page.ACCOUNT_HOME_PRIVILEGED);
    }

    @FXML
    private void confirmCreation(){
        //TODO condition sur la base de donnée, et les champs d'entrées
        //TODO ajout du compte à la base de donnée
        //TODO else popup avec msg d'erreur associé ?
    }
}
