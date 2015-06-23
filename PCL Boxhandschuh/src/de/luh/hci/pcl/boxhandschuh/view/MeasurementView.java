package de.luh.hci.pcl.boxhandschuh.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.jzy3d.colors.Color;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.protractor.Match;
import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;
import de.luh.hci.pcl.boxhandschuh.protractor.Protractor3D;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementTo3dTrajectory;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementToAccelerometerTrace;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementToGyroskopTrace;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementToTrace;

public class MeasurementView extends TabPane{

	private static MeasurementToTrace mt3dt = new MeasurementTo3dTrajectory();
	private static MeasurementToTrace mtacc = new MeasurementToAccelerometerTrace();
	private static MeasurementToTrace mtgyr = new MeasurementToGyroskopTrace();
	
	@FXML
	private Tab trajectory, accelerometer,gyroskop,sensorvalues, protractor3d;
	
	private Punch punch;
	private Plott3D trajectoryPlot;
	private Plott3D accelerometerPlot;
	private Plott3D gyroskopPlot;
	private Plott3D protractor3dPlot;
	private SensorMonitor sensorMonitor;
	private Protractor3D protractor = Protractor3D.getInstance();
	
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
		Match m = protractor.recognize(trace);
		List<Point3D> prepared = protractor.prepareTrace(trace);
		
		List<Point3D> rotatedTrace = new ArrayList<Point3D>();
		for (int i = 0; i < prepared.size(); i++) {
			Point3D gp = prepared.get(i);
		
			RealMatrix gv = MatrixUtils.createRealMatrix(new double[][] {
					{ gp.x }, { gp.y }, { gp.z } });
			RealVector col = m.r.multiply(gv).getColumnVector(0);
			rotatedTrace.add(new Point3D(col.getEntry(0), col.getEntry(1), col.getEntry(2)));
		}
		protractor3dPlot = new Plott3D(prepared);
		protractor3dPlot.addPlott(m.template.getTrace(), Color.BLUE);
		
		protractor3d.setContent(protractor3dPlot);

	}
	
	
	
	public Punch getPunch() {
		return punch;
	}

	


	@Override
	public String toString() {
		return punch.toString();
	}
}
