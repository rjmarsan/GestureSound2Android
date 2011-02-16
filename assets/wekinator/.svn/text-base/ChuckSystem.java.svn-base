/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rebecca
 */
public class ChuckSystem {
    private static ChuckSystem ref = null; //singleton
    protected boolean[] doesParamUseDistribution = new boolean[0];
    protected int numParams = 0;
    public static final String PROP_NUMPARAMS = "numParams";
    protected boolean[] isParamDiscrete = new boolean[0];
    protected int[] numSynthMaxParamVals = new int[0];
    protected boolean usingChuckFeatureExtractor = false;
    protected int numChuckCustomFeatures = 0;
    protected String[] paramNames = new String[0];
    protected String[] chuckFeatureNames;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public enum ChuckSystemState {
        NOT_CONNECTED, //no info from chuck yet
        INVALID, //info from chuck is incomplete or bad
        CONNECTED_AND_VALID //good to go
    };

    protected ChuckSystemState state = ChuckSystemState.NOT_CONNECTED;
    public static final String PROP_STATE = "state";
    
    private Logger logger = Logger.getLogger(ChuckSystem.class.getName());

    //My private constructor
    private ChuckSystem() {

    }

    public void waitForNewSettings() {
        setState(ChuckSystemState.NOT_CONNECTED);
    }


    /**
     * Get the value of state
     *
     * @return the value of state
     */
    public ChuckSystemState getState() {
        return state;
    }

    /**
     * Set the value of state
     *
     * @param state new value of state
     */
    protected void setState(ChuckSystemState state) {
        ChuckSystemState oldState = this.state;
        this.state = state;
        propertyChangeSupport.firePropertyChange(PROP_STATE, oldState, state);
    }


    public static ChuckSystem getChuckSystem() {
        if (ref == null) {
            ref = new ChuckSystem();
        }
        return ref;
    }



    /**
     * Get the value of numParams
     *
     * @return the value of numParams
     */
    public int getNumParams() {
        return numParams;
    }

    /**
     * Set the value of numParams
     *
     * @param numParams new value of numParams
     */
    protected void setNumParams(int numParams) {
        int oldNumParams = this.numParams;
        this.numParams = numParams;
        propertyChangeSupport.firePropertyChange(PROP_NUMPARAMS, oldNumParams, numParams);
        //TODO: change arrays
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }


    /**
     * Get the value of isParamDiscrete
     *
     * @return the value of isParamDiscrete
     */
    public boolean[] isIsParamDiscrete() {
        return isParamDiscrete;
    }

    /**
     * Set the value of isParamDiscrete
     *
     * @param isParamDiscrete new value of isParamDiscrete
     */
    public void setIsParamDiscrete(boolean[] isParamDiscrete) {
        this.isParamDiscrete = isParamDiscrete;
    }

    /**
     * Get the value of isParamDiscrete at specified index
     *
     * @param index
     * @return the value of isParamDiscrete at specified index
     */
    public boolean isIsParamDiscrete(int index) {
        return this.isParamDiscrete[index];
    }

    /**
     * Set the value of isParamDiscrete at specified index.
     *
     * @param index
     * @param newIsParamDiscrete new value of isParamDiscrete at specified index
     */
    public void setIsParamDiscrete(int index, boolean newIsParamDiscrete) {
        this.isParamDiscrete[index] = newIsParamDiscrete;
    }


    /**
     * Get the value of numSynthMaxParamVals
     *
     * @return the value of numSynthMaxParamVals
     */
    public int[] getNumSynthMaxParamVals() {
        return numSynthMaxParamVals;
    }

    /**
     * Set the value of numSynthMaxParamVals
     *
     * @param numSynthMaxParamVals new value of numSynthMaxParamVals
     */
    public void setNumSynthMaxParamVals(int[] numSynthMaxParamVals) {
        this.numSynthMaxParamVals = numSynthMaxParamVals;
    }

