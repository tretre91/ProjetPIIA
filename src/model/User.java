package model;

/**
 * Classe représentant un utilisateur
 */
public class User {
    private String name;
    private Status status;
    private String avatar;

    public User(String name, Status status, String avatar) {
        this.name = name;
        this.status = status;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public String getAvatar() {
        return avatar;
    }
}
