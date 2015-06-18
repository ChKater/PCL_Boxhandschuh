package de.luh.hci.pcl.boxhandschuh.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Measurement {

	private Date start, end;
	private List<MeasurePoint> measurement;
	
	public Measurement(){
		this.measurement = new ArrayList<>(50);
	}
	
	public Date getStart() {
		if(measurement.size() == 0){
			return null;
		}
		return measurement.get(0).getDate();
	}
	
	public Date getEnd() {
		if(measurement.size() == 0){
			return null;
		}
		return measurement.get(measurement.size() - 1).getDate();
	}
	
	public List<MeasurePoint> getMeasurement() {
		return measurement;
	}
	public void setMeasurement(List<MeasurePoint> measurement) {
		this.measurement = measurement;
	}
	
	
}
