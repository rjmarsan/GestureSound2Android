package com.rj.research.uiuc.gesturesound.listeners;

import wekinator.controller.WekinatorManager;

public interface WekaClassifyListener {
	public void updatedOutput(double [] output);
	public void updatedTraining(WekinatorManager weka);
	
	public void startingTraining(WekinatorManager weka);
	public void finishedTraining(WekinatorManager weka);
}
