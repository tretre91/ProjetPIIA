package controller;

import model.Status;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import model.Database;

/**
 * Contrôleur de la page de connexion
 */
public class LoginController implements Initializable {
    private static PreparedStatement getUserInfo;

    static {
        if (Database.isValid() || Database.open()) {
            try {
                getUserInfo = Database.prepareStatement("SELECT status FROM user WHERE name = ?");
            } catch (SQLException e) {
                System.err.printf("ERROR:", e.getMessage());
            }
        }
    }

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
                getUserInfo.setString(1, username); //récupération du status dans la base, on a 1 ligne 1 colonne
                State.setCurrentUser(username, Status.fromInt(getUserInfo.executeQuery().getInt(1))); //mise à jour de l'utilisateur actuel
                View.switchPage(getHomePage());
            } else {
                System.out.println("Wrong password :(");
                passwordField.setStyle("-fx-border-color: red");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            // error("Une erreur interne a eu lieu")
        }
    }

    public Page getHomePage(){
        //TODO
        switch(State.getCurrentStatus()){
            case ADMIN :
            return Page.ACCOUNT_HOME_PRIVILEGED;
            case PARENT:
            return Page.ACCOUNT_HOME_PRIVILEGED;
            case TEEN:
            return Page.ACCOUNT_HOME;
            case CHILD:
            //On skip dans le cas des enfants
            return Page.ACCOUNT_HOME;
        }
        return null;
    }

    public void setUser(){
        //TODO: redéfini les "paramètres" de l'user actuel dans State
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
