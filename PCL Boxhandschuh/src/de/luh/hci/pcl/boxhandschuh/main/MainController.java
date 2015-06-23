package de.luh.hci.pcl.boxhandschuh.main;

import java.net.URL;
import java.util.ResourceBundle;

import de.luh.hci.pcl.boxhandschuh.view.Protractor3DView;
import de.luh.hci.pcl.boxhandschuh.view.SensorMonitor;
import de.luh.hci.pcl.boxhandschuh.view.Measurements;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class MainController implements Initializable{

	@FXML
	private Tab sensorMonitor, measurements, protractor3d;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sensorMonitor.setContent(new SensorMonitor());
		measurements.setContent(new Measurements());
		protractor3d.setContent(new Protractor3DView());
	}

}
