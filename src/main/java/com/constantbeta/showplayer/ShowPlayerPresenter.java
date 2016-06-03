package com.constantbeta.showplayer;

import com.constantbeta.frame.FrameMask;
import com.constantbeta.frame.FrameMaskUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicInteger;

public class ShowPlayerPresenter
{
    private final BorderPane    root;
    private final ImageView     imageView;
    private final Button        playButton;
    private final Button        pauseButton;
    private final Label         timeLabel;

    private final AtomicInteger curTime = new AtomicInteger();
    private FrameMask           curFrameMask;
    private FrameMask           pendingFrameMask;

    public ShowPlayerPresenter(Scene scene)
    {
        this.root        = (BorderPane)scene.lookup("#root");
        this.imageView   = (ImageView) scene.lookup("#imageView");
        this.playButton  = (Button)    scene.lookup("#playButton");
        this.pauseButton = (Button)    scene.lookup("#pauseButton");
        this.timeLabel   = (Label)     scene.lookup("#timeLabel");

        init();
    }

    private void init()
    {
        root.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        playButton.setDisable(true);
        pauseButton.setDisable(true);
    }

    private void setImage()
    {
        if (curFrameMask != pendingFrameMask)
        {
            imageView.setImage(FrameMaskUtils.toFxImage(pendingFrameMask));
            curFrameMask = pendingFrameMask;
        }
    }

    public void onFileOpened(FrameMask firstFrame)
    {
        pendingFrameMask = firstFrame;
        Platform.runLater(this::handleFileOpened);
    }

    private void handleFileOpened()
    {
        playButton.setDisable(false);
        imageView.setFitWidth(pendingFrameMask.getWidth());
        imageView.setFitHeight(pendingFrameMask.getHeight());
        setImage();
    }

    public void onPlayButtonPressed()
    {
        Platform.runLater(this::handlePlayButtonPressed);
    }

    private void handlePlayButtonPressed()
    {
        playButton.setDisable(true);
        pauseButton.setDisable(false);
    }

    public void onPauseButtonPressed()
    {
        Platform.runLater(this::handlePauseButtonPressed);
    }

    private void handlePauseButtonPressed()
    {
        playButton.setDisable(false);
        pauseButton.setDisable(true);
    }

    public void onTimeChanged(int curTime, FrameMask nextFrame)
    {
        pendingFrameMask = nextFrame;
        this.curTime.set(curTime);
        Platform.runLater(this::handleTimeChanged);
    }

    private void handleTimeChanged()
    {
        final int curTime = this.curTime.get();

        int minutes       = curTime / (1000 * 60);
        int seconds       = (curTime - minutes * 1000 * 60) / 1000;
        int millis        = curTime % 1000;

        timeLabel.setText(String.format("%02d:%02d.%03d", minutes, seconds, millis));

        setImage();
    }

    public void onDone()
    {
        Platform.runLater(this::handleDone);
    }

    private void handleDone()
    {
        playButton.setDisable(false);
        pauseButton.setDisable(true);
    }
}
