package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.sqlite.SQLiteErrorCode;

/**
 * Classe abstraite offrant des fonctions pour la gestion des vidéos et catégories dans la base de données
 */
public abstract class Videos {
    private static PreparedStatement addVideoStatement;
    private static PreparedStatement addCategoryStatement;
    private static PreparedStatement getCategoriesStatement;
    private static PreparedStatement getVideosByCategoryStatement;
    private static PreparedStatement getVideoIdStatement;
    private static PreparedStatement getCategoryIdStatement;

    static {
        if (Database.isValid() || Database.open()) {
            try {
                addVideoStatement = Database
                        .prepareStatement(
                                "INSERT OR ABORT INTO video(idc, name, path) VALUES (?, ?, ?)");
                getCategoriesStatement = Database.prepareStatement("SELECT name, status FROM category");
                getVideosByCategoryStatement = Database.prepareStatement(
                        "SELECT video.name, video.path FROM video WHERE idc = ?");
                addCategoryStatement = Database
                        .prepareStatement("INSERT OR ABORT INTO category(name, status) VALUES (?, ?)");
                getVideoIdStatement = Database.prepareStatement("SELECT idv FROM video WHERE name = ?");
                getCategoryIdStatement = Database.prepareStatement("SELECT idc FROM category WHERE name = ?");
                addCategory("Default", Status.CHILD);
            } catch (SQLException e) {
                System.err.printf("ERROR: failure in Users class initialization (%s)\n", e.getMessage());
            }
        }
    }

    /**
     * Récupère la liste des catégories de l'application
     * 
     * @return La liste de toutes les catégories
     * @throws SQLException
     *             En cas d'erreur interne de la base de données
     */
    public static ArrayList<Category> getCategories() throws SQLException {
        ArrayList<Category> categories = new ArrayList<>();

        ResultSet rs = getCategoriesStatement.executeQuery();
        while (rs.next()) {
            categories.add(new Category(rs.getString(1), rs.getInt(2)));
        }

        rs.close();
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

            addVideoStatement.setInt(1, idc);
            addVideoStatement.setString(2, name);
            addVideoStatement.setString(3, path);

            addVideoStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                return false;
            } else {
                throw e;
            }
        }
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
            if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                return false;
            } else {
                System.out.println(e.getMessage());
            }
        }
        return true;
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
                videos.add(new Video(rs.getString(0), rs.getString(1), category));
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
