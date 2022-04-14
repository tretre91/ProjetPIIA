package view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Category;
import model.Video;
import model.Videos;

public class VideoList extends GridPane {
    private String label;
    private Category categorie;

    @FXML
    private Label name = new Label();

    @FXML
    private HBox liste = new HBox();

    public VideoList(Category categorie) {
        super();
        this.label = categorie.name;
        this.categorie = categorie;
        FXMLLoader loader = new FXMLLoader(VideoList.class.getResource("/resources/fxml/categoryPane.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
            // throw new RuntimeException("Failed to load category pane : " + categorie.name);
        }

    }

    @FXML
    private void initialize() {
        name.setText(this.label);
        for (Video v : Videos.getVideosByCategoryWithThumbnails(categorie.name)) {
            //TODO: set un évènement sur le bouton qui lance la vidéo
            liste.getChildren().add(new Miniature(v)); //ajoute le bouton à la HBox
        }
    }
}
