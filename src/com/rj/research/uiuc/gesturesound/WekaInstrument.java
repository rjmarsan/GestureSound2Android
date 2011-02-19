package com.rj.research.uiuc.gesturesound;

import java.io.File;
import java.util.ArrayList;

import wekinator.controller.WekinatorManager;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.TouchListener;
import com.rj.research.uiuc.gesturesound.audio.AudioManager;
import com.rj.research.uiuc.gesturesound.audio.InstrumentManager;
import com.rj.research.uiuc.gesturesound.audio.instruments.OSCInstrument;
import com.rj.research.uiuc.gesturesound.gestures.extractors.ExtractorManager;
import com.rj.research.uiuc.gesturesound.listeners.InstrumentListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaClassifyListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaInstListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaInstrumentEventManager;

public class WekaInstrument implements TouchListener  {
	public final static int NOTHING = 0;
	public final static int PERFORMING = 1;
	public final static int RECORDING = 2;
	public final static int TRAINING = 3;
	
	public int mode = NOTHING;
	
	public String name = "RJTest 11";
	
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
		if (this.saveFolder != null) this.wekamanager.setSaveDir(saveFolder);
		this.eventmanager.fireLoadCompleteEvent(this);
	}
	public void makeWekaManager(String name) {
		instrument.loadCurrentInstFromFolder(new File(saveFolder, name));
		int in = extractormanager.getFeatureVectorSize();
		int out = instrument.getInstrumentParameters().length;
		this.wekamanager = new WekinatorManager(name,in,out,saveFolder);
		if (this.saveFolder != null) this.wekamanager.setSaveDir(saveFolder);
		this.eventmanager.fireLoadCompleteEvent(this);
	}
	
	
	public void newInst(String name) {
		this.name = name;
		instrument.setInstrument(OSCInstrument.name);
		makeWekaManager();
	}
	
	public void setupTest() {
		instrument.setInstrument(OSCInstrument.name);
		makeWekaManager();
	}
	
	
	public void setSaveFolder(File f) {
		this.saveFolder = f;
		if (this.wekamanager != null) this.wekamanager.setSaveDir(f);
	}
	public void load(final String name) {
		Runnable save = new Runnable() {public void run() {
			System.out.println("---------------Starting to load!");
			instrument.setInstrument(OSCInstrument.name);
			try {
				eventmanager.fireLoadStartedEvent();
				makeWekaManager(name);
				WekaInstrument.this.name = name;
				eventmanager.fireLoadCompleteEvent(WekaInstrument.this);
			} catch (Exception e) 
				{e.printStackTrace();}
			finally{
				System.out.println("+++++++++++++++Loaded!!!");
			}}};
		new Thread(new ThreadGroup("loadgroup"), save, "loadthread", 1024*1024).start();
	}
	
	public void save() {
		saveas(name);
	}

	public void saveas(final String name) {
		Runnable save = new Runnable() {public void run() {
			System.out.println("---------------Starting to save as!");
			try {
				eventmanager.fireSaveStartedEvent();
				File savefile = wekamanager.save(name);
				instrument.saveCurrentInstToFolder(savefile);
				WekaInstrument.this.name = name;
				eventmanager.fireSaveCompleteEvent(WekaInstrument.this);
			} catch (Exception e) { e.printStackTrace(); }
			finally {
			System.out.println("+++++++++++++++Saved as!!!");
			}}};
		new Thread(new ThreadGroup("savegroup"), save, "savethread", 1024*1024).start();
	}
	
	
	public String[] getSavedInstances() {
		File[] files = saveFolder.listFiles();
		ArrayList<String> stringfiles = new ArrayList<String>();
		for (File f : files) {
			if (f.isDirectory()) {
				stringfiles.add(f.getName());
			}
		}
		String[] outarray = new String[stringfiles.size()];
		for (int i = 0; i<outarray.length;i++) {
			outarray[i] = stringfiles.get(i);
		}
		return outarray;
	}
	
	/**
	 * Set the global state of the WekaInstrument.   
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
				System.out.println("----Begin offthread training (with larger stack)");
				mode = TRAINING;
				eventmanager.fireWekaTrainBegin(wekamanager);
				wekamanager.train();
				eventmanager.fireWekaTrainEnd(wekamanager);
				mode = PERFORMING;
				System.out.println("----end offthread training");
			}
		};
		new Thread(new ThreadGroup("traingroup"), task, "trainingthread", 1024*1024).start();
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
	public void addWekaInstListener(WekaInstListener inst) {
		eventmanager.addWekaInstListener(inst);
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
			eventmanager.fireWekaTrainEvent(wekamanager);
		}


	}


	
	
	
	
	
	
}