    /**
     * Get the value of numSynthMaxParamVals at specified index
     *
     * @param index
     * @return the value of numSynthMaxParamVals at specified index
     */
    public int getNumSynthMaxParamVals(int index) {
        return this.numSynthMaxParamVals[index];
    }

    /**
     * Set the value of numSynthMaxParamVals at specified index.
     *
     * @param index
     * @param newNumSynthMaxParamVals new value of numSynthMaxParamVals at specified index
     */
    public void setNumSynthMaxParamVals(int index, int newNumSynthMaxParamVals) {
        this.numSynthMaxParamVals[index] = newNumSynthMaxParamVals;
    }

    /**
     * Get the value of doesParamUseDistribution
     *
     * @return the value of doesParamUseDistribution
     */
    public boolean[] isDoesParamUseDistribution() {
        return doesParamUseDistribution;
    }

    /**
     * Set the value of doesParamUseDistribution
     *
     * @param doesParamUseDistribution new value of doesParamUseDistribution
     */
    public void setDoesParamUseDistribution(boolean[] doesParamUseDistribution) {
        this.doesParamUseDistribution = doesParamUseDistribution;
    }

    /**
     * Get the value of doesParamUseDistribution at specified index
     *
     * @param index
     * @return the value of doesParamUseDistribution at specified index
     */
    public boolean isDoesParamUseDistribution(int index) {
        return this.doesParamUseDistribution[index];
    }
    
    /**
     * Set the value of doesParamUseDistribution at specified index.
     *
     * @param index
     * @param newDoesParamUseDistribution new value of doesParamUseDistribution at specified index
     */
    public void setDoesParamUseDistribution(int index, boolean newDoesParamUseDistribution) {
        this.doesParamUseDistribution[index] = newDoesParamUseDistribution;
    }


    /**
     * Get the value of numChuckCustomFeatures
     *
     * @return the value of numChuckCustomFeatures
     */
    public int getNumChuckCustomFeatures() {
        return numChuckCustomFeatures;
    }

    /**
     * Set the value of numChuckCustomFeatures
     *
     * @param numChuckCustomFeatures new value of numChuckCustomFeatures
     */
    public void setNumChuckCustomFeatures(int numChuckCustomFeatures) {
        this.numChuckCustomFeatures = numChuckCustomFeatures;
    }


    //Use this to store all the state info we get from chuck

    //include: # chuck feature extractor features


    /**
     * Get the value of usingChuckFeatureExtractor
     *
     * @return the value of usingChuckFeatureExtractor
     */
    public boolean isUsingChuckFeatureExtractor() {
        return usingChuckFeatureExtractor;
    }

    /**
     * Set the value of usingChuckFeatureExtractor
     *
     * @param usingChuckFeatureExtractor new value of usingChuckFeatureExtractor
     */
    public void setUsingChuckFeatureExtractor(boolean usingChuckFeatureExtractor) {
        this.usingChuckFeatureExtractor = usingChuckFeatureExtractor;
    }

 

