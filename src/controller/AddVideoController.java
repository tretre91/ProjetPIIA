package controller;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.tretre91.controls.ErrorTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Database;
import view.Page;
import view.View;

public class AddVideoController implements Initializable{
    static File userHome = new File(System.getProperty("user.home"));
    static File lastLocation = userHome; // dossier du dernier fichier choisi par l'utilisateur
    
    @FXML private TextField category; //TODO: mettre le bon type

    @FXML private ErrorTextField fileName; //le texfield où on rentre le nom du fichier
    @FXML private File selectedFile; //le fichier sélectionné via parcousr

    @FXML private ErrorTextField newName; //le nom de la vidéo dans le système
    
    @FXML
    private void goBack(){
        View.switchPage(Page.MANAGE_FILES_PRIVILEGED);
    }

    @FXML
    private void selectFile(){
        if (!lastLocation.exists()) {
            lastLocation = userHome;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(lastLocation);
        fileChooser.setTitle("Sélectionner une vidéo");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Vidéos", "*.mp4", "*.mov"));
        
        selectedFile = fileChooser.showOpenDialog(State.getStage());
        if(selectedFile != null){
            lastLocation = selectedFile.getParentFile();
            fileName.setText(selectedFile.getAbsolutePath());
            if (newName.getText().isEmpty()) {
                newName.setText(formatFileName(fileName.getText()));
            }
        }else if (fileName.getText().isEmpty()) {
            fileName.setError("Aucun fichier sélectionné");
        }
    }

    @FXML
    private void tryAjouter() throws SQLException{  
        if(isPathValid(fileName.getText())){
            if(!addVideo(selectedFile.getAbsolutePath(), newName.getText())) {
                newName.setError("Une vidéo avec le même nom existe déjà");
            }
        }else{
            fileName.setError("Le fichier spécifié n'existe pas");
        }
    }

    @FXML
    private void printBD() throws SQLException{
        try{
            PreparedStatement truc = Database.prepareStatement("SELECT name FROM video");
            ResultSet rs = truc.executeQuery();
            System.out.println("\nVidéos :");
            while(rs.next()){
                System.out.println(rs.getString(1));
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        System.out.println();
    }

    private boolean addVideo(String path, String newName) throws SQLException{
        if(this.category.getText() == null || this.category.getText().isEmpty()){
            return model.Videos.addVideo(newName, path, "default");
        }else{
            return model.Videos.addVideo(newName, path, category.getText());
        }
    }

    /**
     * Récupère le nom d'un fichier sans son extension
     * @param s Le chemin vers le fichier
     * @return Le nom du fichier
     */
    @FXML
    private String formatFileName(String s){
        String file = new File(s).getName();
        return file.substring(0, file.lastIndexOf("."));
    }

    private static boolean isPathValid(String path) {
        return path != null && (new File(path).exists());
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }
    
}
