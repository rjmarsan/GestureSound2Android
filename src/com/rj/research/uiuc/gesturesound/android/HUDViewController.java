package com.rj.research.uiuc.gesturesound.android;

import wekinator.controller.WekinatorManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rj.research.uiuc.gesturesound.R;
import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.InstrumentManager;
import com.rj.research.uiuc.gesturesound.audio.Parameter;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.PDSynth;
import com.rj.research.uiuc.gesturesound.listeners.InstrumentListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaClassifyListener;
import com.rj.research.uiuc.gesturesound.listeners.WekaInstListener;


public class HUDViewController extends RelativeLayout implements WekaClassifyListener, WekaInstListener {
	WekaInstrument weka;
	InstrumentViewController instView;
	Handler mHandle;
	
	Button togglehide;
	boolean hidden = false;
	
	
	Button loadbutton;
	Button savebutton;
	Button newbutton;
	Button playbutton;
	Button recordbutton;
	Button testingbutton;
	
	TextView trainingview;
	TextView trainingsamples;
	TextView name;
	
	ProgressDialog currentDialog;
	
	public HUDViewController(Context context, AttributeSet attrs) {
		super(context, attrs);
		instView = (InstrumentViewController) this.findViewById(R.id.instrument_settings);
		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true; //doesnt forward to the bottom part
			}});
	}
	
	public InstrumentListener instrumentListener = new InstrumentListener() {
		public void newInstrument(Instrument instrument) {
			final Instrument inst2 = instrument;
			mHandle.post(new Runnable() { public void run() {
			instView.setInstrument(inst2);
			}});
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
		weka.addWekaInstListener(this);
		
		playbutton = (Button) this.findViewById(R.id.play_button);
		playbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.d("Play button", "Pressed and playing!");
				weka.perform();
				configurePlayButton();
			}	
		});
		
		recordbutton = (Button) this.findViewById(R.id.record_button);
		recordbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				weka.record();
				if (trainingview != null) trainingview.setText("Recording");
				configurePlayButton();
			}	
		});
		
		trainingview = (TextView) this.findViewById(R.id.training_status);
		trainingview.setText("Idle");
		
		
		trainingsamples = (TextView) this.findViewById(R.id.training_samples);

		
		name = (TextView) this.findViewById(R.id.weka_name);
		
		testingbutton = (Button) this.findViewById(R.id.toggle_test_button);
		testingbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (weka.mode() == WekaInstrument.TESTING) {
					weka.stopTesting();
					testingbutton.setText("test");
				} else {
					weka.startTesting();
					testingbutton.setText("stop");
				}
			}
		});
		
		newbutton = (Button) this.findViewById(R.id.new_button);
		newbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("New Weka Instrument");
				//builder.setMessage("Pick a name and instrument");
				final EditText text = new EditText(getContext());
				builder.setView(text);
				final String[] items = InstrumentManager.getInstruments();
				final String[] selecteditem = new String[] {PDSynth.name};
				builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				        selecteditem[0] = items[item];
				    }
				});
				builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {  
					public void onClick(DialogInterface dialog, int whichButton) {  
					  String value = text.getText().toString();  
					  	weka.makeNewWekaInstrument(value, selecteditem[0]);
					}  
				});
				builder.setNegativeButton("Cancel", null);
				AlertDialog alert = builder.create();
				alert.show();
			}	
		});

		
		savebutton = (Button) this.findViewById(R.id.save_button);
		savebutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				weka.save();
//				SharedPreferences prefs = getContext().getSharedPreferences("saveditems", 0);
//				Editor edit = prefs.edit();
//				edit.putString(weka.name, "yay!");
//				edit.commit();
			}	
		});
		
		loadbutton = (Button) this.findViewById(R.id.load_button);
		loadbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
