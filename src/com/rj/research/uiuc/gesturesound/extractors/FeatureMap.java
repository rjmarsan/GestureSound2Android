package com.rj.research.uiuc.gesturesound.extractors;

public class FeatureMap {
	public double[] featurevec;
	public String[] featurenames;
	public FeatureMap(double[] vec, String[] names) {
		featurevec = vec;
		featurenames = names;
	}
}
