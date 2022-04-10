package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import model.Category;
import model.Videos;
import view.CategoryTile;
import view.View;

public class ManageCategoriesController implements Initializable {
    @FXML private TilePane tiles;

    @FXML
    private void goBack() {
        View.switchPage(view.Page.MANAGE_FILES_PRIVILEGED);
    }

    @FXML
    private void addCategory() {
        System.out.println("Ajout de catégorie :)"); // TODO
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            for (Category c : Videos.getCategories()) {
                tiles.getChildren().add(new CategoryTile(c));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
    }

}
