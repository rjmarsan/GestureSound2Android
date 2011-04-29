package com.rj.research.uiuc.gesturesound.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.gestures.extractors.FeatureMap;


public class SettingsSliderBox extends SettingsBox implements OnSeekBarChangeListener, OnCheckedChangeListener, OnClickListener {
	private final static int SLIDER_MAX = 100;
	SeekBar seekbar;
	TextView paramvalue;
	TextView title;
	TextView maptext;
	CheckBox checkbox;
	SettingsChangedListener callback;
	Button editButton;
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
		maptext = (TextView) this.findViewById(R.id.slider_map);
		updateMapText();
		checkbox = (CheckBox) this.findViewById(R.id.slider_checkbox);
		checkbox.setChecked(true);
		checkbox.setOnCheckedChangeListener(this);
		editButton = (Button) this.findViewById(R.id.slider_editbutton);
		editButton.setOnClickListener(this);

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
		maptext.setVisibility(GONE);
		editButton.setVisibility(GONE);
		checkbox.setVisibility(GONE);
	}
	
	public void expand() {
		seekbar.setVisibility(VISIBLE);
		title.setVisibility(VISIBLE);
		maptext.setVisibility(VISIBLE);
		editButton.setVisibility(VISIBLE);
		checkbox.setVisibility(VISIBLE);
	}
	
	private void updateMapText() {
		int[] qualities = param.getQualities();
		String qualstr = "";
		for (int i = 0; i<qualities.length; i++) {
			qualstr += FeatureMap.shortnames[qualities[i]];
			if (i!=qualities.length-1) qualstr += "\n";
		}
		maptext.setText(qualstr);
	}


	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		param.setEnabled(arg1);
		callback.settingsEnabledState(param, arg1);
	}


	@Override
	public void onClick(View v) {
		final String[] items = FeatureMap.names;
		final int[] ids = param.getQualities();
		final boolean[] enabled = new boolean[FeatureMap.options.length];
		for (int i=0; i<FeatureMap.options.length; i++) {
			enabled[i] = false;
			for (int id : ids) if (id == FeatureMap.options[i]) enabled[i] = true;
		}
		final AlertDialog alert;
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("Pick gesture qualities");
		builder.setMultiChoiceItems(items, enabled, new OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				enabled[which] = isChecked;
			}
		});
		builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				int enabledcount = 0;
				for (boolean yeah : enabled) if (yeah) enabledcount++;
				int[] qualities = new int[enabledcount];
				int qualitycount = 0;
				for (int i=0; i<enabled.length; i++) {
					if (enabled[i]) {
						qualities[qualitycount] = FeatureMap.options[i];
						qualitycount ++;
					}
				}
				param.setQualities(qualities);
				updateMapText();
				callback.settingsEditedFeatures(param, qualities);
			}
		});
		builder.setNegativeButton("Cancel", null);
		alert = builder.create();
		alert.show();

	}

	
	
	
	
}
