package com.rj.research.uiuc.gesturesound.gestures.generators;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureExtractor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class QuadraticFeatureBox extends FeatureBox {
	public static FeatureMap currentFeatures;
	public static int FEATURE_BOX_LENGTH = 6;
	
	FeatureExtractor extractor;
	FeatureGenerator[] generators;

	Cursor cursor;
	
	double[] featvec;
		
	public QuadraticFeatureBox(Cursor c, Parameter p) {
		this.cursor = c;
		int[] qualities = p.getQualities();
		float[] trendiness = p.getTrendiness();
		
		this.extractor = new FeatureExtractor();
		this.generators = new FeatureGenerator[qualities.length];
		for (int i=0; i<qualities.length; i++){
			this.generators[i] = new QuadraticHistoryGenerator(qualities[i], FEATURE_BOX_LENGTH, trendiness[i]);
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
