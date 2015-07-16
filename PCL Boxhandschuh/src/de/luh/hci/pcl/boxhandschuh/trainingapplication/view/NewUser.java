package de.luh.hci.pcl.boxhandschuh.trainingapplication.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
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
			count.setText(String.valueOf(currentMeasurement));
            ArduinoConnection.addMeasurementListener(this);
            System.out.println("Connected");

		} else {
			calibrationRunning = !calibrationRunning;
			if (calibrationRunning) {
				ArduinoConnection.addMeasurementListener(this);
				System.out.println("Connected");
				calibration.setText("Kalibrierung pausieren");
			} else {
				calibration.setText("Kalibrierung fortsetzen");
				ArduinoConnection.removeMeasurementListener(this);
                System.out.println("Disconnected");

			}
		}
	}

	@FXML
	public void cancel() {
		main.showMenu();
	}

	@Override
	public void onMeasurement(Measurement measurement) {
	    System.out.println(measurement);
		Punch p = new Punch(measurement);
		try {
            p.setClassName(gesturesToCalibrate[currentGesture].getName());
        } catch (Exception e) {
           return;
        }
		templates.add(p);
		currentMeasurement++;
		if (currentMeasurement >= NUMBER_OF_TEMPLATES_PER_GESTURE) {
			currentMeasurement = 0;
			currentGesture++;
		}
		Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                NewUser.this.classname.setText(gesturesToCalibrate[currentGesture].getName());
                NewUser.this.count.setText(String.valueOf(currentMeasurement));                
            }
        });
		
		if (currentGesture == gesturesToCalibrate.length) {
		    Platform.runLater(new Runnable() {
	            
	            @Override
	            public void run() {
	                NewUser.this.calibration.setDisable(true);
	                NewUser.this.calibration.setText("Kalibrierung Starten");
	                NewUser.this.classname.setDisable(true);
	                NewUser.this.count.setDisable(true);
	                NewUser.this.calibrationFinished.setVisible(true);
	                calibrationComplete.set(true);                          
	            }
	        });
		    

		}
	}
	
	public String toString() {
	    return "NewUser";
	};
}
