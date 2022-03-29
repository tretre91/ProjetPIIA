package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;
import model.Users;
import view.Page;
import view.View;

/**
 * Contrôleur de la page de connexion
 */
public class LoginController implements Initializable {
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text header;

    private String username;

    @FXML
    private void goBack() {
        View.switchPage(Page.USER_SELECTION);
    }

    @FXML
    private void connect() {
        String password = passwordField.getText();

        try {
            if (Users.checkPassword(username, password)) {
                //System.out.println("Congrats, this is the right password");
                // TODO : passage à la page d'acceuil
                
                View.switchPage(getHomePage());
            } else {
                System.out.println("Wrong password :(");
                passwordField.setStyle("-fx-border-color: red");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            // error("Une erreur interne a eu lieu")
        }
        //System.out.println("Password is: " + password);
    }

    public Page getHomePage(){
        //TODO
        switch(State.getCurrentId()){
            case 0:
            return Page.ACCOUNT_HOME_PRIVILEGED;
            case 1:
            return Page.ACCOUNT_HOME_PRIVILEGED;
            case 2:
            return Page.ACCOUNT_HOME;
            case 3:
            //On skip dans le cas des enfants
            return Page.ACCOUNT_HOME;
        }
        return null;
    }

    public void setUser(){
        //TODO redéfini les "paramètres" de l'user actuel dans State
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        username = State.getCurrentUser();
        header.setText("Bienvenue " + username + " !");

        passwordField.setStyle("-fx-border-color: transparent");

        passwordField.textProperty().addListener(((arg0, arg1, arg2) -> {
            passwordField.setStyle("-fx-border-color: transparent");
        }));

        Platform.runLater(() -> {
            passwordField.requestFocus();
        });
    }

}
