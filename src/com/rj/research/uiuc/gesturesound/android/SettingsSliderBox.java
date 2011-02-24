package com.rj.research.uiuc.gesturesound.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.audio.Parameter;


public class SettingsSliderBox extends SettingsBox implements OnSeekBarChangeListener, OnCheckedChangeListener {
	private final static int SLIDER_MAX = 100;
	SeekBar seekbar;
	TextView paramvalue;
	TextView title;
	CheckBox checkbox;
	SettingsChangedListener callback;
	float max;
	float min;
	
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
		title = (TextView) this.findViewById(R.id.slider_name);
		title.setText(p.getName());
		paramvalue = (TextView) this.findViewById(R.id.slider_value);
		paramvalue.setText(p.getValue()+"");
		checkbox = (CheckBox) this.findViewById(R.id.slider_checkbox);
		checkbox.setChecked(true);
		checkbox.setOnCheckedChangeListener(this);

	}
	
	public void setValue(float value) {
		if (seekbar == null)
			return; //ok now something's wrong.
//		seekbar.setProgress((int)((value/max)*SLIDER_MAX));
		seekbar.setProgress((int) (((value-min)/(max-min)) * SLIDER_MAX)) ;
		paramvalue.setText(value+"");
	}
	
	public void setMax(float max) {
		if (seekbar == null)
			return; //ok now something's wrong.
		seekbar.setMax(SLIDER_MAX);
		this.max = max;
	}
	
	public void setMin(float min) {
		if (seekbar == null)
			return; //ok now something's wrong.
		seekbar.setMax(SLIDER_MAX);
		this.min = min;
	}
	
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (callback != null) {
			if (fromUser)
				callback.settingsChanged(param, ((float)progress/(float)SLIDER_MAX)*(max-min));
			updatedSeek(seekBar, progress, fromUser);
		}
	}
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
	public void updatedSeek(SeekBar seekBar, int progress,	boolean fromUser) {
		paramvalue.setText(((float)progress/(float)SLIDER_MAX)*(max-min)+"");
	}
	
	public void collapse() {
		seekbar.setVisibility(GONE);
		title.setVisibility(GONE);
	}
	
	public void expand() {
		seekbar.setVisibility(VISIBLE);
		title.setVisibility(VISIBLE);
	}


	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		param.setEnabled(arg1);
		callback.settingsEnabledState(param, arg1);
	}

	
	
	
	
}
