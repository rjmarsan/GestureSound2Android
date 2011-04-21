package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class VelocityY extends Quality {
	public static String name="velocityY";
	public static float SCALE = 500f;

	public VelocityY() {
		type = FeatureMap.VEL_Y;
		setSmooth(1);
	}
	
	@Override
	public float getQuality(Cursor in) {		
		return in.velY/Lazy.height*SCALE;
	}
	
}