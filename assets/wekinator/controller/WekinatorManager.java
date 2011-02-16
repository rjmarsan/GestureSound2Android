package wekinator.controller;

import java.io.File;

import wekinator.LearningSystem;
import wekinator.SimpleDataset;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;

public class WekinatorManager {
//	public final static String SETTINGS_DIR = "./settings/";
//	public String name;
//	private LearningSystem learnsys;
//	private LearningAlgorithm[] learnarray;
//	private SimpleDataset data;

	public WekinatorManager(int numInParams, int numOutParams) {
//		learnsys = makeLearningSystem(numOutParams);
//		learnarray = new LearningAlgorithm[numInParams];
//		for (int i=0;i<numInParams;i++) {
//			learnarray[i] = new NNLearningAlgorithm();
//		}
//		data = makeDataset(numInParams, numOutParams);
//		learnsys.setDataset(data);
//		learnsys.setLearners(learnarray);
	}
	
	public WekinatorManager(String name, int numInParams, int numOutParams) {
//		try {
//			learnsys = LearningSystem.readFromFile(new File(SETTINGS_DIR+name+"_learnsys.yay"));
//			learnarray = new LearningAlgorithm[numInParams];
//			for (int i=0;i<numInParams;i++) {
//				learnarray[i] = LearningAlgorithm.readFromFile(new File(SETTINGS_DIR+name+"_learnalg_"+i+".yay"));
//			}
//			data = SimpleDataset.readFromFile(new File(SETTINGS_DIR+name+"_dataset.yay"));
//			learnsys.setDataset(data);
//			learnsys.setLearners(learnarray);
//		}catch(Exception e) { e.printStackTrace(); }
	}
	
	private LearningSystem makeLearningSystem(int numOutParams) {
//		boolean[] falseArray = new boolean[numOutParams];
//		for (int i=0; i<falseArray.length; i++) falseArray[i] = false;
//		int[] intArray = new int[numOutParams];
//		for (int i=0; i<falseArray.length; i++) intArray[i] = 1;
//		return new LearningSystem(numOutParams, falseArray, intArray);
		return null;
	}
	
	private SimpleDataset makeDataset(int numInParams, int numOutParams) {
//		boolean[] falseArray = new boolean[numOutParams];
//		for (int i=0; i<falseArray.length; i++) falseArray[i] = false;
//		int[] intArray = new int[numOutParams];
//		for (int i=0; i<falseArray.length; i++) intArray[i] = 1;
//		String[] inNames = new String[numInParams];
//		for (int i=0; i<inNames.length; i++) inNames[i] = "in"+i;
//		String[] outNames = new String[numOutParams];
//		for (int i=0; i<outNames.length; i++) outNames[i] = "out"+i;
//		return new SimpleDataset(numInParams, numOutParams, falseArray, intArray, inNames, outNames);
		return null;
	}
	
	
	
	public void addToTrain(double[] in, double[] out) {
//		learnsys.addToTraining(in, out);
	}
	
	public void train() {
//		for (int i=0; i<learnarray.length;i++) {
//			learnsys.train(i);
//		}
	}
	
	public double[] classify(double[] in) {
//		return learnsys.classify(in);
		return null;
	}

	
	
	
}
