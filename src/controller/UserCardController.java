package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import view.View;
import view.Page;

public class UserCardController implements Initializable {
    private static String currentUser = "";
    private static String currentAvatar = null;
    private static String defaultAvatar = "/resources/icons/baseline_account_circle_black_48dp.png";

    @FXML
    private Text username;
    @FXML
    private ImageView avatar;

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static void setCurrentAvatar(String path) {
        currentAvatar = path;
    }

    @FXML
    private void goToLogin() {
        State.setCurrentUser(username.getText());
        View.switchPage(Page.LOGIN);
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        username.setText(currentUser);
        avatar.setImage(new Image(currentAvatar == null ? defaultAvatar : currentAvatar));
    }

}
