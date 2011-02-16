/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/* TODO this class:
 *
 *  Fix problem waiting for chuck --loop to initialize VM -- can I use waitFor there?
 *  Send chuck output to stdout at least for now, to help with debugging
 *  Grab chuck error output also and send somewhere
 *  Ultimately display in console.
 *
 * */
package wekinator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: kill listener threads when chuck runner stops!
/**
 *
 * @author rebecca
 */
public class ChuckRunner {

    protected static final ChuckRunner ref = new ChuckRunner();
    protected static ChuckConfiguration configuration;
    protected static String lastErrorMessages = "";
    protected static Logger logger = Logger.getLogger(ChuckRunner.class.getName());
    protected static boolean isWindows = !File.separator.equals("/");

    private static String removeC(String s) {
        //Removes "C:" from beginning of string if on windows
       // s.charAt(1) == ":"
       //if (isWindows && (s.startsWith("C:") || s.startsWith("D:"))) {
        if (isWindows && s.length() > 0 && s.charAt(1)==':')     {
            return s.substring(2);
        } else {
            return s;
        }
    }

    private ChuckRunner() {
      //  isWindows = ;
    }

    public enum ChuckRunnerState {

        NOT_RUNNING,
        TRYING_TO_RUN,
        RUNNING
    }
    protected ChuckRunnerState runnerState = ChuckRunnerState.NOT_RUNNING;
    public static final String PROP_RUNNERSTATE = "runnerState";
    public static final String PROP_CONFIGURATION = "configuration";
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

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

    /**
     * Get the value of runnerState
     *
     * @return the value of runnerState
     */
    public static ChuckRunnerState getRunnerState() {
        return ref.runnerState;
    }

    /**
     * Set the value of runnerState
     *
     * @param runnerState new value of runnerState
     */
    protected static void setRunnerState(ChuckRunnerState runnerState) {
        ChuckRunnerState oldRunnerState = ref.runnerState;
        ref.runnerState = runnerState;
        ref.propertyChangeSupport.firePropertyChange(PROP_RUNNERSTATE, oldRunnerState, ref.runnerState);
    }

    public static String getLastErrorMessages() {
        return lastErrorMessages;
    }

    public static ChuckConfiguration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(ChuckConfiguration c) {
        ChuckConfiguration oldConfiguration = configuration;
        if (ref.runnerState != ChuckRunnerState.NOT_RUNNING) {
            try {
                stop();
            } catch (IOException ex) {
                ChuckRunner.logger.log(Level.SEVERE, null, ex);
            }
        }
        configuration = c;
        ref.propertyChangeSupport.firePropertyChange(PROP_CONFIGURATION, oldConfiguration, configuration);
    }

    public static void run() throws IOException {
        stop();
        lastErrorMessages = "";
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ChuckRunner.logger.log(Level.SEVERE, null, ex);
        }

        LinkedList<String[]> cmds = new LinkedList<String[]>();
        String[] s;
        s = new String[2];
        s[0] = configuration.getChuckExecutable();
        s[1] = "--loop";
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "TrackpadFeatureExtractor.ck";
        s[2] = removeC(s[2]);
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "MotionFeatureExtractor.ck";
        s[2] = removeC(s[2]);
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "AudioFeatureExtractor.ck";
        s[2] = removeC(s[2]);
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "HidDiscoverer.ck";
       s[2] = removeC(s[2]);
       cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "CustomOSCFeatureExtractor.ck";
        s[2] = removeC(s[2]);
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "ProcessingFeatureExtractor.ck";
        s[2] = removeC(s[2]);
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        if (configuration.isCustomChuckFeatureExtractorEnabled()) {
            s[2] = configuration.getCustomChuckFeatureExtractorFilename();

        } else {
            s[2] = configuration.getChuckDir() + File.separator + "feature_extractors" + File.separator + "keyboard_rowcol.ck";
        }
        s[2] = removeC(s[2]);
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        if (configuration.isUseOscSynth()) {
            s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "OSC_synth_proxy.ck";
        } else {
            s[2] = configuration.getChuckSynthFilename();
        }
        s[2] = removeC(s[2]);
        cmds.add(s);

        s = new String[3];
        s[0] = configuration.getChuckExecutable();
        s[1] = "+";
        if (configuration.isIsPlayalongLearningEnabled()) {
            s[2] = configuration.getPlayalongLearningFile();
        } else {
            s[2] = configuration.getChuckDir() + File.separator + "score_players" + File.separator + "no_score.ck";
        }
        s[2] = removeC(s[2]);
        cmds.add(s);

