package me.anuar2k.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import me.anuar2k.engine.simulation.DefaultSimulation;
import me.anuar2k.engine.simulation.Simulation;
import me.anuar2k.engine.util.RandomRandSource;
import me.anuar2k.engine.worldsystem.AnimalInsightSystem;
import me.anuar2k.engine.worldsystem.MapStateWorldSystem;
import me.anuar2k.gui.config.Config;
import me.anuar2k.gui.controlpane.ControlPane;
import me.anuar2k.gui.mappane.MapPane;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private HBox mapContainer;

    @FXML
    private Button loadConfiguration;

    @FXML
    private Label mapDimensions;

    @FXML
    private Label jungleDimensions;

    @FXML
    private Label startEnergy;

    @FXML
    private Label moveEnergy;

    @FXML
    private Label plantEnergy;

    @FXML
    private Button addNewMap;

    @FXML
    private Button runAll;

    @FXML
    private Button stopAll;

    @FXML
    private Accordion accordion;

    @FXML
    private Slider frameLength;

    @FXML
    private Label startingAnimalCount;

    @FXML
    private Label minFrameLength;

    @FXML
    private Label measuredFrameLength;

    private final FileChooser fileChooser = new FileChooser();
    public static final DecimalFormat doubleFormat = new DecimalFormat("#.##");
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    private final List<Boolean> running = new ArrayList<>();
    private Config currentConfig = null;
    private int duration = 0;
    private Timeline timeline = null;
    private long lastFrameTime = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //----------handlers----------------
        this.loadConfiguration.setOnAction(evt -> {
            File file = this.fileChooser.showOpenDialog(this.mapContainer.getScene().getWindow());

            if (file != null) {
                this.handleConfigFile(file);
            }
        });

        this.addNewMap.setOnAction(evt -> {
            this.running.add(false);

            MapStateWorldSystem msws = new MapStateWorldSystem();
            AnimalInsightSystem ais = new AnimalInsightSystem();
            Simulation sim = new DefaultSimulation(List.of(msws, ais), new RandomRandSource(), this.currentConfig);

            ControlPane controlPane = new ControlPane(this, this.running.size() - 1);
            this.accordion.getPanes().add(controlPane);

            MapPane mapPane = new MapPane(this, sim, msws, ais, controlPane.running);
            HBox.setHgrow(mapPane, Priority.ALWAYS);
            this.mapContainer.getChildren().add(mapPane);

            this.resizeMaps();
        });

        this.runAll.setOnAction(evt -> {
            for (int i = 0; i < this.running.size(); i++) {
                this.runSimulation(i);
            }
        });

        this.stopAll.setOnAction(evt -> {
            for (int i = 0; i < this.running.size(); i++) {
                this.stopSimulation(i);
            }
        });

        this.mapContainer.widthProperty().addListener(evt -> this.resizeMaps());

        this.frameLength.valueProperty().addListener(evt -> this.adjustDuration());

        //----------default config----------
        Config defaultConfig = new Config();
        defaultConfig.setMapWidth(10);
        defaultConfig.setMapHeight(10);
        defaultConfig.setJungleWidth(4);
        defaultConfig.setJungleHeight(4);
        defaultConfig.setStartEnergy(20);
        defaultConfig.setPlantEnergy(20);
        defaultConfig.setMoveEnergy(1);
        defaultConfig.setStartingAnimalCount(4);

        this.loadConfig(defaultConfig);
        this.adjustDuration();
    }

    public void adjustDuration() {
        this.duration = (int)this.frameLength.getValue();
        this.minFrameLength.setText(this.duration + " ms");
        this.adjustTimeline();
    }

    public void removeMap(int index) {
        if (this.timeline != null) {
            this.timeline.stop();
        }

        this.running.remove(index);
        this.accordion.getPanes().remove(index);
        this.mapContainer.getChildren().remove(index);
        this.resizeMaps();
        this.reindexMaps();

        this.adjustTimeline();
    }

    public void adjustTimeline() {
        if (this.timeline != null) {
            this.timeline.stop();
        }

        if (this.running.contains(true)) {
            List<Integer> toRun = new ArrayList<>();

            for (int i = 0; i < this.running.size(); i++) {
                if (this.running.get(i)) {
                    toRun.add(i);
                }
            }

            this.timeline = new Timeline(new KeyFrame(Duration.millis(this.duration), evt -> {
                for (int mapIndex : toRun) {
                    this.mapTicked(mapIndex);
                }

                long currentTime = System.currentTimeMillis();
                this.measuredFrameLength.setText((currentTime - this.lastFrameTime) + " ms");
                this.lastFrameTime = currentTime;
            }));

            this.lastFrameTime = System.currentTimeMillis();

            this.timeline.setCycleCount(Timeline.INDEFINITE);
            this.timeline.setAutoReverse(false);
            this.timeline.play();

        }
        else {
            this.timeline = null;
        }
    }

    private void mapTicked(int index) {
        MapPane mapPane = (MapPane) this.mapContainer.getChildren().get(index);
        mapPane.tick();

        ControlPane controlPane = (ControlPane) this.accordion.getPanes().get(index);
        controlPane.setStatistics(mapPane.msws.getMapState());
    }

    public void runSimulation(int index) {
        this.running.set(index, true);
        ControlPane controlPane = (ControlPane) this.accordion.getPanes().get(index);
        controlPane.running.set(true);

        this.adjustTimeline();
    }

    public void stopSimulation(int index) {
        this.running.set(index, false);
        ControlPane controlPane = (ControlPane) this.accordion.getPanes().get(index);
        controlPane.running.set(false);
        this.adjustTimeline();
        controlPane.stopped();
    }

    private void reindexMaps() {
        for (int i = 0; i < this.running.size(); i++) {
            ControlPane controlPane = (ControlPane) this.accordion.getPanes().get(i);
            controlPane.setIndex(i);
        }
    }

    private void resizeMaps() {
        for (int i = 0; i < this.running.size(); i++) {
            MapPane mapPane = (MapPane) this.mapContainer.getChildren().get(i);
            mapPane.setMaxWidth(this.mapContainer.getWidth() / this.running.size());
        }
    }

    private void loadConfig(Config config) {
        if (config.getJungleWidth() < 1
                || config.getJungleHeight() < 1
                || config.getMapWidth() < 1
                || config.getMapHeight() < 1) {
            throw new IllegalArgumentException("Dimensions must be greater than zero");
        }

        if (config.getMoveEnergy() < 0 || config.getPlantEnergy() < 0 || config.getStartEnergy() < 0) {
            throw new IllegalArgumentException("Energies must be at least zero");
        }

        this.mapDimensions.setText(config.getMapWidth() + " by " + config.getMapHeight());
        this.jungleDimensions.setText(config.getJungleWidth() + " by " + config.getJungleHeight());
        this.startEnergy.setText(doubleFormat.format(config.getStartEnergy()));
        this.moveEnergy.setText(doubleFormat.format(config.getMoveEnergy()));
        this.plantEnergy.setText(doubleFormat.format(config.getPlantEnergy()));
        this.startingAnimalCount.setText(String.valueOf(config.getStartingAnimalCount()));

        this.currentConfig = config;
    }

    private void handleConfigFile(File file) {
        try {
            Config config = MainController.gson.fromJson(new JsonReader(new FileReader(file)), Config.class);
            this.loadConfig(config);
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Load configuration error");
            alert.setHeaderText(null);
            alert.setContentText("Exception raised during loading the config: " + e.getMessage());

            alert.showAndWait();
        }
    }
}
