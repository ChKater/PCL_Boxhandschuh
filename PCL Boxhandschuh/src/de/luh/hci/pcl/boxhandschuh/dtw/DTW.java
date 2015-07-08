package de.luh.hci.pcl.boxhandschuh.dtw;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.dtw.FastDTW;
import com.dtw.TimeWarpInfo;
import com.timeseries.TimeSeries;
import com.timeseries.TimeSeriesPoint;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Punch;
import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;
import de.luh.hci.pcl.boxhandschuh.transformation.MeasurementTo3dTrajectory;

public class DTW {

	private static Function<DTWTemplate, TimeSeries> accGyrCombinedTemplate = x -> {
		return x.getAccGyrCombined();
	};

	private static Function<DTWTemplate, TimeSeries> accTemplate = x -> {
		return x.getAccelerometer();
	};

	private static Function<DTWTemplate, TimeSeries> gyrTemplate = x -> {
		return x.getGyroscop();
	};
	private static Function<DTWTemplate, TimeSeries> trajTemplate = x -> {
		return x.getTrajectory();
	};
	
	private List<DTWTemplate> dtwTemplates = new ArrayList<>();

	DistanceFunction distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");
	
	private DTWMatch _recognize(TimeSeries punch,
			Function<DTWTemplate, TimeSeries> sensor) {
		
		double minDistance = Double.MAX_VALUE;
		DTWTemplate minTemplate = null;
		for (DTWTemplate template : dtwTemplates) {
			TimeWarpInfo info = FastDTW.getWarpInfoBetween(punch, sensor.apply(template), 30, distFn);
			if(info.getDistance() < minDistance){
				minDistance = info.getDistance();
				minTemplate = template;
			}
		}
		return new DTWMatch(minDistance, minTemplate);
	}

	public void addTemplate(Punch punch) {
		DTWTemplate template = new DTWTemplate(punchToAccTimeSeries(punch),
				punchToGyrTimeSeries(punch),
				punchToAccGYrCombinedTimeSeries(punch),
				punchToTrajTimeSeries(punch), punch.getClassName());
		dtwTemplates.add(template);
	}

	private TimeSeries punchToAccGYrCombinedTimeSeries(Punch p) {
		TimeSeries timeseries = new TimeSeries(6);
		long start = p.getMeasurement().getStart().getTime();
		for (int i = 0; i < p.getMeasurement().getMeasurement().size(); i++) {
			MeasurePoint point = p.getMeasurement().getMeasurement().get(i);
			timeseries.addLast(
					point.getDate().getTime() - start,
					new TimeSeriesPoint(new double[] { point.getAx(),
							point.getAy(), point.getAz(), point.getGx(),
							point.getGy(), point.getGz() }));
		}
		return timeseries;
	}

	private TimeSeries punchToAccTimeSeries(Punch p) {
		TimeSeries timeseries = new TimeSeries(3);
		long start = p.getMeasurement().getStart().getTime();
		for (int i = 0; i < p.getMeasurement().getMeasurement().size(); i++) {
			MeasurePoint point = p.getMeasurement().getMeasurement().get(i);
			timeseries.addLast(
					point.getDate().getTime() - start,
					new TimeSeriesPoint(new double[] { point.getAx(),
							point.getAy(), point.getAz() }));
		}
		return timeseries;
	}

	private TimeSeries punchToGyrTimeSeries(Punch p) {
		TimeSeries timeseries = new TimeSeries(3);
		long start = p.getMeasurement().getStart().getTime();
		for (int i = 0; i < p.getMeasurement().getMeasurement().size(); i++) {
			MeasurePoint point = p.getMeasurement().getMeasurement().get(i);
			timeseries.addLast(
					point.getDate().getTime() - start,
					new TimeSeriesPoint(new double[] {point.getGx(),
							point.getGy(), point.getGz() }));
		}
		return timeseries;
	}

	private MeasurementTo3dTrajectory m3dtr = new MeasurementTo3dTrajectory();
	
	private TimeSeries punchToTrajTimeSeries(Punch p) {
		List<MeasurePoint> measurepPoints = p.getMeasurement().getMeasurement();
		List<Point3D> trajectory = m3dtr.transform(p.getMeasurement());
		TimeSeries timeseries = new TimeSeries(3);
		long start = p.getMeasurement().getStart().getTime();
		for (int i = 0; i < measurepPoints.size(); i++) {
			MeasurePoint point = measurepPoints.get(i);
			Point3D point3D = trajectory.get(i);
			timeseries.addLast(
					point.getDate().getTime() - start,
					new TimeSeriesPoint(new double[] {point3D.x,
							point3D.y, point3D.z}));
		}
		return timeseries;
	}

	public DTWMatch recognizeByAccelerometer(Punch p) {
		return _recognize(punchToAccTimeSeries(p), accTemplate);
	}

	public DTWMatch recognizeByAccGYrCombined(Punch p) {
		return _recognize(punchToAccGYrCombinedTimeSeries(p),
				accGyrCombinedTemplate);
	}

	public DTWMatch recognizeByGyroskop(Punch p) {
		return _recognize(punchToGyrTimeSeries(p), gyrTemplate);
	}

	public DTWMatch recognizeByTrajectory(Punch p) {
		return _recognize(punchToTrajTimeSeries(p), trajTemplate);
	}

}
