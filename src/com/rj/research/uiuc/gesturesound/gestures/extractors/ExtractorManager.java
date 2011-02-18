package com.rj.research.uiuc.gesturesound.gestures.extractors;

import java.util.HashMap;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.generators.FeatureBox;

public class ExtractorManager {	
	
	HashMap<Cursor, FeatureBox> featureboxMap;
	
	public ExtractorManager() {
		featureboxMap = new HashMap<Cursor, FeatureBox>();
	}
	
	public double[] makeFeatureVector(Cursor c) {
		if (featureboxMap.containsKey(c)) {
			return featureboxMap.get(c).makeFeatureVector(c);
		} else {
			featureboxMap.put(c, new FeatureBox(c));
			return featureboxMap.get(c).makeFeatureVector(c);
		}
	}
	
	
	public void touchDown(Cursor c) {
		featureboxMap.put(c, new FeatureBox(c));
	}
	
	public void touchUp(Cursor c) {
		featureboxMap.remove(c);
	}

	public int getFeatureVectorSize() {
		FeatureBox f = new FeatureBox(null);
		return f.numFeaturesGenerated();
	}
	
}
