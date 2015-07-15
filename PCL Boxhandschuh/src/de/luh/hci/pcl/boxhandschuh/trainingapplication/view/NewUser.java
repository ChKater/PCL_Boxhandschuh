package de.luh.hci.pcl.boxhandschuh.trainingapplication.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.arduino.ArduinoConnection;
import de.luh.hci.pcl.boxhandschuh.controller.UserController;
import de.luh.hci.pcl.boxhandschuh.model.Gesture;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.MeasurementListener;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.model.User;

public class NewUser extends GridPane implements MeasurementListener {

	public static final Gesture[] gesturesToCalibrate = new Gesture[] {
			Gesture.PUNCH, Gesture.UPPERCUT, Gesture.HOOK };

	public static final int NUMBER_OF_TEMPLATES_PER_GESTURE = 4;

	@FXML
	private Button calibration, addUser;

	private BooleanProperty calibrationComplete = new SimpleBooleanProperty(
			false);

	private boolean calibrationRunning = false;
	private boolean calibrationStarted = false;

	@FXML
	private Label classname, count, calibrationFinished;
	int currentGesture = 0;

	int currentMeasurement = 0;
	private Main main;

	private List<Punch> templates = new ArrayList<>();

	@FXML
	private TextField username;

	public NewUser(Main main) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"NewUser.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.main = main;
		this.calibrationFinished.setVisible(false);
		addUser.disableProperty().bind(Bindings.createBooleanBinding(() -> {
			return !calibrationComplete.get() || username.getText().trim().length() == 0;
		}, username.textProperty(), calibrationComplete));
	}

	@FXML
	public void addUser() {
		UserController c = new UserController();
		c.login(username.getText());
		User user = c.getActiveUser();
		user.getTrainingData().addAll(templates);
		c.saveUser();
		main.showMenu();
	}

	@FXML
	public void calibration() {
		if (!calibrationStarted) {
			calibrationStarted = true;
			calibrationRunning = true;
			calibration.setText("Kalibrierung pausieren");
			classname.setText(gesturesToCalibrate[currentGesture].getName());
			count.setText(String.valueOf(currentMeasurement + 1));

		} else {
			calibrationRunning = !calibrationRunning;
			if (calibrationRunning) {
				ArduinoConnection.addMeasurementListener(this);
				calibration.setText("Kalibrierung pausieren");
			} else {
				calibration.setText("Kalibrierung fortsetzen");
				ArduinoConnection.removeMeasurementListener(this);
			}
		}
	}

	@FXML
	public void cancel() {
		main.showMenu();
	}

	@Override
	public void onMeasurement(Measurement measurement) {
		Punch p = new Punch(measurement);
		p.setClassName(gesturesToCalibrate[currentMeasurement].getName());
		templates.add(p);
		currentMeasurement++;
		if (currentMeasurement >= NUMBER_OF_TEMPLATES_PER_GESTURE) {
			currentMeasurement = 0;
			currentGesture++;
		}
		if (currentGesture == gesturesToCalibrate.length) {
			this.calibration.setDisable(true);
			this.calibration.setText("Kalibrierung Starten");
			this.classname.setDisable(true);
			this.count.setDisable(true);
			this.calibrationFinished.setVisible(true);
			calibrationComplete.set(true);

		}
	}
}
