package view;

public enum Page {
    USER_SELECTION("users.fxml"),
    LOGIN("login.fxml");

    private final String filename;

    private Page(String filename) {
        this.filename = "/resources/fxml/" + filename;
    }

    public String getFilename() {
        return filename;
    }
}
