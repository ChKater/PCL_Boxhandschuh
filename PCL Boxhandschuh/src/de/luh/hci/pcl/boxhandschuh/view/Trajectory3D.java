package de.luh.hci.pcl.boxhandschuh.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import application.DemoJzy3dFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import de.luh.hci.pcl.boxhandschuh.arduino.FakeArduinoConnection;
import de.luh.hci.pcl.boxhandschuh.io.PunchIO;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePointListener;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.protrector.MeasurementTo3dTrajectory;
import de.luh.hci.pcl.boxhandschuh.protrector.Point3D;

public class Trajectory3D extends SplitPane implements MeasurePointListener {

	@FXML
	private Button measure;

	@FXML
	private ChoiceBox<String> classSelect;

	@FXML
	private TextField person;

	@FXML
	private ListView<Punch> measurements;

	@FXML
	private GridPane chartPane;

	private boolean measurementRunning = false;

	private Measurement m;

	private ObservableList<Punch> punches = FXCollections.observableArrayList();
	private MeasurementTo3dTrajectory mto3dt = new MeasurementTo3dTrajectory();

	private File dataDir = new File("punch-data");
	
	public Trajectory3D() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Trajectory3D.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		measurements.setItems(punches);
		measurements.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					plott(transform(newValue.getTrace()));
				});
		readData();
		FakeArduinoConnection.addMeasurePointListener(this);

	}

	private void readData() {
		
		for (File file : dataDir.listFiles()) {
			if (file.isFile() && !file.getName().startsWith(".")) {
				punches.add(PunchIO.readPunch(file));
			}
		}
	}
	
	

	@FXML
	public void measure() {

		if (measurementRunning) {
			Punch punch = new Punch(m, mto3dt.measurementTo3DTrajectory(m),
					classSelect.getSelectionModel().getSelectedItem(), person
							.getText());
			PunchIO.savePunch(punch);
			punches.add(punch);
			measure.setText("Start");
		} else {
			m = new Measurement();
			measure.setText("Stop");
		}
		measurementRunning = !measurementRunning;
		System.out.println("measurementRunning=" + measurementRunning);

	}

	@Override
	public void OnMeasurePoint(MeasurePoint measurePoint) {
		if (measurementRunning) {
			m.getMeasurement().add(measurePoint);
		}
	}

	private void plott(Coord3d[] data) {
		Scatter satterAccel = new Scatter(data, Color.RED);
		JavaFXChartFactory factory1 = new JavaFXChartFactory();
		AWTChart chart1 = getDemoChart(factory1, "offscreen", satterAccel);
		ImageView imageView1 = factory1.bindImageView(chart1);
		chartPane.getChildren().clear();
		chartPane.add(imageView1, 0, 0);
	}

	private Coord3d[] transform(List<Point3D> trace) {
		Coord3d[] coordArr = new Coord3d[trace.size()];
		for (int i = 0; i < coordArr.length; i++) {
			Point3D p = trace.get(i);
			coordArr[i] = new Coord3d(p.x, p.y, p.z);
		}
		return coordArr;
	}

	private AWTChart getDemoChart(JavaFXChartFactory factory, String toolkit,
			Scatter scatter) {

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
		final Shape surface = Builder.buildOrthonormal(mapper, range, steps);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface
				.getBounds().getZmin(), surface.getBounds().getZmax(),
				new Color(1, 1, 1, .5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(false);

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
