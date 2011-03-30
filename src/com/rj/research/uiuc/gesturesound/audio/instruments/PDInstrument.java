package com.rj.research.uiuc.gesturesound.audio.instruments;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import android.content.Context;
import android.util.Log;

import com.rj.research.uiuc.gesturesound.R;
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
public class PDInstrument extends Instrument {
	public static String name = "PDInstrument";
	private static final int SAMPLE_RATE = 44100;
	private static final String TAG = "Plasma Theremin";

	
	public static class PDSetting {
		public String name;
		public String pd_name;
		public float max;
		public float min;
		public PDSetting(String name, float min, float max) {
			this.name = name; this.pd_name = name; this.min = min; this.max = max;
		}
		public PDSetting(String name, String pd_name, float min, float max) {
			this.name = name; this.pd_name = pd_name; this.min = min; this.max = max;
		}
	}

	public PDInstrument(PDSetting[] settings) {
		parameters = new Parameter[settings.length];
		for (int i=0; i<settings.length; i++) {
			PDSetting setting = settings[i];
			Parameter param = new Parameter(setting.name, setting.pd_name, Parameter.CONTINUOUS, setting.min);
			param.setMinMax(setting.min, setting.max);
			parameters[i] = param;
		}
	}
	          
	          
	public String getName() {
		return name;
	}

	
	@Override
	public void start() {
		onResume();
	}

	@Override
	public void stop() {
		PdAudio.stopAudio();
	}
	
	@Override
	public void cleanup() {
		PdAudio.stopAudio();
		PdBase.release();
	}
	
	@Override
	public void gestureStart() {
	}

	@Override
	public void gestureStop() {		
	}


	@Override
	public void updated() {
		for (int i=0; i<parameters.length; i++) {
			Log.d("PDInst", "Loading new value for "+parameters[i].getName()+"["+parameters[i].getInternalName()+"] is: "+parameters[i].getValue());
			PdBase.sendFloat(parameters[i].getInternalName(), parameters[i].getValue());
		}		
	}
	
	
	
	public int openPatch(final String patch) {
		final File dir = weka.context.getFilesDir();
		final File patchFile = new File(dir, patch);
		int out=-1;
		try {
			IoUtils.extractZipResource(weka.context.getResources().openRawResource(R.raw.patch), dir, true);
//			out = PdUtils.openPatch(patchFile.getAbsolutePath());
			out = PdBase.openPatch(patchFile.getAbsolutePath());
		} catch (final IOException e) {
			e.printStackTrace();
			Log.e("PDInstrument", e.toString() + "; exiting now");
		}
		return out;
	}
	
	
	public void onResume() {
		if (AudioParameters.suggestSampleRate() < SAMPLE_RATE) {
			Log.e(TAG,"required sample rate not available; exiting");
			//return;
		}
		final int nOut = Math.min(AudioParameters.suggestOutputChannels(), 2);
		if (nOut == 0) {
			Log.e(TAG,"audio output not available; exiting");
			//return;
		}
		try {
			PdAudio.initAudio(SAMPLE_RATE, 0, 2, 1, true);
			PdAudio.startAudio(weka.context);
//			PdBase.setReceiver(reciever);
//			PdBase.subscribe("mainlevel");
			} catch (final IOException e) {
			Log.e(TAG, e.toString());
		}
	}

}
