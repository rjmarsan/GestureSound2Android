/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//import javax.swing.event.EventListenerList;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.Reorder;

/**
 *
 * @author Rebecca Fiebrink
 */
public class SimpleDataset {


//    protected EventListenerList listenerList = new EventListenerList();
    protected int numParams = 0;
    protected int numFeatures = 0; //Total # features being stored, doesn't include my metadata
//    private boolean featureParamMask[][] = null;
    protected boolean[] isParamDiscrete = null;
    protected String[] featureNames = null;
    protected String[] paramNames = null;
    protected int numParamValues[] = null;
    protected Instances allInstances = null;
    protected Instances dummyInstances = null; //An empty set of instances with proper heading info
    protected Filter[] learnerFilters = null;
    protected Remove allFeaturesOnly = null;
    protected Remove allParamsOnly = null;
    protected int nextID = 0;
    protected List<RawAudioSegment> audioSegments = null;
    protected int currentTrainingRound = 0;
    protected final int numMetaData = 3;
    protected int idIndex = 0;
    protected int timestampIndex = 1;
    protected int trainingIndex = 2;
    protected static final SimpleDateFormat oldDateFormat = new SimpleDateFormat("HHmmss");
    public static final SimpleDateFormat oldPrettyDateFormat = new SimpleDateFormat("HH:mm:ss");
    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssSSS");
    public static final SimpleDateFormat prettyDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    protected boolean hasInstances = false;
    public static final String PROP_HASINSTANCES = "hasInstances";
    protected FeatureLearnerConfiguration featureLearnerConfiguration = null;
//    private ChangeEvent changeEvent = null;

    public static String getFileExtension() {
        return "wdata";
    }

    public static String getFileTypeDescription() {
        return "Wekinator dataset";
    }

    public static String getDefaultLocation() {
        return "datasets";
    }

    /**
     * Get the value of featureLearnerConfiguration
     *
     * @return the value of featureLearnerConfiguration
     */
    public FeatureLearnerConfiguration getFeatureLearnerConfiguration() {
        return featureLearnerConfiguration;
    }

