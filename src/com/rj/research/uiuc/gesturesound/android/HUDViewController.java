package com.rj.research.uiuc.gesturesound.android;

import com.rj.research.uiuc.gesturesound.WekaInstrument;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class HUDViewController extends RelativeLayout {
	WekaInstrument weka;

	public HUDViewController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setWeka(WekaInstrument instrument) {
		weka = instrument;
	}

}
