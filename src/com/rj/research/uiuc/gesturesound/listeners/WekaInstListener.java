package com.rj.research.uiuc.gesturesound.listeners;

import com.rj.research.uiuc.gesturesound.WekaInstrument;

public interface WekaInstListener {	
	public void startLoadingSetup();
	public void finishedLoadingSetup(WekaInstrument inst);
	
	public void startSavingSetup();
	public void finishedSavingSetup(WekaInstrument inst);
}
