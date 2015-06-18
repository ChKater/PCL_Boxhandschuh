package de.luh.hci.pcl.boxhandschuh.arduino;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePointListener;
import de.luh.hci.pcl.boxhandschuh.model.MeasurementListener;

public class ArduinoConnection implements Runnable {

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
	
	private static ArduinoConnection instance;

    // Constants

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
            "/dev/cu.usbmodem1411" };

    private static final int BYTES_PER_MEASUREMENT = 8;

    public static void start() {
        initInstance();
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

    private InputStream input;

    private ArduinoConnection() {
        initialize();
        //new Thread(this, "ArduinoConnection").start();

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

        CommPortIdentifier portId = null;
        @SuppressWarnings("rawtypes")
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
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
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            input = serialPort.getInputStream();

        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }
    
    public boolean run = true;

    @Override
    public void run() {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
    	while(true){
    		try {
				String input = null;
				while((input = reader.readLine()).startsWith("euler")){}
				//skip Euler
				
				//ypr
				input = reader.readLine();
				String[] ypr = input.split(";");
				double gx = Double.parseDouble(ypr[1]);
				double gy = Double.parseDouble(ypr[2]);
				double gz = Double.parseDouble(ypr[3]);
				
				//aworld
				input = reader.readLine();
				String[] aworld = input.split(";");
				double ax = Double.parseDouble(aworld[1]);
				double ay = Double.parseDouble(aworld[2]);
				double az = Double.parseDouble(aworld[3]);
				
				//fsr
				input = reader.readLine();
				String[] fsr = input.split(";");
				double fsr0 = Double.parseDouble(fsr[2]);
				double fsr1 = Double.parseDouble(fsr[4]);
				double fsr2 = Double.parseDouble(fsr[6]);
				double fsr3 = Double.parseDouble(fsr[8]);
				
				MeasurePoint mp = new MeasurePoint(new Date(), gx, gy, gz, ax, ay, az, fsr0, fsr1, fsr2, fsr3);
				for (MeasurePointListener listener : measurePointListener) {
					listener.OnMeasurePoint(mp);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    }

   
}