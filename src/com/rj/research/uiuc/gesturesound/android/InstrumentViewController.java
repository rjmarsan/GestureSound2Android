package com.rj.research.uiuc.gesturesound.android;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;


public class InstrumentViewController extends LinearLayout implements SettingsChangedListener {

	Instrument mInstrument;
	SettingsBox[] settingsBoxes;
	
	public InstrumentViewController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	public void setInstrument(Instrument instrument) {
		this.removeAllViews();
		
		this.mInstrument = instrument;
		Parameter[] params = instrument.getParameters();
		settingsBoxes = new SettingsBox[params.length];
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i=0; i<settingsBoxes.length; i++) {
			Log.d("InstrumentViewController", "Adding slider for parameter: "+params[i].getName());
			SettingsSliderBox s  = (SettingsSliderBox) inflater.inflate(R.layout.slider, null);
			settingsBoxes[i] = s;
			s.setupFromParameter(params[i]);
			s.setMax(params[i].getMax());
			s.setMin(params[i].getMin());
			s.setSettingsChangedListener(this);
			this.addView(s,i);
		}
	}
	
	public void updateParams(Instrument instrument, Parameter[] params) {
		if (settingsBoxes == null) return;
		for (int i=0; i<settingsBoxes.length; i++) {
			settingsBoxes[i].setValue(params[i].getValue());
		}
	}


	@Override
	public void settingsChanged(Parameter param, float value) {
		param.setValue(value);
		mInstrument.updated();
	}


	public void collapse() {
		for (SettingsBox s : settingsBoxes) {
			s.collapse();
		}
	}
	
	public void expand() {
		for (SettingsBox s : settingsBoxes) {
			s.expand();
		}
	}
	
	
}
