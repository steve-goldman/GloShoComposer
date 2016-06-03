package com.constantbeta.showplayer;

import com.constantbeta.frame.FrameMask;
import javafx.fxml.FXML;
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
    private File       inputFile;
    private ShowFrames showFrames;
    private ShowTimer  showTimer;

    private ShowPlayerPresenter presenter;

    public void init(Stage stage)
    {
        this.stage = stage;
        this.presenter = new ShowPlayerPresenter(stage.getScene());
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
                FrameMask firstFrame       = showFrames.getFrameFor(0);

                presenter.onFileOpened(firstFrame);

                stage.setWidth(showFrames.getWidth());
                stage.setHeight(showFrames.getHeight() + 91);

                showTimer = null;
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void playButtonPressed()
    {
        presenter.onPlayButtonPressed();

        if (null == showTimer)
        {
            showTimer = new ShowTimer(showTimerListener, showFrames.getShowDuration(), 1.0, 10);
        }
        showTimer.start();
    }

    @FXML
    private void pauseButtonPressed()
    {
        presenter.onPauseButtonPressed();
        showTimer.pause();
    }

    private final ShowTimer.Listener showTimerListener = new ShowTimer.Listener()
    {
        @Override
        public void onTick(int curTime)
        {
            FrameMask nextFrame = showFrames.getFrameFor(curTime);
            presenter.onTimeChanged(curTime, nextFrame);
        }

        @Override
        public void onDone()
        {
            presenter.onDone();
            showTimer = null;
        }
    };
}
