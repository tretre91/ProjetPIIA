package view;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class View {
    private static Scene scene;

    public static void setScene(Scene scene) {
        View.scene = scene;
    }

    public static void switchPage(Page page) {
        try {
            String file = page.getFilename();
            URL fxml = View.class.getResource(file);
            Parent root = FXMLLoader.load(fxml);
            scene.setRoot(root);
        } catch (IOException e) {
            System.err.println("ERROR: no fxml file named " + page.getFilename());
            e.printStackTrace();
        };
        
    }
}
