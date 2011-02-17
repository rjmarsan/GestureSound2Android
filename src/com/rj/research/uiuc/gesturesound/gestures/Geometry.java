package com.rj.research.uiuc.gesturesound.gestures;

import com.rj.processing.mt.Point;


//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public class Geometry {
// A class for some geometric global functions
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// returns the euclidian distance between two Points
	public static float dist(Point p0, Point p1) {
		float dx = p1.x - p0.x;
		float dy = p1.y - p0.y;
		float sum = dx*dx + dy*dy;
		return (float) Math.sqrt(sum);
	}

	// returns the linear interpolation of two Points
	public static Point interpolate(Point p0, Point p1,float t) {
		float x = t * p1.x + (1-t) * p0.x;
		float y = t * p1.y + (1-t) * p0.y;
		return new Point(x,y);
	}

	// evaluates a bezier defined by the control polygon
	// which Points are given in the array at the value t
	public static Point evalBezier(Point arr[],float t) {
		for (int iter = arr.length ; iter > 0 ; iter--) {
			for (int i = 1 ; i < iter ; i++) {
				arr[i-1] = interpolate(arr[i-1],arr[i],t);
			}
		}
		return arr[0];
	}

	// evaluates a bezier defined by the control polygon
	// which Points are given in the array at the value t
	// Note: this function is recursive
	public static Point evalBezierRec(Point arr[],float t,int iter) {
		if (iter == 1)
			return arr[0];
		for (int i = 1 ; i < iter ; i++) {
			arr[i-1] = interpolate(arr[i-1],arr[i],t);
		}
		return evalBezierRec(arr,t,iter-1);
	}

}
