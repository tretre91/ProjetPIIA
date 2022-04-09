package model;

/**
 * Classe permettant de représenter une catégorie
 */
public class Category {
    public String name;
    public Status status;

    public Category(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public Category(String name, int status) {
        this(name, Status.fromInt(status));
    }
}
