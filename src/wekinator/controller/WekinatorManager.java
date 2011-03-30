package wekinator.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import wekinator.LearningSystem;
import wekinator.SimpleDataset;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;

public class WekinatorManager {
	private final static int MAX_THREAD_COUNT = 8;
	
	public interface TrainCallback {
		public void trainComplete(WekinatorManager m);
	}
	
	
	public File SETTINGS_DIR=new File(".");
	private LearningSystem learnsys;
	private LearningAlgorithm[] learnarray;
	private SimpleDataset data;
	
	private int numInParams;
	private int numOutParams;
	
	private boolean[] everythingever;

	public WekinatorManager(int numInParams, int numOutParams) {
		this.numInParams = numInParams;
		this.numOutParams = numOutParams;
		learnsys = makeLearningSystem(numOutParams);
		learnarray = new LearningAlgorithm[numOutParams];
		for (int i=0;i<numOutParams;i++) {
			learnarray[i] = new NNLearningAlgorithm();
		}
		data = makeDataset(numInParams, numOutParams);
		learnsys.setDataset(data);
		learnsys.setLearners(learnarray);
        everythingever = new boolean[numInParams];
        for (int i = 0; i < numInParams; i++) {
            //  learnerEnabled[i] = true;
        	everythingever[i] = true;
        }
	}
	
