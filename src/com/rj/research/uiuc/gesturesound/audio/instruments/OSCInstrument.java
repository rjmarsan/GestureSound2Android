package com.rj.research.uiuc.gesturesound.audio.instruments;

import android.util.Log;

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
public class OSCInstrument extends Instrument {
	public static String name = "OSCInstrument";

	public OSCInstrument() {
		Parameter curve = new Parameter("Setting 1", Parameter.CONTINUOUS, 0.5);
		curve.setMinMax(-1, 1);
		Parameter vel = new Parameter("Setting 2", Parameter.CONTINUOUS, 100);
		vel.setMinMax(0, 100);
		parameters = new Parameter[] {
				curve,vel
			};
	}
	          
	          
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updated() {
		for (int i=0; i<parameters.length; i++) {
			Log.d("OSCInstrument", "Loading new value for "+parameters[i].getName()+" is: "+parameters[i].getValue());
		}		
	}
}
