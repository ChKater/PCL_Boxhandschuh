package arduino;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;

import javafx.scene.chart.Chart;

import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;

import application.Measurements;

public class ArduinoConnection implements Runnable {

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
            "COM4", // Windows
            "/dev/cu.usbmodem1411" };

    private static final int BYTES_PER_MEASUREMENT = 8;

    private Measurements measure;
    public static ArduinoConnection getInstance() {
        initInstance();
        return instance;
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
        boolean header = false;
        int prev = 0;
        int current = 0;
        
        
        
        
        //Chart chart = new Chart();
        //chart.getAxeLayout().setMainColor(Color.WHITE);
        //chart.getView().setBackgroundColor(Color.BLACK);
        //chart.getScene().add(scatter);
        //ChartLauncher.openChart(chart);
        
        while (true) {
            try {

                // if (!header && input.available() > 0
                // && (current = input.read()) == 255 && prev == 255) {
                // header = true;
                // } else if (header && input.available() >=
                // BYTES_PER_MEASUREMENT) {
                // prev = 0;
                // current = 0;
                //
                // // 2 Byte number to int
                // int low = input.read() & 0xff;
                // int high = (input.read() & 0xff) << 8;
                // int value = low + high;
                // }
                // prev = current;

                String inputLine = reader.readLine();
               measure.sendData(inputLine);

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

    }

    public static void main(String[] args) {
        ArduinoConnection.getInstance();
    }

    public Measurements getMeasure() {
        return measure;
    }

    public void setMeasure(Measurements measure) {
        this.measure = measure;
    }
}
