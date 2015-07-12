package de.luh.hci.pcl.boxhandschuh.transformation;

import java.util.List;

import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;

public class Speed3dToAVGSpeed {
    
    public double transform (List<Point3D> velos) {
        double veloSum = 0;
        for (Point3D point3d : velos) {
            veloSum += ((point3d.x + point3d.y + point3d.z)/3.0);
        }
        return veloSum/velos.size();
    }
    
}
