package model;

import java.util.ArrayList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe impléméntant les opérations relatives aux utilisateurs sur la base de données
 */
public abstract class Users {
    private static PreparedStatement getUsersStatement;
    private static PreparedStatement checkPasswordStatement;
    private static PreparedStatement createUserStatement;

    static {
        if (Database.isValid() || Database.open()) {
            try {
                getUsersStatement = Database.prepareStatement("SELECT name, status, avatar FROM user");
                checkPasswordStatement = Database
                        .prepareStatement("SELECT name, status, avatar FROM user WHERE name = ? AND password = ?");
                createUserStatement = Database.prepareStatement(
                        "INSERT OR ABORT INTO user(name, password, status, avatar) VALUES (?, ?, ?, ?)");

                createUser(new User("admin", Status.ADMIN, null), "admin");
            } catch (SQLException e) {
                System.err.printf("ERROR: failure in Users class initialization (%s)\n", e.getMessage());
            }
        }
    }

    /**
     * Ajoute un utilisateur à la base de données
     * 
     * @param user
     *            l'utilisateur à ajouter
     * @param password
     *            le mot de passe du nouvel utilisateur
     * @return false si un utilisateur avec le même nom existe déjà, true sinon
     * @throws SQLException
     */
    public static boolean createUser(User user, String password) throws SQLException {
        createUserStatement.setString(1, user.getName());
        createUserStatement.setString(2, password);
        createUserStatement.setInt(3, user.getStatus().getValue());
        createUserStatement.setString(4, user.getAvatar());

        try {
            createUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) { // si on arrive ici, c'est qu'un utilisateur avec le même nom existe déjà
            System.err.printf("ERROR: Failed to add a user to the database (%s)\n", e.getMessage());
            return false;
        } finally {
            createUserStatement.clearParameters();
        }
    }

    /**
     * Récupère la liste des utilisateurs
     * 
     * @return une ArrayList<User> contenant tous les utilisataurs de l'application
     * @throws SQLException
     */
    public static ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        ResultSet rs = getUsersStatement.executeQuery();

        while (rs.next()) {
            users.add(new User(rs.getString("name"), Status.fromInt(rs.getInt("status")), rs.getString("avatar")));
        }

        rs.close();
        return users;
    }

    /**
     * Vérifie le mot de passe d'un utilisateur
     * 
     * @param user
     *            l'utilisateur dont on souhaite vérifier le mot de passe
     * @param password
     *            le mot de passe à vérifier
     * @return L'utilisateur si le mot de passe correspond, null sinon
     * @throws SQLException
     */
    public static User checkPassword(String user, String password) throws SQLException {
        checkPasswordStatement.setString(1, user);
        checkPasswordStatement.setString(2, password);

        ResultSet rs = checkPasswordStatement.executeQuery();

        User result = null;
        if (rs.next()) {
            result = new User(rs.getString("name"), Status.fromInt(rs.getInt("status")), rs.getString("avatar"));
        }

        rs.close();
        checkPasswordStatement.clearParameters();
        return result;
    }
}
