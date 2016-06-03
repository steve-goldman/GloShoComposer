package com.constantbeta.showplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ShowPlayer extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        URL fxmlUrl           = getClass().getResource("showPlayer.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);

        Parent root = fxmlLoader.load();

        ((ShowPlayerController)fxmlLoader.getController()).init(primaryStage);

        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Show Player");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
