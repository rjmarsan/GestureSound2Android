package com.rj.research.uiuc.gesturesound.gestures.qualities;

import Jama.Matrix;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.filters.KalmanFilter;
import com.rj.research.uiuc.gesturesound.gestures.GestureEngine;

public class Velocity extends Quality {
	public static String name="velocity";

	KalmanFilter filter;
	
	float currentValue;
	
	public static Quality cursorDetected(GestureEngine engine) {
		return new Velocity(engine);
	}
	public Velocity(GestureEngine engine) {
		super(engine);
		filter = KalmanFilter.buildKF(0.2, 5, 10);
		filter.setX(new Matrix(new double[][]{{0.01}, {0.01}, {0.01}}));
		filter.predict();
	}

	@Override
	public float update(Cursor in) {
		float val=0.0f;
		
		val =findVelocity(in);
//		//System.out.println("Curvature: "+val);
//		filter.correct(new Matrix(new double[][]{{val}}));
//		filter.predict();
//		//System.out.println("0:"+filter.getX().get(0,0));
//		//System.out.println("1:"+filter.getX().get(1,0));
//		//System.out.println("2:"+filter.getX().get(2,0));
//		val = (float) filter.getX().get(0,0);
//		System.out.println("Velocity: "+val);
		currentValue = val;
		return val;
	}
	private float findVelocity(Cursor in) {
		return in.getVelocityVector().length();

	}

	@Override
	public float getCurrentValue() {
		return currentValue;
	}
	
}