package controller;

import javafx.stage.Stage;
import model.Status;
import model.User;
import model.Video;

/**
 * Classe contenant des informations utiles sur l'état global de l'application
 */
public abstract class State {
    static private User currentUser = new User("blank", null, null);
    static private Stage stage = null;
    static private Video video;

    public static Stage getStage(){
        return stage;
    }

    public static void setStage(Stage s){
        stage = s;
    }

    static public void setCurrentUser(User user) {
        currentUser = user;
    }

    static public void setCurrentUser(String user){
        currentUser.setName(user);
    }

    static public User getCurrentUser() {
        return currentUser;
    }

    static public String getCurrentUsername() {
        return currentUser.getName();
    }

    static public Status getCurrentStatus(){
        return currentUser.getStatus();
    }

    public static Video getCurrentVideo() {
        return video;
    }

    public static void setCurrentVideo(Video video) {
        State.video = video;
    }
}
