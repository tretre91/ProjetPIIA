package view;

import java.io.IOException;
import java.net.URL;
import java.util.Stack;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Classe qui gère la vue de l'application
 */
public abstract class View {
    private static Scene scene;
    private static Page currentPage;
    private static Stack<Page> pages = new Stack<>();

    /**
     * Change la scène utilisée par la vue
     * 
     * @param scene
     *            la nouvelle scène
     */
    public static void setScene(Scene scene) {
        View.scene = scene;
    }

    /**
     * Change la page active, les pages disponibles sont décrites dans l'énumeration
     * {@link Page}
     * 
     * @param page
     *            la nouvelle page
     */
    public static void switchPage(Page page) {
        try {
            String file = page.getFilename();
            URL fxml = View.class.getResource(file);
            Parent root = FXMLLoader.load(fxml);
            scene.setRoot(root);
            currentPage = page;
        } catch (IOException e) {
            System.err.println("ERROR: failed to load fxml file " + page.getFilename());
            e.printStackTrace();
            e.getCause().printStackTrace();
        }
    }

    /**
     * Renvoie la page actuelle
     */
    public static Page getCurrentPage() {
        return currentPage;
    }

    /**
     * Ajout la page courante à la pile des pages
     */
    public static void pushPage() {
        pushPage(currentPage);
    }

    /**
     * Ajoute une page à la pile des pages visitées
     * 
     * @param page
     *            La page à ajouter
     */
    public static void pushPage(Page page) {
        pages.add(page);
    }

    /**
     * Indique la dernière page stockée
     * 
     * @return La dernière page stockée, ou null si la pile est vide
     */
    public static Page peekPage() {
        if (pages.empty()) {
            return null;
        } else {
            return pages.peek();
        }
    }

    /**
     * Retire la dernière page stockée
     * 
     * @return La page retirée, ou null si aucune page n'était dans la pile
     */
    public static Page popPage() {
        if (pages.empty()) {
            return null;
        } else {
            return pages.pop();
        }
    }
}
