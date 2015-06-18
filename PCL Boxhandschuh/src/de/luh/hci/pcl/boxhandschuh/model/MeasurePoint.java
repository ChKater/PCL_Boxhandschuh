package de.luh.hci.pcl.boxhandschuh.model;

import java.util.Date;

public class MeasurePoint {

	private Date date;
	private double gx, gy, gz, ax, ay, az;

	public Date getDate() {
		return date;
	}

	public MeasurePoint() {
		super();
	}

	public MeasurePoint(Date date, double gx, double gy, double gz, double ax, double ay,
			double az) {
		super();
		this.date = date;
		this.gx = gx;
		this.gy = gy;
		this.gz = gz;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
	}

	public double getGx() {
		return gx;
	}

	public void setGx(double gx) {
		this.gx = gx;
	}

	public double getGy() {
		return gy;
	}

	public void setGy(double gy) {
		this.gy = gy;
	}

	public double getGz() {
		return gz;
	}

	public void setGz(double gz) {
		this.gz = gz;
	}

	public double getAx() {
		return ax;
	}

	public void setAx(double ax) {
		this.ax = ax;
	}

	public double getAy() {
		return ay;
	}

	public void setAy(double ay) {
		this.ay = ay;
	}

	public double getAz() {
		return az;
	}

	public void setAz(double az) {
		this.az = az;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "[time=" +  date.getTime()+ "]" + "acc[x=" + ax + ", y=" + ay+ "z=" + az + "]";
	}

}
