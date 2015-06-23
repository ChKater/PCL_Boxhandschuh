package de.luh.hci.pcl.boxhandschuh.transformation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;

public class MeasurementTo3dTrajectory implements MeasurementToTrace{

	
	public List<Point3D> transform(Measurement m){
		Point3D position = new Point3D(0, 0, 0);
		Point3D velocity =  new Point3D(0, 0, 0);
		Date lastTimeStamp = m.getStart();
		List<Point3D> trace = new ArrayList<>();
		trace.add(position.copy());
		for (int i = 0; i < m.getMeasurement().size(); i++) {
			MeasurePoint current = m.getMeasurement().get(i);
			Date currentTimeStamp = current.getDate();
			double dt = secondsBetweeen(lastTimeStamp, currentTimeStamp);
			double dtdt = dt * dt;
			double factor = 9.8 / 16384;
			Point3D acceleration = new Point3D(current.getAx() * factor, current.getAy() * factor, current.getAz() * factor);
			position.x = position.x + velocity.x * dt + 0.5 * acceleration.x * dtdt;
			position.y = position.y + velocity.y * dt + 0.5 * acceleration.y * dtdt;
			position.z = position.z + velocity.z * dt + 0.5 * acceleration.z * dtdt;
			
			velocity.x = velocity.x * acceleration.x * dt;
			velocity.y = velocity.y * acceleration.y * dt;
			velocity.z = velocity.z * acceleration.z * dt;
			
			trace.add(position.copy());
		}
		
		return trace;
	}
	
	
	public double secondsBetweeen(Date d1, Date d2){
		return ((double) (d2.getTime() - d1.getTime())) / 1000;
	}
	
	
}
