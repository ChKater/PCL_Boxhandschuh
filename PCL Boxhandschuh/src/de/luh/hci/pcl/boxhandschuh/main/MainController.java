package de.luh.hci.pcl.boxhandschuh.main;

import java.net.URL;
import java.util.ResourceBundle;

import de.luh.hci.pcl.boxhandschuh.view.SensorMonitor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class MainController implements Initializable{

	@FXML
	private Tab sensorMonitor;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sensorMonitor.setContent(new SensorMonitor());
		
	}

}
