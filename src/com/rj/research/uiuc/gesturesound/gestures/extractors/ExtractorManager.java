package com.rj.research.uiuc.gesturesound.gestures.extractors;

import java.util.HashMap;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.gestures.generators.FeatureBox;
import com.rj.research.uiuc.gesturesound.gestures.generators.FeatureBoxes;
import com.rj.research.uiuc.gesturesound.gestures.generators.QuadraticFeatureBox;

public class ExtractorManager {	
	
	HashMap<Cursor, FeatureBoxes> featureboxMap;
	
	public ExtractorManager() {
		featureboxMap = new HashMap<Cursor, FeatureBoxes>();
	}
	
	public double[][] makeFeatureVector(Cursor c, Parameter[] params) {
		if (featureboxMap.containsKey(c)) {
			return featureboxMap.get(c).makeFeatureVectors(c);
		} else {
			if (featureboxMap.isEmpty()) {
				featureboxMap.put(c, new FeatureBoxes(c, params));
				return featureboxMap.get(c).makeFeatureVectors(c);
			} else {
				return null; //we're only making one FeatureBox for the time being.
			}
		}
	}
	
	
	public void touchDown(Cursor c, Parameter[] params) {
		if (featureboxMap.isEmpty()) 
			featureboxMap.put(c, new FeatureBoxes(c, params));
	}
	
	public void touchUp(Cursor c) {
		featureboxMap.remove(c);
	}
	
	public void allUp(Cursor c) {
		featureboxMap.clear();		
	}

	public int getFeatureVectorSize() {
//		FeatureBox f = new QuadraticFeatureBox(null);
//		return f.numFeaturesGenerated();
		return 0;
	}
	
}
