package com.rj.research.uiuc.gesturesound.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.audio.Parameter;


public class SettingsSliderBox extends SettingsBox implements OnSeekBarChangeListener {
	private final static int SLIDER_MAX = 100;
	SeekBar seekbar;
	SettingsChangedListener callback;
	float max;
	
	Parameter param;
	
	public SettingsSliderBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		seekbar = (SeekBar) this.findViewById(R.id.slide_bar);
	}

	
	public void setSettingsChangedListener(SettingsChangedListener s) {
		this.callback = s;
	}
		
	public void setupFromParameter(Parameter p) {
		param = p;
		seekbar = (SeekBar) this.findViewById(R.id.slide_bar);
		seekbar.setOnSeekBarChangeListener(this);
		TextView title = (TextView) this.findViewById(R.id.slider_name);
		title.setText(p.getName());
	}
	
	public void setValue(float value) {
		if (seekbar == null)
			return; //ok now something's wrong.
		seekbar.setProgress((int)((value/max)*SLIDER_MAX));
	}
	
	public void setMax(float max) {
		if (seekbar == null)
			return; //ok now something's wrong.
		seekbar.setMax(SLIDER_MAX);
		this.max = max;
	}
	
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (callback != null) {
			
			if (fromUser)
				callback.settingsChanged(param, ((float)progress/(float)SLIDER_MAX)*max);
			updatedSeek(seekBar, progress, fromUser);
		}
	}
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
	public void updatedSeek(SeekBar seekBar, int progress,	boolean fromUser) {
		
	}
	
	
	
	
	
	
}
