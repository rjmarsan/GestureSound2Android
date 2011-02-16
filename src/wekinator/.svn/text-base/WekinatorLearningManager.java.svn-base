/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Controls wekinator instance (state)
 *
 * @author rebecca
 */
public class WekinatorLearningManager {
    protected PropertyChangeListener learningSystemPropertyChange = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            learningSystemPropertyChanged(evt);
        }
    };
    private ChangeEvent changeEvent = null;
    private static WekinatorLearningManager ref = null;
    protected double[] params;
    protected boolean[] mask;
    //   protected LearningSystem learningSystem = null;
    // public static final String PROP_LEARNINGSYSTEM = "learningSystem";
    public static final String PROP_PARAMS = "params";
    protected double[] outputs = null;
    protected EventListenerList listenerList = new EventListenerList();
    protected boolean[] trainingMask = null;
    public static final String PROP_TRAININGMASK = "trainingMask";

    /**
     * Get the value of trainingMask
     *
     * @return the value of trainingMask
     */
    public boolean[] getTrainingMask() {
        return trainingMask;
    }

    /**
     * Set the value of trainingMask
     *
     * @param trainingMask new value of trainingMask
     */
    public void setTrainingMask(boolean[] trainingMask) {
        boolean[] oldTrainingMask = this.trainingMask;
        this.trainingMask = trainingMask;
        propertyChangeSupport.firePropertyChange(PROP_TRAININGMASK, oldTrainingMask, trainingMask);
    }

    public void addOutputChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeOutputChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Get the value of outputs
     *
     * @return the value of outputs
     */
    public double[] getOutputs() {
        return outputs;
    }

    /**
     * Set the value of outputs
     *
     * @param outputs new value of outputs
     */
    public void setOutputs(double[] outputs) {
        this.outputs = outputs;
        fireOutputChanged();
    }

    /**
     * Get the value of outputs at specified index
     *
     * @param index
     * @return the value of outputs at specified index
     */
    public double getOutputs(int index) {
        return this.outputs[index];
    }

    /**
     * Set the value of outputs at specified index.
     *
     * @param index
     * @param newOutputs new value of outputs at specified index
     */
    public void setOutputs(int index, double newOutputs) {
        this.outputs[index] = newOutputs;
        fireOutputChanged();
    }

    /**
     * Set the value of p2
     *
     * @param p2 new value of p2
     */
    public void setParams(double[] params) {
        double[] oldparams = this.params; //Issue: When old & new are same reference, this breaks! Everywhere :(
        this.params = params;
        propertyChangeSupport.firePropertyChange(PROP_PARAMS, oldparams, params);
    }

    public void computeCVAccuracyInBackground(int numFolds) {
        computeCVAccuracyInBackground(-1, numFolds);
    }

    public void computeCVAccuracyInBackground(int myParam, int numFolds)  {
        setMode(Mode.EVALUATING);
        WekinatorInstance.getWekinatorInstance().getLearningSystem().computeCVAccuracyInBackground(myParam, numFolds);
    }

    void computeTrainingAccuracyInBackground() {
        computeTrainingAccuracyInBackground(-1);
    }

    void computeTrainingAccuracyInBackground(int myParam) {
        setMode(Mode.EVALUATING);
        WekinatorInstance.getWekinatorInstance().getLearningSystem().computeTrainingAccuracyInBackground(myParam);
    }

    void stopEvaluating() {
       WekinatorInstance.getWekinatorInstance().getLearningSystem().stopEvaluating();

    }


    /*    public enum InitializationState {
    NOT_INITIALIZED,
    INITIALIZED
    };
    protected InitializationState initState = InitializationState.NOT_INITIALIZED;
    public static final String PROP_INITSTATE = "initState"; */
    public enum Mode {

        DATASET_CREATION,
        TRAINING,
        RUNNING,
        EVALUATING,
        NONE
    };
    protected Mode mode = Mode.NONE;
    public static final String PROP_MODE = "mode";
    //  protected double[] outputs = null;
    //  public static final String PROP_OUTPUTS = "outputs";

    /**
     * Get the value of params
     *
     * @return the value of params
     */
    public double[] getParams() {
        return params;
    }

    public void setParamsAndMask(double[] params, boolean[] mask) {
        setParams(params); //this screws it up: everyone's listening!
        //Need to check if null?
        WekinatorInstance.getWekinatorInstance().getLearningSystem().setParamMask(mask);
        this.mask = mask;
    }

    /**
     * Get the value of params at specified index
     *
     * @param index
     * @return the value of params at specified index
     */
    public double getParams(int index) {
        return this.params[index];
    }

    /**
     * Get the value of mode
     *
     * @return the value of mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Set the value of mode
     *
     * @param mode new value of mode
     */
    protected void setMode(Mode mode) {
        Mode oldMode = this.mode;
        this.mode = mode;
        propertyChangeSupport.firePropertyChange(PROP_MODE, oldMode, mode);
    }

    public void startDatasetCreation() throws Exception {
        LearningSystem ls = WekinatorInstance.getWekinatorInstance().getLearningSystem();
       /* if (ls == null || ls.getInitializationState() != LearningSystem.LearningAlgorithmsInitializationState.ALL_INITIALIZED) {
            return;
        } */
        if (ls == null) {
            throw new Exception("Learning system not initialized");
        }

        //if (initState == InitializationState.INITIALIZED) {
        if (mode == Mode.DATASET_CREATION) {
            return;
        }

        if (mode == Mode.TRAINING) {
            stopTraining();
        }
        //if mode = running, don't stop extracting features, just change what I do with them

        if (params == null) {
            params = new double[WekinatorInstance.getWekinatorInstance().getLearningSystem().getNumParams()];
        }

        FeatureExtractionController.startExtracting();
        setMode(Mode.DATASET_CREATION);
        WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset().startNewTrainingRound();

    }

    public void stopRunning() {
        // OscHandler.getOscHandler().stopExtractingFeatures();
        FeatureExtractionController.stopExtracting();
        setMode(Mode.NONE);
    }

    //todo; stuck in training mode here!
    public void startRunning() {
        if (mode == Mode.TRAINING) {
            return;
        }
        if (mode == Mode.DATASET_CREATION) {
            stopDatasetCreation();
        }
        // OscHandler.getOscHandler().initiateRecord();
        FeatureExtractionController.startExtracting();
        setMode(Mode.RUNNING);
    }

    public void stopDatasetCreation() {
        if (mode != Mode.DATASET_CREATION) {
            return;
        }

        // OscHandler.getOscHandler().stopExtractingFeatures();
        FeatureExtractionController.stopExtracting();
        setMode(Mode.NONE);
    }

    public void stopTraining() {
       //abc
       WekinatorInstance.getWekinatorInstance().getLearningSystem().stopTraining();
        setMode(Mode.NONE);
    }

    public void startTraining() {
        setMode(Mode.TRAINING);
        WekinatorInstance.getWekinatorInstance().getLearningSystem().trainInBackground(trainingMask);
    }

    public void startTraining(int paramNum) {
        setMode(Mode.TRAINING);
        WekinatorInstance.getWekinatorInstance().getLearningSystem().trainInBackground(paramNum);
    }

    protected double[] lastFeatures;
    protected double[] lastParams;

    public void updateFeatures(double[] features) {
        if (mode == Mode.RUNNING) {
            try {
                double[] os = WekinatorInstance.getWekinatorInstance().getLearningSystem().classify(features);
                setOutputs(os);
                if (WekinatorRunner.isLogging()) {
                    Plog.runStep(features, os);
                }

                //TODO RAF important TODO TODO TODO: issue of displaying output for dist features
                //TODO here: this already has a distribution if necessary
                OscHandler.getOscHandler().sendParamsToSynth(outputs);

            } catch (Exception ex) {
                Logger.getLogger(WekinatorLearningManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (mode == Mode.DATASET_CREATION) {
           // if (!WekinatorInstance.getWekinatorInstance().isGestureEndMode()) {
                WekinatorInstance.getWekinatorInstance().getLearningSystem().addToTraining(features, params);
           // } else {
           //     lastFeatures = features;
           //     lastParams = params;
           // }
        }
    }
    /**
     * Get the value of learningSystem
     *
     * @return the value of learningSystem
     */
    /*  public LearningSystem getLearningSystem() {
    return learningSystem;
    } */
    /**
     * Set the value of learningSystem
     *
     * @param learningSystem new value of learningSystem
     */
    /*  public void setLearningSystem(LearningSystem learningSystem) {

    LearningSystem oldLearningSystem = this.learningSystem;
    if (oldLearningSystem != null) {
    oldLearningSystem.removePropertyChangeListener(learningSystemPropertyChange);
    }
    this.learningSystem = learningSystem;
    if (learningSystem != null) {
    learningSystem.addPropertyChangeListener(learningSystemPropertyChange);

    }
    updateMyInitState();
    propertyChangeSupport.firePropertyChange(PROP_LEARNINGSYSTEM, oldLearningSystem, learningSystem);
    } */
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

    public static synchronized WekinatorLearningManager getInstance() {
        if (ref == null) {
            ref = new WekinatorLearningManager();
        }
        return ref;
    }

    private void wekinatorInstancePropChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorInstance.PROP_LEARNINGSYSTEM)) {
            updateLearningSystemListener((LearningSystem) evt.getOldValue(), (LearningSystem) evt.getNewValue());
            if (evt.getNewValue() != null)
                resetTrainingMask(((LearningSystem)evt.getNewValue()).getNumParams());
            else
                resetTrainingMask(0);
        } else if (evt.getPropertyName().equals(WekinatorInstance.PROP_NUMPARAMS)) {
                resetTrainingMask((Integer)evt.getNewValue());
        }
    }

    private void resetTrainingMask(int numParams) {
       boolean[] newMask = new boolean[numParams];
       for (int i = 0; i < newMask.length; i++) {
            newMask[i] = true;
       }
       setTrainingMask(newMask);
    }

    //Problem here: WekinatorInstance and learning manager have constructors that reference each other!
    private WekinatorLearningManager() {
        updateLearningSystemListener(null, WekinatorInstance.getWekinatorInstance().getLearningSystem());

        WekinatorInstance.getWekinatorInstance().setWekinatorLearningManager(this);
        WekinatorInstance.getWekinatorInstance().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                wekinatorInstancePropChange(evt);
            }
        });
        if (WekinatorRunner.isLogging()) {
            Plog.setWekinatorLearningManager(this);
        }

        KeyEventPostProcessor processor = new KeyEventPostProcessor() {

            public boolean postProcessKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                        //Start recording
                        System.out.println("pg down start");
                        try {
                            startDatasetCreation();
                        } catch (Exception ex) {
                            System.out.println("log this: dataset creation start failed");
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                        //TODO in future: integrate playalong here.
                    }
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                        System.out.println("pg down stop");

                        stopDatasetCreation();
                    }
                }
                return true;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(processor);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    //ABC: This is waht I need to fix: no longer listening for this on learning system
    //Do I try to synch? Or just have learning system keep state? (probably latter)
    private void learningSystemPropertyChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(LearningSystem.PROP_ISTRAINING)) {
            if (!WekinatorInstance.getWekinatorInstance().getLearningSystem().isIsTraining()
                    && getMode() == Mode.TRAINING) {
                setMode(Mode.NONE);
            }
        } else if (evt.getPropertyName().equals(LearningSystem.PROP_ISEVALUATING)) {
            if (!WekinatorInstance.getWekinatorInstance().getLearningSystem().getIsEvaluating()
                    && getMode() == Mode.EVALUATING) {
                setMode(Mode.NONE);
            }
        }
    }

    private void updateLearningSystemListener(LearningSystem o, LearningSystem n) {
        if (o != null) {
            o.removePropertyChangeListener(learningSystemPropertyChange);
        }
        if (n != null) {
            n.addPropertyChangeListener(learningSystemPropertyChange);
        }
    }

    protected void fireOutputChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
