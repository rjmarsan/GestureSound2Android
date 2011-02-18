package wekinator.controller;

import java.io.File;

import wekinator.LearningSystem;
import wekinator.SimpleDataset;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;

public class WekinatorManager {
	public File SETTINGS_DIR;
	public String name;
	private LearningSystem learnsys;
	private LearningAlgorithm[] learnarray;
	private SimpleDataset data;

	public WekinatorManager(int numInParams, int numOutParams) {
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
			learnsys = LearningSystem.readFromFile(new File(folder, name+"_learnsys.yay"));
			learnarray = new LearningAlgorithm[numInParams];
			for (int i=0;i<numInParams;i++) {
				learnarray[i] = LearningAlgorithm.readFromFile(new File(folder, name+"_learnalg_"+i+".yay"));
			}
			data = SimpleDataset.readFromFile(new File(folder, name+"_dataset.yay"));
			learnsys.setDataset(data);
			learnsys.setLearners(learnarray);
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public void save() {
		try {
			learnsys.writeToFile(new File(SETTINGS_DIR, name+"_learnsys.yay"));
			for (int i=0;i<learnarray.length;i++) {
				learnarray[i].writeToFile(new File(SETTINGS_DIR, name+"_learnalg_"+i+".yay"));
			}
			data.writeToFile(new File(SETTINGS_DIR, name+"_dataset.yay"));
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public void save(File folder) {
		try {
			SETTINGS_DIR = folder;
			learnsys.writeToFile(new File(SETTINGS_DIR, name+"_learnsys.yay"));
			for (int i=0;i<learnarray.length;i++) {
				learnarray[i].writeToFile(new File(SETTINGS_DIR, name+"_learnalg_"+i+".yay"));
			}
			data.writeToFile(new File(SETTINGS_DIR, name+"_dataset.yay"));
		}catch(Exception e) { e.printStackTrace(); }
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
	
	public double[] classify(double[] in) {
		return learnsys.classify(in);
	}

	
	
	
}
