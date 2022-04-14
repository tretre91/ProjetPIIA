package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.SQLiteErrorCode;

/**
 * Classe abstraite offrant des fonctions pour la gestion des vidéos et catégories dans la base de données
 */
public abstract class Videos {
    private static PreparedStatement addVideoStatement;
    private static PreparedStatement removeVideoStatement;
    private static PreparedStatement addCategoryStatement;
    private static PreparedStatement removeCategoryStatement;
    private static PreparedStatement renameCategoryStatement;
    private static PreparedStatement getCategoriesStatement;
    private static PreparedStatement getVideosByCategoryStatement;
    private static PreparedStatement getVideoIdStatement;
    private static PreparedStatement getCategoryIdStatement;

    static {
        if (Database.isValid() || Database.open()) {
            try {
                addVideoStatement = Database.prepareStatement("INSERT OR ABORT INTO video(idc, name, path) VALUES (?, ?, ?)");
                removeVideoStatement = Database.prepareStatement("DELETE FROM video WHERE name = ?");

                addCategoryStatement = Database.prepareStatement("INSERT OR ABORT INTO category(name, status) VALUES (?, ?)");
                removeCategoryStatement = Database.prepareStatement("DELETE FROM category WHERE name = ? AND name != 'default'");
                renameCategoryStatement = Database.prepareStatement("UPDATE OR IGNORE category SET name = ? WHERE name = ?");

                getCategoriesStatement = Database.prepareStatement("SELECT name, status FROM category");
                getVideosByCategoryStatement = Database.prepareStatement("SELECT video.name, video.path FROM video WHERE idc = ?");
                getVideoIdStatement = Database.prepareStatement("SELECT idv FROM video WHERE name = ?");
                getCategoryIdStatement = Database.prepareStatement("SELECT idc FROM category WHERE name = ?");

                Statement statement = Database.createStatement();
                String addDefaultCategory = String.format(
                        "INSERT OR IGNORE INTO category(name, status) VALUES ('default', %d) RETURNING idc", Status.CHILD.getValue());
                ResultSet rs = statement.executeQuery(addDefaultCategory);

                if (rs.next()) {
                    // @formater:off
                    statement.executeUpdate(
                            "CREATE TRIGGER IF NOT EXISTS setToDefaultCategory BEFORE DELETE ON category FOR EACH ROW"
                                    + "  BEGIN"
                                    + "    UPDATE video SET idc = " + rs.getInt(1) + " WHERE idc = OLD.idc;"
                                    + "  END");
                    // @formater:on
                }

                statement.close();
            } catch (SQLException e) {
                System.err.printf("ERROR: failure in Users class initialization (%s)\n", e.getMessage());
            }
        }
    }

    /**
     * Récupère la liste des catégories de l'application
     * 
     * @return La liste de toutes les catégories
     */
    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            ResultSet rs = getCategoriesStatement.executeQuery();
            while (rs.next()) {
                categories.add(new Category(rs.getString(1), rs.getInt(2)));
            }

            rs.close();
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return categories;
    }

    /**
     * Ajoute une vidéo à la base de données
     * 
     * @param name
     *            Le nom de la vidéo
     * @param path
     *            Le chemin du fichier vidéo
     * @param category
     *            La catégorie, on suppose qu'elle existe déjà avant l'ajout
     * @return false si une vidéo avec le même nom existe déjà, true sinon
     * @throws SQLException
     *             En cas d'erreur interne à la base de données
     */
    public static boolean addVideo(String name, String path, String category) throws SQLException {
        try {
            Integer idc = getCategoryId(category);
            System.out.println("\nCatégorie : " + category);
            //TODO: temporaire
            if (idc == null) {
                addCategory(category, Status.fromInt(0));
                idc = getCategoryId(category);
            }
            System.out.println("ID : " + idc);

            addVideoStatement.setInt(1, idc);
            addVideoStatement.setString(2, name);
            addVideoStatement.setString(3, path);

            addVideoStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                return false;
            } else {
                throw e;
            }
        }
    }

    /**
     * Supprime une vidéo
     * 
     * @param name
     *            Le nom de la vidéo à supprimer
     * @return true si la vidéo a été supprimée, false sinon
     */
    public static boolean removeVideo(String name) {
        try {
            removeVideoStatement.setString(1, name);
            return removeVideoStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return false;
    }

    /**
     * Ajoute une nouvelle catégorie
     * 
     * @param name
     *            Le nom de la catégorie
     * @param privilege
     *            Le statut minimum pour avoir accès aux vidéos de cette catégorie
     * @return false si une catégorie avec le même nom existe déjà, true sinon
     */
    public static boolean addCategory(String name, Status privilege) {
        try {
            addCategoryStatement.setString(1, name);
            addCategoryStatement.setInt(2, privilege.getValue());
            addCategoryStatement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
                return false;
            } else {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Supprime une catégorie
     * 
     * @param name
     *            Le nom de la catégorie
     * @return true si une catégorie a été supprimée, false sinon
     */
    public static boolean removeCategory(String name) {
        try {
            removeCategoryStatement.setString(1, name);
            return removeCategoryStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return false;
    }

    /**
     * Récupère les vidéos associées à une catégorie
     * 
     * @param category
     *            La catégorie en question
     * @return La liste de vidéos qui ont cette catégorie
     */
    public static ArrayList<Video> getVideosByCategory(String category) {
        ArrayList<Video> videos;
        try {
            Integer idc = getCategoryId(category);
            getVideosByCategoryStatement.setInt(1, idc);
            ResultSet rs = getVideosByCategoryStatement.executeQuery();

            videos = new ArrayList<>();
            while (rs.next()) {
                videos.add(new Video(rs.getString(1), rs.getString(2), category));
            }

            return videos;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Recupère l'identifiant d'une catégorie à partir de son nom
     * 
     * @return l'identifiant, ou null si aucune catégorie avec ce titre existe
     */
    private static Integer getCategoryId(String category) {
        try {
            getCategoryIdStatement.setString(1, category);
            ResultSet rs = getCategoryIdStatement.executeQuery();
            Integer id = null;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Recupère l'identifiant d'une vidéo à partir de son nom
     * 
     * @return l'identifiant, ou null si aucune vidéo avec ce titre existe
     */
    private static Integer getVideoId(String name) {
        try {
            getVideoIdStatement.setString(1, name);
            ResultSet rs = getVideoIdStatement.executeQuery();
            Integer id = null;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
