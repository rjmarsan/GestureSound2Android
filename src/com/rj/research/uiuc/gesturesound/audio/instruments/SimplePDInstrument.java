package com.rj.research.uiuc.gesturesound.audio.instruments;

import org.puredata.core.PdBase;

import android.util.Log;

/**
 * The general idea with the instrument is... platform independence.
 * 
 * You declare an array of parameters, which are updated periodically. 
 * You can override the update method if you care what happens when they're updated.
 * otherwise when updated() is called, just update the vectors in whatever way is needed.
 * @author rj
 *
 */
public class SimplePDInstrument extends PDInstrument {
	public static String name = "SimplePDInstrument";
	private static final String TAG = "SimplePD";
	
	static PDSetting[] settings = {
		new PDSetting("Pitch","pitch",0,127),
		new PDSetting("Volume","amp",0,1),
	};
	int patch;

	public SimplePDInstrument() {
		super(settings);
	}
	          
	@Override
	public void start() {
		super.start();
		patch = this.openPatch("simpleosc.pd");
	}
 
	
	
	@Override
	public void gestureStart() {
		Log.d(TAG, "Gesture started!");
		PdBase.sendFloat("amp", 1);
	}

	@Override
	public void gestureStop() {	
		Log.d(TAG, "Gesture stopped!");
		PdBase.sendFloat("amp", 0);
	}


}
