package com.constantbeta.showplayer;

import com.constantbeta.frame.FrameMask;
import com.constantbeta.frame.FrameMaskUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShowPlayerController
{
    private Stage      stage;

    @FXML
    private BorderPane root;

    @FXML
    private ImageView  imageView;

    @FXML
    private Button     playButton;

    @FXML
    private Button     pauseButton;

    @FXML
    private Label      timeLabel;

    private File       inputFile;
    private ShowFrames showFrames;
    private ShowTimer  showTimer;
    private FrameMask  curFrameMask;

    public void init(Stage stage)
    {
        this.stage = stage;
        root.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        playButton.setDisable(true);
        pauseButton.setDisable(true);
    }

    @FXML
    private void saveAs()
    {
        // TODO
    }

    @FXML
    private void open()
    {
        FileChooser fileChooser = new FileChooser();
        inputFile = fileChooser.showOpenDialog(stage);
        openFile();
    }

    @FXML
    private void refresh()
    {
        openFile();
    }

    private void openFile()
    {
        if (null != inputFile)
        {
            try
            {
                JSONObject config          = new JSONObject(new JSONTokener(new FileInputStream(inputFile)));
                showFrames                 = ShowFrames.fromJson(config);

                imageView.setFitWidth(showFrames.getWidth());
                imageView.setFitHeight(showFrames.getHeight());

                stage.setWidth(showFrames.getWidth());
                stage.setHeight(showFrames.getHeight() + 91);

                setImageFor(0);

                playButton.setDisable(false);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setImageFor(int elapsedTime)
    {
        FrameMask frameMask = showFrames.getFrameFor(elapsedTime);
        if (curFrameMask != frameMask)
        {
            imageView.setImage(FrameMaskUtils.toFxImage(frameMask));
            curFrameMask = frameMask;
        }
    }

    @FXML
    private void playButtonPressed()
    {
        if (null == showTimer)
        {
            showTimer = new ShowTimer(showTimerListener, showFrames.getShowDuration(), 1.0, 10);
        }

        playButton.setDisable(true);
        pauseButton.setDisable(false);
        showTimer.start();
    }

    @FXML
    private void pauseButtonPressed()
    {
        playButton.setDisable(false);
        pauseButton.setDisable(true);
        showTimer.pause();
    }

    private final ShowTimer.Listener showTimerListener = new ShowTimer.Listener()
    {
        @Override
        public void onTick(int curTime)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    int minutes = curTime / (1000 * 60);
                    int seconds = (curTime - minutes * 1000 * 60) / 1000;
                    int millis  = curTime % 1000;

                    timeLabel.setText(String.format("%02d:%02d.%03d", minutes, seconds, millis));
                    setImageFor(curTime);
                }
            });
        }

        @Override
        public void onDone()
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    playButton.setDisable(false);
                    pauseButton.setDisable(true);
                }
            });
            showTimer = null;
        }
    };
}
