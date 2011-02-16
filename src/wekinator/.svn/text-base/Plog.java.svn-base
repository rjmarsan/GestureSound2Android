/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sun.security.timestamp.TSRequest;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningSystem.EvalStatus;
import wekinator.WekinatorLearningManager.Mode;

/**
 *
 * @author rebecca
 */
public class Plog {

    protected static final Plog ref = new Plog();
    static FileWriter fw = null;
    static PrintWriter p = null;
    static FileWriter rfw = null;
    static PrintWriter r = null;
    static FileWriter ratingw = null;
    static PrintWriter rating = null;
    static int runRound = 0;
    static String logDir = null;
    static String filename = null;
    static String rfilename = null;
    static String ratingFilename = null;
    public static boolean isLogging = true;
    static int lsNum = 0;
    static int fcNum = 0;
    static int chuckConfNum = 0;
    protected static LearningSystem ls = null;
    protected static boolean performanceMode = false;
    protected static long sessionID = 0;
    protected static String lastRunStartTime = "0";

    private Plog() {
    }

    protected static void lmChanged(PropertyChangeEvent evt) {
        //System.out.println("plog got prop change");
        if (evt.getPropertyName().equals(WekinatorLearningManager.PROP_MODE)) {
            WekinatorLearningManager.Mode oldMode = (WekinatorLearningManager.Mode) evt.getOldValue();
            WekinatorLearningManager.Mode newMode = (WekinatorLearningManager.Mode) evt.getNewValue();
            if (newMode == Mode.RUNNING && oldMode != Mode.RUNNING) {
                runStart();
            }

            if (oldMode == Mode.RUNNING && newMode != Mode.RUNNING) {
                log(Msg.RUN_STOP);
            }
            if (newMode == Mode.TRAINING && oldMode != Mode.TRAINING) {
                trainStarted(WekinatorLearningManager.getInstance().getTrainingMask());
            }
            if (oldMode == Mode.TRAINING && newMode != Mode.TRAINING) {
                trainCompleted();
            }

            if (newMode == Mode.DATASET_CREATION && oldMode != Mode.DATASET_CREATION) {
                recordStarted();
            }
            if (oldMode == Mode.DATASET_CREATION && newMode != Mode.DATASET_CREATION) {
                recordStopped();
            }

        /* if (newMode == Mode.EVALUATING && oldMode != Mode.EVALUATING) {
        log(Msg.EVAL_START);
        } else if (oldMode == Mode.EVALUATING && newMode != Mode.EVALUATING) {


        }*/

        }
    }

    public static void log(Msg which) {
        log(which, "");
    }

    public enum Msg {

