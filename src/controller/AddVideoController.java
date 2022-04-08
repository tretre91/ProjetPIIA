package controller;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Database;
import model.User;
import view.Page;
import view.View;

public class AddVideoController {
    @FXML private ToggleGroup groupCategory; //TODO: mettre le bon type

    @FXML private TextField fileName;
    @FXML private File selectedFile;

    @FXML private TextField newName;
    //private static ArrayList<Category> categories;
    
    @FXML
    private void goBack(){
        View.switchPage(Page.MANAGE_FILES_PRIVILEGED);
    }

    @FXML
    private void refreshStyle(){
        fileName.setStyle("-fx-border-color: transparent");
    }

    @FXML
    private void setErrorStyle(){
        fileName.setStyle("-fx-border-color: red");
    }

    @FXML
    private void selectFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Sélectionner une vidéo");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Vidéos", "*.mp4", "*.mov"));
        
        selectedFile = fileChooser.showOpenDialog(State.getStage());
        if(selectedFile != null){
            fileName.setText(selectedFile.getName());
            refreshStyle();
            System.out.println(fileName.getText());
        }else{
            setErrorStyle();
        }
    }

    @FXML
    private void tryAjouter(){
        //si fileName.getText() qui est valide, on le prend
        //sinon si selectedFile.getName() est valide, on le prend
        //+
        String fileNameFormatté = formatFileName(fileName.getText());
        
        if(fileNameFormatté != null){
            if(isValidPath(fileName.getText())){
                selectedFile = new File(fileName.getText()); //le path vers la vidéo, faut vérifier que c'est bien une vidéo justement
                if(/*newName == null ||*/ newName.getText().isEmpty() || newName.getText().isBlank()){
                    addVideo(selectedFile.getAbsolutePath(), fileNameFormatté);
                }else{
                    addVideo(selectedFile.getAbsolutePath(), newName.getText());
                }
            }else{
                setErrorStyle();
            }
            
        }else if(selectedFile != null){
            String selectedFileFormatté = formatFileName(selectedFile.getName());
            if(selectedFileFormatté != null){
                if(/*newName == null ||*/ newName.getText().isEmpty() || newName.getText().isBlank()){
                    addVideo(selectedFile.getAbsolutePath(), selectedFileFormatté);
                }else{
                    addVideo(selectedFile.getAbsolutePath(), newName.getText());
                }
            }else{
                setErrorStyle();
            }
        }else{
            setErrorStyle();
        }
    }

    private void addVideo(String path, String newName){
        System.out.println(path + " : " + newName + " ajouté");
        //Database.addVideo(formatFileName(), newName, categories);
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

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
    
}
