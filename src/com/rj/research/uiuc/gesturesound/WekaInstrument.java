package com.rj.research.uiuc.gesturesound;

import java.io.File;

import wekinator.controller.WekinatorManager;

import android.util.Log;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.TouchListener;
import com.rj.research.uiuc.gesturesound.audio.AudioManager;
import com.rj.research.uiuc.gesturesound.audio.InstrumentManager;
import com.rj.research.uiuc.gesturesound.audio.instruments.OSCInstrument;
import com.rj.research.uiuc.gesturesound.gestures.extractors.ExtractorManager;
import com.rj.research.uiuc.gesturesound.listeners.InstrumentListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaClassifyListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaInstrumentEventManager;

public class WekaInstrument implements TouchListener  {
	public final static int NOTHING = 0;
	public final static int PERFORMING = 1;
	public final static int RECORDING = 2;
	public final static int TRAINING = 3;
	
	public int mode = NOTHING;
	
	/** The core components of a WekaInstrument **/
	/** all of these are UI independent (hopefully) **/
	public WekinatorManager wekamanager;
	public AudioManager audiomanager;
	public ExtractorManager extractormanager;
	public InstrumentManager instrument;
	
	
	public File saveFolder;
	
	/**
	 * I got lazy and want to manage all the events in another object
	 */
	public WekaInstrumentEventManager eventmanager;
	
	public WekaInstrument() {
		this.extractormanager = new ExtractorManager();
		this.instrument = new InstrumentManager(this);
		this.audiomanager = new AudioManager();
		this.eventmanager = new WekaInstrumentEventManager();
	}
	
	public void makeWekaManager() {
		int in = extractormanager.getFeatureVectorSize();
		int out = instrument.getInstrumentParameters().length;
		this.wekamanager = new WekinatorManager(in,out);
	}
	public void makeWekaManager(File f) {
		int in = extractormanager.getFeatureVectorSize();
		int out = instrument.getInstrumentParameters().length;
		this.wekamanager = new WekinatorManager(instrument.getName(), in,out,f);
	}
	
	
	public void setupTest() {
		instrument.setInstrument(OSCInstrument.name);
		makeWekaManager();
	}
	
	
	public void setSaveFolder(File f) {
		this.saveFolder = f;
	}
	public void loadFromFolder(File f) {
		instrument.setInstrument(OSCInstrument.name);
		makeWekaManager(f);
	}
	
	public void saveToFolder(File f) {
		wekamanager.save(f);
	}
	
	public void save() {
		this.saveToFolder(saveFolder);
	}
	
	
	/**
	 * Set the global state of the WekaInstrument.   
	 * This is about the only time I found it necessary to forward a child object's functions through us.
	 * mainly because this is what the program's all about.
	 */
	public void perform() {
		System.out.println("Weka: performing?");
		if (this.mode == TRAINING || this.mode == PERFORMING) 
			return; //no point.
		else if (this.mode == RECORDING)
			train();
		else
			this.mode = PERFORMING;
	}
	public void record() {
		this.mode = RECORDING;
	}
	public void train() {
		this.mode = NOTHING;
		System.out.println("Training!");
		Runnable task = new Runnable() {
			public void run() {
				System.out.println("----Begin offthread training");
				mode = TRAINING;
				eventmanager.fireWekaTrainBegin(wekamanager);
				wekamanager.train();
				eventmanager.fireWekaTrainEnd(wekamanager);
				mode = PERFORMING;
				System.out.println("----end offthread training");
			}
		};
		new Thread(task).start();
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
		extractormanager.touchDown(c);
		updateGenerator(c);
	}
	public void touchUp(Cursor c) {
		extractormanager.touchUp(c);
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
	 * The interface for events... really I only hope this is used for UI stuff.
	 */
	public void addWekaClassifyListener(WekaClassifyListener w) {
		eventmanager.addWekaClassifyListener(w);
	}
	
	public void addInstrumentListener(InstrumentListener instlist) {
		eventmanager.addInstrumentListener(instlist);
	}
	
	
	
	/**
	 * Honestly, this is the biggest function in here.
	 * 
	 */
	public void updateGenerator(Cursor c) {
		double[] featurevector = extractormanager.makeFeatureVector(c);
		if (featurevector == null) return; //something went wrong. return now!
		
		if (mode == PERFORMING) {
			System.out.println("Performing!");
			double[] paramvector = wekamanager.classify(featurevector);
			for (double d : paramvector) System.out.println("D:"+d);
			instrument.setNewParameters(paramvector);
			eventmanager.fireWekaClassifyEvent(paramvector);
		}
		else if (mode == RECORDING) {
			System.out.println("recording!");
			wekamanager.addToTrain(featurevector, instrument.getInstrumentParametersAsDouble());
			//TODO make this event
			//eventmanager.fireWekaTrainEvent(paramvector);
		}


	}


	
	
	
	
	
	
}
