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
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public List<MeasurePoint> getMeasurement() {
		return measurement;
	}
	public void setMeasurement(List<MeasurePoint> measurement) {
		this.measurement = measurement;
	}
	
	
}
