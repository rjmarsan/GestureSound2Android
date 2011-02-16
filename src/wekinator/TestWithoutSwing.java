package wekinator;

import java.io.File;

import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;

public class TestWithoutSwing {
	public static void main(String[] args) {
		test();
		test2();
	}
		
	
	public static void testoriginal() {
////		WekinatorInstance i = WekinatorInstance.getWekinatorInstance();
//		WekinatorLearningManager l = WekinatorLearningManager.getInstance();
//		LearningSystem ls = new LearningSystem(3, new boolean[] {false, false, false}, new int[] {1000,1000, 1});
////		i.setLearningSystem(ls);
//		//    public SimpleDataset(int numFeatures, int numParams, boolean[] isParamDiscrete, int[] numParamValues, String[] featureNames, String[] paramNames) {
//		SimpleDataset d = new SimpleDataset(3,3, new boolean[] {false, false, false}, new int[] {1,1,1}, new String[] {"Something","Something2","asdf1"}, new String[] {"param1", "param2","paramasdf"});
//		ls.setDataset(d);
//		LearningAlgorithm[] learners = new LearningAlgorithm[]{new NNLearningAlgorithm(), new NNLearningAlgorithm(), new NNLearningAlgorithm()};	
//		ls.setLearners(learners);
//		try {
//			OscSynthProxy.setup(3311);
//		} catch (Exception e) { e.printStackTrace(); }
//		
//		l.setMode(Mode.RUNNING);
////		State s = i.getState();
////		System.out.println("State! : "+s);
//		
//		System.out.println("State of learning: "+l.getMode());
//		
//		//l.setParams(new double[] {5,5,3});
////		l.updateFeatures(new double[] {1,2,3});
////		double[] out = l.getOutputs();
////		for (int q=0; q<out.length;q++) {
////			System.out.println("Out! "+out[q]);
////		}
//		System.out.println("Training!");
//
//		
//		//only needed when we're using the learningmanager
//		l.setMode(Mode.DATASET_CREATION);
////		l.setParams(new double[] {5.5, 9.9});
////		l.setOutputs(new double[] {5.5, 9.9});
////		l.updateFeatures(new double[] {1,1,1});
////		l.setParams(new double[] {1.1, 2.2});
////		l.setOutputs(new double[] {1.1, 2.2});
////		l.setParams(new double[] {5.5, 9.9, 11.11});
//		//l.setOutputs(new double[] {5.5, 9.9, 11.11});
////		l.updateFeatures(new double[] {1,1,1});
////		l.setParams(new double[] {1.1, 2.2, 3.3});
//		//l.setOutputs(new double[] {1.1, 2.2, 3.3})
//		
////		l.updateFeatures(new double[] {6,6,6});
//		
//		//Goign through WekinatorLearningManager is a bit blackbox-ish right now.  this is all it calls.
////		WekinatorInstance.getWekinatorInstance().getLearningSystem().addToTraining(new double[] {1,1,1}, new double[] {5.5, 9.9, 11.11});
////		WekinatorInstance.getWekinatorInstance().getLearningSystem().addToTraining(new double[] {2,2,2}, new double[] {3.3, 5.5, 8.8});
////		WekinatorInstance.getWekinatorInstance().getLearningSystem().addToTraining(new double[] {3,3,3}, new double[] {1.1, 2.2, 3.3});
//		ls.addToTraining(new double[] {1,1,1}, new double[] {5.5, 9.9, 11.11});
//		ls.addToTraining(new double[] {2,2,2}, new double[] {3.3, 5.5, 8.8});
//		ls.addToTraining(new double[] {3,3,3}, new double[] {1.1, 2.2, 3.3});
//		
//		//train all the parameters. DONT FORGET TO SET LEARNERS FIRST
//		ls.train(0);
//		ls.train(1);
//		ls.train(2);
//		
//		System.out.println("Running!");
//		
//		l.setMode(Mode.RUNNING);
////		try {
////			Thread.sleep(1000);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//		
////		printDoubleArray(WekinatorInstance.getWekinatorInstance().getLearningSystem().classify(new double[] {1,1,1}));
////		printDoubleArray(WekinatorInstance.getWekinatorInstance().getLearningSystem().classify(new double[] {2,2,2}));
////		printDoubleArray(WekinatorInstance.getWekinatorInstance().getLearningSystem().classify(new double[] {3,3,3}));
////		printDoubleArray(WekinatorInstance.getWekinatorInstance().getLearningSystem().classify(new double[] {4,4,4}));
//		printDoubleArray(ls.classify(new double[] {1,1,1}));
//		printDoubleArray(ls.classify(new double[] {2,2,2}));
//		printDoubleArray(ls.classify(new double[] {3,3,3}));
//		printDoubleArray(ls.classify(new double[] {4,4,4}));
////		OscSynthProxy.setParams(out);
//		
////		l.updateFeatures(new double[] {1,1,1});
////		out = l.getOutputs();
////		for (int q=0; q<out.length;q++) {
////			System.out.println("Out! "+out[q]);
////		}
////		
////		l.updateFeatures(new double[] {6,6,6});
////		out = l.getOutputs();
////		for (int q=0; q<out.length;q++) {
////			System.out.println("Out! "+out[q]);
////		}
////		
////		l.updateFeatures(new double[] {3,3,3});
////		out = l.getOutputs();
////		for (int q=0; q<out.length;q++) {
////			System.out.println("Out! "+out[q]);
////		}
//
//
//		
//		
//
//		
	}
	
	
	public static void printDoubleArray(double[] out) {
		for (int q=0; q<out.length;q++) {
			System.out.println("Out! "+out[q]);
		}
	}
	
	
	
	
	
