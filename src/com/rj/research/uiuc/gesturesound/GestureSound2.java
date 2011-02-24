package com.rj.research.uiuc.gesturesound;

import java.io.File;

import processing.core.PApplet;
import wekinator.LearningSystem;
import wekinator.SimpleDataset;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;
import wekinator.controller.WekinatorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.rj.processing.mt.MTManager;
import com.rj.research.uiuc.gesturesound.android.HUDViewController;
import com.rj.research.uiuc.gesturesound.audio.instruments.OSCInstrument;
import com.rj.research.uiuc.gesturesound.processing.TouchCanvas;


public class GestureSound2 extends PApplet {
	WekaInstrument instrument;
	HUDViewController hud;
	TouchCanvas canvas;
	MTManager mtManager;
	
	Handler mHandler = new Handler();

	public void onCreate(Bundle savedinstance) {
		super.onCreate(savedinstance);
		setupWeka();
		setupMT();
		setupUI();
		
	}
	
	public void setupMT() {
		mtManager = new MTManager();
		mtManager.addTouchListener(instrument);
	}
	
	public void setupWeka() {
		File settingsDir = new File(getFilesDir(), "settings");
		if (!settingsDir.exists()) settingsDir.mkdirs();
    	instrument = new WekaInstrument(settingsDir);
	}
    
    public void setupUI() {
    	setupHud();
    	setupCanvas();
    }
    
    public void setupHud()  {
    	View v = this.getLayoutInflater().inflate(com.rj.research.uiuc.gesturesound.R.layout.wekainstrument, null);
    	this.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	hud = (HUDViewController)this.findViewById(com.rj.research.uiuc.gesturesound.R.id.hid);
    	hud.setWeka(instrument);
    }
    public void setupCanvas() {
    	canvas = new TouchCanvas(mtManager);
    	mtManager.addTouchListener(canvas);
    }
    
    //processing function
    //CALLED NOT ON MAIN UI THREAD
    public void setup() {
//    	test();
//    	test3();
    	mHandler.post(new Runnable() {
    		public void run() {
//		    	instrument.setupTest();		
//		    	instrument.load(OSCInstrument.name);
    			instrument.start();
			}});
    }
    
    
    
    
    //mt version
    @Override
    public boolean surfaceTouchEvent(MotionEvent me) {
    	if (mtManager != null) mtManager.surfaceTouchEvent(me);
    	
    	return super.surfaceTouchEvent(me);
    }

    
    
    public void draw() {
    	background(100);
    	canvas.draw(this);
    }
    
    
    
    /*
    
    
	public void printDoubleArray(double[] out) {
		for (int q=0; q<out.length;q++) {
			System.out.println("Out! "+out[q]);
		}
	}
	
	public void test() {
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
		File parent = this.getFilesDir();
		try {
			System.out.println("writing file to disk!");
			System.out.println("Writing d");
			d.writeToFile(new File(parent, "./test.yay"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("Writing learnsys");
			ls.writeToFile(new File(parent, "./testls.yay"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int i=0;
			for (LearningAlgorithm learn : learners) {
				System.out.println("Writing learnalgo");
				learn.writeToFile(new File(parent, "./testdata"+i+".yay"));
				i++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test2() {
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
	
	
	public void test3() {
		final WekinatorManager m = new WekinatorManager(10, 2);
		m.addToTrain(new double[] {1,2,3,4,5,6,7,8,9,10}, new double[] {1,2});
		m.addToTrain(new double[] {1,2,3,4,5,6,7,8,9,10}, new double[] {2,2});
		m.addToTrain(new double[] {1,2,3,4,5,6,7,8,9,10}, new double[] {5,2});
		m.addToTrain(new double[] {1,2,3,4,5,6,7,8,9,10}, new double[] {1,6});
		m.addToTrain(new double[] {1,2,3,4,5,6,7,8,9,10}, new double[] {1,2});
		m.train();
		Runnable save = new Runnable() {public void run() {
			System.out.println("---------------Starting to save!");
			m.setSaveDir(getFilesDir());
			m.save("testtest2");
			System.out.println("+++++++++++++++Saved!!!");}};
		new Thread(new ThreadGroup("savegroup"), save, "savethread", 262144).start();
	}

    
    
    
    */
    
    
    
    
    

}