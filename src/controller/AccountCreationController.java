package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import model.Status;
import model.User;
import model.Users;
import view.Page;
import view.View;

import java.sql.SQLException;

public class AccountCreationController {

    @FXML private ToggleGroup toggles;
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
        if(isValidIdent()){    
            if(testPassword()){
                try{
                    Users.createUser(new User(ident.getText(), assignStatus(), null), mdp1.getText());
                    ident.setText(null);
                    mdp1.setText(null);
                    mdp2.setText(null);
                    return 0;
                }catch(SQLException e){
                    return 3;
                }
            }else{
                return 2;
            }
        }else{
            return 1;
        }
    }

    private Status assignStatus(){
        if(State.getCurrentStatus() == Status.ADMIN && ((ToggleButton) toggles.getSelectedToggle()).getText().equalsIgnoreCase("P")){
            return Status.PARENT;
        }else{
            return Status.TEEN;
        }
    }



    private boolean testPassword(){
        return mdp1.getText().equals(mdp2.getText()) && !mdp1.getText().isEmpty();
    }

    private boolean isValidIdent(){
        return !ident.getText().isEmpty() && !ident.getText().contains(" ");
    }
}
