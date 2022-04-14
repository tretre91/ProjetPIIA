package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe abstraite qui gère les accès à la base de données de l'application
 */
public abstract class Database {
    private static Connection connection;
    private static PreparedStatement tableExistsStatement;
    private static final String defaultDatabasePath = Database.class.getResource("/resources/").getPath() + "database.db";

    /**
     * Ouvre une connexion à la base de données par défaut
     * 
     * @return false si la connexion a échoué, true sinon
     */
    public static boolean open() {
        return open(defaultDatabasePath);
    }

    /**
     * Ouvre une connexion à une base de données spécifiée par son chemin
     * 
     * @return false si la connexion a échoué, true sinon
     */
    public static boolean open(String databaseFile) {
        try {
            if (isValid()) {
                connection.close();
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile);

            if (tableExistsStatement == null) {
                tableExistsStatement = connection
                        .prepareStatement("SELECT name FROM sqlite_master WHERE type = 'table' AND name = ?");
            }

            initialize();

            return true;
        } catch (SQLException e) {
            System.err.printf("ERROR: failed to open database '%s' (%s)\n", databaseFile, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ferme la connexion à la base de données
     */
    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {

        }
    }

    /**
     * Indique si la connexion est valide
     * 
     * @return true si la connexion est valide, false sinon
     */
    public static boolean isValid() {
        try {
            return connection != null && connection.isValid(0);
        } catch (SQLException e) {
            // n'est jamais atteint car timeout >= 0
            return false;
        }
    }

    /**
     * voir {@link java.sql.Connection#prepareStatement(String)}
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * voir {@link java.sql.Connection#createStatement()}
     * 
     * @return
     * @throws SQLException
     */
    public static Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    private static void initialize() throws SQLException {
        Statement statement = connection.createStatement();

        // @formatter:off

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS user ("
            + "  idu INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "  name TEXT UNIQUE NOT NULL,"
            + "  password TEXT NOT NULL,"
            + "  status INTEGER NOT NULL CHECK (0 <= status AND status <= 3),"
            + "  avatar TEXT,"
            + "  CHECK (password NOT LIKE '' OR status = 3))"
        );

        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS video ("
            + "  idv INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "  idc INTEGER REFERENCES category(idc) NOT NULL,"
            + "  name TEXT UNIQUE NOT NULL,"
            + "  path TEXT NOT NULL)"
        );

        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS category ("
            + "  idc INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "  name TEXT UNIQUE NOT NULL,"
            + "  status INTEGER NOT NULL CHECK (0 <= status AND status <= 3))"
        );

        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS authorization ("
            + "  idu INTEGER REFERENCES user(idu) ON DELETE CASCADE NOT NULL,"
            + "  idv INTEGER REFERENCES video(idv) ON DELETE CASCADE NOT NULL,"
            + "  UNIQUE (idu, idv))"
        );
        
        // @formatter:on

        statement.close();
    }
}
