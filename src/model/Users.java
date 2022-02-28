package model;

import java.util.ArrayList;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {
    private static Connection connection = null;
    private static boolean initialized = false;

    private static PreparedStatement getUsersStatement;
    private static PreparedStatement checkPasswordStatement;
    private static PreparedStatement createUserStatement;

    private static void init() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE NOT NULL, password TEXT NOT NULL)");

        getUsersStatement = connection.prepareStatement("SELECT name FROM user");
        checkPasswordStatement = connection.prepareStatement("SELECT id FROM user WHERE name = ? AND password = ?");
        createUserStatement = connection.prepareStatement("INSERT OR ABORT INTO user(name, password) VALUES (?, ?)");

        createUser("admin", "admin");
    }

    public static boolean open(String databaseFile) {
        if (connection != null) {
            close();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);
            if (!initialized) {
                init();
                initialized = true;
            }
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static void close() {
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {

        }
    }

    public static boolean createUser(String user, String password) throws SQLException {
        createUserStatement.setString(1, user);
        createUserStatement.setString(2, password);
        try {
            createUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) { // si on arrive ici, c'est qu'un utilisateur avec le même nom existe déjà
            System.err.println(e.getMessage());
            return false;
        } finally {
            createUserStatement.clearParameters();
        }
    }

    public static ArrayList<String> getUsers() throws SQLException {
        ArrayList<String> users = new ArrayList<>();

        ResultSet rs = getUsersStatement.executeQuery();

        while (rs.next()) {
            users.add(rs.getString("name"));
        }

        rs.close();

        return users;
    }

    public static boolean checkPassword(String user, String password) throws SQLException {
        checkPasswordStatement.setString(1, user);
        checkPasswordStatement.setString(2, password);

        ResultSet rs = checkPasswordStatement.executeQuery();
        boolean ok = rs.next();

        rs.close();
        checkPasswordStatement.clearParameters();

        return ok;
    }
}
