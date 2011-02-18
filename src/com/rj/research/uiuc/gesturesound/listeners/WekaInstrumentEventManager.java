package com.rj.research.uiuc.gesturesound.listeners;

import java.util.ArrayList;

import wekinator.controller.WekinatorManager;

import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;

public class WekaInstrumentEventManager {
	public ArrayList<WekaClassifyListener> wekaClassifyListeners;
	public ArrayList<InstrumentListener> instrumentSettingsListeners;
	
	
	public WekaInstrumentEventManager() {
		wekaClassifyListeners = new ArrayList<WekaClassifyListener>();
		instrumentSettingsListeners = new ArrayList<InstrumentListener>();
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
	
	
	
	public void fireWekaClassifyEvent(double[] data) {
		for (WekaClassifyListener l : wekaClassifyListeners) {
			l.updatedOutput(data);
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

}
