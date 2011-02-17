package com.rj.research.uiuc.gesturesound.listeners;

public interface WekaClassifyListener {
	public void updatedOutput(double [] output);
	
	public void finishedTraining(WekiManager weka);
}
