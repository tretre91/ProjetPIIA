package controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.TilePane;
import model.Category;
import model.Database;
import model.Videos;
import view.CategoryTile;
import view.Page;
import view.View;

public class ManageCategoriesController implements Initializable {
    static PreparedStatement prepareDeleteStatement;
    static PreparedStatement prepareReassignStatement;
    static PreparedStatement prepareFetchStatement;

    static{
        try {
            prepareDeleteStatement =  Database.prepareStatement("DELETE FROM category WHERE name = ? AND name != 'default'");
            prepareReassignStatement = Database.prepareStatement("UPDATE video SET idc = 1 WHERE idc = ?");
            prepareFetchStatement = Database.prepareStatement("SELECT idc FROM category WHERE name = ?");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @FXML private TilePane tiles;

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
        try {
            for (Category c : Videos.getCategories()) {
                CategoryTile ct = new CategoryTile(c);
                //le menu a faire apparaître sur clic-droit, on lui associe la suppression de la catégorie
                MenuItem item = new MenuItem("Supprimer");
                item.setOnAction(event -> { 
                    try {
                        //va chercher l'idc de la category
                        prepareFetchStatement.setString(1, c.name); 
                        int idc = prepareFetchStatement.executeQuery().getInt(1);
                        //prépare la suppression de la catégorie
                        prepareDeleteStatement.setString(1, c.name);
                        if(prepareDeleteStatement.executeUpdate() == 1){ //une catégorie a été supprimée
                            tiles.getChildren().remove(ct); //update de l'affichage
                            //update à default la category des videos concernées par la suppression
                            prepareReassignStatement.setInt(1,idc);
                            prepareReassignStatement.executeUpdate();
                        }
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
                //le menu a faire apparaître sur clic-droit
                ContextMenu menu = new ContextMenu(item);
                        
                ct.setOnContextMenuRequested(event -> {
                    menu.show((Node)event.getSource(), event.getScreenX(), event.getScreenY());
                });
                tiles.getChildren().add(ct);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de la base de données (" + e.getMessage() + ")");
        }
    }
}