    /**
     * Set the value of featureLearnerConfiguration
     *
     * @param featureLearnerConfiguration new value of featureLearnerConfiguration
     */
    protected void setFeatureLearnerConfiguration(FeatureLearnerConfiguration featureLearnerConfiguration) {
        this.featureLearnerConfiguration = featureLearnerConfiguration;
        try {
            updateFilters();
        } catch (Exception ex) {
            System.out.println("ERROR HERE");
            Logger.getLogger(SimpleDataset.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMappingForLearner(int learner, int[] mapping) throws Exception {
        
        featureLearnerConfiguration.setFeatureMappingForLearner(learner, mapping);
//        if (WekinatorRunner.isLogging) {
//            Plog.logFeatureMap(learner);
//        }
        updateFilters(); //TODO: set to only update this learner's filter
    }

    /**
     * Get the value of hasInstances
     *
     * @return the value of hasInstances
     */
    public boolean isHasInstances() {
        return hasInstances;
    }

    /**
     * Set the value of hasInstances
     *
     * @param hasInstances new value of hasInstances
     */
    protected void setHasInstances(boolean hasInstances) {
       // System.out.println("has instances set to " + hasInstances);
        boolean oldHasInstances = this.hasInstances;
        this.hasInstances = hasInstances;
//        propertyChangeSupport.firePropertyChange(PROP_HASINSTANCES, oldHasInstances, hasInstances);
    }
//    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

//    /**
//     * Add PropertyChangeListener.
//     *
//     * @param listener
//     */
//    public void addPropertyChangeListener(PropertyChangeListener listener) {
//        propertyChangeSupport.addPropertyChangeListener(listener);
//    }
//
//    /**
//     * Remove PropertyChangeListener.
//     *
//     * @param listener
//     */
//    public void removePropertyChangeListener(PropertyChangeListener listener) {
//        propertyChangeSupport.removePropertyChangeListener(listener);
//    }

    public SimpleDataset(int numFeatures, int numParams, boolean[] isParamDiscrete, int[] numParamValues, String[] featureNames, String[] paramNames) {
        if (isParamDiscrete == null || isParamDiscrete.length != numParams) {
            throw new IllegalArgumentException("isParamDiscrete.length must match numParams");
        }
        this.numParams = numParams;
        this.numFeatures = numFeatures;

        /* featureParamMask = new boolean[numFeatures][numParams];
        for (int i = 0; i < this.numFeatures; i++) {
        for (int j = 0; j < this.numParams; j++) {
        featureParamMask[i][j] = true;
        }
        }*/

        this.featureNames = new String[numFeatures];
        if (featureNames != null && featureNames.length == numFeatures) {
            for (int i = 0; i < this.numFeatures; i++) {
                this.featureNames[i] = featureNames[i];
            }
        } else {
            for (int i = 0; i < numFeatures; i++) {
                this.featureNames[i] = "Feature " + Integer.toString(i);
            }
        }


        this.paramNames = new String[this.numParams];

        if (paramNames != null && paramNames.length == numParams) {
            for (int i = 0; i < numParams; i++) {
                this.paramNames[i] = paramNames[i];
            }
        } else {
            for (int i = 0; i < numParams; i++) {
                this.paramNames[i] = "Param " + i;
            }
        }


        this.isParamDiscrete = new boolean[numParams];
        for (int i = 0; i < numParams; i++) {
            this.isParamDiscrete[i] = isParamDiscrete[i];
        }

        this.numParamValues = new int[numParams];
        for (int i = 0; i < numParams; i++) {
            if (numParamValues != null && numParamValues.length > i) {
                this.numParamValues[i] = numParamValues[i];
            } else {
                this.numParamValues[i] = 0;
            }
        }

        //Set up instances
        FastVector ff = new FastVector(numFeatures + numParams + numMetaData); //Include ID, timestamp, training round
        //add ID, timestamp, and training round #
        ff.addElement(new Attribute("ID"));
        ff.addElement(new Attribute("Timestamp")); //yyMMddHHmmss format; stored as double
        ff.addElement(new Attribute("Training round"));
        //new Attribute

        //Add features
        for (int i = 0; i < numFeatures; i++) {
            ff.addElement(new Attribute(this.featureNames[i]));
        }

        //Add parameters
        for (int i = 0; i < numParams; i++) {
            if (isParamDiscrete[i]) {
                if (numParamValues == null || numParamValues.length <= i) {
                    System.out.println("Problem: Length is " + numParamValues.length + " i is " + i);
                    throw new IllegalArgumentException("Invalid isParamDiscrete argument: Must be valid length when using discrete parameters");
                }
                //Create fastVector w/ possible
                FastVector classes = new FastVector(numParams);
                for (int val = 0; val < numParamValues[i]; val++) {
                    classes.addElement((new Integer(val)).toString());
                }
                ff.addElement(new Attribute(this.paramNames[i], classes));

            } else {
                ff.addElement(new Attribute(this.paramNames[i]));
            }
        }

        allInstances = new Instances("dataset", ff, 100);

        //Set up dummy instances to reflect state of actual instances
        dummyInstances = new Instances(allInstances);

        try {
            setFeatureLearnerConfiguration(new FeatureLearnerConfiguration(numParams, numFeatures));

            setupFilters();
        } catch (Exception ex) {
            System.out.println("Error: Couldn't set up filters correctly");
            System.exit(0);
        }

        // idMap = new HashMap<Integer, Instance>();
        audioSegments = new LinkedList<RawAudioSegment>();
    }

    public void setFeatureValue(int index, int featNum, double value) {
        if (featNum < 0 || featNum >= numFeatures) {
            throw new IllegalArgumentException("Invalid feature number in setFeatureValue");
        }
        Instance i = allInstances.instance(index);
        if (i != null) {
            i.setValue(numMetaData + featNum, value);

        } //else TODO ?
    }

   public void setParameterMissing(int index, int paramNum) {
        if (paramNum >= 0 && paramNum < numParams) {
            Instance i = allInstances.instance(index);
            i.setMissing(numMetaData + numFeatures + paramNum);
        }
    }

    public void setParameterValue(int index, int paramNum, double value) {
        if (paramNum < 0 || paramNum >= numParams) {
            throw new IllegalArgumentException("Invalid parameter number in setParameterValue");
        }

        Instance i = allInstances.instance(index);
        if (i == null) {
            return;
        }

        if (isParamDiscrete[paramNum]) {
            int v = (int) value;
            Attribute a = i.attribute(numMetaData + numFeatures + paramNum);
            if (a.isNominal() && v >= 0 && v <= maxLegalDiscreteParamValue(paramNum)) {
                i.setValue(numMetaData + numFeatures + paramNum, v);

            } else {
                System.out.println("error: attribute value out of range");
            //TODO: CHeck this
            }
        } else {
            //Don't care about error checking for real-valued parameter
            i.setValue(numMetaData + numFeatures + paramNum, value);
        }
    }

    public void processEndGestures(boolean[] mask) {
        //For each param in mask, delete all lables w/in same training round w/ same label, except last
        for (int i = 0; i < mask.length; i++) {
            if (mask[i]) {
                for (int j = 0; j < allInstances.numInstances()-1; j++ ) {
                    Instance i1 = allInstances.instance(j);
                    Instance i2 = allInstances.instance(j+1);
                    if (!i1.isMissing(numMetaData + numFeatures + i)
                            && !i2.isMissing(numMetaData + numFeatures + i)
                            && i1.value(numMetaData + numFeatures + i) == i2.value(numMetaData + numFeatures + i)
                            && getTrainingRound(j) == getTrainingRound(j+1)) {
                        i1.setMissing(numMetaData + numFeatures + i);
                    }
                }
            }
        }
    }

    private Instances createInstancesForParameter(int paramNum) {
        if (paramNum < 0 || paramNum >= numParams) {
            throw new IllegalArgumentException("Invalid paramNum");
        }

        //use filter[paramNum];
        Instances i;
        try {
            i = Filter.useFilter(allInstances, learnerFilters[paramNum]);
            //set class index
            i.setClassIndex(i.numAttributes() - 1);
            return i;
        } catch (Exception ex) {
            System.out.println("COuldn't filter");
            Logger.getLogger(SimpleDataset.class.getName()).log(Level.SEVERE, "bad news!", ex);
            return null;
        }

    }

   /* private void updateFilterOld(int filterNum) throws Exception {
        assert (filterNum > 0 && filterNum < learnerFilters.length);

        //Get rid of metadata, other params, and non-used features
        learnerFilters[filterNum] = new Remove();

        String removeString = ""; //string is index starting w/ 1

        for (int j = 0; j < numMetaData; j++) {
            removeString += Integer.toString(j + 1) + ",";
        }

        //Remove unused features
        for (int i = 0; i < numFeatures; i++) {
            if (!featureParamMask[i][filterNum]) {
                removeString += Integer.toString(numMetaData + i + 1) + ",";
            }
        }

        //Get rid of other params
        for (int i = 0; i < numParams; i++) {
            if (i != filterNum) {
                removeString += Integer.toString(numMetaData + numFeatures + i + 1) + ",";
            }
        }

        //Remove last comma
        removeString = removeString.substring(0, removeString.length());

        // RAF CHANGED learnerFilters[filterNum].setAttributeIndices(removeString);

        learnerFilters[filterNum].setInputFormat(dummyInstances);

    } */

    private void updateFilters() throws Exception {
        learnerFilters = new Reorder[numParams];
        //PROBLEM: also have those pesky other features specific to our dataset.
        for (int i = 0; i < numParams; i++) {
            //learnerFilters[i] = featureLearnerConfiguration.getReorderFilterForLearner(i);
            Reorder r = new Reorder();
            int[] featureMapping = featureLearnerConfiguration.getFeatureMappingForLearner(i);
            int[] reordering = new int[featureMapping.length + 1];

            //Features
            for (int f = 0; f < featureMapping.length; f++) {
                reordering[f] = featureMapping[f] + numMetaData;
            }

            //The actual class parameter
            reordering[reordering.length - 1] = numMetaData + numFeatures + i;
            r.setAttributeIndicesArray(reordering);
            r.setInputFormat(dummyInstances);

            learnerFilters[i] = r;

        }
    }

    //TODO: test this
    private void setupFilters() throws Exception {
        // learnerFilters = new Remove[numParams];
        // for (int p = 0; p < numParams; p++) {
        //     updateFilterOld(p);
        // }

        //Filter so only features are present
        allFeaturesOnly = new Remove();
        int[] indicesToRemove = new int[numMetaData + numParams];
        for (int i = 0; i < numMetaData; i++) {
            indicesToRemove[i] = i;
        }
        for (int i = 0; i < numParams; i++) {
            indicesToRemove[numMetaData + i] = numMetaData + numFeatures + i;
        }
        allFeaturesOnly.setAttributeIndicesArray(indicesToRemove);
        allFeaturesOnly.setInputFormat(dummyInstances);

        //Filter so only params are present
        allParamsOnly = new Remove();
        indicesToRemove = new int[numMetaData + numFeatures];
        for (int i = 0; i < numMetaData; i++) {
            indicesToRemove[i] = i;
        }
        for (int i = 0; i < numFeatures; i++) {
            indicesToRemove[numMetaData + i] = numMetaData + i;
        }
        allParamsOnly.setAttributeIndicesArray(indicesToRemove);
        allParamsOnly.setInputFormat(dummyInstances);
    }


    //TODO: Problem here: Should only be able to change this @ beginning... ideally will never change it.
    //Get rid of all references!!
/*    public void setNumParameters(int num) {
    assert(num == numParams);

    } */
    public int getNumDatapoints() {
        return allInstances.numInstances();
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public int getNumFeaturesForParam(int paramNum) {
        if (paramNum < 0 || paramNum >= numParams) {
            System.out.println("Error: Unexpected paramNum: invalid range");
            return 0;
        }

        /*   int sum = 0;
        for (int i = 0; i < numFeatures; i++) {
        if (featureParamMask[i][paramNum]) {
        sum++;
        }
        }
        return sum; */
        return featureLearnerConfiguration.getFeatureMappingForLearner(paramNum).length;

    }

    public int getNumParameters() {
        return numParams;
    }

    /**
     * Convert an array of doubles to an array of classifiable instances
     * @param d the double array
     * @return an Instance array; each instance corresponds to a parameter
     */
    public Instance[] convertToClassifiableInstances(double[] d) {
        if (d == null || d.length != numFeatures) {
            throw new IllegalArgumentException("Wrong d size");
        }
        double data[] = new double[numMetaData + numFeatures + numParams];

        for (int i = 0; i < numFeatures; i++) {
            data[numMetaData + i] = d[i];
        }

        Instance[] is = new Instance[numParams];
        for (int i = 0; i < numParams; i++) {
            is[i] = new DenseInstance(1.0, data);
            Instances tmp = new Instances(dummyInstances);
            tmp.add(is[i]);
            try {
                tmp = Filter.useFilter(tmp, learnerFilters[i]);
                tmp.setClassIndex(tmp.numAttributes() - 1);
                is[i] = tmp.firstInstance();
            } catch (Exception ex) {
                System.out.println("Error: could not filter");
                Logger.getLogger(SimpleDataset.class.getName()).log(Level.SEVERE, null, ex);
            }
            tmp.setClassIndex(tmp.numAttributes() - 1);
        }

        return is;
    }

    /**
     *
     * @param index the Instance ID
     * @param c the class number (between 0 and numParams - 1)
     * @return a classifier-ready Instance, or null if it doesn't exist
     */
    /*public double[] getFeatures(int index) {
    if (idMap.containsKey(index)) {
    Instance i = new Instance(idMap.get(index));
    allFeaturesOnly.input(i);
    Instance ii = allFeaturesOnly.output();
    return ii.toDoubleArray();
    } else {
    System.out.println("No such key found" + index);
    return null;
    }
    }*/
    public double[] getFeatures(int index) {


        Instance i = allInstances.instance(index);
        if (i == null) {
            return null;
        }

        allFeaturesOnly.input(i);
        Instance ii = allFeaturesOnly.output();
        return ii.toDoubleArray();
    }

    public double getFeature(int index, int featNum) {
        Instance i = allInstances.instance(index);
        if (i == null || i.numAttributes() < (featNum + numMetaData)) {
            return Double.NaN;
        }
        return i.value(featNum + numMetaData);
    }

    public double getParam(int index, int paramNum) {
        Instance i = allInstances.instance(index);
        if (i == null || i.numAttributes() < (numFeatures + numMetaData + paramNum)) {
            return Double.NaN;
        }
        if (i.isMissing(numMetaData + numFeatures + paramNum)) {
            return Double.NaN;
        }
        return i.value(numMetaData + numFeatures + paramNum);
    }

    public double getID(int index) {
        //   if (idMap.containsKey(index)) {
        if (index >= 0 && index < allInstances.numInstances()) {

            //Instance i = idMap.get(index);
            Instance in = allInstances.instance(index);
            if (in != null) {
                return in.value(idIndex);
            }
        }
        return 0;
    }

    public double getTimestamp(int index) {
        //   if (idMap.containsKey(index)) {
        if (index >= 0 && index < allInstances.numInstances()) {

            //Instance i = idMap.get(index);
            Instance in = allInstances.instance(index);
            if (in != null) {
                return in.value(timestampIndex);
            }
        }
        return 0;
    }

    public void setTimestamp(int index, String timestamp) throws ParseException {
        if (index >= 0 && index < allInstances.numInstances()) {
            Instance in = allInstances.instance(index);
            if (in != null) {
                Date d = prettyDateFormat.parse(timestamp);
                in.setValue(timestampIndex, Double.parseDouble(dateFormat.format(d)));
                return;
            }
        }
    //Else throw exception? TODO
    }

    public int getTrainingRound(int index) {
        if (index >= 0 && index < allInstances.numInstances()) {

            //Instance i = idMap.get(index);
            Instance in = allInstances.instance(index);
            if (in != null) {
                return (int) in.value(trainingIndex);
            }
        }
        return -1;
    }

    public void setTrainingRound(int index, int round) {
        Instance in = allInstances.instance(index);
        if (in != null) {
            in.setValue(trainingIndex, round);
            if (round > currentTrainingRound) {
                currentTrainingRound = round + 1;
            }
        }


    }

    /**
     *
     * @param index the Instance ID
     * @return an array of all parameter values
     */
    public double[] getParameters(int index) {
        if (index >= 0 && index < allInstances.numInstances()) {
            Instance i = allInstances.instance(index);
            allParamsOnly.input(i);
            Instance ii = allParamsOnly.output();
            return ii.toDoubleArray();
        } else {
            System.out.println("No such index found" + index);
            return null;
        }
    }

    //TODO: use this!
    public void startNewTrainingRound() {
        currentTrainingRound++;
    }

    //TODO TODO TODO: Error in addInstance - wrong parameter value
    //How do we handle missing features or missing parameters??
    public void addInstance(double[] featureVals, double paramVals[], boolean paramMask[], Date timeStamp) {

        if (featureVals == null || featureVals.length != numFeatures) {
            String err = "Wrong feature vals; ";
            if (featureVals == null) {
                err += "got null";
            } else {
                err += "length = " + featureVals.length + " when expected " + numFeatures;
            }
            throw new IllegalArgumentException(err);
        }
        if (paramVals == null || paramVals.length != numParams || paramMask == null || paramMask.length != numParams) {
            //TODO: Error happens here when switch to discrete params:
            throw new IllegalArgumentException("Wrong parameter values or mask");
        }



        int thisId = nextID;
        nextID++;

        double myVals[] = new double[numMetaData + numFeatures + numParams];
        myVals[idIndex] = thisId;
        myVals[trainingIndex] = currentTrainingRound;
        myVals[timestampIndex] = Double.parseDouble(dateFormat.format(timeStamp));

        for (int i = 0; i < numFeatures; i++) {
            myVals[numMetaData + i] = featureVals[i];
        }

        for (int i = 0; i < numParams; i++) {
            if (isParamDiscrete[i] && (paramVals[i] < 0 || paramVals[i] >= numParamValues[i])) {
                throw new IllegalArgumentException("Invalid value for this discrete parameter");
            }

            myVals[numMetaData + numFeatures + i] = paramVals[i];
        }

//        Instance in = new Instance(1.0, myVals);
        Instance in = new DenseInstance(1.0, myVals);
        for (int i = 0; i < paramMask.length; i++) {
            if (!paramMask[i]) {
                in.setMissing(numMetaData + numFeatures + i);
            }
        }
        in.setDataset(allInstances);
        allInstances.add(in);
        setHasInstances(true);
//        fireStateChanged();


    // idMap.put(thisId, in);

    }

    public void addInstance(double[] featureVals, double paramVals[], Date timestamp) {
        boolean mask[] = new boolean[numParams];
        /*for (boolean m : mask) {
        m = false;
        System.out.println("added true");
        }*/
        for (int i = 0; i < mask.length; i++) {
            mask[i] = true;
        }
        addInstance(featureVals, paramVals, mask, timestamp);
    }

    public void addInstance(double[] featureVals, double paramVals[]) {
        Date now = new Date();
        addInstance(featureVals, paramVals, now);
    }

    /**
     *
     * @param index
     * @return true if deleted
     */
    public boolean deleteInstance(int index) {
        if (index >= 0 && index < allInstances.numInstances()) {
            allInstances.delete(index);
            if (allInstances.numInstances() == 0) {
                setHasInstances(false);
            }
//    fireStateChanged();

            return true;

        } else {
            return false;
        }
    }

    /**
     * Delete all instances (doesn't reset data like #feats, #params, etc.)
     */
    public void deleteAll() {
        allInstances.delete();
        setHasInstances(false);
//            fireStateChanged();


    }

    /**
     * 
     * @param c
     * @return a Weka Instances object corresponding to the learning problem for this parameter #
     */
    public Instances getClassifiableInstances(int paramNum) {
        if (paramNum < 0 || paramNum >= numParams) {
            throw new IllegalArgumentException("Invalid paramNum");
        }
        try {
            Instances in = Filter.useFilter(allInstances, learnerFilters[paramNum]);
            in.setClassIndex(in.numAttributes() - 1);
            in.deleteWithMissingClass();
            return in;
        } catch (Exception ex) {
            Logger.getLogger(SimpleDataset.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Deprecated: Don't trust this function!
    //TODO: Remove!
    public void setIsFeatureActiveForParameter(boolean isActive, int featNum, int paramNum) {
        /*      if (featNum < 0 || featNum >= numFeatures || paramNum < 0 || paramNum >= numParams) {
        throw new IllegalArgumentException("Invalid paramNum or featNum");

        }

        if (featureParamMask[featNum][paramNum] != isActive) {
        featureParamMask[featNum][paramNum] = isActive;
        try {
        updateFilterOld(paramNum);
        } catch (Exception ex) {
        System.out.println("Error: Couldn't update filter");
        Logger.getLogger(SimpleDataset.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

        //TODO
        //If not active, do we really want to delete it? Probably not; keep it around just in case.
         * */
    }

    //Deprecated! TODO: remove
    public boolean isFeatureActiveForParameter(int featNum, int paramNum) {
        /* if (featNum < 0 || featNum >= numFeatures || paramNum < 0 || paramNum >= numParams) {
        System.out.println("Error: invalid featNum / paramNum");
        return false;
        }

        return featureParamMask[featNum][paramNum]; */
        return true;
    }

    public void setFeatureName(int featureNum, String name) {
        if (featureNum >= 0 && featureNum < numFeatures) {
            featureNames[featureNum] = name;
            //TODO: must change attribute name too
           //  allInstances.attribute(numMeta + featureNum).
             allInstances.renameAttribute(numMetaData + featureNum, name);
//             fireStateChanged();
        }

    }

    public String getFeatureName(int featureNum) {
        if (featureNum >= 0 && featureNum < numFeatures) {
            return featureNames[featureNum];
        }
        return null;
    }

    public String[] getFeatureNames() {
        return featureNames;
    }

    public void setParameterName(int paramNum, String name) {
        if (paramNum >= 0 && paramNum < numParams) {
            paramNames[paramNum] = name;
//                fireStateChanged();

        }
    }

    public String getParameterName(int paramNum) {
        if (paramNum >= 0 && paramNum < numParams) {
            return paramNames[paramNum];
        }
        return null;
    }

    public String[] getParameterNames() {
        return paramNames;
    }

    public boolean isParameterDiscrete(int paramNum) {
        if (paramNum >= 0 && paramNum < numParams) {
            return isParamDiscrete[paramNum];
        } else {
            return false;
        }
    }

    //Gets the maximum legal value for this parameter
    public int maxLegalDiscreteParamValue(int paramNum) {
        if (paramNum >= 0 && paramNum < numParams && isParamDiscrete[paramNum]) {
            return numParamValues[paramNum] - 1;
        }

        return -1; //TODO: something better?
    }

    //Returns array of maximum *legal class ID* values (not number of classes)
    public int[] getMaxLegalDiscreteParamValues() {
        int[] maxValues = new int[numParamValues.length];
        for (int i = 0; i < maxValues.length; i++) {
            maxValues[i] = numParamValues[i] - 1;
        }
        //return numParamValues;
        return maxValues;
    }

    public String datasetToString() {
        return allInstances.toString();
    }

    /**
     *
     * @param name
     * @param isDiscrete
     * @return the parameter number of this new param
     */
    /* public int addParameter(String name, boolean isDiscrete) {
    //TODO: create new instances, adjust bookkeeping variabless
    return 0;
    }*/
    /**
     *
     * @param name
     * @return the number of the new feature
     */
    /* public int addFeature(String name) {
    //TODO
    return 0;
    }*/
    public String dateDoubleToString(double d) { //TODO: test!
        Date date;
        try {
           /* String ds = Double.toString(d); */ //hack
            String ds = "" + (int)d;

            while (ds.length() < 9) {
                ds = "0" + ds;
            }
            
            date = dateFormat.parse(ds);
            return prettyDateFormat.format(date);

        } catch (ParseException ex) {
            Logger.getLogger(SimpleDataset.class.getName()).log(Level.WARNING, "bad date", ex);
          //  System.out.println("Here");
            return "";
        }
    }

    public void exportAsArffFiles(String filePrefix) {
        //TODO: export arff files, 1 per parameter, as filePrefix_p1.arff ... filePrefix_pN.arff
    }

    public void exportAsArffFile(String filename) throws IOException {
        //TODO: export arff file - 1 file, each param as an attribute, no class set
        writeInstancesToArff(new File(filename));
    }

    public void importFromArffFile(String filename) {
        //TODO: import from arff filename; 
    }

    //TODO: add
    /* raw audio support
     * ability to add metafeatures based on statistics of these features (esp.: moving average, time deltas)
     * 
     * */
    public static void main(String[] args) {
        boolean isDiscrete[] = {true, false};
        int numVals[] = {3, 3};
        String featureNames[] = {"F1", "f2", "F3", "f4", "f5"};
        String paramNames[] = {"P1", "p2"};
        SimpleDataset s = new SimpleDataset(5, 2, isDiscrete, numVals, featureNames, paramNames);

        double fvals[] = {1.0, 2.0, 3.0, 4.0, 5.0};
        double pvals[] = {0.0, 1.5};
        s.addInstance(fvals, pvals);

        double fvals2[] = {2.0, 2.0, 3.0, 4.0, 5.0};
        double pvals2[] = {2.0, 3.5};

        boolean mask1[] = {false, false};
        s.startNewTrainingRound();
        s.addInstance(fvals2, pvals2, mask1, new Date());

        FeatureLearnerConfiguration flc = new FeatureLearnerConfiguration(2, 5);
        int[] t = {4, 3};
        try {
            flc.setFeatureMappingForLearner(0, t);
        //   s.setIsFeatureActiveForParameter(false, 1, 0);
        } catch (Exception ex) {
            Logger.getLogger(SimpleDataset.class.getName()).log(Level.SEVERE, null, ex);
        }
        s.setFeatureLearnerConfiguration(flc);
        System.out.println("Printing out 0");
        Instances i = s.getClassifiableInstances(0);
        System.out.println(i);

        System.out.println("*****\n1:");
        i = s.getClassifiableInstances(1);
        System.out.println(i);


        //   s.setIsFeatureActiveForParameter(false, 1, 0);

        //  s.setIsFeatureActiveForParameter(false, 2, 0);

        //System.out.println(s.isFeatureActiveForParameter(1, 0));


        //       Instances i = s.getClassifiableInstances(1);
        //       System.out.println(i);

        /*      double newfvals[] = {2.0, 4.0, 6.0, 8.0, 10.0};
        Instance is[] = s.convertToClassifiableInstances(newfvals);
        for (int i =0; i < is.length; i++) {
        System.out.println(is[i]);
        System.out.println(is[i].classIndex());
        }*/

        // System.out.println(s.datasetToString());

        /* System.out.println("Instance 0");

        double f[] = s.getFeatures(0);
        for (int i = 0; i < f.length; i++) {
        System.out.print(f[i] + " ");
        }
        System.out.println("");
        double p[] = s.getParameters(1); //TODO: Problem here: doesn't do ints right

        for (int i = 0; i < p.length; i++) {
        System.out.print(p[i] + " ");
        }
        System.out.println("");
        System.out.println(s.getTrainingRound(3));
        System.out.println(s.getTimestamp(3));
        double t = s.getTimestamp(3);
        System.out.println("String is " + s.dateDoubleToString(t));
         */

        /*Instances ii = s.getClassifiableInstances(1);
        System.out.println(ii);
         * */

        System.out.println("***");
        double[] d = s.getParameters(0);
        for (int f = 0; f < d.length; f++) {
            System.out.println(d[f]);
        }

        System.out.println("***");
        d = s.getFeatures(0);
        for (int f = 0; f < d.length; f++) {
            System.out.println(d[f]);
        }



    }

    public static SimpleDataset readFromFile(File f) throws Exception {
       // return (SimpleDataset) SerializedFileUtil.readFromFile(f);
        FileInputStream fin = new FileInputStream(f);
        ObjectInputStream i = new ObjectInputStream(fin);
        SimpleDataset s = loadFromInputStream(i);
        i.close();
        fin.close();
        return s;
    }

    public void writeToFile(File file) throws Exception {
                FileOutputStream fout = new FileOutputStream(file);
        ObjectOutputStream o = new ObjectOutputStream(fout);
        writeToOutputStreamNew(o);
        o.close();
        fout.close();
    }

    void writeInstancesToArff(File file) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(allInstances);
        saver.setFile(file);
        saver.writeBatch();
    }

    public void loadInstancesFromArff(File f) throws IOException {
        //Assumes already initialized properly; just need to load Instances
        ArffLoader loader = new ArffLoader();
        loader.setFile(f);
        Instances i = loader.getDataSet();
        //TODO: allow flexibility here: delete params or add ? params
        if (i.numAttributes() != (numFeatures + numParams + numMetaData)) {
            throw new IOException("Improper number of attributes: expecting " + (numFeatures + numParams + numMetaData));
        }
        allInstances = i;
        setHasInstances(allInstances.numInstances() > 0);

        //Set feature names
        featureNames = new String[numFeatures];
        int index = 0;
        for (int j = numMetaData; j < numMetaData + numFeatures; j++) {
            featureNames[index++] = allInstances.attribute(j).name();
        }

        //Set parameter names ?
        index = 0;
        paramNames = new String[numParams];
        isParamDiscrete = new boolean[numParams];
        numParamValues = new int[numParams];
        for (int j = numMetaData + numFeatures; j < numMetaData + numFeatures + numParams; j++) {
            paramNames[index] = allInstances.attribute(j).name();
            isParamDiscrete[index] = allInstances.attribute(j).isNominal();
            if (isParamDiscrete[index]) {
           //     numParamValues[index] = (int)allInstances.attribute(j).getUpperNumericBound()+1;
                Attribute a = allInstances.attribute(j);
                Enumeration e = a.enumerateValues();
                int max = 0;
                while (e.hasMoreElements()) {
                    String s = (String)e.nextElement();
                    Integer n = Integer.parseInt(s);
                    if (n > max) {
                        max = n;
                    }
                }
                numParamValues[index]= max+1;
            }
            index++;
        }
    }


//    public void addChangeListener(ChangeListener l) {
//        listenerList.add(ChangeListener.class, l);
//    }
//
//    public void removeChangeListener(ChangeListener l) {
//        listenerList.remove(ChangeListener.class, l);
//    }
//
//    protected void fireStateChanged() {
//        Object[] listeners = listenerList.getListenerList();
//        for (int i = listeners.length - 2; i >= 0; i -=2 ) {
//            if (listeners[i] == ChangeListener.class) {
//                if (changeEvent == null) {
//                    changeEvent = new ChangeEvent(this);
//                }
//                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
//            }
//        }
//    }
    
    public void writeToOutputStreamNew(ObjectOutputStream o) throws IOException {
                o.writeInt(numParams);
                o.writeInt(numFeatures);
                o.writeObject(isParamDiscrete);
                o.writeObject(featureNames);
                o.writeObject(paramNames);
                o.writeObject(numParamValues);
                o.writeObject(allInstances);
                o.writeInt(nextID);
                o.writeObject(audioSegments);
                o.writeInt(currentTrainingRound);
                o.writeInt(numMetaData);
                o.writeInt(idIndex);
                o.writeInt(timestampIndex);
                o.writeInt(trainingIndex);
                o.writeObject(dateFormat);
                o.writeObject(prettyDateFormat);
                o.writeBoolean(hasInstances);
                if (featureLearnerConfiguration == null) {
                    o.writeInt(0);
                } else {
                    o.writeInt(1);
                    featureLearnerConfiguration.writeToOutputStreamNew(o);
                }

    }
    public static SimpleDataset loadFromInputStream(ObjectInputStream i) throws IOException, ClassNotFoundException {
        SimpleDataset s;
        int numParams = i.readInt(); //
        int numFeatures = i.readInt(); //
        boolean[] isParamDiscrete = (boolean[]) i.readObject();//
        String[] featureNames = (String[]) i.readObject();//
        String[] paramNames = (String[])i.readObject();//
        int[] numParamValues = (int[]) i.readObject();//
        Instances allInstances = (Instances) i.readObject();//
        int nextId = i.readInt(); //
        List<RawAudioSegment> audioSegments = (List<RawAudioSegment>)i.readObject(); //
        int currentTrainingRound = i.readInt(); //
        int numMetaData = i.readInt(); //Could do something with these later if necessary
        int idIndex = i.readInt(); //later?
        int timestampIndex = i.readInt(); //later?
        int trainingIndex = i.readInt(); //later?
        SimpleDateFormat mdateFormat = (SimpleDateFormat) i.readObject(); //?
       SimpleDateFormat mprettyDateFormat = (SimpleDateFormat) i.readObject(); //?
       boolean hasInstances = i.readBoolean(); //
       int validFeatureLearner = i.readInt();
       FeatureLearnerConfiguration flc = null; //
       if (validFeatureLearner == 1) {
            flc = FeatureLearnerConfiguration.loadFromInputStream(i);
       }

       s = new SimpleDataset(numFeatures, numParams, isParamDiscrete, numParamValues, featureNames, paramNames);
       for (int j = 0; j < allInstances.numInstances(); j++) {
            s.allInstances.add(allInstances.instance(j));
       }
       s.setHasInstances(hasInstances);
       s.nextID = nextId;
       s.setFeatureLearnerConfiguration(flc);
       s.audioSegments = audioSegments;
       s.currentTrainingRound = currentTrainingRound + 1;

       return s;
    }

    

}
