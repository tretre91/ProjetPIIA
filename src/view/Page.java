package view;

/**
 * Enumeration contenant les différentes pages de l'application
 * Chaque page est associée à un fichier .fxml dont le nom peut être obtenu à
 * l'aide de la méthode {@link #getFilename()}
 */
public enum Page {
    USER_SELECTION("users.fxml"), LOGIN("login.fxml"), 
    ACCOUNT_HOME_PRIVILEGED("accountHomePrivileged.fxml"),
    ACCOUNT_HOME("accountHome.fxml"), ACCOUNT_CREATION("accountCreation.fxml"),
    MANAGE_FILES_PRIVILEGED("manageFilesPrivileged.fxml"),
    MANAGE_CATEGORIES("manageCategories.fxml"),
    LIBRARY("library.fxml"),
    ADD_CATEGORY("addCategory.fxml"),
    ADD_VIDEO("addVideo.fxml"),
    VIDEO("video.fxml");

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
