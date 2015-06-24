package de.luh.hci.pcl.boxhandschuh.model;

import java.text.SimpleDateFormat;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;

public class Punch {

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss:S");	
	private Measurement measurement;
	private List<Point3D> trace;
	private String className;
	private String person;

	public Measurement getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public List<Point3D> getTrace() {
		return trace;
	}

	public void setTrace(List<Point3D> trace) {
		this.trace = trace;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public Punch(Measurement measurement, List<Point3D> trace,
			String className, String person) {
		super();
		this.measurement = measurement;
		this.trace = trace;
		this.className = className;
		this.person = person;
	}
	
	@Override
	public String toString(){
		return className + " [person= " + person + ", time=" + sdf.format(measurement.getStart()) + " , length=" + measurement.getMeasurement().size();
	}

	 
}