        if (configuration.isUseOscSynth()) {
            //TODOTODOTODO: Get chuck out of this OSC loop!
            s = new String[3];
            s[0] = configuration.getChuckExecutable();
            s[1] = "+";
            String args = "";
            /*
            String args = ":synthNumParams:" + configuration.getOscSynthConfiguration().numParams;

            args += ":synthIsDiscrete:" + (configuration.getOscSynthConfiguration().isDiscrete[0] ? "1" : "0");
            args += ":synthUsingDistribution:" + (configuration.getOscUseDistribution()[0] ? "1" : "0");
            args += ":synthNumClasses:" + configuration.getNumOscSynthMaxParamVals();
            args += ":synthPort:" + configuration.getOscSynthReceivePort(); */
            s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "main_chuck.ck" + args;
            //s[3] = args;
            s[2] = removeC(s[2]);
            cmds.add(s);
        } else {
            s = new String[3];
            s[0] = configuration.getChuckExecutable();
            s[1] = "+";
            s[2] = configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "main_chuck.ck";
            s[2] = removeC(s[2]);
            cmds.add(s);
        }

        //Now we want to execute these commands.
        int numErrLines = 0;

        for (int i = 0; i < cmds.size(); i++) {
            System.out.print("Executing: ");
            String c[] = cmds.get(i);
            for (int j = 0; j < c.length; j++) {
                System.out.print(c[j] + " ");
            }
            System.out.println("");

            try {
                String line, output;
                output = "";
                Process child = Runtime.getRuntime().exec(cmds.get(i));
                //Runtime.getRuntime().exec

                if (i == 0) {
                    //Special! Fork a thread that listens to the output of this process,
                    //and log lines using logger
                    new LoggerThread(child.getErrorStream());
                    new LoggerThread(child.getInputStream());
                }
                if (i != 0) {
                    try {

                        child.waitFor();
                    } catch (InterruptedException ex) {
                        System.out.println("Couldn't wait");
                        logger.log(Level.SEVERE, null, ex);
                    }

                    BufferedReader input = new BufferedReader(new InputStreamReader(child.getErrorStream()));

                    while ((line = input.readLine()) != null) {
                        numErrLines++;
                        output += "In executing command " + cmds.get(i) + " received error:\n";
                        output += (line + '\n');
                        System.out.println("**" + output);
                        lastErrorMessages += line + "\n";
                    }
                    input.close();
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                }

            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }

        if (numErrLines != 0) {
            logger.log(Level.SEVERE, "Errors encountered running chuck: " + lastErrorMessages);
            setRunnerState(ChuckRunnerState.TRYING_TO_RUN);
            if (WekinatorRunner.isLogging()) {
                Plog.log(Plog.Msg.ERROR, "Error running chuck: " + lastErrorMessages);
            }
        } else {
            System.out.println("A miracle! Chuck runs.");
            setRunnerState(ChuckRunnerState.RUNNING);
            if (WekinatorRunner.isLogging()) {
                Plog.chuckRunSuccessful(configuration);
            }
            //TODO: Move elsewhere: If using OSC synth, call its setup now too
            if (configuration.isUseOscSynth()) {
                OscSynthProxy.setup(configuration.getOscSynthReceivePort());
            }
        }
    }

