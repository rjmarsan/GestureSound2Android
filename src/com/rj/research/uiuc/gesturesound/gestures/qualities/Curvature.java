package com.rj.research.uiuc.gesturesound.gestures.qualities;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.Geometry;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class Curvature extends Quality {
	public static String name="curvature";

//	KalmanFilter filter;
	
	double currentValue=0f;
	ArrayList<double[]> pastValues;

	
	public Curvature() {
		type = FeatureMap.CURV;
		pastValues = new ArrayList<double[]>();
//		filter = KalmanFilter.buildKF(0.2, 5, 10);
//		filter.setX(new Matrix(new double[][]{{0.01}, {0.01}, {0.01}}));
//		filter.predict();
	}

	@Override
	public float update(Cursor in) {
		float val=0.0f;
		
		val = (float) (findCurvature(in)/(Math.PI));
		
		pastValues.add(new double[]{val});
		
		//this is the spline method http://www.faculty.idc.ac.il/arik/Java/ex2/index.html
		int n=15;
		List<Point> past = in.points;
		int sizeofpast = in.points.size();
		if (sizeofpast > n) {
			Point[] s = new Point[n+1];
			for (int i=0;i<n+1;i++) {
				Point p = past.get(sizeofpast-i-1);
				s[i] = new Point(p.x,p.y);
			}
			Point p2,p1,p0;
			p0 = s[0];
			p1 = Geometry.evalBezier(s,0.1f);
			p2 = Geometry.evalBezier(s,0.2f);
			val = (float) findCurvature(p0.x, p0.y, p1.x, p1.y, p2.x, p2.y);
		}
		
//		System.out.println("Curvature: "+val);
		currentValue = val;
		return val;
	}
		
	private double findCurvature(Cursor in) {
		if (in.points.size() < 3)
			return 0.0f;
		int size = in.points.size();
		Point posEvt 	= in.points.get(size-1);
		Point prev 	= in.points.get(size-2);
		Point prev2 	= in.points.get(size-3);
		if (prev == null)
			return 0;
		if (prev2 == null)
			return 0;
		return findCurvature(posEvt.x, posEvt.y, prev.x, prev.y, prev2.x, prev2.y);
	}
	public static double findCurvature(double x1, double y1, double x2, double y2, double x3, double y3) {

		double angle1 = getAngle(x1,y1,x2,y2);
		double angle2 = getAngle(x2,y2,x3,y3);
		if (angle1 == 0.0f || angle2 == 0.0f) {
			return 0.0f;
		}		
		double result = angle1-angle2;
		if (result > Math.PI) {
			//System.out.println("Result too big! taking other atan2: "+result+" New: "+(2*Math.PI-result));
			
			result =  (2*Math.PI-result);
		}
		else if (result < -1*Math.PI) {
			//System.out.println("Result too small! taking other atan2: "+result+" New: "+(2*Math.PI+result));

			result =  (2*Math.PI+result);
		}
		
		//System.out.println("Curvature: "+result+" First Angle:"+angle1+" Second Angle: "+angle2);

		return result;

	}
	public static double getAngle(Point ev1, Point ev2) {
		return getAngle(ev1.x,ev1.y,ev2.x, ev2.y);
	}
	public static double getAngle(double x1, double y1, double x2, double y2) {
		return Math.atan2(x1-x2, y1-y2);
	}

	@Override
	public float getCurrentValue() {
		return (float)currentValue;
	}

	
}