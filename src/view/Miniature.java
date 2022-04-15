package view;

import java.io.IOException;

import controller.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import model.Status;
import model.Video;

public class Miniature extends Button {
    private Video v;

    @FXML private Label name = new Label();
    @FXML private ImageView thumbnail = new ImageView();

    public Miniature(Video v) {
        super();
        this.v = v;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/videoCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load a videoCard : " + v.path);
        }
    }

    @FXML
    private void launchVideo() {
        System.out.println("Path : " + v.path);
        State.setCurrentVideo(v);
        View.pushPage();
        View.switchPage(Page.VIDEO);
    }

    @FXML
    private void initialize() {
        name.setText(v.name);
        thumbnail.setImage(v.thumbnail);
        if (!State.getCurrentStatus().lessThan(Status.PARENT)) {
            ContextMenu menu = new ContextMenu();
            MenuItem item = new MenuItem("éditer");
            item.setOnAction(event -> {
                State.setCurrentVideo(v);
                View.pushPage();
                View.switchPage(Page.EDIT_VIDEO);
            });
            menu.getItems().add(item);
            this.setContextMenu(menu);
        }
    }
}