//				SharedPreferences prefs = getContext().getSharedPreferences("saveditems", 0);
//				Object[] array = prefs.getAll().keySet().toArray();
//				
//				final CharSequence[] items = new CharSequence [array.length+1];
//				for (int i=0; i<array.length; i++) {
//					items[i] = (CharSequence) array[i];
//				}
//				items[array.length] = "RJTest1"; //testing purposes

				final String[] items = weka.getSavedInstances();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("Pick a saved instance");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				        weka.load((String) items[item]);
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
			}	
		});


		
		
		
		togglehide = (Button) this.findViewById(R.id.toggle_hide_button);
		togglehide.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				showhide();
			}	
		});
	}
	
	private void configurePlayButton() {
		if (weka.mode() == WekaInstrument.RECORDING) {
			playbutton.setText("Train");
		} else if (weka.mode() == WekaInstrument.PERFORMING) {
			playbutton.setText("Stop");
		} else {
			playbutton.setText("Play");
		}
	}
	
	public void showhide() {
		hidden = !hidden;
		if (hidden) {
			togglehide.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ic_menu_forward));
			this.setLayoutParams(new LayoutParams(50, LayoutParams.FILL_PARENT));
			newbutton.setVisibility(View.GONE);
			savebutton.setVisibility(View.GONE);
			loadbutton.setVisibility(View.GONE);
			recordbutton.setVisibility(View.GONE);
			trainingview.setVisibility(View.GONE);
			instView.collapse();
			
		} else {
			//togglehide.setText("hide");
			togglehide.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ic_menu_back));
			this.setLayoutParams(new LayoutParams(300, LayoutParams.FILL_PARENT));
			newbutton.setVisibility(View.VISIBLE);
			savebutton.setVisibility(View.VISIBLE);
			loadbutton.setVisibility(View.VISIBLE);
			recordbutton.setVisibility(View.VISIBLE);
			trainingview.setVisibility(View.VISIBLE);
			instView.expand();
		}
	}
	
	public void setStatusText(final WekaInstrument weka) {
		
		mHandle.post(new Runnable() { public void run() {
			String text = "";
			if (weka.mode == WekaInstrument.NOTHING) {
				text = "idle ";
			}
			else if (weka.mode == WekaInstrument.PERFORMING) {
				text = "playing ";
			}
			else if (weka.mode == WekaInstrument.RECORDING) {
				text = "recording: ";
			}
			else if (weka.mode == WekaInstrument.TRAINING) {
				text = "training... ";
			}
			if (trainingview != null) trainingview.setText(text);
			}});
	}
	
	public void makeDialog(String message) {
		currentDialog = ProgressDialog.show(getContext(), "",  message, true);
		currentDialog.show();
	}
	
	public void makeDialog(String title, String message) {
		currentDialog = ProgressDialog.show(getContext(), title,  message, true);
		currentDialog.show();
	}

	public void updateDialog(String title, String message) {
		if (currentDialog != null) {
			currentDialog.setTitle(title);
			currentDialog.setMessage(message);
		}
	}
	
	public void dismissDialog() {
		if (currentDialog != null) {
			currentDialog.dismiss();
			currentDialog = null;
		}
	}
	public void toast(String text) {
		Toast.makeText(getContext(), text, 3).show();
	}

	@Override
	public void finishedTraining(WekinatorManager weka) {
		mHandle.post(new Runnable() { public void run() {
		if (trainingview != null) trainingview.setText("Done!");
		dismissDialog();
		}});
	}
	@Override
	public void trainingProgress(WekinatorManager weka,final int stepsFinished,
			final int totalSteps, final String message) {
		mHandle.post(new Runnable() { public void run() {
			updateDialog("Training "+stepsFinished+"/"+totalSteps, message);
		}});
	}
	@Override
	public void startingTraining(WekinatorManager weka) {
		mHandle.post(new Runnable() { public void run() {
			if (trainingview != null) trainingview.setText("Training...");
			makeDialog("Training", "Remote training beginning");
			}});	
	}

	@Override
	public void updatedTraining(final WekinatorManager weka) {
		mHandle.post(new Runnable() { public void run() {
			if (trainingsamples != null) trainingsamples.setText(weka.getSamples()+"samples");
			}});			
	}
	@Override
	public void updatedOutput(double[] output) {
	}

	@Override
	public void finishedLoadingSetup(final WekaInstrument inst) {
		mHandle.post(new Runnable() { public void run() {
			dismissDialog();
			if (name != null) name.setText(inst.name);
			if (trainingsamples != null) trainingsamples.setText(inst.wekamanager.getSamples()+"samples");
			}});
	}

	@Override
	public void finishedSavingSetup(final WekaInstrument inst) {
		mHandle.post(new Runnable() { public void run() {
			dismissDialog();
			if (name != null) name.setText(inst.name);
			}});
	}

	@Override
	public void startLoadingSetup() {
		mHandle.post(new Runnable() { public void run() {
			makeDialog("Loading. Please wait...");
		}});
	}

	@Override
	public void startSavingSetup() {
		mHandle.post(new Runnable() { public void run() {
			makeDialog("Saving. Please wait...");
		}});	}

	@Override
	public void loadFailed() {
		mHandle.post(new Runnable() { public void run() {
			dismissDialog();
			toast("Load failed!");
		}});
	}
	@Override
	public void saveFailed() {
		mHandle.post(new Runnable() { public void run() {
			dismissDialog();
			toast("Save failed!");
		}});
	}




}
