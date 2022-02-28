package controller;

import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class State {
    static private String currentUser = "admin";
    static private Stage stage;
    static private HashMap<String, String> scenes = new HashMap<>(10) {
        {
            put("users", "/view/users.fxml");
            put("login", "/view/login.fxml");
        }
    };

    static public void setCurrentUser(String user) {
        currentUser = user;
    }

    static public String getCurrentUser() {
        return currentUser;
    }

    static public void setScene(String name) {
        String file = scenes.get(name);
        if (file == null) {
            System.err.println("ERROR: no scene associated with name " + name);
        } else {
            try {
                Parent root = FXMLLoader.load(State.class.getResource(file));
                Scene scene = new Scene(root);
                stage.setScene(scene);
            } catch (IOException e) {
                System.err.println("Failed to load fxml file");
            }
        }
    }

    static public void setStage(Stage newStage) {
        stage = newStage;
    }
}
