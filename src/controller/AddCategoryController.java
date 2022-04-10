package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import model.Status;
import model.Videos;
import view.Page;
import view.View;

public class AddCategoryController implements Initializable {
    @FXML private TextField categoryName;
    @FXML private ToggleGroup toggles;
    @FXML private RadioButton childToggle;
    @FXML private RadioButton teenToggle;
    @FXML private RadioButton parentToggle;

    @FXML
    private void goBack() {
        Page previousPage = View.popPage();
        View.switchPage(previousPage);
    }

    @FXML
    private void create() {
        // TODO : indiquer les erreurs sur l'interface
        String name = categoryName.getText();
        if (name.isEmpty()) {
            System.out.println("Le nom est vide :(");
            return;
        }

        Status status = (Status)toggles.getSelectedToggle().getUserData();
        if (Videos.addCategory(name, status)) {
            goBack();
        } else {
            System.out.println("La catégorie existe déjà");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        childToggle.setUserData(Status.CHILD);
        teenToggle.setUserData(Status.TEEN);
        parentToggle.setUserData(Status.PARENT);

        // permet de toujours avoir au moins un bouton d'appuyé
        for (Object button : toggles.getToggles().toArray()) {
            RadioButton radio = (RadioButton)button;
            radio.setOnAction(event -> {
                if (!radio.isSelected()) {
                    radio.setSelected(true);
                }
            });
        }
    }

}
