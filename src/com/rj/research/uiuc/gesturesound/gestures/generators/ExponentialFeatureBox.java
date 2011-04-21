package com.rj.research.uiuc.gesturesound.gestures.generators;

import java.util.HashMap;

import com.rj.processing.mt.Cursor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureExtractor;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;

public class ExponentialFeatureBox extends FeatureBox {
	public static FeatureMap currentFeatures;
	
	FeatureExtractor extractor;
	FeatureGenerator[] generators;

	Cursor cursor;
	
	double[] featvec;
	
	public ExponentialFeatureBox(Cursor c) {
		this.cursor = c;
		
		this.extractor = new FeatureExtractor();
		this.generators = new FeatureGenerator[] {
				new JustHistoryGenerator(FeatureMap.CURV),
				new JustHistoryGenerator(FeatureMap.VEL_X),
				new JustHistoryGenerator(FeatureMap.VEL_Y),
				//new JustHistoryGenerator(FeatureMap.X),
				//new JustHistoryGenerator(FeatureMap.Y),

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
