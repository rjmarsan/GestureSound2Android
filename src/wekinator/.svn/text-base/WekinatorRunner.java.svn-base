/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import wekinator.util.Util;

/**
 *
 * @author rebecca
 */
public class WekinatorRunner {

    protected static File featureFile = null;
    protected static File lsFile = null;
    protected static File chuckFile = null;
    protected static OptionParser parser = null;
    protected static boolean runAutomatically = false;
    protected static boolean connectAutomatically = false;
    protected static boolean minimizeOnRun = false;
    protected static OptionSpec<String> feat;
    protected static OptionSpec<String> ls;
    protected static OptionSpec<String> ck;
    protected static OptionSpec<Void> run;
    protected static OptionSpec<Void> connect;
    protected static OptionSpec<Void> min;
    protected static OptionSpec<Void> isp;
    protected static boolean isLogging = false; //Can change default here
    protected static boolean isPlork = false;
    protected static boolean isKbow = false;
    private static final WekinatorRunner ref = new WekinatorRunner();

    public static boolean isLogging() {
        return isLogging;
    }

    public static boolean isPlork() {
        return isPlork;
    }

    public static boolean isKbow() {
        return isKbow;
    }

    public static boolean isMinimizeOnRun() {
        return minimizeOnRun;
    }

    /**
     * Get the value of connectAutomatically
     *
     * @return the value of connectAutomatically
     */
    public boolean isConnectAutomatically() {
        return connectAutomatically;
    }

    /**
     * Get the value of runAutomatically
     *
     * @return the value of runAutomatically
     */
    public boolean isRunAutomatically() {
        return runAutomatically;
    }

    private WekinatorRunner() {
        setupParser();
    }

    /**
     * Get the value of featureLoadFilename
     *
     * @return the value of featureLoadFilename
     */
    public static File getFeatureFile() {
        return featureFile;
    }

    /**
     * Get the value of learningSystemLoadFilename
     *
     * @return the value of learningSystemLoadFilename
     */
    public static File getLearningSystemFile() {
        return lsFile;
    }

    /**
     * Get the value of chuckConfigLoadFilename
     *
     * @return the value of chuckConfigLoadFilename
     */
    public static File getChuckConfigFile() {
        return chuckFile;
    }

    protected void setupParser() {
        parser = new OptionParser();
        parser.accepts("help", "prints this help message");
        feat = parser.accepts("feat", "followed by feature configuration file").withRequiredArg().ofType(String.class);
        ls = parser.accepts("ls", "followed by learning system file").withRequiredArg().ofType(String.class);
        ck = parser.accepts("ck", "followed by chuck configuration file (only if not running chuck separately)").withRequiredArg().ofType(String.class);
        run = parser.accepts("run", "start running on load (no argument necessary)"); //run automatically
        connect = parser.accepts("connect", "start OSC connection on load (used only when running chuck separately");
        min = parser.accepts("min", "minimize after running (no argument; used only with --run)");
        isp = parser.accepts("p", "plork student special build with logging (no argument necessary)");
    }

    private static File getFeatureFile(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            return f;
        } else {
            String path = hackyPath() + File.separator + "featureConfigurations";
            f = new File(path, filename);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    private static File getLsFile(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            return f;
        } else {
            String path = hackyPath() + File.separator + "learningSystems";
            f = new File(path, filename);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    private static File getChuckFile(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            return f;
        } else {
            String path = hackyPath() + File.separator + "chuckConfigurations";
            f = new File(path, filename);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }



    private static String hackyPath() {
        return ".." + File.separator + ".." + File.separator + "mySavedSettings";
    }

    public static void main(String[] args) {
              System.setProperty("apple.laf.useScreenMenuBar", "true");

        if (args == null) {
            return;
        }

        OptionSet options;
        try {
            options = WekinatorRunner.parser.parse(args);
            System.out.println("parsed successfully");

            if(options.has("help")) {
                try {
                    System.out.println("Usage: ");
                    WekinatorRunner.parser.printHelpOn(System.out);
                } catch (IOException ex) {
                    Logger.getLogger(WekinatorRunner.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }

            if (options.has(feat)) {
                String s = options.valueOf(feat);
                File f = getFeatureFile(s);
                if (f == null) {
                    System.out.println("Error: feature configuration file " + s + " is not valid");
                    return;
                }
                featureFile = f;
                System.out.println("Loading feature configuration from file " + Util.getCanonicalPath(f));
            }

            if (options.has(ls)) {
                String s = options.valueOf(ls);
                File f = getLsFile(s);
                if (f == null) {
                    System.out.println("Error: learning system file " + s + " is not valid");
                    return;
                }
                lsFile = f;
                System.out.println("Loading learning system from file " + Util.getCanonicalPath(f));
            }

            if (options.has(ck)) {
                String s = options.valueOf(ck);
                File f = getChuckFile(s);
                if (f == null) {
                    System.out.println("Error: chuck configuration file " + s + " is not valid");
                    return;
                }
                chuckFile = f;
                System.out.println("Loading chuck configuration from file " + Util.getCanonicalPath(f));
            }

            if (options.has(feat) && options.has(ls) && options.has(run)) {
                runAutomatically = true;
                System.out.println("Automatically running");

                if (options.has(min)) {
                    minimizeOnRun = true;
                    System.out.println("Minimizing on run");
                }

            } else if (options.has(run)) {
                System.out.println("Warning: Will not automatically run: no feature configuration and/or learning system files have been specified");
            }

            if (options.has(connect)) {
                connectAutomatically = true;
                System.out.println("Automatically connecting");
            }

            if (options.has(isp)) {
                isPlork = true;
                isLogging = true;
                System.out.println("Running plork student special build");
            } 

            //Now run!

            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainGUI b = new MainGUI();
                b.setVisible(true);
            }
        });

        } catch (OptionException ex) {
            System.out.println("Invalid options supplied to Wekinator");
            try {
                WekinatorRunner.parser.printHelpOn(System.out);
            } catch (IOException ex1) {
                Logger.getLogger(WekinatorRunner.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }
}
