package com.rj.research.uiuc.gesturesound;

import processing.core.PApplet;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.rj.processing.mt.MTManager;
import com.rj.research.uiuc.gesturesound.android.HUDViewController;
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
    	instrument = new WekaInstrument();
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
    	mHandler.post(new Runnable() {
    		public void run() {
		    	instrument.setupTest();				
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

}