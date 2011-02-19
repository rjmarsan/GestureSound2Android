package com.rj.research.uiuc.gesturesound.audio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.rj.research.uiuc.gesturesound.WekaInstrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.Instrument;
import com.rj.research.uiuc.gesturesound.audio.instruments.OSCInstrument;

public class InstrumentManager {
	WekaInstrument parent;
	

	
	Instrument currentInstrument;
	HashMap<String, Class<?>> possibleInstruments;
	
	public InstrumentManager(WekaInstrument parent) {
		this.parent = parent;
		possibleInstruments = new HashMap<String, Class<?>>();
		possibleInstruments.put(OSCInstrument.name, OSCInstrument.class);
	}
	
	public void saveCurrentInstToFolder(File folder) {
		try {
			File savefile = new File(folder, "instrument.inst");
			if (!folder.exists()) savefile.mkdirs();
			BufferedWriter out = new BufferedWriter(new FileWriter(savefile));
			out.write(currentInstrument.name);
			out.close();
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public void loadCurrentInstFromFolder(File folder) {
		try {
			File savefile = new File(folder, "instrument.inst");
			String text = InstrumentManager.readTextFile(savefile);
			setInstrument(text);
		}catch(Exception e) { e.printStackTrace(); }
	}
	
	public static String readTextFile(File name) throws IOException {
		StringBuffer sb = new StringBuffer(1024);
		BufferedReader reader = new BufferedReader(new FileReader(name));
				
		char[] chars = new char[1024];
		int numRead = 0;
		while( (numRead = reader.read(chars)) > -1){
			sb.append(String.valueOf(chars));	
		}

		reader.close();

		return sb.toString();
	}
	
	public Instrument getInstrument() {
		return currentInstrument;
	}
	
	
	public void setNewParameters(double[] paramvector) {
		if (currentInstrument != null) {
			currentInstrument.updateParameters(paramvector);
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
			currentInstrument = (Instrument) (possibleInstruments.get(inst)).newInstance();
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


	public double[] getInstrumentParametersAsDouble() {
		Parameter[] params = getInstrumentParameters();
		double[] out = new double[params.length];
		for (int i=0;i<out.length;i++) {
			out[i] = params[i].getValue();
			System.out.println("Param: "+params[i].getName()+" val:"+out[i]);
		}
		return out;
	}


	public String getName() {
		return currentInstrument.name;
	}
}