        LOAD, //done
        NEW_SESSION, //done
        RESET, //done
        CLOSE, //done
        CHUCK_CONFIG_EDIT_BUTTON,
        CHUCK_CONFIG_LOAD_BUTTON,
        CHUCK_CONFIG_LOADED,
        CHUCK_CONFIG_SAVE_BUTTON,
        CHUCK_CONFIG_SAVED,
        CHUCK_CONFIG_CANCEL_BUTTON,
        CHUCK_CONFIG_OK_BUTTON,
        CHUCK_CONFIG_SELECTED,
        CHUCK_RUN_BUTTON,
        CHUCK_STOP_BUTTON,
        CHUCK_RUNNER_STATE_NOT,
        CHUCK_RUNNER_STATE_TRYING,
        CHUCK_RUNNER_STATE_RUNNING,
        CHUCK_RUNNING, //special
        OSC_CONNECT_BUTTON,
        OSC_DISCONNNECT_BUTTON,
        OSC_STATE_NOT_CONNECTED,
        OSC_STATE_CONNECTING,
        OSC_STATE_CONNECTED,
        OSC_STATE_FAIL,
        FEAT_CONF_LOAD_BUTTON,
        FEAT_CONF_SAVE_BUTTON,
        FEAT_CONF_LOADED,
        FEAT_CONF_SAVED,
        FEAT_CONF_SET,
        DATASET_FILE_CHOOSE_BUTTON,
        DATASET_FILE_LOADED,
        DATASET_FILE_ERROR,
        LEARNING_SYSTEM_LOAD_BUTTON,
        LEARNING_SYSTEM_LOADED,
        LEARNING_SYSTEM_SET, //special
        LEARNING_ALGORITHM_SET_WITH_LS,
        SUBPANEL_COLLECT_VIEWED,
        SUBPANEL_TRAIN_VIEWED,
        SUBPANEL_RUN_VIEWED,
        SUBPANEL_CONFIG_VIEWED,
        SCORE_START_BUTTON,
        SCORE_STOP_BUTTON,
        AUDIO_OFF_BUTTON,
        SYNTH_PLAY_THESE_SELECTED,
        SYNTH_SEND_HERE_SELECTED,
        SYNTH_PLAY_ON_DEMAND_SELECTED,
        SYNTH_NOTHING_SELECTED,
        BUTTON_PLAY_HIT, //done: this is synth play button
        PARAM_MASK_EDITED,
        PARAM_VALUES_CHANGED, //TODO: make sure happens w/ playalong too? can differentiate?
        PARAM_CLIPBOARD_ADDED_IN_PANEL, //done
        PARAM_CLIPBOARD_VIEWED, //done
        PARAM_CLIPBOARD_LISTENED,
        PARAM_CLIPBOARD_ADDED_IN_CLIPBOARD,
        PARAM_CLIPBOARD_DELETE,
        PARAM_CLIPBOARD_REARRANGED,
        PARAM_CLIPBOARD_CLOSED,
        BUTTON_PLAYALONG_START, //done
        BUTTON_PLAYALONG_STOP, //done
        BUTTON_RECORD_START,
        BUTTON_RECORD_STOP,
        RECORD_STARTED, //special
        RECORD_STOPPED, //special
        DATASET_CLEARED, //done
        DATA_VIEWER_OPENED, //done
        DATA_VIWER_DELETE_SELECTED, //done
        DATA_VIEWER_ADD_ROW, //done
        DATA_VIEWER_LISTEN, //done
        DATA_VIEWER_SAVE_ARFF_BUTTON, //done
        DATA_VIEWER_DONE, //done
        DATA_VIEWER_DATA_EDITED, //done
        BUTTON_UNTRAIN, //done
        TRAIN_CANCELLED, //special: still log stuff! -- log ls so far & training data
        TRAIN_STARTED, //special
        TRAIN_FINISHED, //special
        BUTTON_RUN_START,
        BUTTON_RUN_STOP, // TODO: really want to be recording these examples... sigh... TODO TODO TODO Can I create another simple-ish dataset??
        RUN_START, //special
        RUN_STOP, //special
        FEATURE_VIEWER_OPENED,
        FEATURE_VIEWER_CLOSED,
        FEATURE_VIEWER_STARTED,
        FEATURE_VIEWER_STOPPED,
        MENU_STUFF, //TODO
        ERROR,
        LEARNER_CHANGED, //special
        EVAL_START_CV, //done
        EVAL_START_TRAIN, //done
        EVAL_END,
        EVAL_CV_RESULTS, //done
        EVAL_TRAIN_RESULTS, //done
        LEARNER_SETTINGS_EDITED, //done
        BUTTON_TRAIN_HIT, //done
        NN_GUI_PREF_SET, //done
        BUTTON_TRAIN_MODEL_SELECT, //done
        BUTTON_EVAL_CANCELLED, //done
        BUTTON_LS_SAVE_HIT, //done
        LEARNER_SETTINGS_NEW_VALUES, //done
        GRAPHICAL_VIEWER_OPENED, //done
        GRAPHICAL_VIEWER_EDIT_MADE, //done
        GRAPHICAL_VIEWER_CLOSED, //done, works on "DONE" button only
        FEATURE_CHOOSER_OPENED, //done
        FEATURE_CHOOSER_CLOSED, //done
        LEARNING_SYSTEM_INFO_FEATURE_MAP, //done
        PANEL_CHUCK_VIEW,
        PANEL_FEATURES_VIEW,
        PANEL_LEARNING_VIEW,
        PANEL_USE_VIEW,
        MENU_SAVE_ARFF, //done
        DATASET_LOADED_FROM_FILE, //done
        SYNTH_INFO, //done for osc synth, need more info for chuck synth
        LEARNING_ALGORITHM_SAVED, //done
        PARAM_CHECKBOX_CHANGED, //done
        TRAINING_ERROR,
        RATING_BUTTON_HIT
    };

