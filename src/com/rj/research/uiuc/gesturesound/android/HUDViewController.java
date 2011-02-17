package com.rj.research.uiuc.gesturesound.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;
import com.rj.research.uiuc.gesturesound.listeners.InstrumentListener;


public class HUDViewController extends RelativeLayout {
	WekaInstrument weka;
	InstrumentViewController instView;

	public HUDViewController(Context context, AttributeSet attrs) {
		super(context, attrs);
		instView = (InstrumentViewController) this.findViewById(R.id.instrument_settings);
	}
	
	public InstrumentListener instrumentListener = new InstrumentListener() {
		public void newInstrument(Instrument instrument) {
			instView.setInstrument(instrument);
		}
		public void updatedParameters(Parameter[] parameters, Instrument instrument, boolean fromUI) {
			TextView debugText = (TextView) HUDViewController.this.findViewById(R.id.debug_box);
			String text = "";
			for (Parameter p : parameters) {
				text += p.getName() + " : "+p.getValue()+"  ,";
			}
			debugText.setText(text);
			
			if (instView != null) instView.updateParams(instrument, parameters);
		}};

	public void setWeka(WekaInstrument instrument) {
		instView = (InstrumentViewController) this.findViewById(R.id.instrument_settings);
		weka = instrument;
		weka.addInstrumentListener(instrumentListener);
	}

}
