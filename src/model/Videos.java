package model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.SQLiteErrorCode;

import javafx.scene.image.Image;

/**
 * Classe abstraite offrant des fonctions pour la gestion des vidéos et catégories dans la base de données
 */
public abstract class Videos {
    private static PreparedStatement addVideoStatement;
    private static PreparedStatement removeVideoStatement;
    private static PreparedStatement updateVideoStatement;
    private static PreparedStatement setThumbnailStatement;

    private static PreparedStatement addCategoryStatement;
    private static PreparedStatement removeCategoryStatement;
    private static PreparedStatement renameCategoryStatement;

    private static PreparedStatement getCategoriesStatement;
    private static PreparedStatement getVideosByCategoryStatement;
    private static PreparedStatement getVideosByCategoryTStatement; // vidéos avec miniature

    private static PreparedStatement getVideoIdStatement;
    private static PreparedStatement getCategoryIdStatement;
    private static PreparedStatement getUserIdStatement;

    private static PreparedStatement addAuthorizationStatement;
    private static PreparedStatement removeAuthorizationStatement;
    private static PreparedStatement getAuthorizedVideosStatement;
    private static PreparedStatement getAuthorizedVideosTStatement; // vidéos avec miniature
    private static PreparedStatement getAuthorizedUsersStatement;

