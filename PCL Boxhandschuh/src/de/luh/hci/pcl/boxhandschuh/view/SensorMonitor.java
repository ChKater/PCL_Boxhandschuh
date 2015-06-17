package de.luh.hci.pcl.boxhandschuh.view;

import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import de.luh.hci.pcl.boxhandschuh.arduino.FakeArduinoConnection;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.MeasurePointListener;

public class SensorMonitor extends GridPane implements MeasurePointListener{

	private static final double MAX_DATA_POINTS = 150;

	@FXML
	private LineChart<Number, Number> acc,gyr;
	
	private int x = 0;

	private NumberAxis xAxis;

	private NumberAxis yAxis;
	
	public SensorMonitor(){
		xAxis = new NumberAxis(0, MAX_DATA_POINTS,
				MAX_DATA_POINTS / 10);
		xAxis.setForceZeroInRange(false);
		xAxis.setAutoRanging(false);

		xAxis.setTickLabelsVisible(false);
		xAxis.setTickMarkVisible(false);
		xAxis.setMinorTickVisible(false);

		yAxis = new NumberAxis();
		yAxis.setAutoRanging(true);

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"SensorMonitor.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		acc.getData().add(new XYChart.Series("acc[x]", FXCollections.observableArrayList()));
		acc.getData().add(new XYChart.Series("acc[Y]", FXCollections.observableArrayList()));
		acc.getData().add(new XYChart.Series("acc[Z]", FXCollections.observableArrayList()));
		
		gyr.getData().add(new XYChart.Series("gyr[x]", FXCollections.observableArrayList()));
		gyr.getData().add(new XYChart.Series("gyr[y]", FXCollections.observableArrayList()));
		gyr.getData().add(new XYChart.Series("gyr[z]", FXCollections.observableArrayList()));
		FakeArduinoConnection.addMeasurePointListener(this);
		
	}

	@Override
	public void OnMeasurePoint(MeasurePoint measurePoint) {
		Platform.runLater(()->{
			System.out.println(x);
			acc.getData().get(0).getData().add(new XYChart.Data<>(x, measurePoint.getAx()));
			acc.getData().get(1).getData().add(new XYChart.Data<>(x, measurePoint.getAy()));
			acc.getData().get(2).getData().add(new XYChart.Data<>(x, measurePoint.getAz()));
			
			gyr.getData().get(0).getData().add(new XYChart.Data<>(x, measurePoint.getGx()));
			gyr.getData().get(1).getData().add(new XYChart.Data<>(x, measurePoint.getGy()));
			gyr.getData().get(2).getData().add(new XYChart.Data<>(x, measurePoint.getGz()));
			((NumberAxis) acc.getXAxis()).setLowerBound(x - MAX_DATA_POINTS);
			((NumberAxis) acc.getXAxis()).setUpperBound(x - 1);
		
			((NumberAxis) gyr.getXAxis()).setLowerBound(x - MAX_DATA_POINTS);
			((NumberAxis) gyr.getXAxis()).setUpperBound(x - 1);
		
			
			x++;
		});
		
	}
}
