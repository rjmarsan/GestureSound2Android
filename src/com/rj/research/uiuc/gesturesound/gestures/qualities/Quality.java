package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public abstract class Quality {
	public static String name="";
	public int type = FeatureMap.X;
	
	public final static float INIT_VAL = -1925102.2f; //negative large number.
	public float currentValue = INIT_VAL;
	
	private float smoothval = 1;

	
	public float update(Cursor in) { 
		float val = getQuality(in);
		if (Float.isInfinite(val)) 
			return currentValue == INIT_VAL ? 0f : currentValue; 
		
		if (currentValue != INIT_VAL && !Float.isInfinite(currentValue))
			currentValue = (currentValue*smoothval+val)/(smoothval + 1);
		else
			currentValue = val;			
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
