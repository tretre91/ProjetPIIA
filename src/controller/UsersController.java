package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import model.User;
import model.Users;

public class UsersController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox usersList;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // permet de se déplacer horizontalement avec un scroll vertical
        usersList.setOnScroll((event) -> {
            if (event.getDeltaX() == 0 && event.getDeltaY() != 0) {
                scrollPane.setHvalue(scrollPane.getHvalue() - event.getDeltaY() / usersList.getWidth());
            }
        });

        try {
            ArrayList<User> users = Users.getUsers();
            ObservableList<Node> list = usersList.getChildren();
            URL userCard = getClass().getResource("/resources/fxml/userCard.fxml");

            for (User user : users) {
                UserCardController.setCurrentUser(user.getName());
                UserCardController.setCurrentAvatar(user.getAvatar());
                Button card = FXMLLoader.load(userCard);
                list.add(card);
            }
        } catch (SQLException e) {
            // TODO
        } catch (IOException e) {
            System.err.println("Failed to load user card (" + e.getMessage() + ")");
        }

    }
}
