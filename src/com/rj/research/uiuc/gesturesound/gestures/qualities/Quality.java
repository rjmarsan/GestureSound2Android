package com.rj.research.uiuc.gesturesound.gestures.qualities;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.GestureEngine;

public abstract class Quality {
	GestureEngine engine;
	public static String name="";
	
	public static Quality cursorDetected(GestureEngine engine) {
		return null;
	}
	
	
	public Quality(GestureEngine engine) {
		this.engine=engine;
	}
	
	public abstract float update(Cursor in);
	
	public abstract float getCurrentValue();
}
