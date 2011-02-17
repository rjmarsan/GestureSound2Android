package com.rj.research.uiuc.gesturesound.listeners;

import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;

public interface InstrumentListener {
	public void updatedParameters(Parameter[] parameters, Instrument instrument, boolean fromUI);
	public void newInstrument(Instrument instrument);
}
