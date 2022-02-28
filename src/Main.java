import controller.State;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Users;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        State.setStage(primaryStage);
        primaryStage.setTitle("Homework Folder Manager");
        primaryStage.setHeight(600);
        primaryStage.setWidth(800);
        State.setScene("users");
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (Users.open("users.db")) {
            launch(args);
            Users.close();
        } else {
            System.err.println("Echec de l'ouverture de la base de données");
        }
    }
}
