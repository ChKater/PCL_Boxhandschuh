package de.luh.hci.pcl.boxhandschuh.model;

public class GestureRow {

	private String method, sensors, isClassname, shouldClassname;
	private double score;

	public GestureRow(String method, String sensors, String isClassname,
			String shouldClassname, double score) {
		super();
		this.method = method;
		this.sensors = sensors;
		this.isClassname = isClassname;
		this.shouldClassname = shouldClassname;
		this.score = score;
	}

	public String getIsClassname() {
		return isClassname;
	}

	public String getMethod() {
		return method;
	}

	public double getScore() {
		return score;
	}

	public String getSensors() {
		return sensors;
	}

	public String getShouldClassname() {
		return shouldClassname;
	}

	public void setIsClassname(String isClassname) {
		this.isClassname = isClassname;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void setSensors(String sensors) {
		this.sensors = sensors;
	}

	public void setShouldClassname(String shouldClassname) {
		this.shouldClassname = shouldClassname;
	}

}
