package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class FeatureGenerator {
	public int LENGTH = 1;
	
	private int type;
	
	public FeatureGenerator(int type) {
		this.type = type;
	}
	
	public FeatureGenerator(int type, int length) {
		this.LENGTH = length;
		d = new double[length];
		for (int i=0;i<length;i++) d[i] = 0;
	}
	
	public int getLength() {
		return LENGTH;
	}
	
	double[] d = new double[1];
	public double[] update(FeatureMap map) {
		return d;
	}
}
