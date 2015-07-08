package de.luh.hci.pcl.boxhandschuh.dtw;

import com.dtw.FastDTW;
import com.dtw.TimeWarpInfo;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;

public class DTWTest {

	public static void main(String[] args) {
		 DistanceFunction distFn = DistanceFunctionFactory.getDistFnByName("EuclideanDistance");
		 final TimeWarpInfo info = FastDTW.getWarpInfoBetween(null, null, Integer.parseInt(args[2]), distFn);
	}
}
