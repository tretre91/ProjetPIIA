package view;

/**
 * <p>Enumeration contenant les différentes pages de l'application</p>
 * Chaque page est associée à un fichier .fxml dont le nom peut être obtenu à
 * l'aide de la méthode {@link #getFilename()}
 */
public enum Page {
    USER_SELECTION("users.fxml"), LOGIN("login.fxml");

    private final String filename;

    private Page(String filename) {
        this.filename = "/resources/fxml/" + filename;
    }

    /**
     * Renvoie le nom du fichier .fxml associé à cette page
     * 
     * @return le nom du fichier fxml correspondant
     */
    public String getFilename() {
        return filename;
    }
}
