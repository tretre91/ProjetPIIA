package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe permettant de représenter une catégorie
 */
public class Category {
    public String name;
    public Status status;

    static {
        System.out.println("done");
        if (Database.isValid() || Database.open()) {
            Videos.addCategory("default", Status.fromInt(3));
        }
    }
    
    public Category(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public Category(String name, int status) {
        this(name, Status.fromInt(status));
    }

    public static int init(){
        return 0;
    }
}
