package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class Speed extends Quality {
	public static String name="speed";

//	KalmanFilter filter;
	
	float currentValue;
	
	public Speed() {
		type = FeatureMap.SPEED;
		setSmooth(1);
	}
	public float getQuality(Cursor in) {
		double length = 0; 
		int size = in.points.size();
		if (size > 1) {
			Point p1 = in.points.get(size-1);
			Point p2 = in.points.get(size-2);
			length =  Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
		}
		
		return (float)length/Lazy.height;
	}

}