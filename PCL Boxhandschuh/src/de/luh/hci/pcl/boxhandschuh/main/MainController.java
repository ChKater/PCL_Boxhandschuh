package de.luh.hci.pcl.boxhandschuh.main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import de.luh.hci.pcl.boxhandschuh.view.Evaluation;
import de.luh.hci.pcl.boxhandschuh.view.Measurements;
import de.luh.hci.pcl.boxhandschuh.view.SensorMonitor;

public class MainController implements Initializable{

	@FXML
	private Tab sensorMonitor, measurements, evaluation;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sensorMonitor.setContent(new SensorMonitor());
		measurements.setContent(new Measurements());	
		evaluation.setContent(new Evaluation());
	}

}
