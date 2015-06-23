package de.luh.hci.pcl.boxhandschuh.protractor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Protractor3D {

	private static Protractor3D instance;
	public static final int N = 32;
	
	public static final double S = 100;

	private static Function<Point3D, Double> x = p -> {
		return p.x;
	};
	private static Function<Point3D, Double> y = p -> {
		return p.y;
	};
	private static Function<Point3D, Double> z = p -> {
		return p.z;
	};
	
	public static Protractor3D getInstance(){
		if(instance == null){
			instance = new Protractor3D();
		}
		return instance;
	}
	
	public List<Template> templates = new ArrayList<>();
	

	private Protractor3D(){}

	public void addTemplate(String id, List<Point3D> trace) {
		templates.add(new Template(prepareTrace(trace), id));
	}
	
	public List<Point3D> prepareTrace(List<Point3D> trace){
		List<Point3D> resampledTrace = resample(trace);
		Point3D c = centroid(resampledTrace);
		c.x = -c.x;
		c.y = -c.y;
		c.z = -c.z;
		translate(resampledTrace, c); // translate to origin
//		normalize(resampledTrace);
		fitToBox(resampledTrace);
		return resampledTrace;
	}

	public Point3D centroid(List<Point3D> points) {
		double cx = 0;
		double cy = 0;
		double cz = 0;
		for (Point3D point : points) {
			cx += point.x;
			cy += point.y;
			cz += point.z;
		}
		cx = cx / points.size();
		cy = cy / points.size();
		cz = cz / points.size();
		return new Point3D(cx, cy, cz);
	}

	public List<Point3D> copy(List<Point3D> points) {
		int n = points.size();
		List<Point3D> c = new ArrayList<Point3D>(n);
		for (Point3D p : points) {
			c.add(new Point3D(p.x, p.y, p.z));
		}
		return c;
	}

	public double distance(Point3D a, Point3D b) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		double dz = a.z - b.z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public void fitToBox(List<Point3D> trace) {
		double max = Double.MIN_VALUE;
		for (Point3D p : trace) {
			double absX = Math.abs(p.x);
			if (absX > max) {
				max = absX;
			}
			double absY = Math.abs(p.y);
			if (absY > max) {
				max = absY;
			}
			double absZ = Math.abs(p.z);
			if (absZ > max) {
				max = absZ;
			}
		}
		double factor = (S / 2.0) / max;
		scale(trace, factor);
	}

	public RealMatrix getRotationMatrix(List<Point3D> g, List<Point3D> t) {
		double[][] nData = {
				{ S(x, x, g, t) + S(y, y, g, t) + S(z, z, g, t),
						S(y, z, g, t) - S(z, y, g, t),
						S(z, x, g, t) - S(x, z, g, t),
						S(x, y, g, t) - S(y, x, g, t) },
				{ S(y, z, g, t) - S(z, y, g, t),
						S(x, x, g, t) - S(y, y, g, t) - S(z, z, g, t),
						S(x, y, g, t) - S(y, x, g, t),
						S(z, x, g, t) + S(x, z, g, t) },
				{ S(z, x, g, t) - S(x, z, g, t), S(x, y, g, t) - S(y, x, g, t),
						-S(x, x, g, t) + S(y, y, g, t) - S(z, z, g, t),
						S(y, z, g, t) + S(z, y, g, t) },
				{ S(x, y, g, t) - S(y, x, g, t), S(z, x, g, t) + S(x, z, g, t),
						S(y, z, g, t) + S(z, y, g, t),
						-S(x, x, g, t) - S(y, y, g, t) + S(z, z, g, t) } };
		RealMatrix n = MatrixUtils.createRealMatrix(nData);
		EigenDecomposition d = new EigenDecomposition(n);
		
		double[] eigenvalues = d.getRealEigenvalues();
		double max = eigenvalues[0];
		int pos = 0;
		for (int i = 1; i < eigenvalues.length; i++) {
			double current = eigenvalues[i];
			if (current > max) {
				max = current;
				pos = i;
			}
		}
		RealVector eigenvector = d.getEigenvector(pos);
		double norm = eigenvector.getNorm();
		if (norm != 0) {
			norm = 1.0 / norm;
		}
		eigenvector = eigenvector.mapMultiply(norm);
		double[] eigenvectorVal = eigenvector.toArray();
		double theta = 2.0 / Math.cos(eigenvectorVal[3] * 180.0 / Math.PI);
		double s = Math.sin(-theta);
		double c = Math.cos(-theta);
		double qx = eigenvectorVal[2];
		double qy = eigenvectorVal[1];
		double qz = eigenvectorVal[0];
		double qxqx = qx * qx;
		double qyqy = qy * qy;
		double qzqz = qz * qz;
		double c1M = 1 - c;
		double[][] rData = {
				{ qxqx * (1 - qxqx) * c, qx * qy * c1M - qz * s,
						qx * qz * c1M + qy * s },
				{ qx * qy * c1M + qz * s, qyqy * (1 - qyqy) * c,
						qy * qz * c1M - qx * c },
				{ qx * qz * c1M, qy * qz * c1M + qx * s, qzqz + (1 - qzqz) * c } };
		return MatrixUtils.createRealMatrix(rData);
	}

	public void normalize(List<Point3D> trace) {
		double mag = 0;
		for (Point3D p : trace) {
			mag += p.x * p.x + p.y * p.y + p.z * p.z;
		}
		mag = Math.sqrt(mag);
		if (mag != 0) {
			mag = 1.0 / mag;
		}
		for (Point3D p : trace) {
			p.x *= mag;
			p.y *= mag;
			p.z *= mag;
		}
	}

	public Match optimalAngle(List<Point3D> g, List<Point3D> t) {
		RealMatrix r = getRotationMatrix(g, t);
		double score = 0;
		for (int i = 0; i < g.size(); i++) {
			Point3D gp = g.get(i);
			RealMatrix gv = MatrixUtils.createRealMatrix(new double[][] {
					{ gp.x }, { gp.y }, { gp.z } });
			Point3D tp = t.get(i);
			RealVector tv = new ArrayRealVector(
					new Double[] { tp.x, tp.y, tp.z });
			score += r.multiply(gv).getColumnVector(0).dotProduct(tv);
		}
		return new Match(score, r);
	}

	public double pathLength(List<Point3D> trace) {
		double length = 0;
		Point3D lastTracePoint = trace.get(0);
		for (int i = 0; i < trace.size(); i++) {
			Point3D tracePoint = trace.get(i);
			double distance = distance(lastTracePoint, tracePoint);
			length += distance;
			lastTracePoint = tracePoint;
		}
		return length;
	}

	public Match recognize(List<Point3D> trace) {
		List<Point3D> preparedTrace = prepareTrace(trace);
		Match bestMatch = null;
		for (Template t : templates) {
			Match m = optimalAngle(preparedTrace, t.getTrace());
			if (bestMatch == null || m.score > bestMatch.score) {
				m.template = t;
				bestMatch = m;
			}
		}

		return bestMatch;
	}

	public List<Point3D> resample(List<Point3D> trace) {
		List<Point3D> newTrace = new ArrayList<>(N);

		int m = trace.size();
		if (m == 0) {
			return newTrace;
		}

		// trace has only a single point
		if (m == 1) {
			Point3D p = trace.get(0);
			for (int i = 0; i < N;) {
				newTrace.add(p.copy());
			}
			return newTrace;
		}

		// at least 2 points in trace

		double pathLength = pathLength(trace);
		double I = pathLength / (N - 1);
		double D = 0;
		Point3D pp = trace.get(0).copy();
		newTrace.add(pp); // add first point of original
							// trace
		for (int i = 1; i < trace.size() && newTrace.size() < N - 1; i++) {
			Point3D p = trace.get(i).copy();
			double d = distance(pp, p);
			if (d > 0 && D + d > I) {
				double delta = (I - D) / d;
				p = new Point3D(pp.x + delta * (p.x - pp.x), pp.y + delta
						* (p.y - pp.y), pp.z + delta * (p.z - pp.z));
				newTrace.add(p);
				D = 0;
				i--;
			} else {
				D += d;
			}
			pp = p;
		}
		newTrace.add(trace.get(m - 1).copy()); // add last point of original trace
		return newTrace;
	}

	public double S(Function<Point3D, Double> first,
			Function<Point3D, Double> second, List<Point3D> g, List<Point3D> t) {
		double sum = 0;
		for (int k = 0; k < g.size(); k++) {
			sum += first.apply(g.get(k)) * second.apply(t.get(k));
		}
		return sum;
	}

	public void scale(List<Point3D> trace, double factor) {
		for (Point3D p : trace) {
			p.x *= factor;
			p.y *= factor;
			p.z *= factor;
		}
	}

	public void translate(List<Point3D> points, Point3D translationVector) {
		for (Point3D point : points) {
			point.x += translationVector.x;
			point.y += translationVector.y;
			point.z += translationVector.z;
		}
	}
}
