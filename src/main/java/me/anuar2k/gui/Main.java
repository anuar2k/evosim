package me.anuar2k.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("scene.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("JavaFX and Gradle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
