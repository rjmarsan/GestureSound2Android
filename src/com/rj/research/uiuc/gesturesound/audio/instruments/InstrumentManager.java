package com.rj.research.uiuc.gesturesound.audio.instruments;

import java.util.HashMap;

public class InstrumentManager {

	Instrument currentInstrument;
	HashMap<String, Class<Instrument>> possibleInstruments;
	
	public void setNewParameters(double[] paramvector) {
		currentInstrument.updateParameters(paramvector);
	}
	
	public Parameter[] getInstrumentParameters() {
		return currentInstrument.getParameters();
	}
	
	
	public HashMap<String, Class<Instrument>> getInstrumentList() {
		return possibleInstruments;
	}
	
	public void setInstrument(String inst) {
		try {
			currentInstrument = (possibleInstruments.get(inst)).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
}
