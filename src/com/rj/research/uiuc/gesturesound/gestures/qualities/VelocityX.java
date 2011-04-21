package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class VelocityX extends Quality {
	public static String name="velocityX";
	public static float SCALE = 500f;

	public VelocityX() {
		type = FeatureMap.VEL_X;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
//		float length = 0; 
//		int size = in.points.size();
//		if (size > 1) {
//			Point p1 = in.points.get(size-1);
//			Point p2 = in.points.get(size-2);
//			length =  (p1.x-p2.x);
//		}
//		
		return in.velX/Lazy.width*SCALE;
	}
	
}