    public void receivedChuckSettings(Object[] o) {
        System.out.println("In received chuck settings - chuckSystem");
        if (o == null || o.length < 6) {
            setState(ChuckSystemState.INVALID);
        }
        try {
            int p = (Integer)o[0];
            setNumParams(p);
            int useCustom = (Integer)o[1];
            setUsingChuckFeatureExtractor(useCustom==1);
            int numCustom = (Integer)o[2];
            if (useCustom==1) {
                setNumChuckCustomFeatures(numCustom);
            } else {
                setNumChuckCustomFeatures(0);
                numCustom = 0;
            }
            int current = 3;

            boolean newDistArray[] = new boolean[p];
            boolean newDiscreteArray[] = new boolean[p];
            int newMaxValsArray[] = new int[p];
            String newNamesArray[] = new String[p];


            String newChuckFeatNamesArray[] = new String[numCustom];

            for (int i = 0; i < p; i++) {
                int b = (Integer)o[current];
                newDistArray[i] = (b == 1);
                current++;
            }
           for (int i = 0; i < p; i++) {
                int b = (Integer)o[current];
                newDiscreteArray[i] = (b == 1);
                current++;
            }
           for (int i = 0; i < p; i++) {
                int b = (Integer)o[current];
                newMaxValsArray[i] = b-1;
                current++;
            }
            for (int i = 0; i < p; i++) {
                String s = (String)o[current];
                newNamesArray[i] = s;
                current++;
            }
            for (int i = 0; i < numCustom; i++) {
                String s = (String)o[current];
                newChuckFeatNamesArray[i] = s;
                current++;  
            }
            setDoesParamUseDistribution(newDistArray);
            setIsParamDiscrete(newDiscreteArray);
            setNumSynthMaxParamVals(newMaxValsArray);
            setParamNames(newNamesArray);
            setChuckFeatureNames(newChuckFeatNamesArray);
            setState(ChuckSystemState.CONNECTED_AND_VALID);
            logger.log(Level.INFO, "Set configuration: " + this.toString());
        } catch (Exception ex) {
                Logger.getLogger(ChuckSystem.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

        /**
     * Get the value of paramNames
     *
     * @return the value of paramNames
     */
    public String[] getParamNames() {
        return paramNames;
    }

    /**
     * Set the value of paramNames
     *
     * @param paramNames new value of paramNames
     */
    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    /**
     * Get the value of paramNames at specified index
     *
     * @param index
     * @return the value of paramNames at specified index
     */
    public String getParamNames(int index) {
        return this.paramNames[index];
    }

    /**
     * Set the value of paramNames at specified index.
     *
     * @param index
     * @param newParamNames new value of paramNames at specified index
     */
    public void setParamNames(int index, String newParamNames) {
        this.paramNames[index] = newParamNames;
    }



        @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public String toString() {
            String s = numParams + " parameters\n";
            for (int i = 0; i < numParams; i++) {
                s += paramNames[i] + " (param" + i + "): ";
                if (isParamDiscrete[i]) {
                    s += "discrete, " + numSynthMaxParamVals[i] + " values, " + (doesParamUseDistribution[i] ? " " : "not ") + "using distribution\n";
                } else {
                    s += "real-valued\n";
                }
            }
            if (usingChuckFeatureExtractor) {
                s += "Using chuck feature extractor with " + numChuckCustomFeatures + " chuck features\n";
            } else {
                s += "Not using chuck feature extractor\n";
            }

            s += "State is " + state + "\n";
            return s;
        }

        /**
     * Get the value of chuckFeatureNames
     *
     * @return the value of chuckFeatureNames
     */
    public String[] getChuckFeatureNames() {
        return chuckFeatureNames;
    }

    /**
     * Set the value of chuckFeatureNames
     *
     * @param chuckFeatureNames new value of chuckFeatureNames
     */
    public void setChuckFeatureNames(String[] chuckFeatureNames) {
        this.chuckFeatureNames = chuckFeatureNames;
    }

    /**
     * Get the value of chuckFeatureNames at specified index
     *
     * @param index
     * @return the value of chuckFeatureNames at specified index
     */
    public String getChuckFeatureNames(int index) {
        return this.chuckFeatureNames[index];
    }

    /**
     * Set the value of chuckFeatureNames at specified index.
     *
     * @param index
     * @param newChuckFeatureNames new value of chuckFeatureNames at specified index
     */
    public void setChuckFeatureNames(int index, String newChuckFeatureNames) {
        this.chuckFeatureNames[index] = newChuckFeatureNames;
    }


    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
   /* void addPropertyChangeListener(PropertyChangeListener listener) {
        //TODO;
    }




    int getNumSynthParams() { }

    boolean[] getDoesParamUseDistribution() { } */

   /*
    boolean isUsable() {

    void setIsSynthParamDiscrete(boolean[] isSynthParamDiscrete);

    void setNumSynthMaxParamVals(int numSynthMaxParamVals);

    void setNumSynthParams(int numSynthParams);

    void setDoesParamUseDistribution(boolean[] doesParamUseDistribution); */

}
