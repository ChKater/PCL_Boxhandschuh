package de.luh.hci.pcl.boxhandschuh.view;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementToAccelerometerTrace;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementToGyroskopTrace;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementToTrace;

public class MeasurementView extends TabPane{

	private static MeasurementToTrace mtacc = new MeasurementToAccelerometerTrace();
	private static MeasurementToTrace mtgyr = new MeasurementToGyroskopTrace();
	
	@FXML
	private Tab trajectory, accelerometer,gyroskop,sensorvalues;
	
	private Punch punch;
	private Plott3D trajectoryPlot;
	private Plott3D accelerometerPlot;
	private Plott3D gyroskopPlot;
	private SensorMonitor sensorMonitor;
	
	public MeasurementView(Punch punch){
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"MeasurementView.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Point3D> trace = punch.getTrace();
		trajectoryPlot = new Plott3D(trace);
		trajectory.setContent(trajectoryPlot);
		accelerometerPlot = new Plott3D(mtacc.transform(punch.getMeasurement()));
		accelerometer.setContent(accelerometerPlot);
		gyroskopPlot = new Plott3D(mtgyr.transform(punch.getMeasurement()));
		gyroskop.setContent(gyroskopPlot);
		sensorMonitor = new SensorMonitor(punch.getMeasurement());
		sensorvalues.setContent(sensorMonitor);


	}
	
	
	
	public Punch getPunch() {
		return punch;
	}

	


	@Override
	public String toString() {
		return punch.toString();
	}
}
