/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 *
 * @author rebecca
 */
public class FeatureConfiguration {

    public static boolean equal(FeatureConfiguration fc1, FeatureConfiguration fc2) {
        if (fc1 == fc2) {
            return true;
        } 

        if (fc1 == null || fc2 == null) {
            return false;
        }

        String[] s1 = fc1.getAllEnabledFeatureNames();
        String[] s2 = fc2.getAllEnabledFeatureNames();
        if (s1.length != s2.length) {
            return false;
        }

        for (int i = 0; i < s1.length; i++) {
            if (!s1[i].equals(s2[i])) {
                return false;
            }
        }
        return true;
    }

    public enum WindowType {

        HAMMING,
        HANN,
        RECTANGULAR
    };

    public enum ProcessingExractorType {

        DOWNSAMPLED_100,
        COLOR_6
    };

    //TODO: add support for multiple channels!
   protected EventListenerList listenerList = new EventListenerList();

    public static final String FFT = "FFT";
    public static final String CENTROID = "Centroid";
    public static final String FLUX = "Flux";
    public static final String RMS = "RMS";
    public static final String ROLLOFF = "Rolloff";
    public static final String CUSTOMCHUCK = "CustomChuck";
    public static final String CUSTOMOSC = "CustomOsc";
    public static final String TRACKPAD = "Trackpad";
    public static final String MOTION = "Motion";
    public static final String PROCESSING = "Processing";
    public static final String HID = "Hid";
    protected int motionSensorExtractionRate = 100;
    protected int fftWindowSize = 256;
    protected WindowType windowType = WindowType.HAMMING;
    protected int audioExtractionRate = 100;
    protected ProcessingExractorType processingExtractorType = ProcessingExractorType.DOWNSAMPLED_100;
    public static final String PROP_HIDSETUP = "hidSetup";
    public HidSetup hidSetup = new HidSetup();
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected int committedNumTotalFeatures = 0;
    protected int committedNumBaseFeatures = 0;

    private ChangeEvent featureNameChangeEvent = null;
    public ChangeEvent getFeatureNameChangeEvent() {
        return featureNameChangeEvent;
    }

    public static String getFileExtension() {
        return "wfconf";
    }

    public static String getFileTypeDescription() {
        return "Feature configuration";
    }

    public static String getDefaultLocation() {
        return "featureConfigurations";
    }

    protected void populateFeatureList() {
        featuresInOrder = new LinkedList<Feature>();
        features = new HashMap<String, Feature>();
        addFeature(FFT, 512);
        addFeature(RMS, 1);
        addFeature(CENTROID, 1);
        addFeature(ROLLOFF, 1);
        addFeature(FLUX, 1);
        addFeature(TRACKPAD, 2);
        addFeature(MOTION, 3);
        Feature f = new HidFeature(HID, 0);
        features.put(HID, f);
        featuresInOrder.add(f);
        addFeature(PROCESSING, 100);
        addFeature(CUSTOMCHUCK, 0);
        addFeature(CUSTOMOSC, 0);
    }

    protected void addFeature(String name, int dim) {
        Feature f = new Feature(name, dim);
        features.put(name, f);
        featuresInOrder.add(f);
    }
    protected HashMap<String, Feature> features;
    protected LinkedList<Feature> featuresInOrder;

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

    public int getNumAudioFeatures() {
        Feature fft = features.get(FFT);
        Feature centroid = features.get(CENTROID);
        Feature flux = features.get(FLUX);
        Feature rms = features.get(RMS);
        Feature rolloff = features.get(ROLLOFF);
        return ((fft.enabled ? ((int) (fft.getDimensionality() / 2)) : 0) + (centroid.enabled ? 1 : 0) + (flux.enabled ? 1 : 0) + (rms.enabled ? 1 : 0) + (rolloff.enabled ? 1 : 0));
    }

