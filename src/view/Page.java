package view;

/**
 * Enumeration contenant les différentes pages de l'application
 * 
 * Chaque page est associée à un fichier .fxml dont le nom peut être obtenu à
 * l'aide de la méthode {@link #getFilename()}
 */
public enum Page {
    USER_SELECTION("users.fxml"),
    LOGIN("login.fxml");

    private final String filename;

    private Page(String filename) {
        this.filename = "/resources/fxml/" + filename;
    }

    /**
     * Gets the name of the fxml file corresponding to this page // TODO : pk c'est en anglais ?
     * 
     * @return the fxml file's name
     */
    public String getFilename() {
        return filename;
    }
}