    static {
        if (Database.isValid() || Database.open()) {
            try {
                addVideoStatement = Database.prepareStatement("INSERT OR ABORT INTO video(idc, name, path) VALUES (?, ?, ?)");
                removeVideoStatement = Database.prepareStatement("DELETE FROM video WHERE name = ?");
                updateVideoStatement = Database.prepareStatement("UPDATE OR IGNORE video SET name = ?, idc = ? WHERE name = ?");
                setThumbnailStatement = Database.prepareStatement("UPDATE OR IGNORE video SET thumbnail = ? WHERE name = ?");

                addCategoryStatement = Database.prepareStatement("INSERT OR ABORT INTO category(name, status) VALUES (?, ?)");
                removeCategoryStatement = Database.prepareStatement("DELETE FROM category WHERE name = ? AND name != 'default'");
                renameCategoryStatement = Database.prepareStatement("UPDATE OR IGNORE category SET name = ? WHERE name = ?");

                getCategoriesStatement = Database.prepareStatement("SELECT name, status FROM category");
                getVideosByCategoryStatement = Database.prepareStatement("SELECT video.name, video.path FROM video WHERE idc = ?");
                getVideosByCategoryTStatement = Database
                        .prepareStatement("SELECT video.name, video.path, video.thumbnail FROM video WHERE idc = ?");

                getVideoIdStatement = Database.prepareStatement("SELECT idv FROM video WHERE name = ?");
                getCategoryIdStatement = Database.prepareStatement("SELECT idc FROM category WHERE name = ?");
                getUserIdStatement = Database.prepareStatement("SELECT idu FROM user WHERE name = ?");

                addAuthorizationStatement = Database.prepareStatement("INSERT INTO authorization VALUES (?, ?)");
                removeAuthorizationStatement = Database.prepareStatement("DELETE FROM authorization WHERE idu = ? AND idv = ?");
                getAuthorizedVideosStatement = Database.prepareStatement("SELECT name, path FROM video, authorization WHERE idu = ? AND authorization.idv = video.idv");
                getAuthorizedVideosTStatement = Database.prepareStatement("SELECT name, path, thumbnail FROM video, authorization WHERE idu = ? AND authorization.idv = video.idv");
                getAuthorizedUsersStatement = Database.prepareStatement("SELECT name, status FROM user, authorization WHERE idv = ? AND authorization.idu = user.idu");

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
     * Mets à jour une vidéo
     * 
     * @return true si une vidéo a été modifiée, false sinon
     */
    public static boolean updateVideo(String oldName, String newName, String category) {
        Integer idc = getCategoryId(category);
        try {
            if (idc != null) {
                updateVideoStatement.setString(1, newName);
                updateVideoStatement.setInt(2, idc);
                updateVideoStatement.setString(3, oldName);
                return updateVideoStatement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return false;
    }

    /**
     * Mets à jour la miniature d'une vidéo
     * 
     * @param name
     *            La vidéo à modifier
     * @param data
     *            Les données de la miniature
     * @return true si la vidéo a été mise à jour, false en cas d'erreur
     */
    public static boolean setThumbnail(String name, InputStream data) {
        try {
            setThumbnailStatement.setBytes(1, data.readAllBytes());
            setThumbnailStatement.setString(2, name);
            return setThumbnailStatement.executeUpdate() == 1;
        } catch (SQLException | IOException e) {
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
     * Récupère les vidéos associées à une catégorie sans leur miniature
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
     * Récupère les vidéos associées à une catégorie avec leurs miniatures
     * 
     * @param category
     *            La catégorie à récuperer
     * @return La liste de vidéos qui ont cette catégorie
     */
    public static ArrayList<Video> getVideosByCategoryWithThumbnails(String category) {
        ArrayList<Video> videos;
        try {
            Integer idc = getCategoryId(category);
            getVideosByCategoryTStatement.setInt(1, idc);
            ResultSet rs = getVideosByCategoryTStatement.executeQuery();

            videos = new ArrayList<>();
            while (rs.next()) {
                videos.add(new Video(rs.getString(1), rs.getString(2), category, new Image(rs.getBinaryStream(3))));
            }

            return videos;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Ajoute l'autorisation d'une vidéo en particulier à un utilisateur
     * 
     * @param user
     *            L'utilisateur
     * @param videoName
     *            Le nom de la vidéo
     * @return true si l'autorisation a été créée
     */
    public static boolean addAuthorization(String user, String videoName) {
        try {
            Integer idu = getUserId(user);
            Integer idv = getVideoId(videoName);

            if (idu != null && idv != null) {
                addAuthorizationStatement.setInt(1, idu);
                addAuthorizationStatement.setInt(2, idv);
                return addAuthorizationStatement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return false;
    }

    /**
     * Retire l'autorisation d'une vidéo en particulier à un utilisateur
     * 
     * @param user
     *            L'utilisateur
     * @param videoName
     *            Le nom de la vidéo
     * @return true si l'autorisation a été retirée
     */
    public static boolean removeAuthorization(String user, String videoName) {
        try {
            Integer idu = getUserId(user);
            Integer idv = getVideoId(videoName);

            if (idu != null && idv != null) {
                removeAuthorizationStatement.setInt(1, idu);
                removeAuthorizationStatement.setInt(2, idv);
                return removeAuthorizationStatement.executeUpdate() == 1;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return false;
    }

    public static ArrayList<Video> getAuthorizedVideos(String user) {
        try {
            Integer idu = getUserId(user);
            
            if (idu != null) {
                ArrayList<Video> videos = new ArrayList<>();
                getAuthorizedVideosStatement.setInt(1, idu);
                ResultSet rs = getAuthorizedVideosStatement.executeQuery();
                while (rs.next()) {
                    videos.add(new Video(rs.getString(1), rs.getString(2), "Autres"));
                }
                return videos;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return null;
    }

    public static ArrayList<Video> getAuthorizedVideosWithThumbnails(String user) {
        try {
            Integer idu = getUserId(user);
            
            if (idu != null) {
                ArrayList<Video> videos = new ArrayList<>();
                getAuthorizedVideosTStatement.setInt(1, idu);
                ResultSet rs = getAuthorizedVideosTStatement.executeQuery();
                while (rs.next()) {
                    videos.add(new Video(rs.getString(1), rs.getString(2), "Autres", new Image(rs.getBinaryStream(3))));
                }
                return videos;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return null;
    }

    public static ArrayList<User> getAuthorizedUsers(String videoName) {
        try {
            Integer idv = getVideoId(videoName);
            
            if (idv != null) {
                ArrayList<User> users = new ArrayList<>();
                getAuthorizedUsersStatement.setInt(1, idv);
                ResultSet rs = getAuthorizedUsersStatement.executeQuery();
                while (rs.next()) {
                    users.add(new User(rs.getString(1), Status.fromInt(rs.getInt(2)), null));
                }
                return users;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
        return null;
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

    private static Integer getUserId(String name) {
        try {
            getUserIdStatement.setString(1, name);
            ResultSet rs = getVideoIdStatement.executeQuery();
            Integer id = null;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            return id;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
