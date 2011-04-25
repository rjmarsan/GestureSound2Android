package com.rj.research.uiuc.gesturesound.gestures.extractors;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.qualities.Curvature;
import com.rj.research.uiuc.gesturesound.gestures.qualities.MTRelDistance;
import com.rj.research.uiuc.gesturesound.gestures.qualities.Quality;
import com.rj.research.uiuc.gesturesound.gestures.qualities.Speed;
import com.rj.research.uiuc.gesturesound.gestures.qualities.VelocityX;
import com.rj.research.uiuc.gesturesound.gestures.qualities.VelocityY;
import com.rj.research.uiuc.gesturesound.gestures.qualities.X;
import com.rj.research.uiuc.gesturesound.gestures.qualities.Y;

public class FeatureExtractor {

	Quality[] qualities = new Quality[] {
			new Curvature(),
			new Speed(),
			new VelocityX(),
			new VelocityY(),
			new X(),
			new Y(),
			new MTRelDistance(),
	};
	
	FeatureMap map;
	
	public FeatureExtractor() {
		int[] types = new int[qualities.length];
		for (int i=0;i<types.length;i++) types[i] = qualities[i].type;
		map = new FeatureMap(new double[qualities.length], types);
	}
	
	public FeatureMap makeFeatureMap(Cursor c) {
		for (int i=0; i<qualities.length; i++) {
			map.featurevec[i] = qualities[i].update(c);
			System.out.println(""+qualities[i].name+": "+map.featurevec[i]);
		}
		return map;
	}
	
}
