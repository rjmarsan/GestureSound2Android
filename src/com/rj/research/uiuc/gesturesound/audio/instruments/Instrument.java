 package com.rj.research.uiuc.gesturesound.audio.instruments;

import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.Parameter;

/**
 * The general idea with the instrument is... platform independence.
 * 
 * You declare an array of parameters, which are updated periodically. 
 * You can override the update method if you care what happens when they're updated.
 * otherwise when updated() is called, just update the vectors in whatever way is needed.
 * @author rj
 *
 */
public abstract class Instrument {
	Parameter[] parameters;
	WekaInstrument weka;
	
	public void updateParameters(double[] in) {
		if (parameters == null) return;
		for (int i=0; i<Math.min(parameters.length, in.length); i++) {
			parameters[i].setValue(in[i]);
		}
		updated();
	}
	
	public void setWeka(WekaInstrument weka) {
		this.weka = weka;
	}
	
	public Parameter[] getParameters() {
		return parameters;
	}
	
	public String getName() {
		return "AbstractInstrument";
	}
	
	
	public abstract void start();
	public abstract void stop();
	public abstract void cleanup();
	public abstract void gestureStart();
	public abstract void gestureStop();
	public abstract void updated();
}
