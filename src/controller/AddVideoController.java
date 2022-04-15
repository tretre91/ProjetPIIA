package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.tretre91.controls.ErrorTextField;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Database;
import model.Videos;
import view.Page;
import view.View;

public class AddVideoController implements Initializable {
    static File userHome = new File(System.getProperty("user.home"));
    static File lastLocation = userHome; // dossier du dernier fichier choisi par l'utilisateur

    @FXML
    private TextField category;
    @FXML
    private ErrorTextField fileName; //le texfield où on rentre le nom du fichier
    @FXML
    private File selectedFile; //le fichier sélectionné via parcours

    @FXML
    private ErrorTextField newName; //le nom de la vidéo dans le système

    @FXML
    private Label importLabel; // le label qui indique l'état de l'improt
    @FXML
    private ProgressBar importProgress; // la barre de progression de l'import
    @FXML
    private Button addButton;

    /**
     * Classe utilisée pour importer la vidéo depuis un autre Thread
     */
    private class AddVideoTask extends Task<Boolean> {
        private String name;
        private String path;

        public AddVideoTask(String videoName, String videoPath) {
            super();
            name = videoName;
            path = videoPath;
        }

        @Override
        protected Boolean call() throws Exception {
            Platform.runLater(() -> importLabel.setText("Import en cours ..."));
            updateValue(false);

            if (!addVideo(path, name)) {
                Platform.runLater(() -> newName.setError("Une vidéo avec le même nom existe déjà"));
                return false;
            }

            updateProgress(25, 100);
            File thumbnail = getThumbnail(path);
            if (thumbnail == null) {
                Platform.runLater(() -> fileName.setError("Erreur lors de la récupération de la miniature"));
                return false;
            }

            updateProgress(70, 100);
            if (!Videos.setThumbnail(name, thumbnail.toURI().toURL().openStream())) {
                Platform.runLater(() -> fileName.setError("Erreur lors de l'import de la miniature"));
                return false;
            }

            updateProgress(100, 100);
            updateValue(true);
            return true;
        }

    }

    @FXML
    private void goBack() {
        View.switchPage(Page.MANAGE_FILES_PRIVILEGED);
    }

    @FXML
    private void selectFile() {
        if (!lastLocation.exists()) {
            lastLocation = userHome;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(lastLocation);
        fileChooser.setTitle("Sélectionner une vidéo");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Vidéos", "*.mp4", "*.mov"));

        selectedFile = fileChooser.showOpenDialog(State.getStage());
        if (selectedFile != null) {
            lastLocation = selectedFile.getParentFile();
            fileName.setText(selectedFile.getAbsolutePath());
            if (newName.getText().isEmpty()) {
                newName.setText(formatFileName(fileName.getText()));
            }
        } else if (fileName.getText().isEmpty()) {
            fileName.setError("Aucun fichier sélectionné");
        }
    }

    /**
     * Récupère la miniature d'une vidéo (la frame à 1 secondes)
     * 
     * @param path
     *            Le chemin vers le fichier vidéo
     * @return un File qui contient le fichier .jpeg avec la vidéo, ou null en cas d'erreur
     */
    private File getThumbnail(String path) {
        try {
            File tmp = File.createTempFile("hfm", ".jpeg");
            String command = "ffmpeg -y -loglevel quiet -i \"" + path
                    + "\" -ss 00:00:01  -vf scale=w=150:h=100:force_original_aspect_ratio=decrease,pad=150:100:-1:-1:color=black -frames:v 1 "
                    + tmp.getAbsolutePath();
            Process proc = Runtime.getRuntime().exec(command);
            if (proc.waitFor() == 0) {
                return tmp;
            } else {
                return new File(getClass().getResource("/resources/icons/baseline_movie_black_48dp.png").getPath());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la miniature (" + e.getMessage() + ")");
            return null;
        } catch (InterruptedException e) {
            System.err.println("Erreur: process interrompu (" + e.getMessage() + ")");
            return null;
        }
    }

    @FXML
    private void tryAjouter() throws SQLException, IOException {
        String path = fileName.getText();

        if (!isPathValid(path)) {
            fileName.setError("Le fichier spécifié n'existe pas");
            return;
        }

        importLabel.setVisible(true);
        importProgress.setVisible(true);
        addButton.setDisable(true); // on désactive le bouton d'ajout pendant l'import

        // on fait le travail sur un autre Thread pour éviter de freeze l'interface
        AddVideoTask task = new AddVideoTask(newName.getText(), path);
        importProgress.progressProperty().bind(task.progressProperty());

        // code appelé à la fin de l'exécution de la tâche
        task.setOnSucceeded(event -> {
            if (task.getValue()) {
                importLabel.setText("Import réussi !");
            } else {
                importLabel.setText("Echec de l'import");
                importProgress.setVisible(false);
            }
            addButton.setDisable(false);

            Timer timer = new Timer();
            TimerTask t = new TimerTask() {
                public void run() {
                    Platform.runLater(() -> {
                        importLabel.setVisible(false);
                        importProgress.setVisible(false);
                    });
                }
            };
            timer.schedule(t, 2000);
            
        });

        Thread worker = new Thread(task);
        worker.setDaemon(true);
        worker.start();
    }

    @FXML
    private void printBD() throws SQLException {
        try {
            PreparedStatement truc = Database.prepareStatement("SELECT name FROM video");
            ResultSet rs = truc.executeQuery();
            System.out.println("\nVidéos :");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println();
    }

    private boolean addVideo(String path, String newName) throws SQLException {
        if (this.category.getText() == null || this.category.getText().isEmpty()) {
            return model.Videos.addVideo(newName, path, "default");
        } else {
            return model.Videos.addVideo(newName, path, category.getText());
        }
    }

    /**
     * Récupère le nom d'un fichier sans son extension
     * 
     * @param s
     *            Le chemin vers le fichier
     * @return Le nom du fichier
     */
    @FXML
    private String formatFileName(String s) {
        String file = new File(s).getName();
        return file.substring(0, file.lastIndexOf("."));
    }

    private static boolean isPathValid(String path) {
        return path != null && (new File(path).exists());
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        importLabel.setVisible(false);
        importProgress.setVisible(false);
    }
}
