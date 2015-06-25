package de.luh.hci.pcl.boxhandschuh.view;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.arduino.FakeArduinoConnection;
import de.luh.hci.pcl.boxhandschuh.io.PunchIO;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePointListener;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementTo3dTrajectory;

public class Measurements extends SplitPane implements MeasurePointListener {

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
	
	private Map<Punch, MeasurementView> punchViews = new HashMap<>();

	private ObservableList<Punch> punches = FXCollections.observableArrayList();
	private MeasurementTo3dTrajectory mto3dt = new MeasurementTo3dTrajectory();

	private File dataDir = new File("punch-data");
	
	public Measurements() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Measurements.fxml"));
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
					MeasurementView punchview = punchViews.get(newValue);
					if(punchview == null){
						punchview = new MeasurementView(newValue);
						punchViews.put(newValue, punchview);
					}
					chartPane.getChildren().clear();
					chartPane.add(punchview,0,0);
				});
		readData();
		FakeArduinoConnection.addMeasurePointListener(this);

	}

	private void readData() {
		
		for (File file : dataDir.listFiles()) {
			if (file.isFile() && !file.getName().startsWith(".")) {
				Punch punch = PunchIO.readPunch(file);
				punches.add(punch);
			}
		}

	}
	
	

	@FXML
	public void measure() {

		if (measurementRunning) {
			Punch punch = new Punch(m, mto3dt.transform(m),
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

	
}
