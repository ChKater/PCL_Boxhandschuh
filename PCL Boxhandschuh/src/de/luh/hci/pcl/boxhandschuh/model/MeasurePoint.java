package de.luh.hci.pcl.boxhandschuh.model;

import java.util.Date;

public class MeasurePoint {

	private Date date;
	private double gx, gy, gz, ax, ay, az;
	private double fsr0, fsr1, fsr2, fsr3;

	public Date getDate() {
		return date;
	}

	public MeasurePoint() {
		super();
	}
	public MeasurePoint(Date date, double gx, double gy, double gz, double ax,
			double ay, double az) {
		super();
		this.date = date;
		this.gx = gx;
		this.gy = gy;
		this.gz = gz;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
	}
	

	public MeasurePoint(Date date, double gx, double gy, double gz, double ax,
			double ay, double az, double fsr0, double fsr1, double fsr2,
			double fsr3) {
		super();
		this.date = date;
		this.gx = gx;
		this.gy = gy;
		this.gz = gz;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
		this.fsr0 = fsr0;
		this.fsr1 = fsr1;
		this.fsr2 = fsr2;
		this.fsr3 = fsr3;
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
	
	

	public double getFsr0() {
		return fsr0;
	}

	public void setFsr0(double fsr0) {
		this.fsr0 = fsr0;
	}

	public double getFsr1() {
		return fsr1;
	}

	public void setFsr1(double fsr1) {
		this.fsr1 = fsr1;
	}

	public double getFsr2() {
		return fsr2;
	}

	public void setFsr2(double fsr2) {
		this.fsr2 = fsr2;
	}

	public double getFsr3() {
		return fsr3;
	}

	public void setFsr3(double fsr3) {
		this.fsr3 = fsr3;
	}

	@Override
	public String toString() {
		return "[time=" +  date.getTime()+ "]" + "acc[x=" + ax + ", y=" + ay+ "z=" + az + "]";
	}

}
