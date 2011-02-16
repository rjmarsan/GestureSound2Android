/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

/**
 *
 * @author rebecca
 */
public class OscController {

    private static OscController ref = new OscController();
    private static final String OSCrecordMessage = "/control/OSCrecord";
    private static final String OSCplayscoreMessage = "/control/OSCplayscore";
    private static final String OSCtrainMessage = "/control/OSCtrain";
    private static final String OSCrunMessage = "/control/OSCstartrun";
    protected boolean oscControllable = true;
    public static final String PROP_OSCCONTROLLABLE = "oscControllable";

    private OscController() {
    }

    public static void addListeners(OSCPortIn receiver) {
        // return listeners;
        addRecordListener(receiver);
        addPlayscoreListener(receiver);
        addTrainListener(receiver);
        addRunListener(receiver);
    }

    private static void addRecordListener(OSCPortIn receiver) {
        OSCListener l = new OSCListener() {

            public void acceptMessage(Date arg0, OSCMessage message) {
                Object[] o = message.getArguments();
                if (o.length > 0 && (o[0] instanceof java.lang.Integer)) {
                    controlRecordReceived(((Integer) o[0]) == 1);
                }
            }
        };
        receiver.addListener(OSCrecordMessage, l);
    }

    private static void addPlayscoreListener(OSCPortIn receiver) {
        OSCListener l = new OSCListener() {

            public void acceptMessage(Date arg0, OSCMessage message) {
                Object[] o = message.getArguments();
                if (o.length > 0 && (o[0] instanceof java.lang.Integer)) {
                    scoreMessageReceived(((Integer) o[0]) == 1);
                }
            }
        };
        receiver.addListener(OSCplayscoreMessage, l);
    }

    private static void addTrainListener(OSCPortIn receiver) {
        OSCListener l = new OSCListener() {
            public void acceptMessage(Date arg0, OSCMessage message) {
                Object[] o = message.getArguments();
                if (o.length > 0 && (o[0] instanceof java.lang.Integer)) {
                    trainMessageReceived(((Integer) o[0]) == 1);
                }
            }
        };
        receiver.addListener(OSCtrainMessage, l);
    }

    private static void addRunListener(OSCPortIn receiver) {
        OSCListener l = new OSCListener() {
            public void acceptMessage(Date arg0, OSCMessage message) {
                Object[] o = message.getArguments();
                if (o.length > 0 && (o[0] instanceof java.lang.Integer)) {
                    runMessageReceived(((Integer) o[0]) == 1);
                }
            }
        };
        receiver.addListener(OSCrunMessage, l);
    }

    /**
     * Get the value of recordControllable
     *
     * @return the value of recordControllable
     */
    public static boolean isOscControllable() {
        return ref.oscControllable;
    }

    /**
     * Set the value of recordControllable
     *
     * @param recordControllable new value of recordControllable
     */
    public static void setOscControllable(boolean oscControllable) {
        boolean oldRecordControllable = ref.oscControllable;
        ref.oscControllable = oscControllable;
        ref.propertyChangeSupport.firePropertyChange(PROP_OSCCONTROLLABLE, oldRecordControllable, oscControllable);
    }
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

    private static void controlRecordReceived(boolean start) {
        if (isOscControllable()) {
            if (start) {
                try {
                    WekinatorLearningManager.getInstance().startDatasetCreation();
                } catch (Exception ex) {
                    System.out.println("log this osc: can't start dataset creation (info)");
                }
            } else {
                WekinatorLearningManager.getInstance().stopDatasetCreation();
            }
        } else {
            System.out.println("not controllable");
        }
    }

    private static void scoreMessageReceived(boolean start) {
        //Hack: move to  playalong proxy or something
        if (isOscControllable()) {
            if (start) {
                OscHandler.getOscHandler().playScore();
                OscHandler.getOscHandler().startGettingParams();
            } else {
                OscHandler.getOscHandler().stopPlayback();
                OscHandler.getOscHandler().stopGettingParams();
            }
        }
    }

    private static void trainMessageReceived(boolean b) {
        if (isOscControllable()) {
            WekinatorLearningManager lm = WekinatorLearningManager.getInstance();
            LearningSystem ls = WekinatorInstance.getWekinatorInstance().getLearningSystem();
            if (lm != null && lm.getMode() == WekinatorLearningManager.Mode.NONE && ls != null && ls.isIsTrainable()) {
                WekinatorLearningManager.getInstance().startTraining();
            } else {
                System.out.println("Received train message but cannot train at this time");
            }
        }
    }

    private static void runMessageReceived(boolean start) {
        if (isOscControllable()) {
            LearningSystem ls = WekinatorInstance.getWekinatorInstance().getLearningSystem();
            WekinatorLearningManager lm = WekinatorLearningManager.getInstance();
            if (start) {
                if (ls != null && ls.isIsRunnable() && lm.mode == WekinatorLearningManager.Mode.NONE) {
                    WekinatorLearningManager.getInstance().startRunning();
                    OscHandler.getOscHandler().startSound();
                }
            } else {
                if (lm.mode == WekinatorLearningManager.Mode.RUNNING) {
                    WekinatorLearningManager.getInstance().stopRunning();
                    OscHandler.getOscHandler().stopSound();
                }
            }
        }
    }
}
