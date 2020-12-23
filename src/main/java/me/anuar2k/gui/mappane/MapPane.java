package me.anuar2k.gui.mappane;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.AnimalProperty;
import me.anuar2k.engine.property.ChildrenProperty;
import me.anuar2k.engine.property.GenomeProperty;
import me.anuar2k.engine.simulation.DefaultSimulation;
import me.anuar2k.engine.simulation.Simulation;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.MapState;
import me.anuar2k.engine.util.RandomRandSource;
import me.anuar2k.engine.worldsystem.AnimalInsightSystem;
import me.anuar2k.engine.worldsystem.MapStateWorldSystem;
import me.anuar2k.gui.MainController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapPane extends Pane {
    @FXML
    private Canvas canvas;

    public final Simulation sim;
    public final MapStateWorldSystem msws;
    public final AnimalInsightSystem ais;
    private MainController controller;
    public final BooleanProperty running;
    private final List<Entity> trackedEntities = new ArrayList<>();

    public MapPane(MainController controller, Simulation sim, MapStateWorldSystem msws, AnimalInsightSystem ais, BooleanProperty running) {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("mappane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.canvas.widthProperty().bind(this.widthProperty());
        this.canvas.heightProperty().bind(this.heightProperty());

        this.canvas.widthProperty().addListener(e -> this.redraw());
        this.canvas.heightProperty().addListener(e -> this.redraw());

        this.controller = controller;
        this.sim = sim;
        this.msws = msws;
        this.ais = ais;
        this.running = running;

        this.canvas.setOnMouseClicked(evt -> {
            if (!this.running.get()) {
                System.out.println("X: " + evt.getX());
                System.out.println("Y: " + evt.getY());

                MapState mapState = this.msws.getMapState();
                if (mapState != null) {
                    int offset = 6;
                    double sideSize = Math.min((this.canvas.getWidth() - offset) / mapState.getWidth(),
                            this.canvas.getHeight() / mapState.getHeight());

                    double width = sideSize * mapState.getWidth();
                    double height = sideSize * mapState.getHeight();

                    double cornerX = (Math.abs(this.canvas.getWidth() - width) / 2);
                    double cornerY = (Math.abs(this.canvas.getHeight() - height) / 2);

                    double clickX = evt.getX() - cornerX;
                    double clickY = evt.getY() - cornerY;

                    int x = (int)Math.floor(clickX / sideSize);
                    int y = (int)Math.floor(clickY / sideSize);

                    Entity animal = this.ais.getAnimalForCoord(new Coord2D(x, y));

                    if (animal != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Animal data");
                        alert.setHeaderText(null);
                        StringBuilder sb = new StringBuilder();
                        sb.append("Animal's genome: ");
                        for (byte gene : animal.getProperty(GenomeProperty.class).getGenome().getGenes()) {
                            sb.append(gene);
                        }
                        sb.append("\n\n");
                        sb.append("Do you wish to track the animal until next stop?");

                        alert.setContentText(sb.toString());

                        Optional<ButtonType> result = alert.showAndWait();
                        ButtonType button = result.orElse(ButtonType.CANCEL);

                        if (button == ButtonType.OK) {
                            this.trackedEntities.add(animal);
                        }
                    }
                }
            }
        });

        this.running.addListener(evt -> {
            if (!this.running.get()) {
                if (this.trackedEntities.size() > 0) {
                    for (Entity e : this.trackedEntities) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Descendants summary");
                        alert.setHeaderText(null);

                        StringBuilder sb = new StringBuilder();
                        sb.append("Animal with genome ");
                        for (byte gene : e.getProperty(GenomeProperty.class).getGenome().getGenes()) {
                            sb.append(gene);
                        }
                        sb.append(" has ");
                        sb.append(e.getProperty(ChildrenProperty.class).walkDescendants());
                        sb.append(" descendants.");

                        alert.setContentText(sb.toString());
                        alert.show();
                    }
                }
                this.trackedEntities.clear();
            }
        });
    }

    public void redraw() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        MapState mapState = this.msws.getMapState();
        if (mapState != null) {
            int offset = 6;
            double sideSize = Math.min((this.canvas.getWidth() - offset) / mapState.getWidth(),
                    this.canvas.getHeight() / mapState.getHeight());

            double width = sideSize * mapState.getWidth();
            double height = sideSize * mapState.getHeight();

            double cornerX = (Math.abs(this.canvas.getWidth() - width) / 2);
            double cornerY = (Math.abs(this.canvas.getHeight() - height) / 2);

            for (int x = 0; x < mapState.getWidth(); x++) {
                for (int y = 0; y < mapState.getHeight(); y++) {
                    gc.setFill(mapState.getCellColors()[x][y]);
                    gc.fillRect((int)(cornerX + sideSize * x),
                            (int)(cornerY + sideSize * y),
                            (int)(cornerX + sideSize * (x + 1)) - (int)(cornerX + sideSize * x),
                            (int)(cornerY + sideSize * (y + 1)) - (int)(cornerY + sideSize * y));
                }
            }
        }
    }

    public void tick() {
        this.sim.tick();
        this.redraw();

        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : this.trackedEntities) {
            if (!e.onMap()) {
                toRemove.add(e);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Animal died!");
                alert.setHeaderText(null);

                StringBuilder sb = new StringBuilder();
                sb.append("Animal with genome ");
                for (byte gene : e.getProperty(GenomeProperty.class).getGenome().getGenes()) {
                    sb.append(gene);
                }
                sb.append(" has died at day ");
                sb.append(this.msws.getMapState().getEpochNo());
                sb.append("!");

                alert.setContentText(sb.toString());
                alert.show();
            }
        }

        for (Entity e : toRemove) {
            this.trackedEntities.remove(e);
        }
    }
}
