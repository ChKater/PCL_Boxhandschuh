package de.luh.hci.pcl.boxhandschuh.dtw;

import com.timeseries.TimeSeries;

public class DTWTemplate {

	private TimeSeries accelerometer;
	private TimeSeries gyroscop;
	private TimeSeries accGyrCombined;
	private TimeSeries trajectory;

	private String classname;

	public DTWTemplate(TimeSeries accelerometer, TimeSeries gyroscop,
			TimeSeries accGyrCombined, TimeSeries trajectory, String classname) {
		super();
		this.accelerometer = accelerometer;
		this.gyroscop = gyroscop;
		this.accGyrCombined = accGyrCombined;
		this.trajectory = trajectory;
		this.classname = classname;
	}

	public TimeSeries getAccelerometer() {
		return accelerometer;
	}

	public void setAccelerometer(TimeSeries accelerometer) {
		this.accelerometer = accelerometer;
	}

	public TimeSeries getGyroscop() {
		return gyroscop;
	}

	public void setGyroscop(TimeSeries gyroscop) {
		this.gyroscop = gyroscop;
	}

	public TimeSeries getAccGyrCombined() {
		return accGyrCombined;
	}

	public void setAccGyrCombined(TimeSeries accGyrCombined) {
		this.accGyrCombined = accGyrCombined;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public TimeSeries getTrajectory() {
		return trajectory;
	}

	public void setTrajectory(TimeSeries trajectory) {
		this.trajectory = trajectory;
	}

}
