package com.rj.research.uiuc.gesturesound.gestures.generators;

public abstract class Generator {
	public abstract int getLength();
	
	public abstract Float[] update(float val);
}
