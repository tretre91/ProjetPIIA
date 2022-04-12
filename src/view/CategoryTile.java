package view;

import model.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;

import controller.ManageCategoriesController;
import controller.State;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import model.Category;
import model.Database;

/**
 * Classe pour représenter les tuiles de caétgories dans la page de gestion des
 * catégories
 */
public class CategoryTile extends GridPane {
    /**
     * Label indiquant qui a accès aux vidéos d'une catégorie
     */
    private class CategoryLabel extends Label {
        public CategoryLabel(Status prio) {
            super();
            getStyleClass().addAll("category-label");
            switch (prio) {
                case ADMIN:
                    setText("Admin");
                    break;
                case PARENT:
                    setText("Parent");
                    getStyleClass().add("parent-label");
                    break;
                case TEEN:
                    setText("Ado");
                    getStyleClass().add("teen-label");
                    break;
                case CHILD:
                    setText("Enfant");
                    getStyleClass().add("child-label");
                    break;
                default:
                    setText("Erreur");
                    break;
            }
            setAlignment(Pos.CENTER);
            setPrefSize(10000, 10000);
            setFont(new Font("Lato Semibold", 14));
        }
    }

    private Label name;

    public CategoryTile(Category category) {
        this.getStyleClass().add("category-tile");
        name = new Label(category.name);
        name.setAlignment(Pos.CENTER);
        name.setPrefSize(10000, 10000);
        name.setFont(new Font("Lato", 16));

        add(name, 0, 0, 3, 1);
        setHgrow(name, Priority.ALWAYS);
        setVgrow(name, Priority.ALWAYS);

        switch (category.status) {
            case CHILD:
                add(new CategoryLabel(Status.CHILD), 2, 1);
            case TEEN:
                add(new CategoryLabel(Status.TEEN), 1, 1);
            case ADMIN:
            case PARENT:
                add(new CategoryLabel(Status.PARENT), 0, 1);
                break;
            default:
                add(new Label("error"), 0, 1, 3, 1);
                break;
        }
    }
}
