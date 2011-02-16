package wekinator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class OscSynthConfiguration {

    protected int numParams = 0;
    protected String[] paramNames = null;
    protected boolean[] isDiscrete = null;
    protected boolean[] isDistribution = null;
    protected int[] maxValue = null;
    protected String validationString = "";
    protected EventListenerList listenerList = new EventListenerList();
    private ChangeEvent changeEvent = null;

    public boolean[] getIsDiscrete() {
        return isDiscrete;
    }

    public boolean[] getIsDistribution() {
        return isDistribution;
    }

    public int[] getMaxValue() {
        return maxValue;
    }

    public int getNumParams() {
        return numParams;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public OscSynthConfiguration(OscSynthConfiguration c1) {
        numParams = c1.numParams;
        isDiscrete = new boolean[c1.numParams];
        System.arraycopy(c1.isDiscrete, 0, isDiscrete, 0, c1.numParams);
        isDistribution = new boolean[c1.numParams];
        System.arraycopy(c1.isDistribution, 0, isDistribution, 0, c1.numParams);
        paramNames = new String[c1.numParams];
        System.arraycopy(c1.paramNames, 0, paramNames, 0, c1.numParams);
        maxValue = new int[c1.numParams];
        System.arraycopy(c1.maxValue, 0, maxValue, 0, c1.numParams);
    }

    public OscSynthConfiguration(int numParams, String[] paramNames, boolean[] isDiscrete, boolean[] isDistribution, int[] maxValue) {
        this.numParams = numParams;
        this.paramNames = paramNames;
        this.isDiscrete = isDiscrete;
        this.isDistribution = isDistribution;
        this.maxValue = maxValue;
    }

    public OscSynthConfiguration() {
        numParams = 0;
        paramNames = new String[0];
        isDiscrete = new boolean[0];
        isDistribution = new boolean[0];
        maxValue = new int[0];
    }

    void setMaxVals(int[] maxvals) {
        this.maxValue = maxvals;
        fireStateChanged();
    }

    void setNames(String[] names) {
        this.paramNames = names;
        fireStateChanged();
    }

    void setDiscrete(boolean[] isDiscrete) {
        this.isDiscrete = isDiscrete;
        fireStateChanged();
    }

    void setDistribution(boolean[] isDistribution) {
        this.isDistribution = isDistribution;
        fireStateChanged();
    }

    void setNumParams(int n) {
        this.numParams = n;
        fireStateChanged();
    }

    public boolean validate() {
        validationString = "";
        if (numParams > 0) {
            if (paramNames == null || isDiscrete == null || isDistribution == null || maxValue == null) {
                validationString = "Null error in OSC synth internal configuration (this should never happen)\n";
                return false;
            }
            if (paramNames.length != numParams || isDiscrete.length != numParams || isDistribution.length != numParams || maxValue.length != numParams) {
                validationString = "Size error in OSC synth internal configuration (this should never happen)\n";
                return false;
            }
            for (int i = 0; i < numParams; i++) {
                if (isDiscrete[i] && maxValue[i] <= 0) {
                    validationString = "OSC Synth max parameter value must be > 0 for discrete parameters\n";
                    return false;
                }
            }

            return true;
        } else {
            validationString = "OSC synth must have > 0 parameters if enabled\n";
            return false;
        }

    }

    public String getValidationReport() {
        return validationString;
    }

    public String getDescription() {
        String s = "OSC synth using " + numParams;
        int i = -1;
        for (boolean isDisc : isDiscrete) {
            if (isDisc) {
                if (i == -1) {
                    i = 1;
                } else if (i == 2) {
                    i = 3;
                }
            } else {
                if (i == -1) {
                    i = 2;
                } else if (i == 1) {
                    i = 3;
                }
            }
        }
        if (i == 1) {
            s += " discrete ";
        } else if (i == 2) {
            s += " continuous ";
        } else {
            s += " discrete and continuous ";
        }
        if (numParams == 1) {
            s += "parameter";
        } else {
            s += "parameters";
        }
        return s;
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }
}