    public int getNumBaseFeatureClassesEnabled() {
        int s = 0;
        for (Feature f : features.values()) {
            if (f.enabled) {
                s++;
            }
        }
        return s;
    }

    public int getNumBaseFeaturesEnabled() {
        int s = 0;
        for (Feature f : features.values()) {
            if (f.enabled) {
                s += f.getDimensionality();
            }
        }
        return s;
    }

    public int getNumFeaturesEnabled() {
        return getNumBaseFeaturesEnabled() + getNumMetaFeaturesEnabled();
    }

    public int getNumMetaFeaturesEnabled() {
        int s = 0;
        for (Feature f : features.values()) {
            if (f.enabled) {
                for (List<MetaFeature> l : f.metaFeatures) {
                    if (l != null) {
                        for (MetaFeature mf : l) {
                            s += mf.getSize();
                        }
                    }
                }
            }
        }
        return s;
    }

    /**
     * Get the value of processingExtractorType
     *
     * @return the value of processingExtractorType
     */
    public ProcessingExractorType getProcessingExtractorType() {
        return processingExtractorType;
    }

    /**
     * Set the value of processingExtractorType
     *
     * @param processingExtractorType new value of processingExtractorType
     */
    public void setProcessingExtractorType(ProcessingExractorType processingExtractorType) {
        this.processingExtractorType = processingExtractorType;
        if (processingExtractorType == processingExtractorType.DOWNSAMPLED_100) {
            setNumProcessingFeatures(100);
        } else {
            setNumProcessingFeatures(6);
        }
    }

    /**
     * Get the value of audioExtractionRate
     *
     * @return the value of audioExtractionRate
     */
    public int getAudioExtractionRate() {
        return audioExtractionRate;
    }

    /**
     * Set the value of audioExtractionRate
     *
     * @param audioExtractionRate new value of audioExtractionRate
     */
    public void setAudioExtractionRate(int audioExtractionRate) {
        this.audioExtractionRate = audioExtractionRate;
    }

    /**
     * Get the value of windowType
     *
     * @return the value of windowType
     */
    public WindowType getWindowType() {
        return windowType;
    }

    /**
     * Set the value of windowType
     *
     * @param windowType new value of windowType
     */
    public void setWindowType(WindowType windowType) {
        this.windowType = windowType;
    }

    /**
     * Get the value of fftWindowSize
     *
     * @return the value of fftWindowSize
     */
    public int getFftWindowSize() {
        return fftWindowSize;
    }

    /**
     * Set the value of fftWindowSize
     *
     * @param fftWindowSize new value of fftWindowSize
     */
    public void setFftWindowSize(int fftWindowSize) {
        if (fftWindowSize > 0) {
            this.fftWindowSize = fftWindowSize;
        }
    }

    /**
     * Get the value of numProcessingFeatures
     *
     * @return the value of numProcessingFeatures
     */
    public int getNumProcessingFeatures() {
        return features.get(PROCESSING).getDimensionality();
    }

    /**
     * Set the value of numProcessingFeatures
     *
     * @param numProcessingFeatures new value of numProcessingFeatures
     */
    protected void setNumProcessingFeatures(int numProcessingFeatures) {
        if (numProcessingFeatures > 0) {
            features.get(PROCESSING).setDimensionality(numProcessingFeatures);
        }
    }

    /**
     * Get the value of useProcessing
     *
     * @return the value of useProcessing
     */
    public boolean isUseProcessing() {
        return features.get(PROCESSING).enabled;

    }

    /**
     * Set the value of useProcessing
     *
     * @param useProcessing new value of useProcessing
     */
    public void setUseProcessing(boolean useProcessing) {
        features.get(PROCESSING).enabled = useProcessing;
    }

    /**
     * Get the value of useOtherHid
     *
     * @return the value of useOtherHid
     */
    public boolean isUseOtherHid() {
        return features.get(HID).enabled;
    }

