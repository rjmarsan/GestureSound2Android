package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class Y extends Quality {
	public static String name="y";
	
	public Y() {
		type = FeatureMap.Y;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
		return in.currentPoint.y/Lazy.height;
	}
	
}
