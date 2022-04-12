package controller;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import view.View;

public class Player implements Initializable{
    @FXML
    private MediaView mediaView;
    @FXML
    private Button playButton;
    @FXML
    private Slider slider;
    @FXML
    private VBox overlay;

    private boolean paused = true;
    private boolean dragging = false;
    private MediaPlayer player;
    private Media media;
    private Scene scene;

    private Lock overlayLock = new ReentrantLock();
    private boolean overlayShown = false;
    private long overlayTimestamp = 0;

    //constructeur
    public Player(Media media, Scene scene) {
        this.media = media;
        this.scene = scene;

        player = new MediaPlayer(media);
        player.setAutoPlay(true);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        mediaView.setMediaPlayer(player);
        overlay.setVisible(false);
        player.setOnReady(() -> setup());
    }

    public void setup() {
        slider.setMin(0);
        slider.setMax(player.getMedia().getDuration().toSeconds());
        slider.setValue(0);

        mediaView.fitHeightProperty().bind(scene.heightProperty());
        mediaView.fitWidthProperty().bind(scene.widthProperty());

        player.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!dragging) {
                slider.adjustValue(newValue.toSeconds());
            }
        });

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (dragging) {
                player.seek(Duration.seconds(newValue.doubleValue()));
            }
        });

        Thread overlayHider = new Thread(() -> {
            while (true) {
                overlayLock.lock();
                try {
                    if (overlayShown && Instant.now().toEpochMilli() - overlayTimestamp > 2000) {
                        overlayShown = false;
                        scene.setCursor(Cursor.NONE);
                        overlay.setVisible(false);
                    }
                } finally {
                    overlayLock.unlock();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("interrupted");
                }
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                switch(e.getCode()){
                    case ESCAPE:
                        exit();
                        break;
                    case UP:
                    //player.setAudioSpectrumInterval(arg0);
                    break;
                    case DOWN:
                    break;
                    default:
                    break;                }
            }
        });

        overlayHider.setDaemon(true);
        overlayHider.start();
    }

    @FXML
    private void togglePlayback() {
        if (paused) {
            player.play();
        } else {
            player.pause();
        }
        paused = !paused;
    }

    //bouger le curseur temporel
    @FXML
    private void startDrag() {
        dragging = true;
    }

    @FXML
    private void endDrag() {
        dragging = false;
    }

    //aller chercher la durée correspondante à un point sur la barre dans la vidéo
    @FXML
    private void seek() {
        player.seek(Duration.seconds(slider.getValue()));
    }

    //afficher/cacher automatiqueemnt les controle
    @FXML
    private void showControls() {
        overlayLock.lock();
        try {
            scene.setCursor(Cursor.DEFAULT);
            overlay.setVisible(true);
            overlayShown = true;
            overlayTimestamp = Instant.now().toEpochMilli();
        } finally {
            overlayLock.unlock();
        }
    }

    private void exit(){
        player.stop();
        Scene scene = new Scene(new Pane());
        scene.getStylesheets().add("/resources/css/categoryTile.css");
        State.getStage().setScene(scene);
        State.getStage().setTitle("Homework Folder Manager");
        View.setScene(scene);
        View.switchPage(view.Page.LIBRARY);
    }
}
