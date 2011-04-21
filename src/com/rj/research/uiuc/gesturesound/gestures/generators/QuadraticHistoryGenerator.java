package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

/**
 * this class calculates the average with quadratic fallout
 * @author rj
 *
 */
public class QuadraticHistoryGenerator extends FeatureGenerator {
	public int LENGTH;
	public int TOTAL_LENGTH;
	
	public int INTERNAL_LENGTH;
	
	
	public double[] data;
	private double[] dataIn;
	private int samples_seen = 0;
	private float a; //the a parameter in ax^2 +bx +c
	
	private int focusType = -1;
	
	public QuadraticHistoryGenerator(int focustype) {
		this(focustype, 8, 1);
	}
	public QuadraticHistoryGenerator(int focustype, int length, float a ) {
		super(focustype);
		this.focusType = focustype;
		setLength(length, a);
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
		//add the newest value to the end!
		dataIn[0] = val;
		
		//now average!
		double avgness = 0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		int last_space = 0;
		int size = 0;
//		System.out.println("update:"+a);
		for (int i=0;i<LENGTH;i++) {
				avgness = 0;
				
				size = (int)(i*i*a);
//				System.out.println("a:"+a);
				if (samples_seen >= last_space+size) { //only do this if we've seen enough samples.
					for (int ir=0;ir < size ; ir++) {
						avgness = avgness + dataIn[last_space+ir];
						if (dataIn[last_space+ir] > max) max = dataIn[last_space+ir];
						if (dataIn[last_space+ir] < min) min = dataIn[last_space+ir];
					}
					last_space = last_space + size;
	//				System.out.println(" "+size+" samples! "+avgness+":"+avgness/size);
					data[i] = avgness/size;
				}
		}
		return data;
	}
	
	
	
}
