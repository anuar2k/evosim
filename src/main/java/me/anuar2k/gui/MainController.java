package me.anuar2k.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import me.anuar2k.engine.simulation.DefaultSimulation;
import me.anuar2k.engine.simulation.Simulation;
import me.anuar2k.engine.util.RandomRandSource;
import me.anuar2k.engine.worldsystem.MapStateWorldSystem;
import me.anuar2k.gui.config.Config;
import me.anuar2k.gui.controlpane.ControlPane;
import me.anuar2k.gui.mappane.MapPane;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.text.DecimalFormat;
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

    private final FileChooser fileChooser = new FileChooser();
    private final DecimalFormat doubleFormat = new DecimalFormat("#.##");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    private Config currentConfig = null;
    private int mapCount = 0;

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
            this.mapCount++;

            MapStateWorldSystem msws = new MapStateWorldSystem();
            Simulation sim = new DefaultSimulation(List.of(msws), new RandomRandSource(), this.currentConfig);

            MapPane mapPane = new MapPane(this, sim, msws);
            HBox.setHgrow(mapPane, Priority.ALWAYS);
            this.mapContainer.getChildren().add(mapPane);

            ControlPane controlPane = new ControlPane(this, this.mapCount - 1);
            this.accordion.getPanes().add(controlPane);
            this.resizeMaps();
        });

        this.runAll.setOnAction(evt -> {
            for (int i = 0; i < this.mapCount; i++) {
                MapPane mapPane = (MapPane) this.mapContainer.getChildren().get(i);
                mapPane.tick();
            }
        });

        this.mapContainer.widthProperty().addListener(evt -> this.resizeMaps());

        //----------default config----------
        Config defaultConfig = new Config();
        defaultConfig.setMapWidth(10);
        defaultConfig.setMapHeight(10);
        defaultConfig.setJungleWidth(4);
        defaultConfig.setJungleHeight(4);
        defaultConfig.setStartEnergy(20);
        defaultConfig.setPlantEnergy(20);
        defaultConfig.setMoveEnergy(1);

        this.loadConfig(defaultConfig);
    }

    public void removeMap(int index) {
        this.accordion.getPanes().remove(index);
        this.mapContainer.getChildren().remove(index);
        this.mapCount--;
        this.resizeMaps();
        this.reindexMaps();
    }

    public void runSimulation(int index) {

    }

    public void stopSimulation(int index) {

    }

    private void reindexMaps() {
        for (int i = 0; i < this.mapCount; i++) {
            ControlPane controlPane = (ControlPane) this.accordion.getPanes().get(i);
            controlPane.setIndex(i);
        }
    }

    private void resizeMaps() {
        for (int i = 0; i < this.mapCount; i++) {
            MapPane mapPane = (MapPane) this.mapContainer.getChildren().get(i);
            //mapPane.setMinWidth(this.mapContainer.getWidth() / this.mapCount);
            mapPane.setMaxWidth(this.mapContainer.getWidth() / this.mapCount);
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
        this.startEnergy.setText(this.doubleFormat.format(config.getStartEnergy()));
        this.moveEnergy.setText(this.doubleFormat.format(config.getMoveEnergy()));
        this.plantEnergy.setText(this.doubleFormat.format(config.getPlantEnergy()));

        this.currentConfig = config;

        System.out.println(this.gson.toJson(config));
    }

    private void handleConfigFile(File file) {
        try {
            Config config = this.gson.fromJson(new JsonReader(new FileReader(file)), Config.class);
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
