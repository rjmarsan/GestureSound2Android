package com.rj.research.uiuc.gesturesound.audio.instruments;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import android.util.Log;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.audio.instruments.PDInstrument.PDSetting;

/**
 * The general idea with the instrument is... platform independence.
 * 
 * You declare an array of parameters, which are updated periodically. 
 * You can override the update method if you care what happens when they're updated.
 * otherwise when updated() is called, just update the vectors in whatever way is needed.
 * @author rj
 *
 */
public class PDSynth extends PDInstrument {
	public static String name = "PDSynth";
	private static final String TAG = "PDSynth";
	
	static PDSetting[] settings = {
		new PDSetting("Volume", "amp1", 0, 1),
		new PDSetting("Pitch", "pitch1", 12, 100),
		new PDSetting("Filter", "filt1", 0, 20),
		new PDSetting("Attack", "envattack1", 0, 2000),
		new PDSetting("Decay", "envdecay1", 0, 2000),
//		new PDSetting("Vibrato Speed", "vibspeed1", 0, 70),
//		new PDSetting("Vibrato Depth", "vibdepth1", 0, 100),
	};
	int patch;

	public PDSynth() {
		super(settings);
	}
	          
	@Override
	public void start() {
		super.start();
		patch = this.openPatch("wekainst.pd");
		zero();
	}
	
	private void zero() {
		PdBase.sendFloat("vibdepth1", 1);
		PdBase.sendFloat("vibspeed1", 1);
		PdBase.sendFloat("tremolodepth1", 1);
		PdBase.sendFloat("tremolospeed1", 1);
		PdBase.sendFloat("ampglob", 1);
		PdBase.sendFloat("onoff1", 0);
	}
 
	
	
	@Override
	public void gestureStart() {
		Log.d(TAG, "Gesture started!");
		PdBase.sendFloat("onoff1", 1);
	}

	@Override
	public void gestureStop() {	
		Log.d(TAG, "Gesture stopped!");
		PdBase.sendFloat("onoff1", 0);
	}


}
