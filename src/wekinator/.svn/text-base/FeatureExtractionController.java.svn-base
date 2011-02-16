/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * //TODO: listen for feature name # / type changes in global wekinator system.
 *
 * @author rebecca
 */
public class FeatureExtractionController {
    protected FeatureViewer featureViewer = null;
    protected boolean extracting = false;
    public static final String PROP_EXTRACTING = "extracting";
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected static double[] lastChuckFeatures = new double[0];
    protected static double[] lastOscFeatures = new double[0];
    protected static double[] lastAllFeatures = new double[0];
    protected static int numChuckFeatures = 0;
    protected static int numOscFeatures = 0;
    private static final FeatureExtractionController ref = new FeatureExtractionController();

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public static void addPropertyChangeListener(PropertyChangeListener listener) {
        ref.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public static void removePropertyChangeListener(PropertyChangeListener listener) {
        ref.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private FeatureExtractionController() {
        WekinatorInstance.getWekinatorInstance().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                wekInstanceChanged(evt);
            }

        });

        setNumFeatsFromConfig(WekinatorInstance.getWekinatorInstance().getFeatureConfiguration());
    }

    /**
     * Get the value of extractingForViewing
     *
     * @return the value of extractingForViewing
     */
    public static boolean isExtracting() {
        return ref.extracting;
    }

    /**
     * Set the value of extractingForLearning
     *
     * @param extractingForLearning new value of extractingForLearning
     */
    private static void setExtracting(boolean extracting) {

        boolean oldTmpProp = ref.extracting;
        ref.extracting = extracting;
        ref.propertyChangeSupport.firePropertyChange(PROP_EXTRACTING, oldTmpProp, ref.extracting);

    }

    public static void showFeatureViewer() {
        //TODO: update when global features change!
        if (ref.featureViewer == null) {
            ref.featureViewer = new FeatureViewer();
        }
        //So names will refresh if necessary:
        ref.featureViewer.setNames(WekinatorInstance.getWekinatorInstance().getFeatureConfiguration().getAllEnabledFeatureNames());

        ref.featureViewer.setVisible(true);
        ref.featureViewer.toFront();
    }

    private static void updateFeatures() {
        double[] fs;
        if (numChuckFeatures != 0 && numOscFeatures == 0) {        
            fs = WekinatorInstance.getWekinatorInstance().getFeatureConfiguration().process(lastChuckFeatures);
        } else if (numChuckFeatures == 0 && numOscFeatures != 0) {
            fs = WekinatorInstance.getWekinatorInstance().getFeatureConfiguration().process(lastOscFeatures);
        } else {

            for (int i = 0; i < numChuckFeatures; i++) {
                lastAllFeatures[i] = lastChuckFeatures[i];
            }
            for (int i = 0; i < numOscFeatures; i++) {
                lastAllFeatures[numChuckFeatures + i] = lastOscFeatures[i];
            }
            fs = WekinatorInstance.getWekinatorInstance().getFeatureConfiguration().process(lastAllFeatures);
        }

       // if (isExtracting()) {
            if (ref.featureViewer != null) {
                ref.featureViewer.updateFeatures(fs);
            }
            WekinatorLearningManager.getInstance().updateFeatures(fs);
       // }
    }

    public static void updateChuckFeatures(double[] d) {
        if (d.length == numChuckFeatures) {
            lastChuckFeatures = d;
            updateFeatures();
        }
    }

    public static void updateOscFeatures(double[] d) {
        if (d.length == numOscFeatures) {
            lastOscFeatures = d;
            updateFeatures();
        }
    }

    public static void startExtracting() {
        setExtracting(true);
        OscHandler.getOscHandler().initiateRecord();
    }

    public static void stopExtracting() {
        setExtracting(false);
        OscHandler.getOscHandler().stopExtractingFeatures();
    }

    private void setNumFeatsFromConfig(FeatureConfiguration fc) {
        if (fc == null) {
            numChuckFeatures = 0;
            numOscFeatures = 0;
            lastChuckFeatures = new double[0];
            lastOscFeatures = new double[0];
            lastAllFeatures = new double[0];
        } else {
            int numTotal = fc.getNumBaseFeaturesEnabled();
            lastAllFeatures = new double[numTotal];
            if (fc.isUseCustomOscFeatures()) {
                numOscFeatures = fc.getNumCustomOscFeatures();
            } else {
                numOscFeatures = 0;
            }
            lastOscFeatures = new double[numOscFeatures];
            numChuckFeatures = numTotal - numOscFeatures; 
            lastChuckFeatures = new double[numChuckFeatures];
        }
    }


    private void wekInstanceChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorInstance.PROP_FEATURECONFIGURATION)) {
            FeatureConfiguration fc = WekinatorInstance.getWekinatorInstance().getFeatureConfiguration();
            //TODO: Not sure if hid setup changing here will be bad thing in terms of # chuck features expected
            //i.e. fc itself might not be replaced, but might change setup / num feats
            setNumFeatsFromConfig(fc);
        }
    }
}
