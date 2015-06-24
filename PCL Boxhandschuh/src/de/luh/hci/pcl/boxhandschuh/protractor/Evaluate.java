package de.luh.hci.pcl.boxhandschuh.protractor;

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

import de.luh.hci.pcl.boxhandschuh.io.PunchIO;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementTo3dTrajectory;

public class Evaluate {

	public static void main(String[] args) {
		File dataDir = new File("protractor-evaluate");
		HashMap<String, List<Punch>> dataSets = new HashMap<>();
		MeasurementTo3dTrajectory mt3dt = new MeasurementTo3dTrajectory();
		try {

			for (File file : dataDir.listFiles()) {
				if (file.isFile() && !file.getName().startsWith(".")) {
					String prefix = file.getName().split("_")[0];

					List<Punch> punches = dataSets.get(prefix);
					if (punches == null) {
						punches = new ArrayList<>();
						dataSets.put(prefix, punches);
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
						Punch punch = new Punch(m, mt3dt.transform(m), prefix,
								"Guido");
//						PunchIO.savePunch(punch);
						punches.add(punch);
					} else {
						System.out.println(file.getName());
					}
					br.close();
				}
			}
			Protractor3D p3D = Protractor3D.getInstance();
			Random rnd = new Random();
			
			HashMap<String, HashMap<String, Integer>> resultTraj = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				resultTraj.put(prefix, new HashMap<>());
			}
			
			int RUNS = 10;
			System.out.println("3d Trajectory");

			for (int j = 0; j < RUNS; j++) {
				HashMap<String, List<Punch>> data = copy(dataSets);
				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					for (int i = 0; i < 4; i++) {
						p3D.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
				}
				

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, Integer> counting = resultTraj.get(prefix);
					List<Punch> punches = data.get(prefix);
					for (Punch punch : punches) {
						Match m = p3D.recognizeByTrajectory(punch);
						int count = 0;
						try {
							count = counting.get(m.template.getId());
						} catch (Exception e) {
						}
						counting.put(m.template.getId(), count + 1);

					}
				}
			}
			
			for (String prefix : resultTraj.keySet()) {
				System.out.println("Klasse: " + prefix);
				System.out.println("Erkannt:");
				HashMap<String, Integer> counting = resultTraj.get(prefix);
				for (String id : counting.keySet()) {
					System.out.println(id + ": " + counting.get(id));
				}
			}
			
			System.out.println("Acc");
			HashMap<String, HashMap<String, Integer>> resultAcc = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				resultAcc.put(prefix, new HashMap<>());
			}
			
			

			for (int j = 0; j < RUNS; j++) {
				HashMap<String, List<Punch>> data = copy(dataSets);
				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					for (int i = 0; i < 4; i++) {
						p3D.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
				}
				

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, Integer> counting = resultAcc.get(prefix);
					List<Punch> punches = data.get(prefix);
					for (Punch punch : punches) {
						Match m = p3D.recognizeByAccelerometer(punch);
						int count = 0;
						try {
							count = counting.get(m.template.getId());
						} catch (Exception e) {
						}
						counting.put(m.template.getId(), count + 1);

					}
				}
			}
			
			for (String prefix : resultAcc.keySet()) {
				System.out.println("Klasse: " + prefix);
				System.out.println("Erkannt:");
				HashMap<String, Integer> counting = resultAcc.get(prefix);
				for (String id : counting.keySet()) {
					System.out.println(id + ": " + counting.get(id));
				}
			}
			
			System.out.println("DCA");
			HashMap<String, HashMap<String, Integer>> resultDCA = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				resultDCA.put(prefix, new HashMap<>());
			}
			
			

			for (int j = 0; j < RUNS; j++) {
				HashMap<String, List<Punch>> data = copy(dataSets);
				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					for (int i = 0; i < 4; i++) {
						p3D.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
				}
				

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, Integer> counting = resultDCA.get(prefix);
					List<Punch> punches = data.get(prefix);
					for (Punch punch : punches) {
						String m = p3D.recognizeByDCA(punch, 0.0);
						int count = 0;
						try {
							count = counting.get(m);
						} catch (Exception e) {
						}
						counting.put(m, count + 1);

					}
				}
			}
			
			for (String prefix : resultDCA.keySet()) {
				System.out.println("Klasse: " + prefix);
				System.out.println("Erkannt:");
				HashMap<String, Integer> counting = resultDCA.get(prefix);
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

	private static HashMap<String, List<Punch>> copy(
			HashMap<String, List<Punch>> dataSets) {
		HashMap<String, List<Punch>> copy = new HashMap<>();
		for (String id : dataSets.keySet()) {
			List<Punch> punches = dataSets.get(id);
			copy.put(id, new ArrayList<>(punches));
		}
		return copy;
	}

	public static List<Point3D> transformToPoint3d(Measurement m) {
		List<Point3D> trace = new ArrayList<>();
		for (int i = 0; i < m.getMeasurement().size(); i++) {
			MeasurePoint p = m.getMeasurement().get(i);
			trace.add(new Point3D(p.getAx(), p.getAy(), p.getAz()));
		}
		return trace;
	}
}
