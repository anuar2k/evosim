package me.anuar2k.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import me.anuar2k.gui.mappane.MapPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private HBox mapcontainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MapPane pane1 = new MapPane(mapcontainer);
        MapPane pane2 = new MapPane(mapcontainer);

        HBox.setHgrow(pane1, Priority.SOMETIMES);
        HBox.setHgrow(pane2, Priority.SOMETIMES);
        this.mapcontainer.getChildren().add(pane1);
        this.mapcontainer.getChildren().add(pane2);

        pane1.redraw();
        pane2.redraw();
    }
}
