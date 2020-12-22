package me.anuar2k.gui.controlpane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import me.anuar2k.gui.MainController;

import java.io.IOException;

public class ControlPane extends TitledPane {
    @FXML
    private Button resume;

    @FXML
    private Button stop;

    @FXML
    private Button remove;

    @FXML
    private Label statistics;

    private int index;
    private MainController controller;
    public ControlPane(MainController controller, int index) {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("controlpane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.setIndex(index);
        this.controller = controller;

        this.resume.setOnAction(evt -> {
            this.controller.runSimulation(this.index);
        });

        this.stop.setOnAction(evt -> {
            this.controller.stopSimulation(this.index);
        });

        this.remove.setOnAction(evt -> {
            this.controller.removeMap(this.index);
        });
    }

    public void setIndex(int index) {
        this.index = index;
        this.setText("Map " + (index + 1));
    }
}
