package model;

/**
 * Classe permettant de représenter une vidéo
 */
public class Video {
    public String name;
    public String path;
    public String category;

    public Video(String name, String path, String category) {
        this.name = name;
        this.path = path;
        this.category = category;
    }
}
