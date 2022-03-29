package controller;

import model.Database;

/**
 * Classe contenant des informations utiles sur l'état global de l'application
 */
public abstract class State {
    static private String currentUser = "admin"; //utiliser son id plutôt ?
    static private int currentId = 0;

    static public void setCurrentUser(String user) {
        currentUser = user;
    }

    static public String getCurrentUser() {
        return currentUser;
    }

    static public void setCurrentId(int id){
        currentId = id;
    }

    static public int getCurrentId(){
        return currentId;
    }

    static public int getCurrentStatus(){
        //TODO select status from user where
        //Database.prepareStatement("select status from user where idu==" + currentId + ";");
        //un bail comme ça ?
        return 0;
    }
}
