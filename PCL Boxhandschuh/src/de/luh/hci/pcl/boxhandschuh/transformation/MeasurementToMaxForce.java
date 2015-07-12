package de.luh.hci.pcl.boxhandschuh.transformation;

import de.luh.hci.pcl.boxhandschuh.model.MeasurePoint;
import de.luh.hci.pcl.boxhandschuh.model.Measurement;

public class MeasurementToMaxForce {
    
    public double transform(Measurement m) {
        
        double maxForce = 0;
        for (int i = 0; i < m.getMeasurement().size(); i++) {
            MeasurePoint p = m.getMeasurement().get(i);
            maxForce = p.getFsr0() > maxForce ? p.getFsr0() : maxForce;
            maxForce = p.getFsr1() > maxForce ? p.getFsr1() : maxForce;
            maxForce = p.getFsr2() > maxForce ? p.getFsr2() : maxForce;
            maxForce = p.getFsr3() > maxForce ? p.getFsr3() : maxForce;
        }
        return maxForce;
    }
    

}
