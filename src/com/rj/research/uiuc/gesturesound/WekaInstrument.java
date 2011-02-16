package com.rj.research.uiuc.gesturesound;

import java.io.File;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.TouchListener;
import com.rj.research.uiuc.gesturesound.audio.AudioManager;
import com.rj.research.uiuc.gesturesound.audio.instruments.InstrumentManager;
import com.rj.research.uiuc.gesturesound.extractors.ExtractorManager;

public class WekaInstrument implements TouchListener  {
	public final static int NOTHING = 0;
	public final static int PERFORMING = 1;
	public final static int RECORDING = 2;
	public final static int TRAINING = 3;
	
	public int mode = NOTHING;
	
	/** The core components of a WekaInstrument **/
	/** all of these are UI independent (hopefully) **/
//	public WekinatorManager wekamanager;
	public AudioManager audiomanager;
	public ExtractorManager extractormanager;
	public InstrumentManager instrument;
	
	
	
	public WekaInstrument() {
		this.extractormanager = new ExtractorManager();
		this.instrument = new InstrumentManager();
		this.audiomanager = new AudioManager();
	}
	
	
	public void setup() {
	}
	
	
	public void loadFromFile(File f) {
		
	}
	
	public void saveToFile(File f) {
		
	}
	
	
	/**
	 * Set the global state of the WekaInstrument.   
	 * This is about the only time I found it necessary to forward a child object's functions through us.
	 * mainly because this is what the program's all about.
	 */
	public void perform() {
		this.mode = PERFORMING;
	}
	public void record() {
		this.mode = RECORDING;
	}
	public void train() {
		this.mode = TRAINING;
		//we actually need to train things at this point.
	}
	public void doNothing() {
		this.mode = NOTHING;
	}
	public int mode() {
		return this.mode;
	}
	
	
	/**
	 * These get forwarded to the ExtractionManager and start that input chain.
	 * basically:
	 * TouchEvent -> WekaInstrument vv
	 *                       ExtractionManager vv-> Canvas -> visuals
	 *                                     WekinatorManager vv
	 *                                             InstrumentManager vv-> InstrumentView 
	 *                                                             SOUND!
	 */
	public void touchDown(Cursor c) {
		updateGenerator(c);
	}
	public void touchUp(Cursor c) {
		updateGenerator(c);
	}
	public void touchMoved(Cursor c) {
		updateGenerator(c);
	}
	@Override
	public void touchAllUp(Cursor c) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	/**
	 * Honestly, this is the biggest function in here.
	 * 
	 */
	public void updateGenerator(Cursor c) {
		double[] featurevector = extractormanager.makeFeatureVector(c);
		double[] paramvector = featurevector;//wekamanager.classify(featurevector);
		instrument.setNewParameters(paramvector);
	}


	
	
	
	
	
	
}
