package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

/**
 * this class calculates the average, min, and max of the history, with exponential fallout
 * @author rj
 *
 */
public class JustHistoryGenerator extends FeatureGenerator {
	public final static int LENGTH = 6;
	public final static int TOTAL_LENGTH = LENGTH;
	
	public final static int INTERNAL_LENGTH = 1 << (LENGTH+1);
	
	
	public double[] data;
	private double[] dataIn;
	private int samples_seen = 0;
	
	private int focusType = -1;
	
	public JustHistoryGenerator(int focustype) {
		super(focustype);
		this.focusType = focustype;
		data = new double[TOTAL_LENGTH];
		dataIn = new double[INTERNAL_LENGTH];
		for (int i=0;i<TOTAL_LENGTH;i++) data[i]=0f;
		for (int i=0;i<INTERNAL_LENGTH;i++) dataIn[i]=0f;
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
		int last_space = 0;
		int size = 0;
		for (int i=0;i<LENGTH;i++) {
				avgness = 0;
				
				size = 1<<i;
			if (samples_seen >= last_space+size) { //only do this if we've seen enough samples.
				for (int ir=0;ir < size ; ir++) {
					avgness = avgness + dataIn[last_space+ir];
				}
				last_space = last_space + size;
//				System.out.println(" "+size+" samples! "+avgness+":"+avgness/size);
				data[i] = avgness/size;
			}
		}
		return data;
	}
	
	
	
}
