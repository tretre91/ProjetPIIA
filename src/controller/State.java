package controller;

public abstract class State {
    static private String currentUser = "admin";

    static public String getCurrentUser() {
        return currentUser;
    }
}
