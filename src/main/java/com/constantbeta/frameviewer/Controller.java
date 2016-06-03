package com.constantbeta.frameviewer;

import com.constantbeta.frame.FrameMask;
import com.constantbeta.frame.FrameMaskUtils;
import com.constantbeta.frame.layer.MaskLayer;
import com.constantbeta.frame.layer.MaskLayerFactory;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller
{
    private Stage      stage;
    private FrameMask  frameMask;

    @FXML
    private BorderPane root;

    @FXML
    private ImageView  maskFrameImageView;

    private File       inputFile;

    public void init(Stage stage)
    {
        this.stage      = stage;
        root.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @FXML
    private void saveAs()
    {
        FileChooser fileChooser = new FileChooser();
        File target = fileChooser.showSaveDialog(stage);
        if (null != target)
        {
            BufferedImage bufferedImage = FrameMaskUtils.toBufferedImage(frameMask);
            try
            {
                ImageIO.write(bufferedImage, "PNG", target);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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

                int        width           = config.getInt("width");
                int        height          = config.getInt("height");
                JSONObject maskLayerConfig = config.getJSONObject("maskLayer");
                MaskLayer  maskLayer       = MaskLayerFactory.create(width, height, maskLayerConfig);

                frameMask                  = new FrameMask(width, height, maskLayer);

                maskFrameImageView.setFitWidth(width);
                maskFrameImageView.setFitHeight(height);
                maskFrameImageView.setImage(FrameMaskUtils.toFxImage(frameMask));

                stage.setWidth(width);
                stage.setHeight(height + 51);

                BufferedImage bufferedImage = FrameMaskUtils.toBufferedImage(frameMask);
                try
                {
                    ImageIO.write(bufferedImage, "PNG", new File("current_image.png"));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
}
