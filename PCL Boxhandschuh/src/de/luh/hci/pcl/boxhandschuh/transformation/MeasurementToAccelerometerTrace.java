package de.luh.hci.pcl.boxhandschuh.transformation;

import java.util.ArrayList;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.protrector.Point3D;

public class MeasurementToAccelerometerTrace implements MeasurementToTrace {

	@Override
	public List<Point3D> transform(Measurement m) {
		List<Point3D> trace = new ArrayList<>();
		for (int i = 0; i < m.getMeasurement().size(); i++) {
			MeasurePoint p = m.getMeasurement().get(i);
			trace.add(new Point3D(p.getAx(), p.getAy(), p.getAz()));
		}
		return trace;
	}

}
