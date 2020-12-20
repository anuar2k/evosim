package me.anuar2k.gui.mappane;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class MapPane extends Pane {
    public MapPane(Color color) {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("mappane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().add(new Label("abcdef"));
    }
}
