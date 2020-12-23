package me.anuar2k.gui.controlpane;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.util.MapState;
import me.anuar2k.gui.MainController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControlPane extends TitledPane {
    @FXML
    private Button resume;

    @FXML
    private Button stop;

    @FXML
    private Button remove;

    @FXML
    private Label statistics;

    @FXML
    private Button runFor;

    @FXML
    private TextField epochs;

    @FXML
    private CheckBox gatherStats;

    private int index;
    private final MainController controller;
    private final List<MapState> gatheredStats = new ArrayList<>();
    private int toRun = 0;
    public BooleanProperty running = new SimpleBooleanProperty(false);

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
//            this.running.set(true);
        });

        this.stop.setOnAction(evt -> {
            this.controller.stopSimulation(this.index);
//            this.running.set(false);
        });

        this.remove.setOnAction(evt -> {
            this.controller.removeMap(this.index);
            this.running.set(false);
        });

        this.runFor.setOnAction(evt -> {
            try {
                int runForCount = Integer.parseInt(this.epochs.getText());
                if (runForCount < 1) {
                    throw new IllegalArgumentException("the value must be at least 1");
                }
                this.toRun = runForCount;
                this.controller.runSimulation(index);
            }
            catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Run for n epochs error");
                alert.setHeaderText(null);
                alert.setContentText("Exception raised when scheduling a run: " + e.getMessage());

                alert.showAndWait();
            }
        });

        this.resume.disableProperty().bind(this.running);
        this.stop.disableProperty().bind(Bindings.not(this.running));
        this.remove.disableProperty().bind(this.running);
        this.runFor.disableProperty().bind(this.running);
        this.epochs.disableProperty().bind(this.running);
        this.gatherStats.disableProperty().bind(this.running);
    }

    public void setIndex(int index) {
        this.index = index;
        this.setText("Map " + (index + 1));
    }

    public void setStatistics(MapState mapState) {
        if (this.gatherStats.isSelected()) {
            this.gatheredStats.add(mapState);
        }
        StringBuilder sr = new StringBuilder();
        sr.append("Epoch no: ").append(mapState.getEpochNo()).append('\n');
        sr.append("Animal count: ").append(mapState.getAnimalCount()).append('\n');

        sr.append("Dominant genome: ");
        if (mapState.getDominantGenome() != null) {
            for (byte gene : mapState.getDominantGenome()) {
                sr.append(gene);
            }
        }
        else {
            sr.append("none");
        }
        sr.append('\n');
        sr.append("Average energy: ").append(MainController.doubleFormat.format(mapState.getAverageEnergy())).append('\n');
        sr.append("Average life length: ").append(MainController.doubleFormat.format(mapState.getAverageLifeLength())).append('\n');
        sr.append("average children count: ").append(MainController.doubleFormat.format(mapState.getAverageChildrenCount())).append('\n');

        this.statistics.setText(sr.toString());

        if (this.toRun == 1) {
            this.controller.stopSimulation(this.index);
            this.toRun = 0;
        }
        if (this.toRun > 0) {
            this.toRun--;
        }
    }

    public void stopped() {
        if (!this.running.get()) {
            if (this.gatherStats.isSelected() && this.gatheredStats.size() > 0) {
                MapState average = MapState.average(this.gatheredStats);
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("stats.json");
                File file = fileChooser.showSaveDialog(this.getScene().getWindow());

                if (file != null) {
                    System.out.println("file not null");
                    try {
                        FileWriter fw = new FileWriter(file);
                        fw.write(MainController.gson.toJson(average));
                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Save file error");
                        alert.setHeaderText(null);
                        alert.setContentText("Exception raised during saving: " + e.getMessage());

                        alert.showAndWait();
                    }
                }
            }
            this.gatherStats.selectedProperty().set(false);
            this.gatheredStats.clear();
        }

        this.toRun = 0;
    }
}
