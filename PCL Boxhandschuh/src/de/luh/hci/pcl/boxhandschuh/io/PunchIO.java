package de.luh.hci.pcl.boxhandschuh.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.protrector.MeasurementTo3dTrajectory;

public class PunchIO {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss:SSS");
	
	private static MeasurementTo3dTrajectory mt3dt = new MeasurementTo3dTrajectory();

	public static void savePunch(Punch punch, File file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
			bw.write("Position,Time,ACCX,ACCY,ACCZ,GyrY,GyrP,GyrR,Class,Person\n");
			for (int i = 0; i < punch.getMeasurement().getMeasurement().size(); i++) {
				MeasurePoint p = punch.getMeasurement().getMeasurement().get(i);
				bw.write(i + "," + sdf.format(p.getDate()) + "," + p.getAx()
						+ "," + p.getAy() + "," + p.getAz() + "," + p.getGx()
						+ "," + p.getGy() + "," + p.getGz() + ","
						+ punch.getClassName() + "," + punch.getPerson() + "\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Punch readPunch(File file) {
		Measurement m = new Measurement();
		String person = null;
		String className = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = br.readLine()) != null){
				String[] values = line.split(",");
				if(values[0].equals("Position")){
					MeasurePoint p = new MeasurePoint(sdf.parse(values[1]),
							Double.parseDouble(values[5]),
							Double.parseDouble(values[6]),
							Double.parseDouble(values[7]),
							Double.parseDouble(values[2]),
							Double.parseDouble(values[3]),
							Double.parseDouble(values[4]));
					m.getMeasurement().add(p);
					person = values[9];
					className = values[8];
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Punch(m, mt3dt.measurementTo3DTrajectory(m), className, person);
	}

	public static void savePunch(Punch punch) {
		savePunch(
				punch,
				new File("punch-data/" + punch.getClassName() + "_"
						+ punch.getPerson() + "_"
						+ sdf.format(punch.getMeasurement().getStart()) + ".csv"));

	}
}
