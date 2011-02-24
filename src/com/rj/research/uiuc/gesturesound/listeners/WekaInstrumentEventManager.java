package com.rj.research.uiuc.gesturesound.listeners;

import java.util.ArrayList;

import wekinator.controller.WekinatorManager;

import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;

public class WekaInstrumentEventManager {
	public ArrayList<WekaClassifyListener> wekaClassifyListeners;
	public ArrayList<InstrumentListener> instrumentSettingsListeners;
	public ArrayList<WekaInstListener> wekaInstListeners;
	
	
	public WekaInstrumentEventManager() {
		wekaClassifyListeners = new ArrayList<WekaClassifyListener>();
		instrumentSettingsListeners = new ArrayList<InstrumentListener>();
		wekaInstListeners = new ArrayList<WekaInstListener>();
	}
	
	
	public void addWekaClassifyListener(WekaClassifyListener l) { 
		if (l != null) {
			wekaClassifyListeners.add(l);
		}
	}
	
	
	public void addInstrumentListener(InstrumentListener l) { 
		if (l != null) {
			instrumentSettingsListeners.add(l);
		}
	}
	
	public void addWekaInstListener(WekaInstListener l) { 
		if (l != null) {
			wekaInstListeners.add(l);
		}
	}
	
	
	
	public void fireWekaClassifyEvent(double[] data) {
		for (WekaClassifyListener l : wekaClassifyListeners) {
			l.updatedOutput(data);
		}
	}

	public void fireWekaTrainEvent(WekinatorManager wekamanager) {
		for (WekaClassifyListener l : wekaClassifyListeners) {
			l.updatedTraining(wekamanager);
		}		
	}
	
	public void fireWekaTrainBegin(WekinatorManager man) {
		for (WekaClassifyListener l : wekaClassifyListeners) {
			l.startingTraining(man);
		}
	}
	
	public void fireWekaTrainEnd(WekinatorManager man) {
		for (WekaClassifyListener l : wekaClassifyListeners) {
			l.finishedTraining(man);
		}
	}
	
	public void fireInstrumentSettingsEvent(Parameter[] params, Instrument inst, boolean fromUI) {
		for (InstrumentListener l : instrumentSettingsListeners) {
			l.updatedParameters(params, inst, fromUI);
		}
	}
	
	public void fireNewInstrumentEvent(Instrument inst) {
		for (InstrumentListener l : instrumentSettingsListeners) {
			l.newInstrument(inst);
		}
	}
	

	public void fireSaveStartedEvent() {
		System.out.println("Firing savestartedevent");
		for (WekaInstListener l : wekaInstListeners) {
			l.startSavingSetup();
		}
	}
	public void fireSaveFailedEvent() {
		System.out.println("Firing savefailed");
		for (WekaInstListener l : wekaInstListeners) {
			l.saveFailed();
		}
	}
	public void fireSaveCompleteEvent(WekaInstrument inst) {
		System.out.println("Firing savecompletevent");
		for (WekaInstListener l : wekaInstListeners) {
			l.finishedSavingSetup(inst);
		}
	}
	
	public void fireLoadStartedEvent() {
		System.out.println("Firing loadstartedevent");
		for (WekaInstListener l : wekaInstListeners) {
			l.startLoadingSetup();
		}
	}
	public void fireLoadFailedEvent() {
		System.out.println("Firing loadfailed");
		for (WekaInstListener l : wekaInstListeners) {
			l.loadFailed();
		}
	}
	public void fireLoadCompleteEvent(WekaInstrument inst) {
		System.out.println("Firing loadfinishedevent");
		for (WekaInstListener l : wekaInstListeners) {
			l.finishedLoadingSetup(inst);
		}
	}

}