    /**
     * Set the value of useOtherHid
     *
     * @param useOtherHid new value of useOtherHid
     */
    public void setUseOtherHid(boolean useOtherHid) {
        features.get(HID).enabled = useOtherHid;
    }

    /**
     * Get the value of motionSensorExtractionRate
     *
     * @return the value of motionSensorExtractionRate
     */
    public int getMotionSensorExtractionRate() {
        return motionSensorExtractionRate;
    }

    /**
     * Set the value of motionSensorExtractionRate
     *
     * @param motionSensorExtractionRate new value of motionSensorExtractionRate
     */
    public void setMotionSensorExtractionRate(int motionSensorExtractionRate) {
        if (motionSensorExtractionRate > 0) {
            this.motionSensorExtractionRate = motionSensorExtractionRate;
        }
    }

    /**
     * Get the value of useMotionSensor
     *
     * @return the value of useMotionSensor
     */
    public boolean isUseMotionSensor() {
        return features.get(MOTION).enabled;
    }

    /**
     * Set the value of useMotionSensor
     *
     * @param useMotionSensor new value of useMotionSensor
     */
    public void setUseMotionSensor(boolean useMotionSensor) {
        features.get(MOTION).enabled = useMotionSensor;
    }

    /**
     * Get the value of useTrackpad
     *
     * @return the value of useTrackpad
     */
    public boolean isUseTrackpad() {
        return features.get(TRACKPAD).enabled;
    }

    /**
     * Set the value of useTrackpad
     *
     * @param useTrackpad new value of useTrackpad
     */
    public void setUseTrackpad(boolean useTrackpad) {
        features.get(TRACKPAD).enabled = useTrackpad;
    }

    /**
     * Get the value of numCustomOscFeatures
     *
     * @return the value of numCustomOscFeatures
     */
    public int getNumCustomOscFeatures() {
        return (features.get(CUSTOMOSC).getDimensionality());
    }

    /**
     * Set the value of numCustomOscFeatures
     *
     * @param numCustomOscFeatures new value of numCustomOscFeatures
     */
    public void setNumCustomOscFeatures(int numCustomOscFeatures) { //TODO: more error checking >0?
        features.get(CUSTOMOSC).setDimensionality(numCustomOscFeatures);
    }

    /**
     * Get the value of useCustomOscFeatures
     *
     * @return the value of useCustomOscFeatures
     */
    public boolean isUseCustomOscFeatures() {
        return features.get(CUSTOMOSC).enabled;
    }

    /**
     * Set the value of useCustomOscFeatures
     *
     * @param useCustomOscFeatures new value of useCustomOscFeatures
     */
    public void setUseCustomOscFeatures(boolean useCustomOscFeatures) {
        features.get(CUSTOMOSC).enabled = useCustomOscFeatures;
    }

    /**
     * Get the value of numCustomChuckFeatures
     *
     * @return the value of numCustomChuckFeatures
     */
    public int getNumCustomChuckFeatures() {
        return features.get(CUSTOMCHUCK).getDimensionality();
    }

    /**
     * Set the value of numCustomChuckFeatures
     *
     * @param numCustomChuckFeatures new value of numCustomChuckFeatures
     */
    public void setNumCustomChuckFeatures(int numCustomChuckFeatures) {
        features.get(CUSTOMCHUCK).setDimensionality(numCustomChuckFeatures);
    }

    /**
     * Get the value of useCustomChuckFeatures
     *
     * @return the value of useCustomChuckFeatures
     */
    public boolean isUseCustomChuckFeatures() {
        return features.get(CUSTOMCHUCK).enabled;
    }

    /**
     * Set the value of useCustomChuckFeatures
     *
     * @param useCustomChuckFeatures new value of useCustomChuckFeatures
     */
    public void setUseCustomChuckFeatures(boolean useCustomChuckFeatures) {
        features.get(CUSTOMCHUCK).enabled = useCustomChuckFeatures;
    }

