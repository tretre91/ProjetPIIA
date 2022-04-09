package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import model.Status;
import model.User;
import model.Users;
import view.Page;
import view.View;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountCreationController implements Initializable {

    @FXML private ToggleGroup toggles;
    @FXML private ToggleButton parentToggle;
    @FXML private ToggleButton teenToggle;
    @FXML private TextField ident;
    @FXML private PasswordField mdp1;
    @FXML private PasswordField mdp2;

    @FXML
    private void goBack() {
        View.switchPage(Page.ACCOUNT_HOME_PRIVILEGED);
    }

    @FXML
    private void confirmCreation() throws SQLException{
        switch(attemptCreation()){
            case 0:
            System.out.println("L'utilisateur a bien été créé !");
            break;
            case 1:
            System.out.println("L'identifiant n'est pas valide");
            break;
            case 2:
            System.out.println("Le mot de passe n'est pas correct");
            break;
            case 3:
            System.out.println("Cet identifiant est déjà utilisé");
            break;
            default:
            break;
        }
    }

    private int attemptCreation() throws SQLException{
        if (!isValidIdent()) {
            return 1;
        } else if (!testPassword()) {
            return 2;
        } else {
            try {
                if (Users.createUser(new User(ident.getText(), assignStatus(), null), mdp1.getText())) {
                    return 0;
                }
            } catch (SQLException e) {

            }
            return 3;
        }
    }

    private Status assignStatus(){
        return (Status)toggles.getSelectedToggle().getUserData();
    }

    private boolean testPassword(){
        return mdp1.getText().equals(mdp2.getText()) && !mdp1.getText().isEmpty();
    }

    private boolean isValidIdent(){
        return !ident.getText().isEmpty() && !ident.getText().contains(" ");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        parentToggle.setUserData(Status.PARENT);
        teenToggle.setUserData(Status.TEEN);

        parentToggle.setOnAction(event -> {
            if (!parentToggle.isSelected()) {
                parentToggle.setSelected(true);
            }
        });

        if (State.getCurrentStatus().lessThan(Status.ADMIN)) {
            // TODO : ajouter un infobulle pour indiquer que le compte ne peut pas créer de parents
            parentToggle.setDisable(true);
        }

        teenToggle.setOnAction(event -> {
            if (!teenToggle.isSelected()) {
                teenToggle.setSelected(true);
            }
        });
    }
}
