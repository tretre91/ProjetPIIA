package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Status;
import view.Page;
import view.View;

public class AccountHomeController implements Initializable {
    @FXML
    private VBox mainPane;
    @FXML
    private GridPane dialogPane;
    @FXML
    private DialogPane disconnectDialog;

    @FXML
    private void disconnect() {
        // si l'utilisateur veut se déconnecter on montre le popup
        dialogPane.setVisible(true);
    }

    @FXML
    private void goToCreate() {
        View.switchPage(Page.ACCOUNT_CREATION);
    }

    @FXML
    private void goToManage() {
        if (State.getCurrentStatus().getValue() <= Status.PARENT.getValue()) {
            View.switchPage(Page.MANAGE_FILES_PRIVILEGED);
        } else {
            System.out.println("Manage (low tier)");
        }
    }

    @FXML
    private void goToLib() {
        System.out.println("later");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialogPane.setVisible(false);

        Button cancelDisconnect = (Button)disconnectDialog.lookupButton(ButtonType.CANCEL);
        
        cancelDisconnect.setOnAction(event -> {
            dialogPane.setVisible(false);
        });

        Button comfirmDisconnect = (Button)disconnectDialog.lookupButton(ButtonType.YES);
        
        comfirmDisconnect.setOnAction(event -> {
            View.switchPage(Page.USER_SELECTION);
        });
    }
}