	public static LearningSystem makeLs() {
		LearningSystem ls = new LearningSystem(3, new boolean[] {false, false, false}, new int[] {1000,1000, 1});
		SimpleDataset d = new SimpleDataset(3,3, new boolean[] {false, false, false}, new int[] {1,1,1}, new String[] {"Something","Something2","asdf1"}, new String[] {"param1", "param2","paramasdf"});
		ls.setDataset(d);
		
		LearningAlgorithm[] learners = new LearningAlgorithm[]{new NNLearningAlgorithm(), new NNLearningAlgorithm(), new NNLearningAlgorithm()};	
		ls.setLearners(learners);
		return ls;
	}
	
	public static void test() {
		LearningSystem ls = new LearningSystem(3, new boolean[] {false, false, false}, new int[] {1000,1000, 1});
		SimpleDataset d = new SimpleDataset(3,3, new boolean[] {false, false, false}, new int[] {1,1,1}, new String[] {"Something","Something2","asdf1"}, new String[] {"param1", "param2","paramasdf"});
		ls.setDataset(d);
		LearningAlgorithm[] learners = new LearningAlgorithm[]{new NNLearningAlgorithm(), new NNLearningAlgorithm(), new NNLearningAlgorithm()};	
		ls.setLearners(learners);
//		try {
//			OscSynthProxy.setup(3311);
//		} catch (Exception e) { e.printStackTrace(); }
		
		
		
		System.out.println("Training!");

		
		//no more wekinatorlearningmanager! straight up learningsystem.
		ls.addToTraining(new double[] {1,1,1}, new double[] {5.5, 9.9, 11.11});
		ls.addToTraining(new double[] {2,2,2}, new double[] {3.3, 5.5, 8.8});
		ls.addToTraining(new double[] {3,3,3}, new double[] {1.1, 2.2, 3.3});
		
		//train all the parameters. DONT FORGET TO SET LEARNERS FIRST
		ls.train(0);
		ls.train(1);
		ls.train(2);
		
		System.out.println("Running!");
		

		printDoubleArray(ls.classify(new double[] {1,1,1}));
		printDoubleArray(ls.classify(new double[] {2,2,2}));
		printDoubleArray(ls.classify(new double[] {3,3,3}));
		printDoubleArray(ls.classify(new double[] {4,4,4}));
//		OscSynthProxy.setParams(ls.classify(new double[] {4,4,4}));
		try {
			System.out.println("writing file to disk!");
			d.writeToFile(new File("./test.yay"));
			ls.writeToFile(new File("./testls.yay"));
			int i=0;
			for (LearningAlgorithm learn : learners) {
				learn.writeToFile(new File("./testdata"+i+".yay"));
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test2() {
		System.out.println("Loading file from disk!");
		try {
			LearningSystem ls = LearningSystem.readFromFile(new File("./testls.yay"));
			SimpleDataset d;
			d = SimpleDataset.readFromFile(new File("./test.yay"));
			ls.setDataset(d);
			
			LearningAlgorithm[] learners = new LearningAlgorithm[3];	
			for (int i=0; i<3; i++) {
				learners[i] = NNLearningAlgorithm.readFromFile(new File("./testdata"+i+".yay"));
			}
			ls.setLearners(learners);

			System.out.println("trying with disk values!");

			printDoubleArray(ls.classify(new double[] {1,1,1}));
			printDoubleArray(ls.classify(new double[] {2,2,2}));
			printDoubleArray(ls.classify(new double[] {3,3,3}));
			printDoubleArray(ls.classify(new double[] {4,4,4}));

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		

	}
	
	
}
