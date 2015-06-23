package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.javafx.JavaFXRenderer3d;
import org.jzy3d.javafx.controllers.JavaFXCameraMouseController;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import arduino.ArduinoConnection;

/**
 * Showing how to pipe an offscreen Jzy3d chart image to a JavaFX ImageView.
 * 
 * {@link JavaFXChartFactory} delivers dedicated
 * {@link JavaFXCameraMouseController} and {@link JavaFXRenderer3d}
 * 
 * Support Rotation control with left mouse button hold+drag Scaling scene using
 * mouse wheel Animation (camera rotation with thread)
 * 
 * TODO : Mouse right click shift Keyboard support (rotate/shift, etc)
 * 
 * @author Martin Pernollet
 */
public class DemoJzy3dFX extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    ArrayList<Coord3d> coord = new ArrayList<Coord3d>();
    ArrayList<Coord3d> coordAccel = new ArrayList<Coord3d>();
    private ArrayList<Date> timeStamps = new ArrayList<>();
    private StackPane pane;
    boolean running;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Jzy3d|FX");
        pane = new StackPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

        EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (!running && keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                    System.out.println("press");
                    coord = new ArrayList<Coord3d>();
                    coordAccel = new ArrayList<Coord3d>();
                    timeStamps = new ArrayList<>();
                    running = true;
                }
            }
        };
        scene.setOnKeyPressed(keyEventHandler);
        EventHandler<KeyEvent> keyEventHandlerRel = new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                    System.out.println("rel");
                    running = false;
                    writeToFile("Haken", "Guido");
                    plott();

                }
            }
        };
        scene.setOnKeyReleased(keyEventHandlerRel);

        // factory1.addSceneSizeChangedListener(chart1, scene);

        stage.setWidth(500);
        stage.setHeight(500);

        Measurements measureGyro = new Measurements() {

            @Override
            public void sendData(String inputLine) {

                if (running) {

                    if (inputLine.contains("ypr")) {

                        String[] split = inputLine.split("\\s+");

                        Coord3d co = new Coord3d(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
                        coord.add(co);

                        // mpu.gyroReading(Float.parseFloat(split[1]),Float.parseFloat(split[2]),Float.parseFloat(split[3]));
                    } else if (inputLine.contains("aworld")) {
                        String[] split = inputLine.split("\\s+");
                        Coord3d co = new Coord3d(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
                        timeStamps.add(new Date());
                        coordAccel.add(co);

                    }

                } else {
                    System.out.println(inputLine);
                }

            }
        };

        ArduinoConnection.getInstance().setMeasure(measureGyro);
        new Thread(ArduinoConnection.getInstance()).start();

    }

    public void plott() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                Scatter scatterGyro = new Scatter(coord.toArray(new Coord3d[1]), Color.RED);
                Scatter satterAccel = new Scatter(coordAccel.toArray(new Coord3d[1]), Color.RED);
                // Jzy3d
                JavaFXChartFactory factory1 = new JavaFXChartFactory();
                AWTChart chart1 = DemoJzy3dFX.this.getDemoChart(factory1, "offscreen", satterAccel);
                ImageView imageView1 = factory1.bindImageView(chart1);

                JavaFXChartFactory factory2 = new JavaFXChartFactory();
                AWTChart chart2 = DemoJzy3dFX.this.getDemoChart(factory2, "offscreen", scatterGyro);
                ImageView imageView2 = factory1.bindImageView(chart2);

                // JavaFX

                VBox v = new VBox();
                v.getChildren().add(imageView1);
                v.getChildren().add(imageView2);
                pane.getChildren().clear();
                pane.getChildren().add(v);

                ArduinoConnection.getInstance().run = false;

            }
        });
    }

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private void writeToFile(String action, String person) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data/" + action + "_" + person + "_" + sdf.format(new Date())), false));
            bw.write("Position,Time,ACCX,ACCY,ACCZ,GyrY,GyrP,GyrR,Action,Person");
            for (int i = 0; i < coord.size() - 3; i++) {
                bw.write(i + "," + sdf.format(timeStamps.get(i)) + "," + coordAccel.get(i).x + "," + coordAccel.get(i).y + "," + coordAccel.get(i).z + "," + coord.get(i).x + "," + coord.get(i).y + "," + coord.get(i).z + "," + action + "," + person + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit, Scatter scatter) {

        // -------------------------------
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                return x * Math.sin(x * y);
            }
        };

        // Define range and precision for the function to plot
        Range range = new Range(-3, 3);
        int steps = 80;

        // Create the object to represent the function over the given range.
//        final Shape surface = Builder.buildOrthonormal(mapper, range, steps);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
//        surface.setFaceDisplayed(true);
//        surface.setWireframeDisplayed(false);

        // -------------------------------
        // Create a chart
        Quality quality = Quality.Advanced;
        // quality.setSmoothPolygon(true);
        // quality.setAnimated(true);

        // let factory bind mouse and keyboard controllers to JavaFX node
        AWTChart chart = (AWTChart) factory.newChart(quality, toolkit);
        // chart.getScene().add(scatterGyro);
        chart.getScene().add(scatter);
        return chart;
    }
}