    /**
     * Get the value of fftSize
     *
     * @return the value of fftSize
     */
    public int getFftSize() {
        return features.get(FFT).getDimensionality() * 2;
    }

    /**
     * Set the value of fftSize
     *
     * @param fftSize new value of fftSize
     */
    public void setFftSize(int fftSize) {
        features.get(FFT).setDimensionality(fftSize / 2);
    }

    /**
     * Get the value of useRolloff
     *
     * @return the value of useRolloff
     */
    public boolean isUseRolloff() {
        return features.get(ROLLOFF).enabled;
    }

    /**
     * Set the value of useRolloff
     *
     * @param useRolloff new value of useRolloff
     */
    public void setUseRolloff(boolean useRolloff) {
        features.get(ROLLOFF).enabled = useRolloff;
    }

    /**
     * Get the value of useRMS
     *
     * @return the value of useRMS
     */
    public boolean isUseRMS() {
        return features.get(RMS).enabled;
    }

    /**
     * Set the value of useRMS
     *
     * @param useRMS new value of useRMS
     */
    public void setUseRMS(boolean useRMS) {
        features.get(RMS).enabled = useRMS;
    }

    /**
     * Get the value of useFlux
     *
     * @return the value of useFlux
     */
    public boolean isUseFlux() {
        return features.get(FLUX).enabled;
    }

    /**
     * Set the value of useFlux
     *
     * @param useFlux new value of useFlux
     */
    public void setUseFlux(boolean useFlux) {
        features.get(FLUX).enabled = useFlux;
    }

    /**
     * Get the value of useCentroid
     *
     * @return the value of useCentroid
     */
    public boolean isUseCentroid() {
        return features.get(CENTROID).enabled;
    }

    /**
     * Set the value of useCentroid
     *
     * @param useCentroid new value of useCentroid
     */
    public void setUseCentroid(boolean useCentroid) {
        features.get(CENTROID).enabled = useCentroid;
    }

    /**
     * Get the value of hidSetup
     *
     * @return the value of hidSetup
     */
    public HidSetup getHidSetup() {
        return hidSetup;
    }

    /**
     * Set the value of hidSetup
     *
     * @param hidSetup new value of hidSetup
     */
    public void setHidSetup(HidSetup hidSetup) {
        HidSetup oldHidSetup = this.hidSetup;
        this.hidSetup = hidSetup;
        features.get(HID).setDimensionality(hidSetup.getNumFeaturesUsed());
        propertyChangeSupport.firePropertyChange(PROP_HIDSETUP, oldHidSetup, hidSetup);
    }

