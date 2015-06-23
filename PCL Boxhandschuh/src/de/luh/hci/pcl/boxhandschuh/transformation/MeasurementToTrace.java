package de.luh.hci.pcl.boxhandschuh.transformation;

import java.util.List;

import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.protrector.Point3D;

public interface MeasurementToTrace {

	public List<Point3D> transform(Measurement m);
}
