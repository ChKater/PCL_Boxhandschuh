package de.luh.hci.pcl.boxhandschuh.view;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.dtw.DTW;
import de.luh.hci.pcl.boxhandschuh.dtw.DTWMatch;
import de.luh.hci.pcl.boxhandschuh.io.PunchIO;
import de.luh.hci.pcl.boxhandschuh.model.GestureRow;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.protractor.Match;
import de.luh.hci.pcl.boxhandschuh.protractor.Protractor3D;

public class Gesture extends GridPane {

	private static DTW dtw;
	private static Protractor3D p3d;
	
	@FXML
	private TableView<GestureRow> gestureResults;

	private static void init() {
		if (dtw == null || p3d == null) {
			dtw = new DTW();
			p3d = new Protractor3D();
			for (File file : new File("training-data").listFiles()) {
				if (file.isFile() && !file.getName().startsWith(".")) {
					Punch punch = PunchIO.readPunch(file);
					dtw.addTemplate(punch);
					p3d.addTemplate(punch);
				}
			}
		}
	}

	public Gesture(Punch punch) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"Gesture.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		DTWMatch mDTW = null;
		mDTW = dtw.recognizeByAccelerometer(punch);
		gestureResults.getItems().add(new GestureRow("Fast-DTW", "Accelerometer", mDTW.getTemplate().getClassname(), punch.getClassName(), mDTW.getScore()));
		mDTW = dtw.recognizeByGyroskop(punch);
		gestureResults.getItems().add(new GestureRow("Fast-DTW", "Gyroskop", mDTW.getTemplate().getClassname(), punch.getClassName(), mDTW.getScore()));
		mDTW = dtw.recognizeByAccGYrCombined(punch);
		gestureResults.getItems().add(new GestureRow("Fast-DTW", "Accelerometer + Gyroskop", mDTW.getTemplate().getClassname(), punch.getClassName(), mDTW.getScore()));
		mDTW = dtw.recognizeByTrajectory(punch);
		gestureResults.getItems().add(new GestureRow("Fast-DTW", "Trajectory", mDTW.getTemplate().getClassname(), punch.getClassName(), mDTW.getScore()));
		
		Match mP3D= null;
		mP3D = p3d.recognizeByAccelerometer(punch);
		gestureResults.getItems().add(new GestureRow("Protractor3D", "Accelerometer", mP3D.template.getId(), punch.getClassName(), mP3D.score));
		mP3D = p3d.recognizeByGyroskop(punch);
		gestureResults.getItems().add(new GestureRow("Protractor3D", "Gyroskop", mP3D.template.getId(), punch.getClassName(), mP3D.score));
		mP3D = p3d.recognizeByTrajectory(punch);
		gestureResults.getItems().add(new GestureRow("Protractor3D", "Trajectory", mP3D.template.getId(), punch.getClassName(), mP3D.score));
		
		gestureResults.getItems().add(new GestureRow("Protractor3D", "DCA", p3d.recognizeByDCA(punch, 0.8), punch.getClassName(), -1));



	}
}
