package de.luh.hci.pcl.boxhandschuh.arduino;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePointListener;
import de.luh.hci.pcl.boxhandschuh.model.MeasurementListener;

public class FakeArduinoConnection {

	private static Set<MeasurementListener> measurementListener = new HashSet<>();
	private static Set<MeasurePointListener> measurePointListener = new HashSet<>();

	public static void addMeasurementListener(MeasurementListener listener) {
		measurementListener.add(listener);
	}

	public static void addMeasurePointListener(MeasurePointListener listener) {
		measurePointListener.add(listener);
	}

	public static void removeMeasurementListener(MeasurementListener listener) {
		measurementListener.remove(listener);
	}

	public static void removeMeasurePointListener(MeasurePointListener listener) {
		measurePointListener.remove(listener);
	}

	public static void start() {
		new Thread(() -> {
			Random rnd = new Random();

			while(true){
				MeasurePoint m = new MeasurePoint(new Date(), rnd.nextInt(512), rnd.nextInt(512), rnd.nextInt(512), rnd.nextInt(512),rnd.nextInt(512),rnd.nextInt(512));
				for (MeasurePointListener listener : measurePointListener) {
					listener.OnMeasurePoint(m);
				}
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
	}
}
