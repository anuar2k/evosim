package me.anuar2k.gui.mappane;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import me.anuar2k.engine.simulation.DefaultSimulation;
import me.anuar2k.engine.simulation.Simulation;
import me.anuar2k.engine.util.MapState;
import me.anuar2k.engine.util.RandomRandSource;
import me.anuar2k.engine.worldsystem.MapStateWorldSystem;
import me.anuar2k.gui.MainController;

import java.io.IOException;
import java.util.List;

public class MapPane extends Pane {
    @FXML
    Canvas canvas;

    private final Simulation sim;
    private final MapStateWorldSystem msws;
    private MainController controller;

    public MapPane(MainController controller, Simulation sim, MapStateWorldSystem msws) {
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
        this.msws = msws;
        this.sim = sim;
    }

    public void redraw() {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());

        MapState mapState = this.msws.getMapState();
        if (mapState != null) {
//            int sideSize = (int)Math.min(this.canvas.getWidth() / mapState.getWidth(),
//                    this.canvas.getHeight() / mapState.getHeight());
//
//            int width = sideSize * mapState.getWidth();
//            int height = sideSize * mapState.getHeight();
//
//            int cornerX = (int)(Math.abs(this.canvas.getWidth() - width) / 2);
//            int cornerY = (int)(Math.abs(this.canvas.getHeight() - height) / 2);
//
//            for (int x = 0; x < mapState.getWidth(); x++) {
//                for (int y = 0; y < mapState.getHeight(); y++) {
//                    gc.setFill(mapState.getCellColors()[x][y]);
//                    gc.fillRect(cornerX + sideSize * x, cornerY + sideSize * y, sideSize, sideSize);
//                }
//            }
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
    }
}
