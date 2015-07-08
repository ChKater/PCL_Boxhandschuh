package de.luh.hci.pcl.boxhandschuh.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.dtw.DTW;
import de.luh.hci.pcl.boxhandschuh.dtw.DTWMatch;
import de.luh.hci.pcl.boxhandschuh.io.PunchIO;
import de.luh.hci.pcl.boxhandschuh.model.EvaluationRow;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.protractor.Match;
import de.luh.hci.pcl.boxhandschuh.protractor.Protractor3D;import de.luh.hci.pcl.boxhandschuh.protractor.Template;


public class Evaluation extends GridPane {

	@FXML
	private TableView<EvaluationRow> result;

	@FXML
	private TextField numberOfTrainingTemplates, runs;

	private HashMap<String, List<Punch>> dataSets;
	private List<Function<Punch, DTWMatch>> toEvaluateDTW;
	private List<String> toEvaluateSensorsDTW;
	private List<Function<Punch, Match>> toEvaluateProtractor3D;
	private List<String> toEvaluateProtractor3DSensors;

	private DTW dtw;
	private Protractor3D p3d = Protractor3D.getInstance();

	public Evaluation() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Evaluation.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		toEvaluateDTW = new ArrayList<>();
		toEvaluateSensorsDTW = new ArrayList<>();
		toEvaluateSensorsDTW.add("Accelerometer");
		toEvaluateDTW.add(punch -> {
			return dtw.recognizeByAccelerometer(punch);
		});
		toEvaluateSensorsDTW.add("Gyroskop");
		toEvaluateDTW.add(punch -> {
			return dtw.recognizeByGyroskop(punch);
		});
		toEvaluateSensorsDTW.add("Accelerometer + Gyroskop");
		toEvaluateDTW.add(punch -> {
			return dtw.recognizeByAccGYrCombined(punch);
		});
		toEvaluateSensorsDTW.add("Trajectory");
		toEvaluateDTW.add(punch -> {
			return dtw.recognizeByTrajectory(punch);
		});
		
		toEvaluateProtractor3D = new ArrayList<>();
		toEvaluateProtractor3DSensors = new ArrayList<>();
		toEvaluateProtractor3DSensors.add("Accelerometer");
		toEvaluateProtractor3D.add(punch -> {
			return p3d.recognizeByAccelerometer(punch);
		});
		toEvaluateProtractor3DSensors.add("Gyroskop");
		toEvaluateProtractor3D.add(punch -> {
			return p3d.recognizeByGyroskop(punch);
		});
		
		toEvaluateProtractor3DSensors.add("Trajectory");
		toEvaluateProtractor3D.add(punch -> {
			return p3d.recognizeByTrajectory(punch);
		});
		
		toEvaluateProtractor3DSensors.add("DCA");
		toEvaluateProtractor3D.add(punch -> {
			Match m = new Match(0, null);
			m.template = new Template(p3d.recognizeByDCA(punch, 0.8));
			return m;
		});
	}

	public void evaluate() {
		result.getItems().clear();
		Random rnd = new Random();
		loadData();
		int RUNS = Integer.parseInt(runs.getText());
		int NUMBER_OF_TRAINING_TEMPLATES = Integer
				.parseInt(numberOfTrainingTemplates.getText());

		for (int i = 0; i < toEvaluateDTW.size(); i++) {
			Function<Punch, DTWMatch> function = toEvaluateDTW.get(i);
			HashMap<String, HashMap<String, Integer>> results = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				results.put(prefix, new HashMap<>());
			}
			for (int j = 0; j < RUNS; j++) {
				dtw = new DTW();
				HashMap<String, List<Punch>> data = copy(dataSets);
				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					for (int k = 0; k < NUMBER_OF_TRAINING_TEMPLATES; k++) {
						dtw.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
				}

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, Integer> counting = results.get(prefix);
					List<Punch> punches = data.get(prefix);
					for (Punch punch : punches) {
						DTWMatch m = function.apply(punch);
						int count = 0;
						try {
							count = counting
									.get(m.getTemplate().getClassname());
						} catch (Exception e) {
						}
						counting.put(m.getTemplate().getClassname(), count + 1);

					}
				}
			}

			for (String classname : results.keySet()) {
				result.getItems().add(
						new EvaluationRow("Fast-DTW", toEvaluateSensorsDTW.get(i),
								classname, results.get(classname)));
			}
		}
		
		for (int i = 0; i < toEvaluateProtractor3D.size(); i++) {
			Function<Punch, Match> function = toEvaluateProtractor3D.get(i);
			HashMap<String, HashMap<String, Integer>> results = new HashMap<>();
			for (String prefix : dataSets.keySet()) {
				results.put(prefix, new HashMap<>());
			}
			for (int j = 0; j < RUNS; j++) {
				p3d.clear();
				HashMap<String, List<Punch>> data = copy(dataSets);
				// train
				for (String prefix : data.keySet()) {
					List<Punch> traceList = data.get(prefix);
					for (int k = 0; k < NUMBER_OF_TRAINING_TEMPLATES; k++) {
						p3d.addTemplate(traceList.remove(rnd.nextInt(traceList
								.size())));
					}
				}

				// test
				for (String prefix : data.keySet()) {
					HashMap<String, Integer> counting = results.get(prefix);
					List<Punch> punches = data.get(prefix);
					for (Punch punch : punches) {
						Match m = function.apply(punch);
						int count = 0;
						try {
							count = counting
									.get(m.template.getId());
						} catch (Exception e) {
						}
						counting.put(m.template.getId(), count + 1);

					}
				}
			}

			for (String classname : results.keySet()) {
				result.getItems().add(
						new EvaluationRow("Protractor3D", toEvaluateProtractor3DSensors.get(i),
								classname, results.get(classname)));
			}
		}
	}

	private void loadData() {
		File dataDir = new File("punch-data");
		dataSets = new HashMap<>();
		for (File file : dataDir.listFiles()) {
			if (file.isFile() && !file.getName().startsWith(".")) {
				String prefix = file.getName().split("_")[0];

				List<Punch> punches = dataSets.get(prefix);
				if (punches == null) {
					punches = new ArrayList<>();
					dataSets.put(prefix, punches);
				}

				Punch punch = PunchIO.readPunch(file);
				punches.add(punch);
			} else {
				System.out.println(file.getName());
			}
		}
		result.getColumns().get(3).getColumns().clear();
		for (String classname : dataSets.keySet()) {
			TableColumn<EvaluationRow, Integer> column = new TableColumn<>(
					classname);
			column.setText(classname);
			column.setSortable(true);
			column.setPrefWidth(75);
			column.setCellValueFactory(p -> {
				return new ReadOnlyObjectWrapper<Integer>(p.getValue()
						.getRecognized(classname));
			});
			result.getColumns().get(3).getColumns().add(column);
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

}
