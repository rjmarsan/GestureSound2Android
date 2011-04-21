package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class X extends Quality {
	public static String name="x";
	
	public X() {
		type = FeatureMap.X;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
		return in.currentPoint.x/Lazy.width;
	}
	
}
