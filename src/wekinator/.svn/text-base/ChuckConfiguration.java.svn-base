/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import wekinator.util.*;

/**
 *
 * @author rebecca
 */
public class ChuckConfiguration {

    //  private String chuckDirectory = "No directory set";
    private String wekDir = "No directory set";
    private String chuckExecutable = "/usr/bin/chuck";
    private boolean customChuckFeatureExtractorEnabled = false;
    private String customChuckFeatureExtractorFilename = "No file selected";
    private int numCustomChuckFeaturesExtracted = 0;
    private boolean oscFeatureExtractorEnabled = false;
    private int numOSCFeaturesExtracted = 0;
    private int oscFeatureExtractorSendPort = 6453; //Matches defaults in chuck code
    private boolean useChuckSynthClass = true;
    private String chuckSynthFilename = "No file selected";
    private boolean useOscSynth = false;
    // private int numOscSynthParams = 0;
    // private boolean isOscSynthParamDiscrete[] = new boolean[0];
    // private boolean oscUseDistribution[] = new boolean[0];
    protected boolean usable = false;
    public static final String PROP_USABLE = "usable";
    private int oscSynthReceivePort = 12000; //Matches defaults in chuck code
    private int oscSynthSendPort = 6448; //Matches defaults in chuck code
    private boolean isPlayalongLearningEnabled = false;
    private String playalongLearningFile = "No file selected";
    private String locationToSaveMyself = "myConfiguration.chuckconfiguration";
    // private int numOscSynthMaxParamVals = 2;
    protected OscSynthConfiguration oscSynthConfiguration = new OscSynthConfiguration();

    public static String getFileExtension() {
        return "wckconf";
    }

    public static String getFileTypeDescription() {
        return "ChucK configuration";
    }

    public static String getDefaultLocation() {
        // String dir = WekinatorInstance.getWekinatorInstance().getSettings().getDefaultSettingsDirectory();
        return "chuckConfigurations";
    //return dir + File.separator + "hidConfigurations";
    }

    public static ChuckConfiguration readFromInputStream(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ChuckConfiguration c = new ChuckConfiguration();
        int version;
        try {
            version = in.readInt(); //version number
        } catch (Exception ex) {
            version = 1;
        }
        c.setWekDir((String) in.readObject());
        c.setChuckExecutable((String) in.readObject());
        c.setCustomChuckFeatureExtractorEnabled(in.readBoolean());
        c.setCustomChuckFeatureExtractorFilename((String) in.readObject());
        c.setNumCustomChuckFeaturesExtracted(in.readInt());
        c.setOscFeatureExtractorEnabled(in.readBoolean());
        c.setNumOSCFeaturesExtracted(in.readInt());
        c.setOscFeatureExtractorSendPort(in.readInt());
        c.setUseChuckSynthClass(in.readBoolean());
        c.setChuckSynthFilename((String) in.readObject());
        c.setUseOscSynth(in.readBoolean());
        c.getOscSynthConfiguration().setNumParams(in.readInt());
        c.getOscSynthConfiguration().setDiscrete((boolean[]) in.readObject());
        c.getOscSynthConfiguration().setDistribution((boolean[]) in.readObject());

        boolean usable = in.readBoolean();
        c.setOscSynthReceivePort(in.readInt());
        c.setOscSynthSendPort(in.readInt());
        c.setIsPlayalongLearningEnabled(in.readBoolean());
        c.setPlayalongLearningFile((String) in.readObject());
        c.setLocationToSaveMyself((String) in.readObject());
        if (version == 1) {
            int maxVal = in.readInt();
            int[] maxvals = new int[c.getOscSynthConfiguration().numParams];
            String[] oscSynthNames = new String[maxvals.length];
            for (int i = 0; i < maxvals.length; i++) {
                maxvals[i] = maxVal;
                oscSynthNames[i] = "Param" + i;
            }
            c.getOscSynthConfiguration().setMaxVals(maxvals);
            c.getOscSynthConfiguration().setNames(oscSynthNames);
        } else { //version 2
            c.getOscSynthConfiguration().setMaxVals((int[]) in.readObject());
            c.getOscSynthConfiguration().setNames((String[]) in.readObject());
        }
        c.setUsable(usable);
        return c;
    }

    public static ChuckConfiguration readFromFile(File f) throws IOException, ClassNotFoundException {
        FileInputStream fin = null;
        ChuckConfiguration c;

        fin = new FileInputStream(f);
        ObjectInputStream sin = new ObjectInputStream(fin);
        c = ChuckConfiguration.readFromInputStream(sin);

        sin.close();
        fin.close();

        return c;
    }

