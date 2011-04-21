package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.audio.Parameter;

public class FeatureBoxes {
	int[] numfeatures;
	double[][] featurevectors;
	Parameter[] params;
	FeatureBox[] boxes;
	Cursor cursor;
	public FeatureBoxes(Cursor c, Parameter[] params) {
		this.params = params;
		cursor = c;
		this.featurevectors = new double[params.length][];
		this.numfeatures = new int[params.length];
		boxes = new FeatureBox[params.length];
		for (int i=0; i<params.length; i++) {
			boxes[i] = new QuadraticFeatureBox(c,params[i]);
		}
	}
	
	public int[] numFeaturesGenerated() {
		for (int i=0; i<boxes.length; i++) {
			numfeatures[i] = boxes[i].numFeaturesGenerated();
		}
		return numfeatures;
	}
	
	
	public double[][] makeFeatureVectors(Cursor c) {
		for (int i=0; i<boxes.length; i++) {
			featurevectors[i] = boxes[i].makeFeatureVector(c);
		}
		return featurevectors;

	}


}
