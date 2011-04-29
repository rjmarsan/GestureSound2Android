package com.rj.research.uiuc.gesturesound.gestures.qualities;

import processing.core.PApplet;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.Point;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class MTRelDistanceDiff extends Quality {
	public static String name="MTRelativeDistance";
	
	public MTRelDistanceDiff() {
		type = FeatureMap.MTREL_DIST_DIFF;
		setSmooth(1);
	}

	@Override
	public float getQuality(Cursor in) {
		if (in.curId == 0 && in.currentPoint != null) { //we're the first cursor id
			float avgdist = 0;
			float count = 0;
			for (Cursor c : in.manager.cursors) {
				if (c != null && c.curId != 0 && c.currentPoint != null) {
					count = count + 1;
					avgdist += PApplet.dist(in.currentPoint.x, in.currentPoint.y, c.currentPoint.x, c.currentPoint.y);
				}
			}
			avgdist = avgdist/count;
			
			float avgrms = 0;
			for (Cursor c : in.manager.cursors) {
				if (c != null && c.curId != 0 && c.currentPoint != null) {
					float dist = PApplet.dist(in.currentPoint.x, in.currentPoint.y, c.currentPoint.x, c.currentPoint.y);
					avgrms += PApplet.sqrt((dist-avgdist)*(dist-avgdist));
				}
			}
			return avgrms == 0? Float.NEGATIVE_INFINITY : avgrms / count / Lazy.width;
		} 
		return Float.NEGATIVE_INFINITY;
		
		
	}
	
}
