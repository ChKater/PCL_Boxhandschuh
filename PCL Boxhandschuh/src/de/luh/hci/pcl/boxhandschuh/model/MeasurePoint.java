package de.luh.hci.pcl.boxhandschuh.model;

import java.util.Date;

public class MeasurePoint {

	private Date date;
	private int gx, gy, gz, ax, ay, az;

	public Date getDate() {
		return date;
	}

	public MeasurePoint() {
		super();
	}

	public MeasurePoint(Date date, int gx, int gy, int gz, int ax, int ay,
			int az) {
		super();
		this.date = date;
		this.gx = gx;
		this.gy = gy;
		this.gz = gz;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getGx() {
		return gx;
	}

	public void setGx(int gx) {
		this.gx = gx;
	}

	public int getGy() {
		return gy;
	}

	public void setGy(int gy) {
		this.gy = gy;
	}

	public int getGz() {
		return gz;
	}

	public void setGz(int gz) {
		this.gz = gz;
	}

	public int getAx() {
		return ax;
	}

	public void setAx(int ax) {
		this.ax = ax;
	}

	public int getAy() {
		return ay;
	}

	public void setAy(int ay) {
		this.ay = ay;
	}

	public int getAz() {
		return az;
	}

	public void setAz(int az) {
		this.az = az;
	}

}
