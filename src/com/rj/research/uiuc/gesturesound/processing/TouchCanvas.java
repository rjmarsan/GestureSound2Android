package com.rj.research.uiuc.gesturesound.processing;

import processing.core.PApplet;
import processing.core.PFont;

import com.rj.processing.mt.Cursor;
import com.rj.processing.mt.MTManager;
import com.rj.processing.mt.Point;
import com.rj.processing.mt.TouchListener;
import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;
import com.rj.research.uiuc.gesturesound.gestures.generators.QuadraticFeatureBox;

public class TouchCanvas implements TouchListener {
	MTManager m;
	WekaInstrument weka;

	private final static int[] CUR_TRAIL_STRK = {100,100,100};
	private final static int[] CUR_TRAIL_FILL = {60,60,60};
	private final static int[] GRID_STRK = {55,55,55};
	private final static int[] GRID_FILL = {0,0,0,0};
	private final static int GRID_LINES = 8;
	private final static int[] GEST_VAL_STRK = {0,0,200};
	private final static int[] GEST_VAL_FILL = {0,0,100};

	
	PFont font;
	
	public TouchCanvas(MTManager m, WekaInstrument weka) {
		this.m = m;
		this.weka = weka;
	}
	
	public void draw(PApplet p) {
		if (font == null) {
			font = p.loadFont("MyriadPro-Regular-30.vlw");
			p.textFont(font);
			//p.textMode(PApplet.SCREEN);
		}
		drawGrid(p);
		drawCursorTrail(p);
		drawFeatureStats(p);
	}
	
	private void drawGrid(PApplet p) {
		p.pushStyle();
		p.stroke(GRID_STRK[0],GRID_STRK[1],GRID_STRK[2]);
		p.noFill();
		
		//cartesian grid
		float spacing = p.width/GRID_LINES;
		float ybuffer = (p.height%spacing) / 2f;
		float centerx = p.width/2f;
		float centery = p.height/2f;
		for (int i=0; i<GRID_LINES; i++) {
			p.line(0, ybuffer+spacing*i, p.width, ybuffer+spacing*i);
			p.line(spacing*i, 0, spacing*i, p.height);
			p.ellipse(centerx, centery, spacing*i, spacing*i);
		}
		
		
		p.popStyle();
	}
	
	private void drawCursorTrail(PApplet p) {
		p.pushStyle();
		p.rectMode(PApplet.CENTER);
		p.ellipseMode(PApplet.CENTER);
		
		
		synchronized (m.cursors) {
			Cursor firstCursor = null;
			for (Cursor c : m.cursors)
				if (c != null && c.curId == 0) firstCursor = c;
			for (Cursor c : m.cursors) {
				if (c != null && c.currentPoint != null) {
					p.stroke(CUR_TRAIL_STRK[0],CUR_TRAIL_STRK[1],CUR_TRAIL_STRK[2]);
					p.fill(CUR_TRAIL_FILL[0],CUR_TRAIL_FILL[1],CUR_TRAIL_FILL[2]);
					Point nextPoint = null;
					final int max = Math.min(20, c.points.size()-2);
					for (int x = 0; x < max; x++) {
						Point point = c.points.get(c.points.size()-x-1);
						if (nextPoint != null) {
							p.line(nextPoint.x, nextPoint.y, point.x, point.y);
							p.rect(point.x, point.y, 5, 5);
						}
						nextPoint = point;
					}
					if (firstCursor != null) {
						p.stroke(0,200,200);
						p.fill(0,100,100);
						p.line(firstCursor.currentPoint.x, firstCursor.currentPoint.y, c.currentPoint.x, c.currentPoint.y);
					}
					
					p.noFill();
					if (c.curId == 0) {
						p.stroke(220,220,220);
					} else {
						float x = 200;
						switch (c.curId % 4){
						case 0:
							p.stroke(x/10f,x,x);
							break;
						case 1:
							p.stroke(x,x,x/10f);
							break;
						case 2:
							p.stroke(x,x/10f,x);
							break;
						case 3:
							p.stroke(x*1.2f,x/10f,x/10f);
							break;
						}
					}
					p.ellipse(c.currentPoint.x, c.currentPoint.y, p.width/13f, p.width/13f);
					p.text(""+c.curId,c.currentPoint.x-p.width/26f, c.currentPoint.y-p.width/26f);


				}
			}
		}
		p.popStyle();
	}
	
	
	private void drawFeatureStats(PApplet p) {
		p.pushStyle();
		p.stroke(GEST_VAL_STRK[0],GEST_VAL_STRK[1],GEST_VAL_STRK[2]);
		p.fill(GEST_VAL_FILL[0],GEST_VAL_FILL[1],GEST_VAL_FILL[2]);
		p.rectMode(PApplet.CORNER);

		FeatureMap map = QuadraticFeatureBox.currentFeatures;
		if (map == null) return;
		float width = p.width/20;
		float scale = 40;
		for (int i=0;i<map.featurevec.length;i++) {
			int vectype = map.featuretypes[i];
			float val = (float)map.featurevec[i];
			p.rect(p.width-width*(i+1), scale, width, val*scale);
			
		}
		
		p.popStyle();
	}
	
	private void drawParameters(PApplet p) {
		Parameter[] params = weka.instrument.getInstrumentParameters();
		FeatureMap map = QuadraticFeatureBox.currentFeatures;
		
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
