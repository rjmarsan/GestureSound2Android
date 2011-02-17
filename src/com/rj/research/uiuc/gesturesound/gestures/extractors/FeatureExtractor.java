package com.rj.research.uiuc.gesturesound.gestures.extractors;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.qualities.Curvature;
import com.rj.research.uiuc.gesturesound.gestures.qualities.Quality;
import com.rj.research.uiuc.gesturesound.gestures.qualities.Velocity;

public class FeatureExtractor {

	Quality[] qualities = new Quality[] {
			new Velocity(),
			new Curvature()
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
		}
		return map;
	}
	
}
