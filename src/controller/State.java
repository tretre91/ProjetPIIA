package controller;

public abstract class State {
    static private String currentUser = "admin";

    static public void setCurrentUser(String user) {
        currentUser = user;
    }

    static public String getCurrentUser() {
        return currentUser;
    }
}
