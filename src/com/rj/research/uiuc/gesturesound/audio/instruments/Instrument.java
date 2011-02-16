package com.rj.research.uiuc.gesturesound.audio.instruments;

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
	
	public void updateParameters(double[] in) {
		for (int i=0; i<Math.min(parameters.length, in.length); i++) {
			parameters[i].setValue(in[i]);
		}
	}
	
	public Parameter[] getParameters() {
		return parameters;
	}
	
	
	public abstract void start();
	public abstract void stop();
	public abstract void updated();
}
