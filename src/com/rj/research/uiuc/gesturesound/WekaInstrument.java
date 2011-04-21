package com.rj.research.uiuc.gesturesound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import wekinator.controller.WekinatorManager;
import android.content.Context;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.TouchListener;
import com.rj.research.uiuc.gesturesound.android.RemoteSolver2;
import com.rj.research.uiuc.gesturesound.android.RemoteSolver2.RemoteSolverCallback;
import com.rj.research.uiuc.gesturesound.audio.AudioManager;
import com.rj.research.uiuc.gesturesound.audio.InstrumentManager;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.PDSynth;
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
	public final static int TESTING = 3;
	
	public boolean NETWORK_TRAIN = true;
	public Context context; //sorry I had to break some non-android generality here.
	
	public int mode = NOTHING;
	
	public String name = "RJTest 11";
	
	/** The core components of a WekaInstrument **/
	/** all of these are UI independent (hopefully) **/
	public WekinatorManager wekamanager;
	public AudioManager audiomanager;
	public ExtractorManager extractormanager;
	public InstrumentManager instrument;
 
	
	public File searchFolder;
	public File saveFolder;
	
	/**
	 * I got lazy and want to manage all the events in another object
	 */
	public WekaInstrumentEventManager eventmanager;
	
	public WekaInstrument(File searchFolder) {
		this.extractormanager = new ExtractorManager();
		this.instrument = new InstrumentManager(this);
		this.audiomanager = new AudioManager();
		this.eventmanager = new WekaInstrumentEventManager();
		this.searchFolder = searchFolder;
	}

	
	public void makeWekaInstrument(File saveFolder) throws Exception {
		this.saveFolder = saveFolder;
		loadWekaInfoFromFolder(saveFolder);
		instrument.loadCurrentInstFromFolder(saveFolder);
//		int in = extractormanager.getFeatureVectorSize();
//		int out = instrument.getInstrumentParameters().length;
//		this.wekamanager = new WekinatorManager(name,in,out,saveFolder);
		this.wekamanager = new WekinatorManager(saveFolder);
		if (this.saveFolder != null) this.wekamanager.setSaveDir(saveFolder);
//		this.eventmanager.fireLoadCompleteEvent(this);
	}
	
	
	public void start() {
		String[] instances = this.getSavedInstances();
		if (instances != null && instances.length > 0) {
			load(instances[0]);
		} else {
			makeNewWekaInstrument("Test instrument 1");
		}
	}
	
	public void makeNewWekaInstrument(String name) {
		eventmanager.fireLoadStartedEvent();
		this.name = name;
		this.saveFolder = new File(searchFolder, name);
		instrument.setInstrument(PDSynth.name);
		int in = extractormanager.getFeatureVectorSize();
		int out = instrument.getInstrumentParameters().length;
		this.wekamanager = new WekinatorManager(in,out);
		this.eventmanager.fireLoadCompleteEvent(this);
		eventmanager.fireLoadCompleteEvent(this);
	}

	public void load(String name) {
		load(new File(searchFolder, name));
	}

	public void load(final File saveFolder) {
		Runnable save = new Runnable() {public void run() {
			System.out.println("---------------Starting to load!");
				try {
					eventmanager.fireLoadStartedEvent();
					makeWekaInstrument(saveFolder);
					eventmanager.fireLoadCompleteEvent(WekaInstrument.this);
				} catch (Exception e) {
					eventmanager.fireLoadFailedEvent();
					e.printStackTrace();
				}
				 }};
		new Thread(new ThreadGroup("loadgroup"), save, "loadthread", 1024*1024*4).start();
	}
	public void loadWekaInfoFromFolder(File folder) throws Exception {
		FileInputStream f = new FileInputStream(new File(folder, "wekainfo.yay"));
		Properties p = new Properties();
		p.load(f);
		this.name = (String) p.get("wekaname");
	}
	
	
	public void save() {
		if (saveFolder == null)
			saveas(new File(searchFolder, name));
		else
			saveas(saveFolder);
	}

	public void saveas(final File saveFolder) {
		Runnable save = new Runnable() {public void run() {
			System.out.println("---------------Starting to save as!");
			try {
				eventmanager.fireSaveStartedEvent();
				saveAllWekaState(saveFolder);
				eventmanager.fireSaveCompleteEvent(WekaInstrument.this);
			} catch (Exception e) { 
				eventmanager.fireSaveFailedEvent();
				e.printStackTrace(); 
			} finally {
				System.out.println("+++++++++++++++Saved as!!!");
			}}};
		new Thread(new ThreadGroup("savegroup"), save, "savethread", 1024*1024*4).start();
	}	
	
	private void saveAllWekaState(File folder) throws IOException {
		instrument.saveCurrentInstToFolder(folder);
		wekamanager.save(folder);
		saveWekaInfoToFolder(folder);
	}
	
	private void saveWekaInfoToFolder(File folder) throws IOException {
		if (!folder.exists()) folder.mkdirs();
		File outfile = new File(folder, "wekainfo.yay");
		if (!outfile.exists()) outfile.createNewFile();
		FileOutputStream f = new FileOutputStream(outfile);
//		FileOutputStream f = new FileOutputStream(new File(folder, "wekainfo.yay"));
		Properties p = new Properties();
		p.put("wekaname", name);
		p.store(f, "weka instrument settings");
		f.close();
	}
  
	public void setSaveFolder(File f) {
		this.saveFolder = f;
		if (this.wekamanager != null) this.wekamanager.setSaveDir(f);
	}


	

	
	
	public String[] getSavedInstances() {
		File[] files = searchFolder.listFiles();
		ArrayList<String> stringfiles = new ArrayList<String>();
		for (File f : files) {
			if (f.isDirectory()) {
//				try {
//					FileInputStream proptestfile = new FileInputStream(new File(f, "wekainfo.yay"));
//					Properties p = new Properties();
//					p.load(proptestfile);
//					stringfiles.add((String) p.get("wekaname"));
//				} catch (Exception e) { e.printStackTrace(); }
				stringfiles.add(f.getName());
			}
		}
		String[] outarray = new String[stringfiles.size()];
		for (int i = 0; i<outarray.length;i++) {
			outarray[i] = stringfiles.get(i);
		}
		return outarray;
	}
	
	
	public void setContext(Context context) {
		this.context = context;
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
		if (NETWORK_TRAIN) {
			final Runnable task2 = new Runnable() {
				public void run() {
					System.out.println("----Begin remote training (with larger stack)");
					mode = TRAINING;
					eventmanager.fireWekaTrainBegin(wekamanager);
					RemoteSolver2 s = new RemoteSolver2();
					try {
						wekamanager = s.solve(wekamanager, context, new RemoteSolverCallback() {
							public void progress(RemoteSolver2 solver, int stepsComplete, int totalSteps, String status) {
								eventmanager.fireWekaTrainUpdate(wekamanager, stepsComplete, totalSteps, status);
							}});
					} catch (Exception e) {
						e.printStackTrace();
					}
					eventmanager.fireWekaTrainEnd(wekamanager);
					mode = PERFORMING;
					System.out.println("----end remote training");
				}
			};
			new Thread(new ThreadGroup("traingroup"), task2, "trainingthread", 1024*1024*4).start();
		} else {
			final Runnable task = new Runnable() {
				public void run() {
					System.out.println("----Begin offthread training (with larger stack)");
					mode = TRAINING;
					eventmanager.fireWekaTrainBegin(wekamanager);
					wekamanager.trainMultithread();
					eventmanager.fireWekaTrainEnd(wekamanager);
					mode = PERFORMING;
					System.out.println("----end offthread training");
				}
			};
			new Thread(new ThreadGroup("traingroup"), task, "trainingthread", 1024*1024*4).start();
		}

	}
	public void doNothing() {
		this.mode = NOTHING;
	}
	public void startTesting() {
		this.mode = TESTING;
		instrument.getInstrument().gestureStart();
	}
	public void stopTesting() {
		this.mode = NOTHING;
		instrument.getInstrument().gestureStop();
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
		instrument.getInstrument().gestureStart();
		Parameter[] params = this.instrument.getInstrumentParameters();
		extractormanager.touchDown(c, params);
		updateGenerator(c);
	}
	public void touchUp(Cursor c) {
		extractormanager.touchUp(c);
		//updateGenerator(c);
		instrument.getInstrument().gestureStop();
	}
	public void touchMoved(Cursor c) {
		updateGenerator(c);
	}
	@Override
	public void touchAllUp(Cursor c) {
		extractormanager.allUp(c);
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
		Parameter[] params = this.instrument.getInstrumentParameters();
		double[][] featurevector = extractormanager.makeFeatureVector(c, params);
		if (featurevector == null) return; //something went wrong. return now!
		
		if (mode == PERFORMING) {
			System.out.println("Performing!");
			double[] paramvector = wekamanager.classify(featurevector);
			for (double d : paramvector) System.out.println("D:"+d);
			instrument.setNewParameters(paramvector, false);
			eventmanager.fireWekaClassifyEvent(paramvector);
		}
		else if (mode == RECORDING) {
			System.out.println("recording!");
			wekamanager.addToTrain(featurevector, instrument.getInstrumentParametersAsDouble(), instrument.getParameterMask());
			//TODO make this event
			eventmanager.fireWekaTrainEvent(wekamanager);
		}


	}


	
	
	
	
	
	
}