    public static void setWekinatorLearningManager(WekinatorLearningManager lm) {
        lm.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                lmChanged(evt);
            }
        });
    }
    private static boolean isSetup = false;

    private static void wekInstPropChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorInstance.PROP_FEATURECONFIGURATION)) {
            featConfigSet((FeatureConfiguration) evt.getNewValue());
        } else if (evt.getPropertyName().equals(WekinatorInstance.PROP_LEARNINGSYSTEM)) {
            learningSystemSet((LearningSystem) evt.getNewValue());
            logFeatureMaps();
        }
    }

    public static void setup(String parentDir) throws IOException {
        if (!isSetup) {
            WekinatorInstance.getWekinatorInstance().addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    wekInstPropChanged(evt);
                }
            });
            isSetup = true;
        }
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        sessionID = Long.parseLong(dateFormat.format(d));
        filename = parentDir + File.separator + "log.txt";
        rfilename = parentDir + File.separator + "run.txt";
        ratingFilename = parentDir + File.separator + "rating.txt";
        logDir = parentDir + File.separator;
        fw = new FileWriter(filename, true);
        p = new PrintWriter(fw);
        rfw = new FileWriter(rfilename, true);
        r = new PrintWriter(rfw);
        ratingw = new FileWriter(ratingFilename, true);
        rating = new PrintWriter(ratingw);
        log(Msg.NEW_SESSION, "" + sessionID);
        resetRun();

    }

    public static void startPlog() {
        try {
            p.close();
            fw.close();
            fw = new FileWriter(filename, false);
            p = new PrintWriter(fw);

            r.close();
            rfw.close();
            rfw = new FileWriter(rfilename, false);
            r = new PrintWriter(rfw);

            rating.close();
            ratingw.close();
            ratingw = new FileWriter(ratingFilename, false);
            rating = new PrintWriter(ratingw);

            log(Msg.RESET);
            resetRun();
        } catch (Exception ex) {
            System.out.println("problem starting log.");
        }
    }

    private static void resetRun() {
        r.println(ts() + ",#RESET#");
        runRound = 0;
    }

    private static void resetRating() {
        rating.println(ts() + ",#RESET#");
    }

    public static void close() throws IOException {
        p.flush();
        p.close();
        fw.close();

        r.flush();
        r.close();
        rfw.close();
        
        rating.flush();
        rating.close();
        ratingw.close();
    }

    public static void rate(int r) {
        rating.println(ts() + "," + lastRunStartTime + "," + r);
    }

    protected static String ts() {
        return Long.toString((new Date()).getTime());
    }

   public static void log(String ts, Msg which, String m) {
        if (p == null) {
            System.out.println("ERROR: logging with null log!");
            return;
        }
        p.println(ts + "," + which.ordinal() + "," + which + "," + m);
   }

    public static void log(Msg which, String m) {
        if (p == null) {
            System.out.println("ERROR: logging with null log!");
            return;
        }
        p.println(ts() + "," + which.ordinal() + "," + which + "," + m);
    }

    public static void featConfigSet(FeatureConfiguration f) {
        //log feat config properties as well as save to file
        fcNum++;
        String s = "" + fcNum + "," + "NULL=" + (f == null);
        if (f != null) {
            s += ",MOTION=" + f.isUseMotionSensor() + ", COLOR=" + f.isUseProcessing() + ", HID=" + f.isUseOtherHid();
        }

        log(Msg.FEAT_CONF_SET, s);
        if (f != null) {
            saveFeatureConfiguration(fcNum, f);
        }
    }

    public static void learningSystemSet(LearningSystem ls) {
        //log feat config properties as well as save to file
        lsNum++;
        String s;
        if (ls != null) {
            s = "nParams=" + ls.getNumParams() + ",LEARNERS=(";
            LearningAlgorithm[] algs = ls.getLearners();
            for (int i = 0; i < algs.length; i++) {
                if (algs[i] != null) {
                    s += algs[i].getName() + ",";
                } else {
                    s += "NULL,";
                }
            }
            s += ")";
        } else {
            s = "LS=NULL";
        }
        log(Msg.LEARNING_SYSTEM_SET, "" + lsNum + "," + s);


        if (ls != null) {
            saveLearningSystem(lsNum, ls);
        }

        updateLearningSystemListeners(ls);
    }
    private static ChangeListener learnerChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            learnerChanged(e);
        }
    };
    private static PropertyChangeListener lsChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            lsPropertyChanged(evt);
        }
    };

    private static void lsPropertyChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(LearningSystem.PROP_ISEVALUATING)) {
            if (!ls.isEvaluating) {
                EvalStatus es = ls.getEvalStatus();
                String s;
                double[] results;
                if (es.isCV) {
                    results = ls.getCvResults();
                    //updateResults(results, true);
                    s = "CV_RESULTS=(";


                } else {
                    results = ls.getTrainResults();
                    s = "TRAIN_RESULTS=(";
                }
                for (int i = 0; i < results.length; i++) {
                    s += results[i] + ",";
                }
                s += ")";

                if (es.isCV) {
                    log(Msg.EVAL_CV_RESULTS, s);
                } else {
                    log(Msg.EVAL_TRAIN_RESULTS, s);
                }
            }
        }
    }

    private static void updateLearningSystemListeners(LearningSystem newls) {
        if (ls != null) {
            ls.removeLearnerChangeListener(learnerChangeListener);
            ls.removePropertyChangeListener(lsChangeListener);
        }

        ls = newls;
        if (ls != null) {
            ls.addLearnerChangeListener(learnerChangeListener);
            ls.addPropertyChangeListener(lsChangeListener);
        }
    }

    private static void learnerChanged(ChangeEvent e) {
        lsNum++;
        String s;
        if (ls != null) {
            s = lsNum + ",nParams=" + ls.getNumParams() + ",";
            LearningAlgorithm[] algs = ls.getLearners();
            for (int i = 0; i < algs.length; i++) {
                if (algs[i] != null) {
                    s += algs[i].getName();
                } else {
                    s += "NULL,";
                }
            }
        } else {
            s = "LS=NULL";
        }
        log(Msg.LEARNER_CHANGED, s);
        if (ls != null) {
            saveLearningSystem(lsNum, ls);
        }
    }

    public static void lsSet(LearningSystem l) {
        //log ls properties, including each algorithm and which feature used; save to file
    }

    public static void datasetLoaded(SimpleDataset d) {
        //log dataset properties (# instances), save to file
    }

    public void learningAlgorithmSet(LearningAlgorithm la, int paramNum) {
    }

    public static void recordStarted() {
        int currentNum = 0;
        if (WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null) {
            currentNum = WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset().getNumDatapoints();
        }
        log(Msg.RECORD_STARTED, "nInst=" + currentNum);
    }

    public static void recordStopped() {
        int currentNum = 0;
        if (WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null) {
            currentNum = WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset().getNumDatapoints();
        }
        log(Msg.RECORD_STOPPED, "nInst=" + currentNum);
    }

    public static void trainStarted(boolean[] trainMask) {
        String s = "MASK=(";
        for (int i = 0; i < trainMask.length; i++) {
            s += trainMask[i] + ",";
        }
        s += ")";
        log(Msg.TRAIN_STARTED, s);
    }

    public static void trainCompleted() {
        //save ls and dataset so I can compute train error later and eval against other learners
        lsNum++;
        log(Msg.TRAIN_FINISHED, "" + lsNum);
        saveLearningSystem(lsNum, WekinatorInstance.getWekinatorInstance().getLearningSystem());
    }

    public static void trainCancelled() {
        //save ls and dataset so I can compute train error later and eval against other learners
        lsNum++;
        log(Msg.TRAIN_CANCELLED, "" + lsNum);
        saveLearningSystem(lsNum, WekinatorInstance.getWekinatorInstance().getLearningSystem());

    }

    public static void chuckRunSuccessful(ChuckConfiguration c) {
        chuckConfNum++;
        String s = "FILENUM=" + chuckConfNum + ",";
        boolean isChuckSynth = c.isUseChuckSynthClass();
        if (isChuckSynth) {
            s += "chuckSynth=" + c.getChuckSynthFilename() + ",";
        } else {
            s += "oscSynth,numFeats=" + c.getNumOSCFeaturesExtracted() + ",isDiscrete=" + c.getIsOscSynthParamDiscrete()[0];
        }
        log(Msg.CHUCK_RUNNING, s);
        saveChuckConfiguration(chuckConfNum, c);
        //Also save some info on synth
        logSynthInfo(c);
    }

    public static void logSynthInfo(ChuckConfiguration c) {
        boolean isChuck = c.isUseChuckSynthClass();
        String s;
        if (!isChuck) {
            s = "isChuck=" + (c.isUseChuckSynthClass() ? "1" : "0") + ",";
            s += "nParams=" + SynthProxy.getNumParams() + ",discrete={";
            for (int i = 0; i < SynthProxy.getNumParams(); i++) {
                if (SynthProxy.isParamDiscrete(i)) {
                    s += "1,";
                } else {
                    s += "0,";
                }
            }
            s += "},distributions={";
            for (int i = 0; i < SynthProxy.getNumParams(); i++) {
                if (SynthProxy.isParamDiscrete(i)) {
                    s += SynthProxy.isParamDistribution(i) + ",";
                } else {
                    s += "-1,";
                }
            }
            s += "},maxLegalValue={";
            for (int i = 0; i < SynthProxy.getNumParams(); i++) {
                if (SynthProxy.isParamDiscrete(i)) {
                    s += SynthProxy.paramMaxValue(i) + ",";
                } else {
                    s += "-1,";
                }
            }
            s += "}";
        } else {
            s = "isChuck=1,filename=" + c.getChuckSynthFilename();
            
        }
        log(Msg.SYNTH_INFO, s);
    }

    public static void logFeatureMap(int learner) {
        if (ls != null && ls.getDataset() != null) {
            FeatureLearnerConfiguration flc = ls.getDataset().getFeatureLearnerConfiguration();
            if (flc != null) {
                // for (int i = 0; i < flc.numLearners; i++) {
                int[] mapping = flc.getFeatureMappingForLearner(learner);
                String s = "{";
                for (int j = 0; j < mapping.length; j++) {
                    s += mapping[j] + ",";
                }
                s += "}";
                log(Msg.LEARNING_SYSTEM_INFO_FEATURE_MAP, "param=" + learner + ",mapping=" + s);
            // }
            }
        } else {
            log(Msg.LEARNING_SYSTEM_INFO_FEATURE_MAP, "Null configuration");
        }
    }

    public static void logFeatureMaps() {

        if (ls != null && ls.getDataset() != null) {
            FeatureLearnerConfiguration flc = ls.getDataset().getFeatureLearnerConfiguration();
            if (flc != null) {
                for (int i = 0; i < flc.numLearners; i++) {
                    int[] mapping = flc.getFeatureMappingForLearner(i);
                    String s = "{";
                    for (int j = 0; j < mapping.length; j++) {
                        s += mapping[j] + ",";
                    }
                    s += "}";
                    log(Msg.LEARNING_SYSTEM_INFO_FEATURE_MAP, "param=" + i + ",mapping=" + s);
                }
            }
        } else {
            log(Msg.LEARNING_SYSTEM_INFO_FEATURE_MAP, "Null configuration");
        }
    }

    protected static void saveLearningSystem(int num, LearningSystem ls) {
        File f = new File(logDir + sessionID + "ls" + num + "." + LearningSystem.getFileExtension());
        try {
            ls.writeToFile(f);
        /* if (ls != null && ls.getDataset() != null) {
        FeatureLearnerConfiguration flc = ls.getDataset().getFeatureLearnerConfiguration();
        if (flc != null) {
        for (int i = 0; i < flc.numLearners; i++) {
        int[] mapping = flc.getFeatureMappingForLearner(i);
        String s = "{";
        for (int j = 0; j < mapping.length; j++) {
        s += mapping[j] + ",";
        }
        s += "}";
        log(Msg.LEARNING_SYSTEM_INFO_FEATURE_MAP, "param=" + i + ",mapping=" + s);
        }
        }
        } else {
        log(Msg.LEARNING_SYSTEM_INFO_FEATURE_MAP, "Null configuration");
        } */
        } catch (IOException ex) {
            log(Msg.ERROR, "Couldn't write ls number " + num + " to file: " + ex.getMessage());
        }
    }

    public static void saveChuckConfiguration(int num, ChuckConfiguration c) {
        File f = new File(logDir + sessionID + "ck" + num + "." + ChuckConfiguration.getFileExtension());
        try {
            c.writeToFile(f);
        } catch (Exception ex) {
            log(Msg.ERROR, "Couldn't write ck number " + num + " to file: " + ex.getMessage());

        }
    }

    public static void saveFeatureConfiguration(int num, FeatureConfiguration fc) {
        File f = new File(logDir + sessionID + "fc" + num + "." + FeatureConfiguration.getFileExtension());
        try {
            fc.writeToFile(f);
        } catch (Exception ex) {
            log(Msg.ERROR, "Couldn't write fc number " + num + " to file: " + ex.getMessage());

        }
    }

    public static void paramClipboardUsed() {
        //log contents of clipboard
    }

    public static void beginRecording(boolean[] paramMask) {
        //log when, mask
    }

    public static void stopRecording(SimpleDataset d) {
        //log # examples now
    }

    protected void popupError(String err) {
        // JOptionPane er = new JOptionPane("Error with logging!" + err, JOptionPane.ERROR_MESSAGE);
        // er.sho
        JOptionPane.showMessageDialog(null, err, "Error with logging!", JOptionPane.ERROR_MESSAGE);
    }

    public static void runStart() {
        runRound++;
        lastRunStartTime = ts();
        log(lastRunStartTime, Msg.RUN_START, runRound + "," + lsNum);
    }

    public static void flush() {
        p.flush();
        r.flush();
        rating.flush();
    }

    public static void runStep(double[] features, double[] params) {
        if (!performanceMode) {

            //Really want to log this in a file; 1 file per run round.
            r.print(ts() + "," + runRound + "," + features.length + "," + params.length);
            for (int j = 0; j < features.length; j++) {
                r.print("," + features[j]);
            }
            for (int j = 0; j < params.length; j++) {
                r.print("," + params[j]);
            }
            r.print("\n");
        }
    }

    public static void evalStart(int paramNum, boolean isCV, int numFolds) {
    }

    public static void evalFinish(int paramNum, boolean isCV, int numFolds, int results) { //need better results rep and indication of whether canceled
    }
}
