package com.rj.research.uiuc.gesturesound.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;


public class InstrumentViewController extends RelativeLayout {

	Instrument mInstrument;
	
	public InstrumentViewController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	public void setInstrument(Instrument instrument) {
		this.mInstrument = instrument;
	}
}
