/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator.LearningAlgorithms;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Same trained model should be able to be used
 * when more features are added to the extracted set (ignoring new features).
 *
 * Store feature identities here??
 *
 * @author rebecca
 */
public abstract class LearningAlgorithm {
    public static String getFileExtension() {
        return "wmodel";
    }

    public static String getFileTypeDescription() {
        return "learning algorithm";
    }

    public static String getDefaultLocation() {
        return "individualModels";
    }

    public static String PROP_TRAININGSTATE = "trainingState";
    protected TrainingState trainingState = TrainingState.NOT_TRAINED;

    public abstract String getSettingsDescription();

    public enum TrainingState {
        NOT_TRAINED, TRAINING, TRAINED
    };

        /**
     * Get the value of trainingState
     *
     * @return the value of trainingState
     */
    public TrainingState getTrainingState() {
        return trainingState;
    }

    /**
     * Set the value of trainingState
     *
     * @param trainingState new value of trainingState
     */
    protected void setTrainingState(TrainingState trainingState) {
        TrainingState oldTrainingState = this.trainingState;
        this.trainingState = trainingState; //abc: this next line is firing & causing reaction; not 2nd time
        propertyChangeSupport.firePropertyChange(PROP_TRAININGSTATE, oldTrainingState, trainingState);
    }
    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


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

    public abstract LearningAlgorithm copy();

    public abstract LearningAlgorithmSettingsPanel getSettingsPanel();

    public void setFastAccurate(double value) {
        //Underling classes only implement if implementsFastAccurate is true
    };

    public boolean implementsFastAccurate() { return false; }

    public abstract String getName();

    public abstract double classify(Instance instance) throws Exception;

    public abstract double[] distributionForInstance(Instance instance) throws Exception;

    public abstract void train(Instances instances) throws Exception;

    public abstract void forget();

    public abstract double computeAccuracy(Instances instances) throws Exception;

    public abstract double computeCVAccuracy(int numFolds, Instances instances) throws Exception;
    
    public abstract void writeToOutputStream(ObjectOutputStream o) throws IOException;

    public static LearningAlgorithm readFromFile(File f) throws ClassCastException, Exception {
        FileInputStream fin = new FileInputStream(f);
        ObjectInputStream i = new ObjectInputStream(fin);
        LearningAlgorithm a = readFromInputStream(i);
        i.close();
        fin.close();
        return a;
    }

     public void writeToFile(File f) throws Exception {
        FileOutputStream fout = new FileOutputStream(f);
        ObjectOutputStream o = new ObjectOutputStream(fout);
        writeToOutputStream(o);
        o.close();
        fout.close();
    }

    public static LearningAlgorithm readFromInputStream(ObjectInputStream i) throws IOException, ClassNotFoundException {
         String name = (String)i.readObject();
         if (name.equals(AdaboostM1LearningAlgorithm.class.getName())) {
             return AdaboostM1LearningAlgorithm.readFromInputStream(i, true);
         } else if (name.equals(IbkLearningAlgorithm.class.getName())) {
             return IbkLearningAlgorithm.readFromInputStream(i, true);
         } else if (name.equals(J48LearningAlgorithm.class.getName())) {
             return J48LearningAlgorithm.readFromInputStream(i, true);
         } else if (name.equals(NNLearningAlgorithm.class.getName())) {
             return NNLearningAlgorithm.readFromInputStream(i, true);
         } else if (name.equals(SMOLearningAlgorithm.class.getName())) {
             return SMOLearningAlgorithm.readFromInputStream(i, true);
         } else if (name.equals(OtherClassifierLearningAlgorithm.class.getName())) {
             return OtherClassifierLearningAlgorithm.readFromInputStream(i, true);
         } else if (name.equals(HmmLearningAlgorithm.class.getName())) {
            return HmmLearningAlgorithm.readFromInputStream(i, true);
         }
         else throw new IOException("No learning algorithm found for name " + name);
     }

}
