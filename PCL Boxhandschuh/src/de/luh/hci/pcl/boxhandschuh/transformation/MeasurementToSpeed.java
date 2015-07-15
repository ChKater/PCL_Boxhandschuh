package de.luh.hci.pcl.boxhandschuh.transformation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;
import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;

public class MeasurementToSpeed implements MeasurementToTrace {

    @Override
    public List<Point3D> transform(Measurement m) {
        Point3D velocity =  new Point3D(0, 0, 0);
        Date lastTimeStamp = m.getStart();
        List<Point3D> speedTrace = new ArrayList<>();
        
//        for (int i = 0; i < m.getMeasurement().size(); i++) {
//            MeasurePoint current = m.getMeasurement().get(i);
//            Date currentTimeStamp = current.getDate();
////            double dt = MeasurementTo3dTrajectory.secondsBetweeen(lastTimeStamp, currentTimeStamp);
//            lastTimeStamp = currentTimeStamp;
////            Point3D acceleration = new Point3D(current.getAx() * MeasurementTo3dTrajectory.factor, current.getAy() * MeasurementTo3dTrajectory.factor, current.getAz() * MeasurementTo3dTrajectory.factor);
//            
//            velocity.x = velocity.x * acceleration.x * dt;
//            velocity.y = velocity.y * acceleration.y * dt;
//            velocity.z = velocity.z * acceleration.z * dt;
//            
//            speedTrace.add(velocity.copy());
//        }
        
        return speedTrace;
    }

}
