package wekinator.controller;

import java.io.File;

import wekinator.LearningSystem;
import wekinator.SimpleDataset;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;

public class WekinatorManager {
	
	public interface TrainCallback {
		public void trainComplete(WekinatorManager m);
	}
	
	
	public File SETTINGS_DIR;
	public String name;
	private LearningSystem learnsys;
	private LearningAlgorithm[] learnarray;
	private SimpleDataset data;

	public WekinatorManager(int numInParams, int numOutParams) {
		name = "Test___";
		learnsys = makeLearningSystem(numOutParams);
		learnarray = new LearningAlgorithm[numOutParams];
		for (int i=0;i<numOutParams;i++) {
			learnarray[i] = new NNLearningAlgorithm();
		}
		data = makeDataset(numInParams, numOutParams);
		learnsys.setDataset(data);
		learnsys.setLearners(learnarray);
	}
	
	public WekinatorManager(String name, int numInParams, int numOutParams, File folder) {
		try {
			SETTINGS_DIR = folder;
			File savefolder = new File(SETTINGS_DIR, name);
			if (!savefolder.exists()) new File(savefolder, "test").mkdirs();
			learnsys = LearningSystem.readFromFile(new File(savefolder, "learnsys.yay"));
			learnarray = new LearningAlgorithm[numInParams];
			for (int i=0;i<numInParams;i++) {
				learnarray[i] = LearningAlgorithm.readFromFile(new File(savefolder, "learnalg_"+i+".yay"));
			}
			data = SimpleDataset.readFromFile(new File(savefolder, "dataset.yay"));
			learnsys.setDataset(data);
			learnsys.setLearners(learnarray);
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public String getName() {
		return name;
	}
	
	public void setSaveDir(File dir) {
		this.SETTINGS_DIR = dir;
	}
		
	public File save() {
		File savefolder = new File(SETTINGS_DIR, name);
		try {
			if (!savefolder.exists()) new File(savefolder, "test").mkdirs();
			learnsys.writeToFile(new File(savefolder, "learnsys.yay"));
			for (int i=0;i<learnarray.length;i++) {
				learnarray[i].writeToFile(new File(savefolder, "learnalg_"+i+".yay"));
			}
			data.writeToFile(new File(savefolder, "dataset.yay"));
			return savefolder;
		}catch(Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public File save(String name) {
		this.name = name;
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
		ThreadGroup group = new ThreadGroup("TrainingGroup");
		Thread tasks[] = new Thread[learnarray.length];
		for (int i=0; i<learnarray.length;i++) {
			final int task = i;
			Runnable t = new Runnable() {
				public void run() {
					learnsys.train(task);
				}
			};
			tasks[i] = new Thread(group, t);
		}
		for (Thread t : tasks) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public double[] classify(double[] in) {
		return learnsys.classify(in);
	}

	public int getSamples() {
		return learnsys.getDataset().getNumDatapoints();
	}

	
	
	
}
