package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.TilePane;
import model.Category;
import model.Videos;
import view.CategoryTile;
import view.Page;
import view.View;

public class ManageCategoriesController implements Initializable {
    @FXML
    private TilePane tiles;

    @FXML
    private void goBack() {
        View.switchPage(view.Page.MANAGE_FILES_PRIVILEGED);
    }

    @FXML
    private void addCategory() {
        View.pushPage();
        View.switchPage(Page.ADD_CATEGORY);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        for (Category c : Videos.getCategories()) {
            CategoryTile ct = new CategoryTile(c);
            //le menu a faire apparaître sur clic-droit, on lui associe la suppression de la catégorie
            MenuItem item = new MenuItem("Supprimer");
            item.setOnAction(event -> {
                if (Videos.removeCategory(c.name)) { //une catégorie a été supprimée
                    tiles.getChildren().remove(ct); //update de l'affichage
                }
            });
            //le menu a faire apparaître sur clic-droit
            ContextMenu menu = new ContextMenu(item);

            ct.setOnContextMenuRequested(event -> {
                menu.show((Node)event.getSource(), event.getScreenX(), event.getScreenY());
            });
            tiles.getChildren().add(ct);
        }
    }
}
