package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class Velocity extends Quality {
	public static String name="velocity";

//	KalmanFilter filter;
	
	float currentValue;
	
	public Velocity() {
		type = FeatureMap.SPEED;

//		filter = KalmanFilter.buildKF(0.2, 5, 10);
//		filter.setX(new Matrix(new double[][]{{0.01}, {0.01}, {0.01}}));
//		filter.predict();
	}

	@Override
	public float update(Cursor in) {
		float val=0.0f;
		
		val =findVelocity(in);
		currentValue = (currentValue*1+val)/2;
		return currentValue;
	}
	private float findVelocity(Cursor in) {
		double length = 0; 
		int size = in.points.size();
		if (size > 1) {
			Point p1 = in.points.get(size-1);
			Point p2 = in.points.get(size-2);
			length =  Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
		}
		
		return (float)length;

	}

	@Override
	public float getCurrentValue() {
		return currentValue;
	}
	
}