package controller;

import java.io.File;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Database;
import view.Page;
import view.View;

public class AddVideoController implements Initializable{
    static PreparedStatement getAllVideoNames;
    static{
        try{
            getAllVideoNames = Database.prepareStatement("SELECT name FROM video");
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
    @FXML private TextField category; //TODO: mettre le bon type

    @FXML private TextField fileName; //le texfield où on rentre le nom du fichier

    @FXML private TextField newName; //le nom de la vidéo dans le système

    private File selectedFile; //le fichier sélectionné via parcours
    
    @FXML
    private void goBack(){
        View.switchPage(Page.MANAGE_FILES_PRIVILEGED);
    }

    @FXML
    private void setErrorStyle(Node n){
        n.setStyle("-fx-border-color: red");
    }

    @FXML
    private void selectFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Sélectionner une vidéo");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Vidéos", "*.mp4", "*.mov"));
        
        File tmpFile = fileChooser.showOpenDialog(State.getStage());
        if(tmpFile != null){
            selectedFile = tmpFile;
            fileName.setText(selectedFile.getName());
        }else{
            setErrorStyle(fileName);
        }
    }

    @FXML
    private void tryAjouter() throws SQLException{
        String fileNameFormatté = formatFileName(fileName.getText());
        
        if(isPathValid(fileName.getText())){
            if(isNameValid(newName.getText())){
                addVideo(selectedFile.getAbsolutePath(), newName.getText());
            }else if(isNameValid(fileNameFormatté)){
                addVideo(selectedFile.getAbsolutePath(), fileNameFormatté);
            }else{
                setErrorStyle(newName);
                //afficher une erreur dédiée
            }
        }else{
            setErrorStyle(fileName);
            //afficher une erreur dédiée
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

    private void addVideo(String path, String newName) throws SQLException{
        if(this.category.getText() == null || this.category.getText().isEmpty()){
            model.Videos.addVideo(newName, path, "default");
        }else{
            model.Videos.addVideo(newName, path, category.getText());
        }
    }

    @FXML
    private String formatFileName(String s){
        try {
            if(s == null || s.isEmpty()){
                return null;
            }else{
                return s.split(".mp4")[0];
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isNameValid(String name){
        if(name != null && !name.isEmpty() && !name.isBlank()){
            try {
                ResultSet rs = getAllVideoNames.executeQuery();
                while(rs.next()){
                    if(rs.getString(1).equals(name)){
                        return false;
                    }
                }    
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            return true;
        }else{
            return false;
        }
        
    }

    private static boolean isPathValid(String path) {
        if(path == null || path.isEmpty() || path.isBlank()){
            return false;
        }else{
            try {
                Paths.get(path); //TODO: mdr ça fait rien ça ptn
            } catch (InvalidPathException | NullPointerException ex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        fileName.setStyle("-fx-border-color: transparent");
        newName.setStyle("-fx-border-color: transparent");

        fileName.textProperty().addListener(((arg0, arg1, arg2) -> {
            fileName.setStyle("-fx-border-color: transparent");
        }));

        newName.textProperty().addListener(((arg0, arg1, arg2) -> {
            newName.setStyle("-fx-border-color: transparent");
        }));
    }
    
}
