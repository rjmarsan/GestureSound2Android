package com.rj.research.uiuc.gesturesound.gestures.extractors;

import java.util.Arrays;

public class FeatureMap {
	public final static int NOP = -1;
	public final static int X = 0;
	public final static int Y = 1;
	public final static int VEL_X = 2;
	public final static int VEL_Y = 3;
	public final static int CURV = 4;
	public final static int SPEED = 5;
	public final static int MTREL_DIST = 6;
	public final static int MTREL_DIST_DIFF = 7;
	public final static int MTREL_ARC = 8;
	public final static int MTREL_ARC_DIFF = 9;
	public final static int MTREL_VEL_DIST = 10;
	public final static int MTREL_VEL_ARC = 11;
	
	public final static int[] options = {
		X,
		Y,
		VEL_X,
		VEL_Y,
		CURV,
		SPEED,
		MTREL_DIST,
		MTREL_DIST_DIFF,
		MTREL_ARC,
		MTREL_ARC_DIFF,
		MTREL_VEL_DIST,
		MTREL_VEL_ARC,
	};
	
	public final static String[] names = {
		"X",
		"Y",
		"Velocity - X",
		"Velocity - Y",
		"Curvature",
		"Speed",
		"Multitouch - Distance",
		"Multitouch - Distance Diference",
		"Multitouch - Arc",
		"Multitouch - Arc Difference",
		"Multitouch - Speed (Relative)",
		"Multitouch - Speed of rotation",
	};
	public final static String[] shortnames = {
		"X",
		"Y",
		"Vel. X",
		"Vel. Y",
		"Curve",
		"Speed",
		"MT Dist.",
		"MT Dist. Diff.",
		"MT Arc",
		"MT Arc Diff.",
		"MT Speed",
		"MT Rot.",
	};
	
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