    public FeatureConfiguration() {
        populateFeatureList();
        WekinatorInstance.getWekinatorInstance().addOscFeatureNamesChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                wekOscFeaturesChanged();
            }
        });
    }

    /**
     * Get the value of useFFT
     *
     * @return the value of useFFT
     */
    public boolean isUseFFT() {
        return features.get(FFT).enabled;
    }

    /**
     * Set the value of useFFT
     *
     * @param useFFT new value of useFFT
     */
    public void setUseFFT(boolean useFFT) {
        features.get(FFT).enabled = useFFT;
    }

    public void validate() throws Exception {
        String errorString = "";
        if (getNumFeaturesEnabled() == 0) {
            errorString += "Must have more than 0 features.\n";
        }

        if (isUseCustomChuckFeatures() && getNumCustomChuckFeatures() <= 0) {
            errorString += "If using a custom chuck feature extractor, must have more than 0 custom chuck features\n";
        }

        if (isUseCustomOscFeatures() && getNumCustomOscFeatures() <= 0) {
            errorString += "If using a custom OSC feature extractor, must have more than 0 custom OSC features\n";
        }

        if (getNumAudioFeatures() > 0) {
            if (getFftSize() <= 0 || fftWindowSize <= 0 || audioExtractionRate <= 0) {
                errorString += "If using audio features, must have a valid (> 0) fft size, window size, and extraction rate\n";
            }

            if (fftWindowSize > getFftSize()) {
                errorString += "If using audio features, window size must be no greater than the FFT size\n";
            }
        }

        if (isUseMotionSensor() && motionSensorExtractionRate <= 0) {
            errorString += "Invalid motion sensor extraction rate\n";
        }

        if (isUseOtherHid() && (hidSetup == null || !hidSetup.isUsable())) {
            errorString += "Must have valid HID setup in order to use other hid\n";
        }

        if (errorString.length() > 0) {
            throw new Exception(errorString);
        }

       syncOscFeatures();

        //Commit # features
        committedNumTotalFeatures = getNumFeaturesEnabled();
        committedNumBaseFeatures = getNumBaseFeaturesEnabled();
    }

    public static FeatureConfiguration readFromFile(File f) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(f);
        ObjectInputStream in = new ObjectInputStream(fin);
        FeatureConfiguration fc = FeatureConfiguration.readFromInputStream(in);

         //hack:
        if (fc.isUseOtherHid()) {
            HidSetup h = fc.getHidSetup();
            WekinatorInstance.getWekinatorInstance().setCurrentHidSetup(h); //TODO: fix this; it sets up some background communication that happens immediately. Ideally would wait for this before "go" finishes.
        }
        in.close();
        fin.close();
        return fc;

    }

    public void writeToFile(File file) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fout);
        this.writeToOutputStreamNew(out);
        out.close();
        fout.close();
    }

    public String[] getEnabledBaseFeatureClassNames() {
        //Problem: Could result in different order every time!
        // String s[] = new String[getNumBaseFeaturesEnabled()]; //Problem: is base feature # dimensons or # features?
        LinkedList<String> s = new LinkedList<String>();

        int i = 0;
        for (Feature f : featuresInOrder) {
            if (f.enabled) {
                s.add(f.name);
            }
        }
        return s.toArray(new String[0]);
    }

    public void addMetaFeature(String featureName, MetaFeature.Type metafeatureType, int featureDimension) {
        if (features.containsKey(featureName)) { //TODO: also check that metafeature name is ok!
            Feature f = features.get(featureName);
            if (featureDimension < f.getDimensionality()) {
                LinkedList<MetaFeature> metafeatures = f.metaFeatures.get(featureDimension);
                metafeatures.add(MetaFeature.createForType(metafeatureType, f));
            } else {
                System.out.println("invalid feature dimension " + featureDimension);
            }
        } else {
            System.out.println("Error: no feature with name " + featureName);
        }
    }

    public void removeAllMetaFeatures() {
        for (Feature f : features.values()) {
            f.metaFeatures = new ArrayList<LinkedList<MetaFeature>>(f.getDimensionality());
            for (int i = 0; i < f.getDimensionality(); i++) {
                LinkedList<MetaFeature> l = new LinkedList<MetaFeature>();
                f.metaFeatures.add(l);
            }
        }
    }

    protected HashMap<String, Feature> getBaseFeatures() {
        return features;
    }

    //Doesn't affect features not in the HashMap!
    public void setMetaFeaturesFromMatrix(HashMap<String, ArrayList<LinkedList<MetaFeature>>> list) {
        for (String fname : list.keySet()) {
            ArrayList<LinkedList<MetaFeature>> l = list.get(fname);
            ArrayList<LinkedList<MetaFeature>> m = features.get(fname).metaFeatures;
            if (l != null) {
                if (l.size() == m.size()) {

                    //  m = l;
                    features.get(fname).metaFeatures = l;
                } else {
                    for (int i = 0; (i < m.size() && i < l.size()); i++) {
                        m.set(i, l.get(i));
                    }
                }

            }
        }
    }

    public HashMap<String, ArrayList<LinkedList<MetaFeature>>> getMetaFeatureMatrix() {
        HashMap<String, ArrayList<LinkedList<MetaFeature>>> list = new HashMap<String, ArrayList<LinkedList<MetaFeature>>>();
        for (String fname : features.keySet()) {
            list.put(fname, features.get(fname).metaFeatures);
        }
        return list;
    }

    public String[] getBaseEnabledFeatureNames() {
        String[] s = new String[getNumBaseFeaturesEnabled()];
        int index = 0;

        for (Feature f : featuresInOrder) {
            if (f.enabled) {
                String[] ss = f.getReadableNames();
                for (int i = 0; i < ss.length; i++) {
                    s[index++] = ss[i];
                }
                
            }
        }
        return s;
    }

    public String[] getAllEnabledFeatureNames() {
        if (getNumMetaFeaturesEnabled() == 0) {
            return getBaseEnabledFeatureNames();
        }

        String[] s = new String[getNumFeaturesEnabled()];
        int i = 0;
        for (Feature feat : featuresInOrder) {
            int featNum = 0;

            if (feat.enabled) {
                ArrayList<LinkedList<MetaFeature>> mflists = feat.metaFeatures;
                String[] ss = feat.getReadableNames();
                for (int j = 0; j < feat.getDimensionality(); j++) {
                    
                    s[i++] = ss[j];
                    
                    for (MetaFeature mf : mflists.get(j)) {
                       /// s[i++] = ss[j] + "_" + featNum++; //err: mf is null here!
                        for (int k = 0; k < mf.getSize(); k++) {
                            s[i++] = ss[j] + "_" + mf.getOperationName() + "_" + k;
                        }
                    }

                }
            }
        }
        return s;
    }

    public double[] process(double[] f) {
        if (f.length != committedNumBaseFeatures) {
            System.out.println("Error: wrong num features received. Expected " + committedNumBaseFeatures + ", received " + f.length);
            return new double[0];
        }

        if (committedNumTotalFeatures == committedNumBaseFeatures) {
            return f;
        }

        double[] out = new double[committedNumTotalFeatures];

        int i = 0; //index into output array
        int j = 0; //index into original feature array
        for (Feature feat : featuresInOrder) {
            if (feat.enabled) {
                for (int d = 0; d < feat.getDimensionality(); d++) {
                    out[i++] = f[j];
                    LinkedList<MetaFeature> mflist = feat.metaFeatures.get(d); //this is causing exception for index that should be ok (16 when 19 feats - metafeats not counted?)
                    for (MetaFeature mf : mflist) {
                        double[] mfout = mf.computeForNextFeature(f, j);
                        for (int k = 0; k < mfout.length; k++) {
                            out[i++] = mfout[k];
                        }
                    }

                    j++;
                }
            }
        }
        return out;
    }

    protected class Feature {

        public Feature(String name, int dimensionality) {
            this.name = name;
            setDimensionality(dimensionality);
            this.metaFeatures = new ArrayList<LinkedList<MetaFeature>>(dimensionality);
            for (int i = 0; i < dimensionality; i++) {
                this.metaFeatures.add(new LinkedList<MetaFeature>());
            }
        }

        public void setDimensionality(int dim) {
            this.dimensionality = dim;
            if (metaFeatures == null) {
                metaFeatures = new ArrayList<LinkedList<MetaFeature>>(dimensionality);

            }
            while (metaFeatures.size() < dim) {
                metaFeatures.add(new LinkedList<MetaFeature>());
            }
            while (metaFeatures.size() > dim) {
                metaFeatures.remove(metaFeatures.size() - 1);
            }
        }
        public String name = "feature";
        public boolean enabled = false;
        public ArrayList<LinkedList<MetaFeature>> metaFeatures = null;
        protected int dimensionality = 0;
        public boolean hasFancyName = false;
        public String[] fancyNames = new String[0];

        public String[] getReadableNames() {
            if (hasFancyName) {
                return fancyNames;
            } else {
                String s[] = new String[dimensionality];
                for (int i = 0; i < dimensionality; i++) {
                    s[i] = name + "_" + i;
                }
                return s;
            }
        }

        public int getDimensionality() {
            return dimensionality;
        }

        public void setFancyNames(String[] s) {
            fancyNames = s;
            hasFancyName = (s != null);
        }

        public void writeToOutputStream(ObjectOutputStream o) throws IOException {
            o.writeObject(name);
            o.writeBoolean(enabled);
            o.writeInt(getDimensionality());
            for (int i = 0; i < metaFeatures.size(); i++) {
                LinkedList<MetaFeature> list = metaFeatures.get(i);
                o.writeInt(list.size());
                for (MetaFeature f : list) {
                    o.writeObject(f.getType());
                }
            }
        }
    }

    protected class HidFeature extends Feature {

        public HidFeature(String name, int dimensionality) {
            super(name, dimensionality);
        }

        @Override
        public int getDimensionality() {
            if (hidSetup == null) {
                return 0;
            }

            return hidSetup.getNumFeaturesUsed();
        }
    }



    public void writeToOutputStreamNew(ObjectOutputStream o) throws IOException {
        //each feature
        o.writeObject("FeatureConfiguration version 2: with custom osc name support; mf history length");
        o.writeInt(2);

        o.writeInt(features.size());

        for (Feature f : features.values()) {
          o.writeObject(f.name);
          o.writeBoolean(f.enabled);
          o.writeInt(f.getDimensionality());

            if (f.enabled) {
                for (int i = 0; i < f.metaFeatures.size(); i++) {
                    LinkedList<MetaFeature> list = f.metaFeatures.get(i);
                    o.writeInt(list.size());
                    for (MetaFeature m : list) {
                        o.writeObject(m.getType());
                    }
                }
            }
        }
        o.writeInt(motionSensorExtractionRate);
        o.writeInt(fftWindowSize);
       // o.writeObject(windowType);
        switch (windowType) {
            case HAMMING:
                o.writeInt(1);
                break;
            case HANN:
                o.writeInt(2);
                break;
            case RECTANGULAR:
                o.writeInt(3);
                break;
            default:
                o.writeInt(-1);
        }

        o.writeInt(audioExtractionRate);
       // o.writeObject(processingExtractorType);
        switch (processingExtractorType) {
            case COLOR_6:
                o.writeInt(1);
                break;
            case DOWNSAMPLED_100:
                o.writeInt(2);
                break;
            default:
                o.writeInt(-1);
                break;
        }

        if (hidSetup == null) {
            o.writeInt(0);
        } else {
            o.writeInt(1);
            hidSetup.writeToOutputStream(o);
        }
        
        //Version 1:
        Feature osc = features.get(CUSTOMOSC);
        o.writeBoolean(osc.hasFancyName);
        if (osc.hasFancyName) {
            o.writeObject(osc.fancyNames);
        }

        //version 2:
        o.writeInt(History.n);

     }

    public static FeatureConfiguration readFromInputStream(ObjectInputStream i) throws IOException, ClassNotFoundException {
        FeatureConfiguration fc = new FeatureConfiguration();
        LinkedList<History> historyMfs =new LinkedList<History>();
        int savedVersion = 0;

        try {
            String obj = (String)i.readObject(); //Saved version / description
            savedVersion = i.readInt();

        } catch (Exception ex) {
            //Do nothing: older version
        }


        int numFeats = i.readInt();
        for (int n = 0; n < numFeats; n++) {
            String name = (String)i.readObject();
            boolean enabled = i.readBoolean();
            int dim = i.readInt();
            Feature feat = fc.features.get(name);
            if (enabled) {
                if (feat != null) {
                    feat.enabled = true;
                    feat.setDimensionality(dim); //updates metaFeatures arraylist

                    for (int d = 0; d < dim; d++) {
                        LinkedList<MetaFeature> list = new LinkedList<MetaFeature>();
                        int numMeta = i.readInt();
                        for (int m = 0; m < numMeta; m++) {
                            MetaFeature.Type t = (MetaFeature.Type) i.readObject();

                            MetaFeature mf = MetaFeature.createForType(t, feat);
                            if (t == MetaFeature.Type.HISTORY) {
                                historyMfs.add((History)mf);
                            }
                            list.add(mf);
                        }
                        feat.metaFeatures.set(d, list);
                    }

                } else {
                    throw new IOException("Not sure what to do with feature " + name );
                }
            } else {
                if (feat != null) {
                    feat.enabled = false;
                    feat.setDimensionality(dim);
                }
            }
        }

        int x = i.readInt();
        fc.setMotionSensorExtractionRate(x);
        x = i.readInt();
        fc.setFftWindowSize(x);
       // WindowType type = (WindowType)i.readObject();
        x = i.readInt();
        switch (x) {
            case 1:
                fc.setWindowType(WindowType.HAMMING);
                break;
            case 2:
                fc.setWindowType(WindowType.HANN);
                break;
            case 3:
                fc.setWindowType(WindowType.RECTANGULAR);
                        break;
            default:
                System.out.println("Info: bad window type; using hamming");
                fc.setWindowType(WindowType.HAMMING);

        }

        x = i.readInt();
        fc.setAudioExtractionRate(x);
        x = i.readInt();
        switch (x) {
            case 1:
                fc.setProcessingExtractorType(ProcessingExractorType.COLOR_6);
                break;
            case 2:
                fc.setProcessingExtractorType(ProcessingExractorType.DOWNSAMPLED_100);
                break;
            default:
                System.out.println("Info: bad processing type; using color6");
                fc.setProcessingExtractorType(ProcessingExractorType.COLOR_6);
        }
        x = i.readInt();
        if (x == 0) {
          //  fc.setHidSetup(null);
            //do nothing
        } else {
            HidSetup h = HidSetup.readFromInputStream(i);
            fc.setHidSetup(h);
        }
        fc.committedNumTotalFeatures = fc.getNumFeaturesEnabled();
        fc.committedNumBaseFeatures = fc.getNumBaseFeaturesEnabled();

        //version 1:
        if (savedVersion >= 1) {
         //TODO ABC
            boolean hasFancyOsc = i.readBoolean();
            Feature osc = fc.features.get(CUSTOMOSC);

            if (hasFancyOsc) {
                osc.hasFancyName = true;
               osc.setFancyNames((String[])i.readObject());
            } else {
                osc.hasFancyName = false;
            }
        }

        if (savedVersion >= 2) {
            int n = i.readInt();
            History.n = n; //doesn't work by itslef -- mfs already initialized the wrong way
            for (History mf : historyMfs) {
                mf.updateSize();
            }


        }

        return fc;
    }

    public void setOscCustomFeatureNames(String[] names) {
        if (names == null || names.length != getNumCustomOscFeatures()) {
            throw new IllegalArgumentException("Wrong size of feature names");
        }
        Feature osc = features.get(CUSTOMOSC);
        osc.setFancyNames(names);
        fireFeatureNamesChanged();
    }

    public void addFeatureNamesChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeFeatureNamesChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireFeatureNamesChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -=2 ) {
            if (listeners[i] == ChangeListener.class) {
                if (featureNameChangeEvent == null) {
                    featureNameChangeEvent = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(featureNameChangeEvent);
            }
        }
    }


    private void wekOscFeaturesChanged() {
        syncOscFeatures();
    }
    
    protected void syncOscFeatures() {
     if (WekinatorInstance.getWekinatorInstance().hasCustomOscFeatureNames()) {
            if (isUseCustomOscFeatures() && getNumCustomOscFeatures() == WekinatorInstance.getWekinatorInstance().getCustomOscFeatureNames().length)
            setOscCustomFeatureNames(WekinatorInstance.getWekinatorInstance().getCustomOscFeatureNames());
        }
    }
}
