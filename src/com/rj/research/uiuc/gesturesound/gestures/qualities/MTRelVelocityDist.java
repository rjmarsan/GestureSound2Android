package com.rj.research.uiuc.gesturesound.gestures.qualities;

import processing.core.PApplet;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class MTRelVelocityDist extends Quality {
	public static String name="MTRelativeVelocityDistance";
	public static float SCALE = 12f;
	
	public MTRelVelocityDist() {
		type = FeatureMap.MTREL_VEL_DIST;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
		if (in.curId == 0 && in.currentPoint != null  && in.points != null && in.points.size() >= 2) { //we're the first cursor id
			Cursor second = null;
			for (Cursor c : in.manager.cursors) {
				if (c != null && c.curId != 0 && c.currentPoint != null && c.points != null && c.points.size() >= 2) {
					second = c; //we found the second cursor!
					float distance1 = PApplet.dist(in.currentPoint.x, in.currentPoint.y, second.currentPoint.x, second.currentPoint.y);
					Point in_lastPoint = in.points.get(in.points.size()-2);
					Point second_lastPoint = c.points.get(c.points.size()-2);
					float distance2 = PApplet.dist(in_lastPoint.x, in_lastPoint.y, second_lastPoint.x, second_lastPoint.y);
					return (distance1-distance2)/Lazy.height*SCALE;
					
				}
			}
		} 
		return Float.NEGATIVE_INFINITY;		
	}
	
}
