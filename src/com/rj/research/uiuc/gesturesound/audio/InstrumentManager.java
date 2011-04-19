package com.rj.research.uiuc.gesturesound.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.OSCInstrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.PDSynth;
import com.rj.research.uiuc.gesturesound.audio.instruments.SimplePDInstrument;

public class InstrumentManager {
	WekaInstrument parent;
	

	
	Instrument currentInstrument;
	HashMap<String, Class<?>> possibleInstruments;
	
	public InstrumentManager(WekaInstrument parent) {
		this.parent = parent;
		possibleInstruments = new HashMap<String, Class<?>>();
		possibleInstruments.put(OSCInstrument.name, OSCInstrument.class);
		possibleInstruments.put(SimplePDInstrument.name, SimplePDInstrument.class);
		possibleInstruments.put(PDSynth.name, PDSynth.class);
	}
	
	public void saveCurrentInstToFolder(File folder) throws IOException {
		if (!folder.exists()) folder.mkdirs();
		File outfile = new File(folder, "instrument.inst");
		FileOutputStream f = new FileOutputStream(outfile);
		Properties p = new Properties();
		p.put("instrumentname", currentInstrument.getName());
		p.store(f, "instrument settings");
		f.close();
	}
	
	public void loadCurrentInstFromFolder(File folder) throws IOException {
		FileInputStream f = new FileInputStream(new File(folder, "instrument.inst"));
		Properties p = new Properties();
		p.load(f);
		String name = (String) p.get("instrumentname");
		System.out.println("Loading current inst setting... name:"+name);
		setInstrument(name);
	}
	

	public Instrument getInstrument() {
		return currentInstrument;
	}
	
	
	public void setNewParameters(double[] paramvector, boolean overwrite) {
		if (currentInstrument != null) {
			currentInstrument.updateParameters(paramvector, overwrite);
			parent.eventmanager.fireInstrumentSettingsEvent(getInstrumentParameters(), currentInstrument, false);
		}
	}
	
	public Parameter[] getInstrumentParameters() {
		return currentInstrument.getParameters();
	}
	
	
	public HashMap<String, Class<?>> getInstrumentList() {
		return possibleInstruments;
	}
	
	/**
	 * returns true on success, false on failure
	 * @param inst
	 * @return
	 */
	public boolean setInstrument(String inst) {
		try {
			if (currentInstrument != null) { 
				currentInstrument.stop();
				currentInstrument.cleanup();
			}
			currentInstrument = (Instrument) (possibleInstruments.get(inst)).newInstance();
			currentInstrument.setWeka(parent);
			currentInstrument.start();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} finally {
			parent.eventmanager.fireNewInstrumentEvent(currentInstrument);
		}
		return true;
	}

	double[] paramvals = {};
	public double[] getInstrumentParametersAsDouble() {
		Parameter[] params = getInstrumentParameters();
		if (paramvals.length != params.length) paramvals = new double[params.length];
		for (int i=0;i<paramvals.length;i++) {
			paramvals[i] = params[i].getValue();
			System.out.println("Param: "+params[i].getName()+" val:"+paramvals[i]);
		}
		return paramvals;
	}
	
	boolean[] parammask = {};
	public boolean[] getParameterMask() {
		Parameter[] params = getInstrumentParameters();
		if (parammask.length != params.length) parammask = new boolean[params.length];
		for (int i=0;i<parammask.length;i++) {
			parammask[i] = params[i].isEnabled();
			System.out.println("Param: "+params[i].getName()+" enabled:"+parammask[i]);
		}
		return parammask;
	}

	public String getName() {
		return currentInstrument.getName();
	}
}
