package de.luh.hci.pcl.boxhandschuh.transformation;

import java.util.ArrayList;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.arduino.ArduinoConnection;
import de.luh.hci.pcl.boxhandschuh.dtw.DTW;
import de.luh.hci.pcl.boxhandschuh.dtw.DTWMatch;
import de.luh.hci.pcl.boxhandschuh.model.Gesture;
import de.luh.hci.pcl.boxhandschuh.model.GestureListener;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.MeasurementListener;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.model.User;

public class MeasurementToGestureFactory implements MeasurementListener{
	
	private static final MeasurementToMaxForce mtmf = new MeasurementToMaxForce();
	private static final MeasurementToMaxSpeed mtms = new MeasurementToMaxSpeed();
	
	private List<GestureListener> listeners = new ArrayList<>();
	private DTW dtw = new DTW();

	public MeasurementToGestureFactory(User user) {
		super();
		ArduinoConnection.addMeasurementListener(this);
		for (Punch punch : user.getTrainingData()) {
			dtw.addTemplate(punch);
		}
	}
	
	public void addListener(GestureListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(GestureListener listener){
		listeners.remove(listener);
	}

	@Override
	public void onMeasurement(Measurement measurement) {
		DTWMatch match = dtw.recognizeByAccelerometer(new Punch(measurement));
		double maxForce = mtmf.transform(measurement);
		double maxSpeed = mtms.transform(measurement);
		for (GestureListener gestureListener : listeners) {
			gestureListener.onGesture(Gesture.getByName(match.getTemplate().getClassname()), match.getScore(), maxSpeed, maxForce);
		}
	}
	
	public void disconnect(){
		ArduinoConnection.removeMeasurementListener(this);
	}
	
	

}
