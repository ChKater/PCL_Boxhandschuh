package de.luh.hci.pcl.boxhandschuh.arduino;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import de.luh.hci.pcl.boxhandschuh.model.GestureListener;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePointListener;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.MeasurementListener;

public class ArduinoConnection implements SerialPortEventListener, Runnable, MeasurePointListener {

	private static List<MeasurementListener> measurementListener = new ArrayList<>();
	private static List<MeasurePointListener> measurePointListener = new ArrayList<>();

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
	



	private static ArduinoConnection instance;

	// Constants
	private static final String PORT = "/dev/cu.usbmodem1421";

	/** Milliseconds to block while waiting for port open */
	public static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	public static final int DATA_RATE = 115200;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac
																				// OS
																				// X
			"/dev/ttyUSB0", // Linux
			"/dev/ttyACM0", // Linux
			"COM4", // Windows
			"/dev/cu.usbmodem1411", PORT };

	public static void start() {
		initInstance();
		new Thread(instance).start();
	}

	private static void initInstance() {
		if (instance == null) {
			instance = new ArduinoConnection();
		}
	}

	public static void reset() {
		if (instance != null) {
			instance.close();
			instance = new ArduinoConnection();
		}
	}

	SerialPort serialPort;

	private BufferedReader input;
	private OutputStream output;

	private ArduinoConnection() {
		initialize();
		addMeasurePointListener(this);

	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	private synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	public void initialize() {
		// the next line is for Raspberry Pi and
		// gets us into the while loop and was suggested here was suggested
		// http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		// System.setProperty("gnu.io.rxtx.SerialPorts", PORT);

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
//			serialPort.addEventListener(this);
//			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	private double gx, gy, gz, ax, ay, az, fsr0, fsr1, fsr2, fsr3;
	private boolean measurementStarted = false;

	@Override
	public void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				 System.out.println(inputLine);

				if (measurementStarted) {
					if (inputLine.startsWith("ypr")) {

						String[] ypr = inputLine.split(";");
						gx = Double.parseDouble(ypr[1]);
						gy = Double.parseDouble(ypr[2]);
						gz = Double.parseDouble(ypr[3]);
					}
					if (inputLine.startsWith("aworld")) {
						String[] aworld = inputLine.split(";");
						ax = Double.parseDouble(aworld[1]);
						ay = Double.parseDouble(aworld[2]);
						az = Double.parseDouble(aworld[3]);
					}
					if (inputLine.startsWith("FSR")) {
						String[] fsr = inputLine.split(";");
						fsr0 = Double.parseDouble(fsr[2]);
						fsr1 = Double.parseDouble(fsr[4]);
						fsr2 = Double.parseDouble(fsr[6]);
						fsr3 = Double.parseDouble(fsr[8]);
					}

					if (measurementComplete()) {
						// System.out.println("New MeasurePoint");
						MeasurePoint mp = new MeasurePoint(new Date(), gx, gy,
								gz, ax, ay, az, fsr0, fsr1, fsr2, fsr3);
						for (MeasurePointListener listener : measurePointListener) {
							listener.OnMeasurePoint(mp);
						}
						measurementReset();
					}

				} else {
					if (inputLine.startsWith("euler")) {
						measurementStarted = true;
					}
				}

			} catch (Exception e) {
				System.err.println(e.toString());
				measurementReset();
			}
		}

	}

	private void measurementReset() {
		gx = Double.MIN_NORMAL;
		gy = Double.MIN_NORMAL;
		gz = Double.MIN_NORMAL;
		ax = Double.MIN_NORMAL;
		ay = Double.MIN_NORMAL;
		az = Double.MIN_NORMAL;
		fsr0 = Double.MIN_NORMAL;
		fsr1 = Double.MIN_NORMAL;
		fsr2 = Double.MIN_NORMAL;
		fsr3 = Double.MIN_NORMAL;
		measurementStarted = false;
	}

	private boolean measurementComplete() {
		return gx != Double.MIN_NORMAL && gy != Double.MIN_NORMAL
				&& gz != Double.MIN_NORMAL && ax != Double.MIN_NORMAL
				&& ay != Double.MIN_NORMAL && az != Double.MIN_NORMAL
				&& fsr0 != Double.MIN_NORMAL && fsr1 != Double.MIN_NORMAL
				&& fsr2 != Double.MIN_NORMAL && fsr3 != Double.MIN_NORMAL;
	}

	@Override
	public void run() {

		
		while (true) {
			try {
				String inputLine = null;
				while (!(inputLine = input.readLine()).startsWith("euler")) {
				}
				// skip Euler

				// ypr
				inputLine = input.readLine();
				String[] ypr = inputLine.split(";");
				double gx = Double.parseDouble(ypr[1]);
				double gy = Double.parseDouble(ypr[2]);
				double gz = Double.parseDouble(ypr[3]);

				//areal
				inputLine = input.readLine();
				//skip areal
				
				// aworld
				inputLine = input.readLine();
				String[] aworld = inputLine.split(";");
				double ax = Double.parseDouble(aworld[1]);
				double ay = Double.parseDouble(aworld[2]);
				double az = Double.parseDouble(aworld[3]);

				// fsr
				inputLine = input.readLine();
				String[] fsr = inputLine.split(";");
				double fsr0 = Double.parseDouble(fsr[2]);
				double fsr1 = Double.parseDouble(fsr[4]);
				double fsr2 = Double.parseDouble(fsr[6]);
				double fsr3 = Double.parseDouble(fsr[8]);

				MeasurePoint mp = new MeasurePoint(new Date(), gx, gy, gz, ax,
						ay, az, fsr0, fsr1, fsr2, fsr3);
				for (MeasurePointListener listener : measurePointListener) {
					listener.OnMeasurePoint(mp);
				}
			} catch (Exception e) {
				e.printStackTrace();
//				System.exit(1);
			}
		}

	}

	private List<MeasurePoint> history = new LinkedList<>();
	
	private final int BORDER = 200000;
	private final int MIN_MEASUREPOINTS = 30;
	private List<MeasurePoint> points = new ArrayList<>();
	private boolean collecting = false;
	@Override
	public void OnMeasurePoint(MeasurePoint measurePoint) {
		double sumFromHistory = getAccelerationSumFromHistory();
//		System.out.println(sumFromHistory);
		if(sumFromHistory > BORDER && !collecting){
			collecting = true;
			Iterator<MeasurePoint> iterator = history.iterator();
			while(iterator.hasNext()){
				MeasurePoint point = iterator.next();
				points.add(point);
			}
		} else if(sumFromHistory <= BORDER){
			collecting = false;
			if(points.size() >= MIN_MEASUREPOINTS){
		         System.out.println("size: " + points.size());

				Measurement m = new Measurement();
				m.setMeasurement(points);
				for (MeasurementListener listener : measurementListener) {
					listener.onMeasurement(m);
				}
				
			}
			points = new ArrayList<>();
		}
		history.add(measurePoint);
		if(history.size() >= 20){
		    history.remove(0);
		}
		if(collecting){
		    System.out.println(sumFromHistory);
			points.add(measurePoint);
		}
	}
	
	private double getAccelerationSumFromHistory(){
		double sum = 0.0;
		Iterator<MeasurePoint> iterator = history.iterator();
		while(iterator.hasNext()){
			MeasurePoint point = iterator.next();
			sum += Math.abs(point.getAx()) + Math.abs(point.getAy()) + Math.abs(point.getAz());
		}
		return sum;
	}

}
