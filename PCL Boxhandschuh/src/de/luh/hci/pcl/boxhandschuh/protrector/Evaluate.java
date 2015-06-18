package de.luh.hci.pcl.boxhandschuh.protrector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;

public class Evaluate {

	public static void main(String[] args) {
		File dataDir = new File("data");
		HashMap<String, List<List<Point3D>>> dataSets = new HashMap<>();
		MeasurementTo3dTrajectory mt3dt = new MeasurementTo3dTrajectory();
		try {

			for (File file : dataDir.listFiles()) {
				if (file.isFile() && !file.getName().startsWith(".")) {
					String prefix = file.getName().split("_")[0];
					List<List<Point3D>> traceList = dataSets.get(prefix);
					if(traceList == null){
						traceList = new ArrayList<>();
						dataSets.put(prefix, traceList);
					}
					
					BufferedReader br = new BufferedReader(new FileReader(file));
					ArrayList<String> lines = new ArrayList<>();
					String line;
					while ((line = br.readLine()) != null) {
						lines.add(line);
					}
					if (lines.size() > 40) {
						// resample
						Measurement m = new Measurement();
						long now = new Date().getTime();
						m.setStart(new Date(now));

						for (int i = 1; i < lines.size(); i++) {
							String row = lines.get(i);
							String[] rowData = row.split(",");
							MeasurePoint p = new MeasurePoint(new Date(now),
									Double.parseDouble(rowData[5]),
									Double.parseDouble(rowData[6]),
									Double.parseDouble(rowData[7]),
									Double.parseDouble(rowData[2]),
									Double.parseDouble(rowData[3]),
									Double.parseDouble(rowData[4]));
							m.getMeasurement().add(p);
							now += 10;
						}
						traceList.add(mt3dt.measurementTo3DTrajectory(m));

					}
					br.close();
				}
			}
			Protrector3D p3D = new Protrector3D();
			Random rnd = new Random();
			//train
			for (String prefix : dataSets.keySet()) {
				List<List<Point3D>> traceList = dataSets.get(prefix);
				for (int i = 0; i < 5; i++) {
					p3D.addTemplate(prefix, traceList.remove(rnd.nextInt(traceList.size())));
				}
			}
			
			//test
			for (String prefix : dataSets.keySet()) {
				System.out.println("class: " + prefix);
				HashMap<String, Integer> counting = new HashMap<>();

				List<List<Point3D>> traceList = dataSets.get(prefix);
				for (List<Point3D> trace : traceList) {
					Match m = p3D.recognize(trace);
					Integer count = counting.get(m.template.getId());
					if(count == null){
						counting.put(m.template.getId(), 1);
					} else {
						counting.put(m.template.getId(), count + 1);
					}
				}
				System.out.println("recognized:");
				for (String id : counting.keySet()) {
					System.out.println(id + ": " + counting.get(id));
				}
			}
		

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
