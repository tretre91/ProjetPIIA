package view;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Classe qui gère la vue de l'application
 */
public abstract class View {
    private static Scene scene;

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
        } catch (IOException e) {
            System.err.println("ERROR: failed to load fxml file " + page.getFilename());
            e.printStackTrace();
        }
    }
}
