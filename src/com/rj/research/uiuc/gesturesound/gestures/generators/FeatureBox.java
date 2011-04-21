package com.rj.research.uiuc.gesturesound.gestures.generators;

import java.util.HashMap;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureExtractor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public abstract class FeatureBox {
	public abstract int numFeaturesGenerated();
	
	
	public abstract double[] makeFeatureVector(Cursor c);


}