	public WekinatorManager(String name, int numInParams, int numOutParams, File folder) {
		try {
			SETTINGS_DIR = folder;
			this.numInParams = numInParams;
			this.numOutParams = numOutParams;
			File savefolder = SETTINGS_DIR;
			if (!savefolder.exists()) new File(savefolder, "test").mkdirs();
			System.out.println("Inflating learnsys!");
			learnsys = LearningSystem.readFromFile(new File(savefolder, "learnsys.yay"));
			learnarray = new LearningAlgorithm[numOutParams];
			for (int i=0;i<numOutParams;i++) {
				System.out.println("Inflating learnalg: "+i);
				learnarray[i] = LearningAlgorithm.readFromFile(new File(savefolder, "learnalg_"+i+".yay"));
			}
			System.out.println("Inflating dataset!");
			data = SimpleDataset.readFromFile(new File(savefolder, "dataset.yay"));
			learnsys.setDataset(data);
			learnsys.setLearners(learnarray);
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	/**
	 * Load from this exact folder.
	 * @param folder
	 * @throws Exception 
	 */
	public WekinatorManager(File folder) throws Exception {
		//try {
			loadSettings(new File(folder, "wekasettings.yay"));
			SETTINGS_DIR = folder;
			File savefolder = folder;
			if (!savefolder.exists()) new File(savefolder, "test").mkdirs();
			learnsys = LearningSystem.readFromFile(new File(savefolder, "learnsys.yay"));
			learnarray = new LearningAlgorithm[numOutParams];
			for (int i=0;i<numOutParams;i++) {
				learnarray[i] = LearningAlgorithm.readFromFile(new File(savefolder, "learnalg_"+i+".yay"));
			}
			data = SimpleDataset.readFromFile(new File(savefolder, "dataset.yay"));
			learnsys.setDataset(data);
			learnsys.setLearners(learnarray);
		//}catch(Exception e) { e.printStackTrace(); }
	}
	
	private void loadSettings(File infile) throws IOException {
		FileInputStream f = new FileInputStream(infile);
		Properties p = new Properties();
		p.load(f);
		
		numInParams = Integer.parseInt((String) p.get("insize"));
		numOutParams = Integer.parseInt((String) p.get("outsize"));
//		name = (String) p.get("name");

	}
	
//	public String getName() {
//		return name;
//	}
	
	public void setSaveDir(File dir) {
		this.SETTINGS_DIR = dir;
	}
	public File getSaveDir() {
		return SETTINGS_DIR ;
	}
		
	public File save() {
		System.out.println("Savefolder: settingsdir:"+SETTINGS_DIR);
		File savefolder = SETTINGS_DIR;
		try {
			if (!savefolder.exists()) savefolder.mkdirs();
			saveSettings(new File(savefolder, "wekasettings.yay"));
			learnsys.writeToFile(new File(savefolder, "learnsys.yay"));
			for (int i=0;i<learnarray.length;i++) {
				learnarray[i].writeToFile(new File(savefolder, "learnalg_"+i+".yay"));
			}
			data.writeToFile(new File(savefolder, "dataset.yay"));
			return savefolder;
		}catch(Exception e) { e.printStackTrace(); }
		return null;
	}
	
	private void saveSettings(File outfile) throws IOException {
		FileOutputStream f = new FileOutputStream(outfile);
		Properties p = new Properties();
		p.put("insize", ""+numInParams);
		p.put("outsize", ""+numOutParams);
//		p.put("name", ""+name);
		p.store(f, "testing");
		f.close();
	}
	
	public File save(File outfolder) {
//		this.name = name;
		this.SETTINGS_DIR = outfolder;
		return save();
	}
	
	private LearningSystem makeLearningSystem(int numOutParams) {
		boolean[] falseArray = new boolean[numOutParams];
		for (int i=0; i<falseArray.length; i++) falseArray[i] = false;
		int[] intArray = new int[numOutParams];
		for (int i=0; i<falseArray.length; i++) intArray[i] = 1;
		return new LearningSystem(numOutParams, falseArray, intArray);
	}
	
	private SimpleDataset makeDataset(int numInParams, int numOutParams) {
		boolean[] falseArray = new boolean[numOutParams];
		for (int i=0; i<falseArray.length; i++) falseArray[i] = false;
		int[] intArray = new int[numOutParams];
		for (int i=0; i<falseArray.length; i++) intArray[i] = 1;
		String[] inNames = new String[numInParams];
		for (int i=0; i<inNames.length; i++) inNames[i] = "in"+i;
		String[] outNames = new String[numOutParams];
		for (int i=0; i<outNames.length; i++) outNames[i] = "out"+i;
		return new SimpleDataset(numInParams, numOutParams, falseArray, intArray, inNames, outNames);
	}
	
	
	
	public void addToTrain(double[] in, double[] out) {
		addToTrain(in, out, null);
	}
	public void addToTrain(double[] in, double[] out, boolean[] filter) {
		learnsys.setParamMask(filter);
		learnsys.addToTraining(in, out);
		System.out.println("Size of training set:"+learnsys.getDataset().getNumDatapoints());
	}
	
	public void train() {
		for (int i=0; i<learnarray.length;i++) {
			learnsys.train(i);
		}
	}
	
	public void train(int i) {
		learnsys.train(i);
	}
	
	public void trainMultithread() {
		Collection<Callable<Void>> stuff = new ArrayList<Callable<Void>>();
		for (int i=0; i<learnarray.length; i++) {
			final int tasknum = i;
			Callable<Void> task = new Callable<Void>() {
				public Void call() throws Exception {
					System.out.println("Training : "+tasknum);
					learnsys.train(tasknum);
					System.out.println("finished : "+tasknum);
					return null;
				}};
			stuff.add(task);
		}
		ExecutorService execpool = Executors.newFixedThreadPool(MAX_THREAD_COUNT);
		try {
			for (Future f : execpool.invokeAll(stuff)) {
				f.get();
			}
		} catch (Exception e) {
		}
	}
	
	public double[] classify(double[] in) {
		return learnsys.classify(in);
	}

	public int getSamples() {
		return learnsys.getDataset().getNumDatapoints();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((SETTINGS_DIR == null) ? 0 : SETTINGS_DIR.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + Arrays.hashCode(everythingever);
		result = prime * result + Arrays.hashCode(learnarray);
		result = prime * result
				+ ((learnsys == null) ? 0 : learnsys.hashCode());
		result = prime * result + numInParams;
		result = prime * result + numOutParams;
		return result;
	}

	

	
	
	
	
	
}
