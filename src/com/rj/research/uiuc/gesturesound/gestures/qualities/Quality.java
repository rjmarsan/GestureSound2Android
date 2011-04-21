package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public abstract class Quality {
	public static String name="";
	public int type = FeatureMap.X;
	
	public float currentValue = 0.0f;
	
	private float smoothval = 1;

	
	public float update(Cursor in) { 
		float val = getQuality(in);
		currentValue = (currentValue*smoothval+val)/(smoothval + 1);
		return currentValue;

	}
	
	protected void setSmooth(float smoothval) {
		this.smoothval = smoothval;
	}
	
	public abstract float getQuality(Cursor in);
	
	
	public float getCurrentValue() {
		return currentValue;
	}
	
	
	
	
}