    public void writeToFile(File f) throws IOException {
        FileOutputStream fout = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fout);
        this.writeToOutputStream(out);
        out.close();
        fout.close();
    }

    protected void writeToOutputStream(ObjectOutputStream out) throws IOException {
        out.writeInt(2); //version number
        out.writeObject(wekDir);
        out.writeObject(chuckExecutable);
        out.writeBoolean(customChuckFeatureExtractorEnabled);
        out.writeObject(customChuckFeatureExtractorFilename);
        out.writeInt(numCustomChuckFeaturesExtracted);
        out.writeBoolean(oscFeatureExtractorEnabled);
        out.writeInt(numOSCFeaturesExtracted);
        out.writeInt(oscFeatureExtractorSendPort);
        out.writeBoolean(useChuckSynthClass);
        out.writeObject(chuckSynthFilename);
        out.writeBoolean(useOscSynth);
        out.writeInt(oscSynthConfiguration.numParams);
        out.writeObject(oscSynthConfiguration.getIsDiscrete());
        out.writeObject(oscSynthConfiguration.getIsDistribution());
        out.writeBoolean(usable);
        out.writeInt(oscSynthReceivePort);
        out.writeInt(oscSynthSendPort);
        out.writeBoolean(isPlayalongLearningEnabled);
        out.writeObject(playalongLearningFile);
        out.writeObject(locationToSaveMyself);
        out.writeObject(oscSynthConfiguration.getMaxValue());
        out.writeObject(oscSynthConfiguration.getParamNames());
    }

    /**
     * Get the value of usable
     *
     * @return the value of usable
     */
    public boolean isUsable() {
        return usable;
    }

    /**
     * Set the value of usable
     *
     * @param usable new value of usable
     */
    private void setUsable(boolean usable) {
        boolean oldUsable = this.usable;
        this.usable = usable;
        propertyChangeSupport.firePropertyChange(PROP_USABLE, oldUsable, usable);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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

    public boolean[] getOscUseDistribution() {
        //return oscUseDistribution;
        return oscSynthConfiguration.isDistribution;
    }

    public void setOscSynthConfiguration(OscSynthConfiguration c) {
        this.oscSynthConfiguration = c;
    }

    public OscSynthConfiguration getOscSynthConfiguration() {
        return oscSynthConfiguration;
    }

    // private boolean oscSynthUseDistribution = false;
    public ChuckConfiguration() {
        // isOscSynthParamDiscrete = new boolean[0];
        try {
            File f = new File(Util.getCanonicalPath(new File("")));
            String preferredPath = f.getParentFile().getParentFile().getParentFile().getAbsolutePath();
            File f2 = new File(preferredPath);
            if (f2.exists() && !f2.getName().equals("project")) {
                wekDir = Util.getCanonicalPath(f2) + File.separator + "project";
            }
            if (File.separator.equals("/")) {
                chuckExecutable = "/usr/bin/chuck";
            } else {
                chuckExecutable = "C:\\windows\\system32\\chuck.exe";
            }

        } catch (Exception ex) {
        }
    }

    /* public void writeToFile(File settingsFile) throws FileNotFoundException, IOException {
    FileOutputStream fout = new FileOutputStream(settingsFile);
    ObjectOutputStream out = new ObjectOutputStream(fout);
    locationToSaveMyself = settingsFile.getCanonicalPath();
    out.writeObject(this);
    out.close();
    fout.close();
    } */
    public ChuckConfiguration(ChuckConfiguration c) {
        setEqualTo(c);
    }

    public void validate() throws Exception {
        String errorString = "";

        File f = new File(wekDir);
        String coreString = wekDir + File.separator + "chuck" + File.separator + "core_chuck" + File.separator; //TODO: make work for windows
        File f2 = new File(coreString);

        // if (!s.equals("wekinator") && !s.equals("project"))
        //File wekDirFile = new File(wekDir);
        if (!f.exists() || !f.isDirectory()) {
            errorString += "Wekinator project directory does not exist or is not a directory\n";
        } else if (!f.exists() || !f.getName().equals("project")) {
            errorString += "Wekinator project directory must refer to a directory called \"project/\"\n";
        } else if (!f2.exists() || !f2.isDirectory()) {
            errorString += "Wekinator project directory must be inside the wekinator directory that you downloaded, containing subdirectories chuck, java, etc.\n";
        }

        //Check for legal chuck executable
        f = new File(chuckExecutable);

        if (!(f.exists() && f.isFile())) {
            errorString += "Chuck executable must be the chuck binary file called \"chuck\".\n";
        } else if (!f.getName().equalsIgnoreCase("chuck") && !f.getName().equalsIgnoreCase("chuck.exe")) {
            errorString += "Chuck executable must be the chuck binary file called \"chuck\" (OS X, linux) or \"chuck.exe\" (windows).\n";
        }

        //Check chuck feature extractor
        if (customChuckFeatureExtractorEnabled) {
            if (!isChuckFile(customChuckFeatureExtractorFilename)) {
                errorString += "Invalid chuck feature extractor class file: Must be .ck file.\n";
            }
            if (numCustomChuckFeaturesExtracted <= 0) {
                errorString += "Number of chuck features must be > 0.\n";
            }
        }

        //Check OSC feature extractor
        if (oscFeatureExtractorEnabled && numOSCFeaturesExtracted <= 0) {
            errorString += "Number of custom OSC features must be > 0 if using OSC feature extractor.\n";
        }

        //Check ChucK synth class
        if (useChuckSynthClass && !isChuckFile(chuckSynthFilename)) {
            errorString += "Invalid chuck synth class file: Must be .ck file.\n";
        }

        if (useOscSynth) {
            if (oscSynthConfiguration == null) {
                errorString += "OSC synth has not been configured\n";
            } else {
                if (!oscSynthConfiguration.validate()) {
                    errorString += oscSynthConfiguration.getValidationReport();
                }
                if (oscSynthSendPort <= 0) {
                    errorString += "OSC synth send port must be > 0.\n";
                }
                if (oscSynthReceivePort <= 0) {
                    errorString += "OSC synth receive port must be > 0 (try 12000).\n";
                }
            }
        }

        //Check playalong learning
        if (isPlayalongLearningEnabled && !isChuckFile(playalongLearningFile)) {
            errorString += "Invalid chuck score player class file: Must be .ck file.\n";
        }

        if (errorString.length() != 0) {
            throw new Exception(errorString);
        }

        setUsable(true);
    }

    private boolean isChuckFile(String filename) {
        File f = new File(filename);
        if (!(f.exists() && f.isFile())) {
            return false;
        }

        String lastPart = f.getName();

        String parts[] = lastPart.split("\\.");
        if (parts.length < 2 || !parts[parts.length - 1].equals("ck")) {
            return false;
        }

        return true;

    }

    public void setEqualTo(ChuckConfiguration c) {
        //chuckDirectory = c.chuckDirectory;
        wekDir = c.wekDir;
        customChuckFeatureExtractorEnabled = c.customChuckFeatureExtractorEnabled;
        customChuckFeatureExtractorFilename = c.customChuckFeatureExtractorFilename;
        numCustomChuckFeaturesExtracted = c.numCustomChuckFeaturesExtracted;
        oscFeatureExtractorEnabled = c.oscFeatureExtractorEnabled;
        numOSCFeaturesExtracted = c.numOSCFeaturesExtracted;
        oscFeatureExtractorSendPort = c.oscFeatureExtractorSendPort;
        useChuckSynthClass = c.useChuckSynthClass;
        chuckSynthFilename = c.chuckSynthFilename;
        useOscSynth = c.useOscSynth;
        oscSynthConfiguration = new OscSynthConfiguration(c.oscSynthConfiguration);


        oscSynthReceivePort = c.oscSynthReceivePort;
        oscSynthSendPort = c.oscSynthSendPort;
        isPlayalongLearningEnabled = c.isPlayalongLearningEnabled;
        playalongLearningFile = c.playalongLearningFile;
        locationToSaveMyself = c.locationToSaveMyself;
        oscSynthConfiguration = new OscSynthConfiguration(c.oscSynthConfiguration);

        setUsable(false);
        //       setUsable(c.usable);
        try {
            validate();
        } catch (Exception ex) {
            //Do nothing
        }
    }

    public String getChuckSynthFilename() {
        return chuckSynthFilename;
    }

    public void setChuckSynthFilename(String chuckSynthFilename) {
        this.chuckSynthFilename = chuckSynthFilename;
        setUsable(false);

    }

    public String getChuckDir() {
        //return chuckDirectory;
        return wekDir + File.separator + "chuck";
    }

    public String getWekDir() {
        return wekDir;
    }

    /*   public void setChuckDir(String chuckDir) {
    this.chuckDirectory = chuckDir;
    setUsable(false);

    } */
    public void setWekDir(String wdir) {
        this.wekDir = wdir;
        setUsable(false);
    }

    public boolean isCustomChuckFeatureExtractorEnabled() {
        return customChuckFeatureExtractorEnabled;
    }

    public void setCustomChuckFeatureExtractorEnabled(boolean customChuckFeatureExtractorEnabled) {
        this.customChuckFeatureExtractorEnabled = customChuckFeatureExtractorEnabled;
        setUsable(false);

    }

    public String getCustomChuckFeatureExtractorFilename() {
        return customChuckFeatureExtractorFilename;
    }

    public void setCustomChuckFeatureExtractorFilename(String customChuckFeatureExtractorFilename) {
        this.customChuckFeatureExtractorFilename = customChuckFeatureExtractorFilename;
        setUsable(false);

    }

    public boolean[] getIsOscSynthParamDiscrete() {
        return oscSynthConfiguration.isDiscrete;
    }

    public boolean isIsPlayalongLearningEnabled() {
        return isPlayalongLearningEnabled;
    }

    public void setIsPlayalongLearningEnabled(boolean isPlayalongLearningEnabled) {
        this.isPlayalongLearningEnabled = isPlayalongLearningEnabled;
        setUsable(false);

    }

    public int getNumCustomChuckFeaturesExtracted() {
        return numCustomChuckFeaturesExtracted;
    }

    public void setNumCustomChuckFeaturesExtracted(int numCustomChuckFeaturesExtracted) {
        this.numCustomChuckFeaturesExtracted = numCustomChuckFeaturesExtracted;
        setUsable(false);

    }

    public int getNumOSCFeaturesExtracted() {
        return numOSCFeaturesExtracted;
    }

    public void setNumOSCFeaturesExtracted(int numOSCFeaturesExtracted) {
        this.numOSCFeaturesExtracted = numOSCFeaturesExtracted;
        setUsable(false);

    }

    public boolean isOscFeatureExtractorEnabled() {
        return oscFeatureExtractorEnabled;
    }

    public void setOscFeatureExtractorEnabled(boolean oscFeatureExtractorEnabled) {
        this.oscFeatureExtractorEnabled = oscFeatureExtractorEnabled;
        setUsable(false);

    }

    public int getOscFeatureExtractorSendPort() {
        return oscFeatureExtractorSendPort;
    }

    public void setOscFeatureExtractorSendPort(int oscFeatureExtractorSendPort) {
        this.oscFeatureExtractorSendPort = oscFeatureExtractorSendPort;
        setUsable(false);

    }

    public int getOscSynthReceivePort() {
        return oscSynthReceivePort;
    }

    public void setOscSynthReceivePort(int oscSynthReceivePort) {
        this.oscSynthReceivePort = oscSynthReceivePort;
        setUsable(false);
    }

    public int getOscSynthSendPort() {
        return oscSynthSendPort;
    }

    public void setOscSynthSendPort(int oscSynthSendPort) {
        this.oscSynthSendPort = oscSynthSendPort;
        setUsable(false);

    }

    public String getPlayalongLearningFile() {
        return playalongLearningFile;
    }

    public void setPlayalongLearningFile(String playalongLearningFile) {
        this.playalongLearningFile = playalongLearningFile;
        setUsable(false);

    }

    public boolean isUseChuckSynthClass() {
        return useChuckSynthClass;
    }

    public void setUseChuckSynthClass(boolean useChuckSynthClass) {
        this.useChuckSynthClass = useChuckSynthClass;
        setUsable(false);

    }

    public boolean isUseOscSynth() {
        return useOscSynth;
    }

    public void setUseOscSynth(boolean useOscSynth) {
        this.useOscSynth = useOscSynth;
        setUsable(false);

    }

    public String getSaveLocation() {
        return locationToSaveMyself;
    }

    public void setSaveLocation(String locationToSaveMyself) {
        this.locationToSaveMyself = locationToSaveMyself;
        setUsable(false);

    }

    public String getChuckExecutable() {
        return chuckExecutable;
    }

    public void setChuckExecutable(String chuckExecutable) {
        this.chuckExecutable = chuckExecutable;
        setUsable(false);

    }

    public String getLocationToSaveMyself() {
        return locationToSaveMyself;
    }

    public void setLocationToSaveMyself(String locationToSaveMyself) {
        this.locationToSaveMyself = locationToSaveMyself;
        setUsable(false);

    }

    /*  public int[] getNumOscSynthMaxParamVals() {
    return oscSynthConfiguration.maxValue;
    }

    public void setNumOscSynthMaxParamVals(int[] numOscSynthMaxParamVals) {
    oscSynthConfiguration.maxValue = new int[numOscSynthMaxParamVals.length];
    for (int i = 0; i < numOscSynthMaxParamVals.length; i++) {
    oscSynthConfiguration.maxValue[i] = numOscSynthMaxParamVals[i];
    }
    setUsable(false);

    } */

    /* public void setOscSynthNames(String[] names) {
    oscSynthConfiguration.paramNames = new String[names.length];
    for (int i = 0; i < names.length; i++) {
    oscSynthConfiguration.paramNames[i] = names[i];
    }
    setUsable(false);

    } */
}
