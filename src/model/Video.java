package model;

import javafx.scene.image.Image;

/**
 * Classe permettant de représenter une vidéo
 */
public class Video {
    public String name;
    public String path;
    public String category;
    public Image thumbnail;

    public Video(String name, String path, String category) {
        this(name, path, category, null);
    }

    public Video(String name, String path, String category, Image thumbnail) {
        this.name = name;
        this.path = path;
        this.category = category;
        this.thumbnail = thumbnail;
    }
}
