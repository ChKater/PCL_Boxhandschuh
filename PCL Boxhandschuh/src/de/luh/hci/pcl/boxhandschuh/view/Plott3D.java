package de.luh.hci.pcl.boxhandschuh.view;

import java.util.List;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import de.luh.hci.pcl.boxhandschuh.protractor.Point3D;

public class Plott3D extends GridPane {

	private AWTChart chart;
	private JavaFXChartFactory factory;

	public Plott3D(List<Point3D> trace) {
		setPrefHeight(Double.MAX_VALUE);
		setPrefWidth(Double.MAX_VALUE);
		factory = new JavaFXChartFactory();
		chart = (AWTChart) factory.newChart(Quality.Fastest, "offscreen");

		plott(transform(trace), Color.RED);
		
	}
	
	public void addPlott(List<Point3D> trace, Color color){
		plott(transform(trace), color);
	}
	
	
	

	private void plott(Coord3d[] data, Color color) {
		Scatter scatter = new Scatter(data, color);
		chart.getScene().add(scatter);
		LineStrip sls = new LineStrip();
		sls.add(new Point(scatter.getData()[0], Color.GREEN));
		sls.add(new Point(scatter.getData()[1], Color.GREEN));
		sls.setDisplayed(true);
		chart.getScene().getGraph().add(sls);
		Coord3d prev = scatter.getData()[1];
		for (int i = 2; i < scatter.getData().length; i++) {
			Coord3d cur = scatter.getData()[i];
			LineStrip ls = new LineStrip();

			ls.add(new Point(prev, color));
			ls.add(new Point(cur, color));
			ls.setDisplayed(true);
			prev = cur;
			chart.getScene().getGraph().add(ls);
		}
		ImageView imageView1 = factory.bindImageView(chart);
		getChildren().clear();
		add(imageView1, 0, 0);
	}

	private Coord3d[] transform(List<Point3D> trace) {
		Coord3d[] coordArr = new Coord3d[trace.size()];
		for (int i = 0; i < coordArr.length; i++) {
			Point3D p = trace.get(i);
			coordArr[i] = new Coord3d(p.x, p.y, p.z);
		}
		return coordArr;
	}
}
