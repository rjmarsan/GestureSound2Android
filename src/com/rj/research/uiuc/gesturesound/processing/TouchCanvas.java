package com.rj.research.uiuc.gesturesound.processing;

import java.util.ArrayList;

import processing.core.PApplet;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.MTManager;
import com.rj.processing.mt.TouchListener;

public class TouchCanvas implements TouchListener {
	MTManager m;
	
	public TouchCanvas(MTManager m) {
		this.m = m;
	}
	
	public void draw(PApplet p) {
		p.pushStyle();
		p.stroke(0,200,0);
		p.fill(0,100,0);
		p.rectMode(PApplet.CENTER);
		for (Cursor c : (ArrayList<Cursor>)m.cursors.clone()) {
			if (c != null && c.currentPoint != null)
				p.rect(c.currentPoint.x, c.currentPoint.y, 50, 50);
		}
		p.popStyle();
	}

	@Override
	public void touchAllUp(Cursor c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void touchDown(Cursor c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void touchMoved(Cursor c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void touchUp(Cursor c) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
