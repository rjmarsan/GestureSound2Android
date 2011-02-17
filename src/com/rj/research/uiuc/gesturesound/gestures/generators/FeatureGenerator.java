package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class FeatureGenerator {
	public int LENGTH = 1;
	
	private int type;
	
	public FeatureGenerator(int type) {
		this.type = type;
	}
	
	public int getLength() {
		return LENGTH;
	}
	
	double[] d = new double[1];
	public double[] update(FeatureMap map) {
		System.out.println(map.toString());
		d[0] = map.get(type);
		System.out.println("Looking for "+type+" and got value: "+d[0]);
		return d;
	}
}
