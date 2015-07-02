package de.luh.hci.pcl.boxhandschuh.protractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.luh.hci.pcl.boxhandschuh.io.PunchIO;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementTo3dTrajectory;

public class CompareProtactorScores {

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
			
			HashMap<String, HashMap<String, List<Double>>> resultTraj = new HashMap<>();
			/*
			for (String prefix : dataSets.keySet()) {
				resultTraj.put(prefix, new HashMap<String, List<Double>>());
			}
			*/
			for (String prefix : dataSets.keySet()) {
				HashMap<String, List<Double>> m = new HashMap<String, List<Double>>();
				for(String prefix2 : dataSets.keySet()){
					List<Double> l = new ArrayList<Double>();
					m.put(prefix2, l);
					
				}
				resultTraj.put(prefix, m);
//				resultTraj.put(prefix, new HashMap<String, List<Double>>());
			}
			
			
			int RUNS = 10;
			System.out.println("3d Trajectory");

			for (int j = 0; j < RUNS; j++) {
				HashMap<String, List<Punch>> data = copy(dataSets);
				//Map<String, List<Punch>> data = Collections.unmodifiableMap(dataSets);
				HashMap<String, List<Punch>> testData = new HashMap<String, List<Punch>>();
				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					List<Punch> punchesToBeTested = new ArrayList<Punch>();

					for (int i = 0; i < 4; i++) {
						p3D.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					for (int i = 0; i < 4; i++) {
						punchesToBeTested.add(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					
					testData.put(prefix, punchesToBeTested);
				}
				

				// test
				for (String prefix : testData.keySet()) {
//					HashMap<String, List<ExtendedMatch>> counting = new HashMap<String, List<ExtendedMatch>>();
					HashMap<String, List<Double>> counting = resultTraj.get(prefix);
					List<Punch> punches = testData.get(prefix);
					List<ExtendedMatch> matchScores = new ArrayList<ExtendedMatch>();
					MatchType r;
					//Score score;
					for (Punch punch : punches) {
						//comment1();
						Match m = p3D.recognizeByTrajectory(punch);
						List<Double> scores = new ArrayList<Double>();

						double score = m.score;
						try {
							scores = counting.get(m.template.getId());
							scores.add(score);
						} catch (Exception e) {
							System.err.println("Error while receiving the scores list and addding an element to the list");
						}
						counting.put(m.template.getId(), scores);

					}
				}
			}
			
			for (String prefix : resultTraj.keySet()) {
				System.out.println("Klasse: " + prefix);
				System.out.println("Erkannt:");
				HashMap<String, List<Double>> counting = resultTraj.get(prefix);
				for (String id : counting.keySet()) {
					System.out.print(id + ": ");
					List<Double> ld = counting.get(id);
					Collections.sort(ld);
					for(Double d : ld){
						System.out.printf("%.2f",d);
						System.out.print(", ");
					}
					System.out.println();
				}
			}
//			System.exit(1000);
			
			System.out.println("Acc");
			/*
			HashMap<String, HashMap<String, Integer>> resultAcc = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				resultAcc.put(prefix, new HashMap<>());
			}
			*/
			HashMap<String, HashMap<String, List<Double>>> resultAcc = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				HashMap<String, List<Double>> m = new HashMap<String, List<Double>>();
				for(String prefix2 : dataSets.keySet()){
					List<Double> l = new ArrayList<Double>();
					m.put(prefix2, l);
					
				}
				resultAcc.put(prefix, m);
//				resultTraj.put(prefix, new HashMap<String, List<Double>>());
			}
			
			

			for (int j = 0; j < RUNS; j++) {
				HashMap<String, List<Punch>> data = copy(dataSets);
				HashMap<String, List<Punch>> testData = new HashMap<String, List<Punch>>();

				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					List<Punch> punchesToBeTested = new ArrayList<Punch>();

					for (int i = 0; i < 4; i++) {
						p3D.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					for (int i = 0; i < 4; i++) {
						punchesToBeTested.add(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					testData.put(prefix, punchesToBeTested);
				}
				

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, List<Double>> counting = resultAcc.get(prefix);
					List<Punch> punches = data.get(prefix);
					for (Punch punch : punches) {
						Match m = p3D.recognizeByAccelerometer(punch);
						List<Double> scores = new ArrayList<Double>();

						double score = m.score;
						try {
							scores = counting.get(m.template.getId());
							scores.add(score);
						} catch (Exception e) {
							System.err.println("Error while receiving the scores list and addding an element to the list");
						}
						counting.put(m.template.getId(), scores);

					}
				}
			}
			
			for (String prefix : resultAcc.keySet()) {
				System.out.println("Klasse: " + prefix);
				System.out.println("Erkannt:");
				HashMap<String, List<Double>> counting = resultAcc.get(prefix);
				for (String id : counting.keySet()) {
					System.out.print(id + ": ");
					List<Double> ld = counting.get(id);
					Collections.sort(ld);
					for(Double d : ld){
						System.out.printf("%.2f",d);
						System.out.print(", ");
					}
					System.out.println();
				}
			}
			
			System.out.println("Gyr");
			HashMap<String, HashMap<String, List<Double>>> resultGyr = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				HashMap<String, List<Double>> m = new HashMap<String, List<Double>>();
				for(String prefix2 : dataSets.keySet()){
					List<Double> l = new ArrayList<Double>();
					m.put(prefix2, l);
					
				}
				resultGyr.put(prefix, m);
			}
			
			

			for (int j = 0; j < RUNS; j++) {
				HashMap<String, List<Punch>> data = copy(dataSets);
				HashMap<String, List<Punch>> testData = new HashMap<String, List<Punch>>();

				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					List<Punch> punchesToBeTested = new ArrayList<Punch>();

					for (int i = 0; i < 4; i++) {
						p3D.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					for (int i = 0; i < 4; i++) {
						punchesToBeTested.add(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					testData.put(prefix, punchesToBeTested);
				}
				

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, List<Double>> counting = resultGyr.get(prefix);
					List<Punch> punches = data.get(prefix);
					List<Double> scores = null;
					for (Punch punch : punches) {
						Match m = p3D.recognizeByGyroskop(punch);
						double score = m.score;
						scores = new ArrayList<Double>();

						try {
							scores = counting.get(m.template.getId());
							scores.add(score);
						} catch (Exception e) {
							System.err.println("Error while receiving the scores list and addding an element to the list");
						}
						counting.put(m.template.getId(), scores);

					}
				}
			}
			
			for (String prefix : resultGyr.keySet()) {
				System.out.println("Klasse: " + prefix);
				System.out.println("Erkannt:");
				HashMap<String, List<Double>> counting = resultAcc.get(prefix);
				for (String id : counting.keySet()) {
					System.out.print(id + ": ");
					List<Double> ld = counting.get(id);
					Collections.sort(ld);
					for(Double d : ld){
						System.out.printf("%.2f",d);
						System.out.print(", ");
					}
					System.out.println();
				}
			}
			
			System.out.println("DCA");
			HashMap<String, HashMap<String, List<Double>>> resultDCA = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				HashMap<String, List<Double>> m = new HashMap<String, List<Double>>();
				for(String prefix2 : dataSets.keySet()){
					List<Double> l = new ArrayList<Double>();
					m.put(prefix2, l);
					
				}
				resultDCA.put(prefix, m);
			}
			
			

			for (int j = 0; j < RUNS; j++) {
				HashMap<String, List<Punch>> data = copy(dataSets);
				HashMap<String, List<Punch>> testData = new HashMap<String, List<Punch>>();

				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					List<Punch> punchesToBeTested = new ArrayList<Punch>();

					for (int i = 0; i < 4; i++) {
						p3D.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					for (int i = 0; i < 4; i++) {
						punchesToBeTested.add(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
					testData.put(prefix, punchesToBeTested);
				}
				

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, List<Double>> counting = resultDCA.get(prefix);
					List<Punch> punches = data.get(prefix);
					List<Double> scores = null;

					for (Punch punch : punches) {
						List<String> m = p3D.recognizeByDCAExtended(punch, 1.0);
						double score = Double.valueOf(m.get(1));
						scores = new ArrayList<Double>();

						try {
							scores = counting.get(m.get(0));
							scores.add(score);
						} catch (Exception e) {
							System.err.println("Error while receiving the scores list and addding an element to the list");
						}
						counting.put(m.get(0), scores);

					}
				}
			}
			
			for (String prefix : resultDCA.keySet()) {
				System.out.println("Klasse: " + prefix);
				System.out.println("Erkannt:");
				HashMap<String, List<Double>> counting = resultDCA.get(prefix);
				for (String id : counting.keySet()) {
					System.out.print(id + ": ");
					List<Double> ld = counting.get(id);
					Collections.sort(ld);
					for(Double d : ld){
						System.out.printf("%.2f",d);
						System.out.print(", ");
					}
					System.out.println();
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

	private static void comment1() {
		// TODO Auto-generated method stub
		/*
		if(prefix.equals(m.template.getId())){
			r = MatchType.POSITIVE;
		}
		else {
			r = MatchType.NEGATIVE;
		}
		//Jakub
		//List<Match> matches = p3D.recognizeAllByTrajectory(punch);
		matchScores.add( new ExtendedMatch(m, r));
		*/
		
		/*
		double score = m.score;
		try {
			if(m.template.getId().equals(prefix)){
				score = Double.min(counting.get(m.template.getId()), m.score);
			}
			else {
				score = Double.max(counting.get(m.template.getId()), m.score);
			}
			//count = counting.get(m.template.getId());
		} catch (Exception e) {
			score = m.score;
		}
		
		counting.put(m.template.getId(), score);
		*/
		
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

	
}
