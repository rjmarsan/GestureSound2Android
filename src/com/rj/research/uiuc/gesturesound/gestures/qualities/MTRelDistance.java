package com.rj.research.uiuc.gesturesound.gestures.qualities;

import processing.core.PApplet;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class MTRelDistance extends Quality {
	public static String name="MTRelativeDistance";
	
	public MTRelDistance() {
		type = FeatureMap.MTREL_DIST;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
		if (in.curId == 0 && in.currentPoint != null) { //we're the first cursor id
			Cursor second = null;
			for (Cursor c : in.manager.cursors) {
				if (c != null && c.curId != 0 && c.currentPoint != null) {
					second = c; //we found the second cursor!
					float distance = PApplet.dist(in.currentPoint.x, in.currentPoint.y, second.currentPoint.x, second.currentPoint.y);
					return distance/Lazy.height;
					
				}
			}
		} else {
			return 0f;
		}
		return 0f;
		
		
	}
	
}
