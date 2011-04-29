package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureExtractor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class QuadraticFeatureBox extends FeatureBox {
	public static FeatureMap currentFeatures;
	public static int FEATURE_BOX_LENGTH = 6;
	public static int MAX_GENERATORS = 2;
	public static int CURRENT_MAX_FEATURES_GENERATED = FEATURE_BOX_LENGTH * MAX_GENERATORS;
	
	FeatureExtractor extractor;
	FeatureGenerator[] generators;

	Cursor cursor;
	
	double[] featvec;
		
	public QuadraticFeatureBox(Cursor c, Parameter p) {
		this.cursor = c;
		int[] qualities = p.getQualities();
		float[] trendiness = p.getTrendiness();
		
		this.extractor = new FeatureExtractor();
		this.generators = new FeatureGenerator[MAX_GENERATORS];
		for (int i=0; i < MAX_GENERATORS; i++){
			if (qualities.length > i)
				this.generators[i] = new QuadraticContinuousHistoryGenerator(qualities[i], FEATURE_BOX_LENGTH, trendiness[i], 1);
			else
				this.generators[i] = new FeatureGenerator(FeatureMap.NOP, FEATURE_BOX_LENGTH);
		};
		
		featvec = new double[numFeaturesGenerated()];
	}
	
	
	public int numFeaturesGenerated() {
		int num = 0;
		for (FeatureGenerator f : generators) {
			num += f.getLength();
		}
		return num;
	}
	
	
	public double[] makeFeatureVector(Cursor c) {
		FeatureMap features = extractor.makeFeatureMap(c);
		currentFeatures = features;
				
		for (int i=0;i<featvec.length;i++) featvec[i]=0;
		
		int offset = 0;
		for (FeatureGenerator featgen : generators) {
			double[] generated = featgen.update(features);
//			System.out.println("Generated:"+generated[0]);
			writeAll(featvec, generated, offset);
			offset += generated.length;
		}
		
		return featvec;
	}
	
	private int writeAll(double[] out, double[] in, int start) {
		for (int i=0;i<in.length;i++) {
			out[start+i] = in[i];
		}
		return in.length+start;
	}


}
