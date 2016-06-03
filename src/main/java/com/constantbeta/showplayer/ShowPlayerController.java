package com.constantbeta.showplayer;

import com.constantbeta.frame.FrameMask;
import com.constantbeta.frame.FrameMaskUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

    private File       inputFile;
    private ShowFrames showFrames;
    private ShowTimer  showTimer;
    private FrameMask  curFrameMask;

    public void init(Stage stage)
    {
        this.stage = stage;
        root.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
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
                showTimer                  = new ShowTimer(showTimerListener, showFrames.getShowDuration(), 1.0, 10);

                imageView.setFitWidth(showFrames.getWidth());
                imageView.setFitHeight(showFrames.getHeight());

                stage.setWidth(showFrames.getWidth());
                stage.setHeight(showFrames.getHeight() + 51);

                setImageFor(0);

                // TODO: move to play button
                showTimer.start();
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

    private final ShowTimer.Listener showTimerListener = new ShowTimer.Listener()
    {
        @Override
        public void onTick(int curTime)
        {
            Platform.runLater(() -> setImageFor(curTime));
        }

        @Override
        public void onDone()
        {
            System.out.println("show is done");
        }
    };
}
