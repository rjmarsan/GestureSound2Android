package com.rj.research.uiuc.gesturesound.gestures.extractors;

import java.util.Arrays;

public class FeatureMap {
	public final static int X = 0;
	public final static int Y = 1;
	public final static int VEL_X = 2;
	public final static int VEL_Y = 3;
	public final static int CURV = 4;
	public final static int SPEED = 5;
	
	public double[] featurevec;
	public int[] featuretypes;
	public FeatureMap(double[] vec, int[] types) {
		featurevec = vec;
		featuretypes = types;
	}
	
	public double get(int type) {
		int loc = -1;
		for (int i=featuretypes.length-1;i>=0;i--) {
			if (featuretypes[i] == type) loc = i;
		}
		if (loc >= 0)
			return featurevec[loc];
		else
			return -12999;
	}
	
	public String toString() {
		String str = "";
		for (int i=0; i<featurevec.length; i++) {
			str += "["+featuretypes[i]+":"+featurevec[i]+"],";
		}
		return str;
	}
}
