package com.rj.research.uiuc.gesturesound.listeners;

import com.rj.research.uiuc.gesturesound.WekaInstrument;

public interface WekaInstListener {	
	public void startLoadingSetup();
	public void loadFailed();
	public void finishedLoadingSetup(WekaInstrument inst);
	
	public void startSavingSetup();
	public void saveFailed();
	public void finishedSavingSetup(WekaInstrument inst);
}
