package com.rj.research.uiuc.gesturesound.audio;

import java.util.HashMap;

import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.OSCInstrument;

public class InstrumentManager {
	WekaInstrument parent;
	

	
	Instrument currentInstrument;
	HashMap<String, Class<?>> possibleInstruments;
	
	public InstrumentManager(WekaInstrument parent) {
		this.parent = parent;
		possibleInstruments = new HashMap<String, Class<?>>();
		possibleInstruments.put(OSCInstrument.name, OSCInstrument.class);
	}
	
	
	public void setNewParameters(double[] paramvector) {
		if (currentInstrument != null) {
			currentInstrument.updateParameters(paramvector);
			parent.eventmanager.fireInstrumentSettingsEvent(getInstrumentParameters(), currentInstrument, false);
		}
	}
	
	public Parameter[] getInstrumentParameters() {
		return currentInstrument.getParameters();
	}
	
	
	public HashMap<String, Class<?>> getInstrumentList() {
		return possibleInstruments;
	}
	
	/**
	 * returns true on success, false on failure
	 * @param inst
	 * @return
	 */
	public boolean setInstrument(String inst) {
		try {
			currentInstrument = (Instrument) (possibleInstruments.get(inst)).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} finally {
			parent.eventmanager.fireNewInstrumentEvent(currentInstrument);
		}
		return true;
	}
}
