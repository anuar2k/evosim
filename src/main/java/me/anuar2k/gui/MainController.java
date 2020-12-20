package me.anuar2k.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import me.anuar2k.gui.mappane.MapPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private HBox mapcontainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.mapcontainer.getChildren().addAll(new MapPane(Color.RED), new MapPane(Color.BLUEVIOLET));
    }
}
