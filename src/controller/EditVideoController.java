package controller;

import org.tretre91.controls.ErrorTextField;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Category;
import model.Videos;
import view.Page;
import view.View;

public class EditVideoController {
    @FXML private Label currentName;
    @FXML private Label currentCategory;
    @FXML private ErrorTextField newName;
    @FXML private ComboBox<String> newCategory;
    @FXML private ImageView thumbnail;
    @FXML private DialogPane editDialog;
    @FXML private DialogPane deleteDialog;

    @FXML
    private void goBack() {
        View.switchPage(View.popPage());
    }

    @FXML
    private void edit() {
        if (newName.getText() == null || newName.getText().isEmpty()) {
            newName.setError("Le nom ne peut pas être vide");
        }
        if (Videos.updateVideo(currentName.getText(), newName.getText(), newCategory.getValue())) {
            editDialog.getParent().setVisible(true);
        } else {
            newName.setError("Une vidéo avec le même nom existe déjà");
        }
    }

    @FXML
    private void displayComfirmPrompt() {
        deleteDialog.getParent().setVisible(true);
    }

    @FXML
    private void initialize() {
        model.Video v = State.getCurrentVideo();
        thumbnail.setImage(v.thumbnail);
        currentName.setText(v.name);
        currentCategory.setText(v.category);

        newName.setText(v.name);

        // on ajoute toutes les catégories comme option dans la combo box
        for (Category c: Videos.getCategories()) {
            newCategory.getItems().add(c.name);
        }
        newCategory.getSelectionModel().select(v.category);

        // on chache les dialogues
        deleteDialog.getParent().setVisible(false);
        editDialog.getParent().setVisible(false);

        // le bouton ok du dialogue d'edit revient à la page précedente
        ((Button)editDialog.lookupButton(ButtonType.OK)).setOnAction(event -> goBack());
        
        // le bouton oui du dialogue de suppresion supprime la vidéo
        ((Button)deleteDialog.lookupButton(ButtonType.YES)).setOnAction(event -> {
            Videos.removeVideo(currentName.getText());
            goBack();
        });

        // le bouton annuler du dialogue de suppresion cache le dialogue
        ((Button)deleteDialog.lookupButton(ButtonType.CANCEL)).setOnAction(event -> {
            deleteDialog.getParent().setVisible(false);
        });
    }
}