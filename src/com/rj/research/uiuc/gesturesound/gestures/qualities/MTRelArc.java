package com.rj.research.uiuc.gesturesound.gestures.qualities;

import processing.core.PApplet;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class MTRelArc extends Quality {
	public static String name="MTRelativeArc";
	public static float SCALE = 0.3f;
	
	public MTRelArc() {
		type = FeatureMap.MTREL_ARC_DIFF;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
		if (in.curId == 0 && in.currentPoint != null) { //we're the first cursor id
			Cursor second = null;
			
			float avgangle = 0;
			float count = 0;
			for (Cursor c : in.manager.cursors) {
				if (c != null && c.curId != 0 && c.currentPoint != null) {
					second = c; //we found the second cursor!
					Point p1 = in.currentPoint;
					Point p2 = second.currentPoint;
					avgangle += PApplet.atan2(p1.x-p2.x, p1.y-p2.y);	
					count += 1;
				}
			}
			if (count > 0) return avgangle / count * SCALE;
		} 
		return Float.NEGATIVE_INFINITY;
		
		
	}
	
}
