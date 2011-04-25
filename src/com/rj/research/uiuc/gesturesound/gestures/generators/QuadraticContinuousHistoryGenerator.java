package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

/**
 * this class calculates the average with quadratic fallout
 * @author rj
 *
 */
public class QuadraticContinuousHistoryGenerator extends FeatureGenerator {
	public static int EARLY_VALUE_AVG = 0;
	public static float NULL_VALUE = 0f;
	
	public int LENGTH;
	public int TOTAL_LENGTH;
	
	public int INTERNAL_LENGTH;
	
	
	public double[] data;
	private double[] dataIn;
	private int samples_seen = 0;
	private float a; //the a parameter in ax^2 +bx +c
	
	private int focusType = -1;
	private float q;
	
	public QuadraticContinuousHistoryGenerator(int focustype) {
		this(focustype, 8, 1, 1f);
	}
	public QuadraticContinuousHistoryGenerator(int focustype, int length, float a, float q ) {
		super(focustype);
		this.focusType = focustype;
		setLength(length, a);
		this.q = q;
	}
	
	public void setLength(int len, float a ) {
		LENGTH = len;
		TOTAL_LENGTH = LENGTH;
		INTERNAL_LENGTH = 0;
		for (int i=1;i<len+1;i++)
			INTERNAL_LENGTH += i*i*a;
//		System.out.println("Internal length: "+INTERNAL_LENGTH);
		data = new double[TOTAL_LENGTH];
		dataIn = new double[INTERNAL_LENGTH];
		for (int i=0;i<TOTAL_LENGTH;i++) data[i]=0f;
		for (int i=0;i<INTERNAL_LENGTH;i++) dataIn[i]=0f;
		this.a=a;
	}
	
	
	@Override
	public int getLength() {
		return TOTAL_LENGTH;
	}

	@Override
	public double[] update(FeatureMap map) {
		double val = map.get(focusType);
		samples_seen ++;
		
		//shift everything over one
		for (int i=((INTERNAL_LENGTH)-1);i>0;i--) {
			dataIn[i] = dataIn[i-1];
		}
		//add the newest value to  ,the end!
		dataIn[0] = val;
		
		//now average!
		int last_space = 0;
		float size = 0;
//		System.out.println("update:"+a);
		for (int i=0;i<LENGTH;i++) {				
				size = i*i*a;
				float weight = (size+1f)/q;
//				System.out.println("a:"+a);
				if (samples_seen >= last_space+size) { //only do this if we've seen enough samples.
					data[i] = (data[i] * (weight-1) + dataIn[last_space])/weight;
					last_space = last_space + (int)size;
				} else {
					data[i] = NULL_VALUE;
				}
		}
		//smooth out early values to see if that causes errors
		int min = EARLY_VALUE_AVG;//(samples_seen < EARLY_VALUE_AVG) ? samples_seen : EARLY_VALUE_AVG;
		int count = 0;
		float average = 0;
		for (int i=0; i<min; i++) {
			if (data[i] != NULL_VALUE) {
				average += data[i];
				count ++;
			}
		}
		average = average/count;
		//now apply the averaged values;
		for (int i=0; i<min; i++) {
			data[i] = average;
		}
		//System.out.println(data[0]);
		return data;
	}
	
	
	
}
