package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public abstract class Quality {
	public static String name="";
	public int type = FeatureMap.X;
	
	public abstract float update(Cursor in);
	
	public abstract float getCurrentValue();
}
