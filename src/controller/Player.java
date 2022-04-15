package controller;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Video;
import view.View;

public class Player {
    @FXML
    private MediaView mediaView;
    @FXML
    private Text title;
    @FXML
    private Button playButton;
    @FXML
    private Slider slider;
    @FXML
    private VBox overlay;
    @FXML
    private Slider volume;
    @FXML
    private ImageView pauseIcon;
    @FXML
    private ImageView playIcon;
    @FXML
    private ImageView enterFullscreenIcon;
    @FXML
    private ImageView exitFullscreenIcon;

    private boolean paused = true;
    private boolean fullscreen = false;
    private boolean dontExit = false;
    private boolean dragging = false;
    private MediaPlayer player;
    private Media media;
    private Scene scene;
    private Stage stage;

    private Lock overlayLock = new ReentrantLock();
    private boolean overlayShown = false;
    private long overlayTimestamp = 0;

    @FXML
    private void initialize() throws IOException {
        Video v = State.getCurrentVideo();
        title.setText(v.name);
        stage = State.getStage();
        scene = stage.getScene();

        media = new Media(new File(v.path).toURI().toURL().toString());
        player = new MediaPlayer(media);
        mediaView.setMediaPlayer(player);

        exitFullscreenIcon.setVisible(false);
        enterFullscreenIcon.setVisible(true);

        player.setOnPaused(() -> {
            pauseIcon.setVisible(false);
            playIcon.setVisible(true);
        });

        player.setOnPlaying(() -> {
            playIcon.setVisible(false);
            pauseIcon.setVisible(true);
        });

        player.setOnEndOfMedia(() -> {
            // TODO
        });

        stage.fullScreenProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                enterFullscreenIcon.setVisible(false);
                exitFullscreenIcon.setVisible(true);
                dontExit = false;
            } else {
                exitFullscreenIcon.setVisible(false);
                enterFullscreenIcon.setVisible(true);
                dontExit = true;
            }
            fullscreen = newValue;
        });

        player.setAutoPlay(true);
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

        volume.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (dragging) {
                player.setVolume(newValue.doubleValue() / 100.);
            }
        });

        Thread overlayHider = new Thread(() -> {
            while (true) {
                overlayLock.lock();
                try {
                    if (overlayShown && Instant.now().toEpochMilli() - overlayTimestamp > 2000 && !dragging) {
                        overlayShown = false;
                        scene.setCursor(Cursor.NONE);
                        overlay.setVisible(false);
                        // overlay.setOpacity(0);
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

        scene.setOnMouseMoved(event -> {
            showControls();
        });

        scene.setOnKeyPressed(event -> handleKeyEvent(event));

        showControls();

        overlayHider.setDaemon(true);
        overlayHider.start();
    }

    private void handleKeyEvent(KeyEvent e) {
        System.out.println(e.getCode().toString());
        showControls();
        switch (e.getCode()) {
            case ESCAPE:
                if (!dontExit) {
                    exit();
                }
                dontExit = false;
                break;
            case UP:
                volume.increment();
                player.setVolume(volume.getValue() / 100.);
                System.out.println(volume.getValue());
                break;
            case DOWN:
                volume.decrement();
                System.out.println(volume.getValue());
                player.setVolume(volume.getValue() / 100.);
                break;
            case SPACE:
            case K:
                togglePlayback();
                break;
            case F:
                toggleFullscreen();
                dontExit = false;
                break;
            case RIGHT:
                slider.increment();
                player.seek(Duration.seconds(slider.getValue()));
                break;
            case LEFT:
                slider.decrement();
                player.seek(Duration.seconds(slider.getValue()));
                break;
            default:
                break;
        }
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

    @FXML
    private void toggleFullscreen() {
        fullscreen = !fullscreen;
        stage.setFullScreen(fullscreen);
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
            // overlay.setOpacity(1);
            overlayShown = true;
            overlayTimestamp = Instant.now().toEpochMilli();
        } finally {
            overlayLock.unlock();
        }
    }

    //méthode appelée quand on appuie sur escape
    @FXML
    private void exit() {
        stage.setFullScreen(false);
        player.stop();
        scene.setCursor(Cursor.DEFAULT);
        View.switchPage(View.popPage());
    }
}