    public static void ignoreRunErrors(boolean ignore) {
        if (ref.runnerState == ChuckRunnerState.TRYING_TO_RUN) {
            if (ignore) {
                setRunnerState(ChuckRunnerState.RUNNING);
            } else {
                try {
                    stop();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void stop() throws IOException {
        String[] s = new String[2];
        s[0] = configuration.getChuckExecutable();
        s[1] = "--kill";
        Process child = Runtime.getRuntime().exec(s);

        String cmd2;
        if (isWindows) {
            cmd2 = "taskkill /f /im chuck";
        } else
        {
            cmd2 =  "killall chuck";
        }
        try {
            Process child2 = Runtime.getRuntime().exec(cmd2);

            logger.log(Level.INFO, "Attempted to kill chuck");
            setRunnerState(ChuckRunnerState.NOT_RUNNING);
            ChuckSystem.getChuckSystem().waitForNewSettings();
        } catch (IOException ex) {

            cmd2 = "tskill /A chuck";
            //cmd2 = "killall chuck";
            Process child2 = Runtime.getRuntime().exec(cmd2);

            logger.log(Level.INFO, "Attempted to kill chuck again");
            setRunnerState(ChuckRunnerState.NOT_RUNNING);
            ChuckSystem.getChuckSystem().waitForNewSettings();

        }

    }

    public void restart() throws IOException {
        stop();
        run();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static void exportConfigurationToChuckFile(ChuckConfiguration configuration, File file) throws IOException {
        //Open output stream
        BufferedWriter w = null;
        w = new BufferedWriter(new FileWriter(file));

        w.write("//Automatically generated machine.add file\n");
        w.write("//Created " + (new Date()).toString() + "\n\n");
        w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "TrackpadFeatureExtractor.ck") + "\");\n");
        w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "MotionFeatureExtractor.ck") + "\");\n");
        w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "AudioFeatureExtractor.ck") + "\");\n");
        w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "HidDiscoverer.ck") + "\");\n");
        w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "CustomOSCFeatureExtractor.ck") + "\");\n");

        w.write("Machine.add(\"" +winescape( configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "ProcessingFeatureExtractor.ck") + "\");\n");
        if (configuration.isCustomChuckFeatureExtractorEnabled()) {
            w.write("Machine.add(\"" + winescape(configuration.getCustomChuckFeatureExtractorFilename()) + "\");\n");

        } else {
            w.write("Machine.add(\"" +winescape( configuration.getChuckDir() + File.separator + "feature_extractors" + File.separator + "keyboard_rowcol.ck") + "\");\n");
        }

        if (configuration.isUseOscSynth()) {
            w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "OSC_synth_proxy.ck") + "\");\n");
        } else {
            w.write("Machine.add(\"" + winescape(configuration.getChuckSynthFilename()) + "\");\n");
        }

        if (configuration.isIsPlayalongLearningEnabled()) {
            w.write("Machine.add(\"" + winescape(configuration.getPlayalongLearningFile()) + "\");\n");
        } else {
            w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "score_players" + File.separator + "no_score.ck") + "\");\n");
        }

        if (configuration.isUseOscSynth()) {
            //TODOTODOTODO check this -- no CHuck involvement in OSC synth
            String args = "";
            /*
            
            String args = ":synthNumParams:" + configuration.getNumOscSynthParams();

            args += ":synthIsDiscrete:" + (configuration.getIsOscSynthParamDiscrete()[0] ? "1" : "0");
            args += ":synthUsingDistribution:" + (configuration.getOscUseDistribution()[0] ? "1" : "0");
            args += ":synthNumClasses:" + configuration.getNumOscSynthMaxParamVals();
            args += ":synthPort:" + configuration.getOscSynthReceivePort(); */
            w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "main_chuck.ck") + args + "\");\n");
        } else {
            w.write("Machine.add(\"" + winescape(configuration.getChuckDir() + File.separator + "core_chuck" + File.separator + "main_chuck.ck") + "\");\n");
        }

        w.close();
    }

    private static String winescape(String s) {
        //Escape backslash in windows
        if (isWindows) {
            String[] parts = s.split("\\\\");
            String r;
            if (parts.length > 0 && parts[0].length() > 1 && parts[0].charAt(1) == ':') {
              //  r = parts[0].charAt(0) + "\\" + ":";
                r = "";
            } else {
                r = parts[0];
            }
            for (int i = 1; i < parts.length; i++) {
                r += "/" + parts[i];
            }
            System.out.println("r is" + r);
            return r;
        } else {
            return s;

        }

    }
}

class LoggerThread implements Runnable {

    Thread t;
    BufferedReader input;

    LoggerThread(InputStream is) {
        input = new BufferedReader(new InputStreamReader(is));
        t = new Thread(this, "my thread");
        t.start();
    }

    public void run() {
        boolean stop = false;
        while (!stop) {
            try {
                ///byte[] byteArray = new byte[2];
                int b = input.read();
                // input.read

                if (b == -1) {
                    stop = true;
                // System.out.println("made it to end of stream");
                } else {
                    //TODO: send to console in reasonable way
                    System.out.print((char) b);
                    //String s = String.
                    Console.getInstance().log(String.valueOf((char) b));
                }
            } catch (IOException ex) {
                Logger.getLogger(LoggerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
