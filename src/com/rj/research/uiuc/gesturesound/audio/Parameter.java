package com.rj.research.uiuc.gesturesound.audio;

public class Parameter {
	String name;
	public final static int CONTINUOUS = 0;  //any value from min to max
	public final static int ARBITRARY_SELECTOR = 1; //any values in the list
	public final static int SELECTOR = 2; //integer values from 0 to n
	int type;
	
	//continuous parameters
	float cont_min = 0;
	float cont_max = 1;
	
	//arbitrary selector parameters
	float[] values;
	
	//selector parameters
	int selector_max = 10;
	
	//the current value of the parameter.  
	//if in selector mode, index will be the real value,
	//if in continuous mode, value is the value 
	float value = 0;
	int index = 0;
	
	public Parameter(String name, int type, double value) {
		this.name = name;
		this.type = type;
		setValue(value);
	}
	
	/**
	 * this function only applies if you're in ARBITRARY_SELECTOR mode
	 * @param values
	 */
	public void setValues(float[] values) {
		this.values = values;
	}
	
	/**
	 * this function only applies if you're in CONTINUOUS mode
	 * @param values
	 */
	public void setMinMax(float min, float max) {
		this.cont_min = min;
		this.cont_max = max;
	}
	
	/**
	 * this function only applies if you're in SELECTOR mode
	 * @param values
	 */
	public void setMax(int max) {
		this.selector_max = max;
	}
	
	
	/**
	 * this is the generic method to change the value of a parameter, reguarless of the mode
	 * for selector modes, the value will be truncated, and it will be stored as an index
	 * @param value
	 */
	public void setValue(double value) {
		if (type == CONTINUOUS) {
			this.value = (float) value;
		}
		else {
			this.index = (int) value;
		}
	}
	
	
	/**
	 * returns the value of the parameter reguardless of the mode.  it will always return a float
	 * @return
	 */
	public float getValue() {
		if (type == CONTINUOUS) {
			return this.value;
		}
		else if (type == SELECTOR) {
			return this.index;
		}
		else {
			if (values != null) {
				return values[index];
			}
		}
		return -1;	
	}
	
	
	public String getName() {
		return name;
	}
	
	public float getRange() {
		if (type == CONTINUOUS) {
			return this.cont_max - this.cont_min;
		}
		else if (type == SELECTOR) {
			return this.selector_max;
		}
		else {
			if (values != null) {
				return this.values.length;
			}
		}
		return -1;
	}
	
	public float getMin() {
		if (type == CONTINUOUS) {
			return this.cont_min;
		}
		return 0; //otherwise 0 is the minimum
	}
	
	public float getMax() {
		if (type == CONTINUOUS) {
			return this.cont_max;
		}
		else if (type == SELECTOR) {
			return this.selector_max;
		}
		else {
			if (values != null) {
				return this.values.length;
			}
		}
		return -1;//error!
	}

}
