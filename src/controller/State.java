package controller;

import model.Status;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Database;

/**
 * Classe contenant des informations utiles sur l'état global de l'application
 */
public abstract class State {
    static private String currentUser = "blank";
    static private Status currentStatus = null;
    static private Stage stage = null;

    public static Stage getStage(){
        return stage;
    }

    public static void setStage(Stage s){
        stage = s;
    }

    static public void setCurrentUser(String user, Status status) {
        currentUser = user;
        currentStatus = status;
    }

    static public void setCurrentUser(String user){
        currentUser = user;
    }

    static public String getCurrentUser() {
        return currentUser;
    }

    static public Status getCurrentStatus(){
        return currentStatus;
    }
}
