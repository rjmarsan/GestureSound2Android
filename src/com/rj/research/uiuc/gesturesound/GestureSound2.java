package com.rj.research.uiuc.gesturesound;

import processing.core.PApplet;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class GestureSound2 extends PApplet {
	WekaInstrument instrument;
	HIDViewController hid;

	public void onCreate(Bundle savedinstance) {
		super.onCreate(savedinstance);
		setupWeka();
	}
    
    public void setupWeka() {
    	View v = this.getLayoutInflater().inflate(com.rj.research.uiuc.gesturesound.R.layout.wekainstrument, null);
    	this.addContentView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	hid = this.findViewById(com.rj.research.uiuc.gesturesound.R.id.hid)
    }
    
    public void setup() {
    	instrument = new WekaInstrument();
    }
    
    
    
    public void draw() {
    	background(100);
    }

}