package com.rj.research.uiuc.gesturesound.android;

import wekinator.controller.WekinatorManager;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;
import com.rj.research.uiuc.gesturesound.listeners.InstrumentListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaClassifyListener;


public class HUDViewController extends RelativeLayout implements WekaClassifyListener {
	WekaInstrument weka;
	InstrumentViewController instView;
	Handler mHandle;
	
	Button togglehide;
	boolean hidden = false;
	
	
	Button loadbutton;
	Button savebutton;
	Button playbutton;
	Button recordbutton;
	
	TextView trainingview;

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
		mHandle = new Handler();
		instView = (InstrumentViewController) this.findViewById(R.id.instrument_settings);
		weka = instrument;
		weka.addInstrumentListener(instrumentListener);
		weka.addWekaClassifyListener(this);
		
		playbutton = (Button) this.findViewById(R.id.play_button);
		playbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.d("Play button", "Pressed and playing!");
				weka.perform();
			}	
		});
		
		recordbutton = (Button) this.findViewById(R.id.record_button);
		recordbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				weka.record();
				if (trainingview != null) trainingview.setText("Recording");
			}	
		});
		
		trainingview = (TextView) this.findViewById(R.id.training_status);
		trainingview.setText("Idle");
		
		
		savebutton = (Button) this.findViewById(R.id.save_button);
		savebutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				weka.save();
			}	
		});
		
		loadbutton = (Button) this.findViewById(R.id.load_button);
		loadbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//TODO make it load something
			}	
		});


		
		
		
		togglehide = (Button) this.findViewById(R.id.toggle_hide_button);
		togglehide.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				showhide();
			}	
		});
	}
	
	public void showhide() {
		hidden = !hidden;
		if (hidden) {
			togglehide.setText("show");
			this.setLayoutParams(new LayoutParams(50, LayoutParams.FILL_PARENT));
			savebutton.setVisibility(View.GONE);
			loadbutton.setVisibility(View.GONE);
			recordbutton.setVisibility(View.GONE);
			trainingview.setVisibility(View.GONE);
			instView.collapse();
			
		} else {
			togglehide.setText("hide");
			this.setLayoutParams(new LayoutParams(300, LayoutParams.FILL_PARENT));
			savebutton.setVisibility(View.VISIBLE);
			loadbutton.setVisibility(View.VISIBLE);
			recordbutton.setVisibility(View.VISIBLE);
			trainingview.setVisibility(View.VISIBLE);
			instView.expand();
		}
	}

	@Override
	public void finishedTraining(WekinatorManager weka) {
		mHandle.post(new Runnable() { public void run() {
		if (trainingview != null) trainingview.setText("Done!");
		}});
	}

	@Override
	public void startingTraining(WekinatorManager weka) {
		mHandle.post(new Runnable() { public void run() {
			if (trainingview != null) trainingview.setText("Training...");
			}});	
	}

	@Override
	public void updatedOutput(double[] output) {
	}

}
