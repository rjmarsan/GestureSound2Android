/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.io.IOException;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import org.jdesktop.swingworker.SwingWorker;
import weka.core.Instance;
import weka.core.Instances;
import wekinator.Plog.Msg;
import wekinator.util.SerializedFileUtil;

/**
 *
 * @author rebecca
 */
public class LearningSystem {

    private ChangeEvent changeEvent = null;
    protected EventListenerList listenerList = new EventListenerList();
    // protected EventListenerList cvListenerList = new EventListenerList();
    // protected EventListenerList trListenerList = new EventListenerList();
    protected boolean[] paramMask;
    protected double[] outputs;
    protected boolean[] paramUsingDistribution;
    protected int[] numMaxValsForParameter;
    protected transient SwingWorker trainingWorker = null;
    protected transient EvaluationWorker evaluationWorker = null;
    protected double[] cvResults;
    public static final String PROP_CVRESULTS = "cvResults";
    protected double[] trainResults;
    public static final String PROP_TRAINRESULTS = "trainResults";
    public static final String PROP_DATASET = "dataset";
    protected int learnerToEvaluate = -1;
    protected int learnerToTrain = -1;
    protected int numFolds = 2;
    protected EvaluationType evaluationType = EvaluationType.CV;
    protected SimpleDataset dataset = null;
    protected int numParams = 0;
    protected LearningAlgorithm[] learners;
    //  protected boolean[] learnerEnabled;
    protected TrainingStatus trainingProgress = new TrainingStatus();
    public static final String PROP_TRAININGPROGRESS = "trainingProgress";
    protected boolean isEvaluating = false;

    /**
     * Get the value of trainingProgress
     *
     * @return the value of trainingProgress
     */
    public TrainingStatus getTrainingProgress() {
        return trainingProgress;
    }

    /**
     * Set the value of trainingProgress
     *
     * @param trainingProgress new value of trainingProgress
     */
    public void setTrainingProgress(TrainingStatus trainingProgress) {
        TrainingStatus oldTrainingProgress = this.trainingProgress;
        this.trainingProgress = trainingProgress;
        propertyChangeSupport.firePropertyChange(PROP_TRAININGPROGRESS, oldTrainingProgress, trainingProgress);
    }
    protected EvalStatus evalStatus = new EvalStatus();
    public static final String PROP_EVALSTATUS = "evalStatus";

    /**
     * Get the value of evalStatus
     *
     * @return the value of evalStatus
     */
    public EvalStatus getEvalStatus() {
        return evalStatus;
    }

    /**
     * Set the value of evalStatus
     *
     * @param evalStatus new value of evalStatus
     */
    public void setEvalStatus(EvalStatus evalStatus) {
        EvalStatus oldEvalStatus = this.evalStatus;
        this.evalStatus = evalStatus;
        propertyChangeSupport.firePropertyChange(PROP_EVALSTATUS, oldEvalStatus, evalStatus);
    }
    protected transient PropertyChangeListener learnerChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            learnerPropertyChanged(evt);
        }
    };
    protected PropertyChangeListener trainingWorkerListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            trainingWorkerChanged(evt);
        }
    };
    protected PropertyChangeListener evalWorkerListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            evalWorkerChanged(evt);
        }
    };
    protected PropertyChangeListener datasetListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            datasetChanged(evt);
        }
    };
    protected transient Logger logger = Logger.getLogger(LearningSystem.class.getName());
    //   protected LearningAlgorithmsInitializationState initializationState = LearningAlgorithmsInitializationState.NOT_INITIALIZED;
    //   public static final String PROP_INITIALIZATIONSTATE = "initializationState";
    // protected LearningAlgorithmsTrainingState algorithmsTrainingState = LearningAlgorithmsTrainingState.NOT_TRAINED;
    protected DatasetState datasetState = DatasetState.NO_DATA;
    public static final String PROP_DATASETSTATE = "datasetState";
    // protected LearningSystemTrainingState systemTrainingState = LearningSystemTrainingState.NOT_TRAINED;
    //  public static final String PROP_SYSTEMTRAININGSTATE = "systemTrainingState";
    //  protected EvaluationState evaluationState = EvaluationState.NOT_EVALUATED;
    //  public static final String PROP_EVALUATIONSTATE = "evaluationState";

    //TODO: move into LearningManager
    protected boolean isTraining = false;
    public static final String PROP_ISTRAINING = "isTraining";
    public static final String PROP_ISEVALUATING = "isEvaluating";

    /**
     * Get the value of isTraining
     *
     * @return the value of isTraining
     */
    public boolean isIsTraining() {
        return isTraining;
    }

    /**
     * Set the value of isTraining
     *
     * @param isTraining new value of isTraining
     */
    public void setIsTraining(boolean isTraining) {
        boolean oldIsTraining = this.isTraining;
        this.isTraining = isTraining;
        propertyChangeSupport.firePropertyChange(PROP_ISTRAINING, oldIsTraining, isTraining);
    }

    /**
     * Set the value of isTraining
     *
     * @param isTraining new value of isTraining
     */
    public void setIsEvaluating(boolean isEvaluating) {
        boolean oldIsEvaluating = this.isEvaluating;
        this.isEvaluating = isEvaluating;
        propertyChangeSupport.firePropertyChange(PROP_ISEVALUATING, oldIsEvaluating, isEvaluating);
    }

    public boolean getIsEvaluating() {
        return isEvaluating;
    }

    void stopTraining() {
        if (isTraining) {
            trainingWorker.cancel(true);
        }
    }

    void stopEvaluating() {
        if (isEvaluating) {
            evaluationWorker.cancel(true);
        }
    }

    private void trainingWorkerChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            if (trainingWorker.getState() == SwingWorker.StateValue.DONE) {
                //SwingWorker.StateValue.
                System.out.println("should be setting training to false here");
                setIsTraining(false);
            }
        } // else if = progress: save this for when extracted to learning manager.

    }

    private void evalWorkerChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            if (evaluationWorker.getState() == SwingWorker.StateValue.DONE) {
                //System.out.println("should be setting eval to false here");
                // isEvaluating = false;
                setIsEvaluating(false);
            }
        } // else if = progress: save this for when extracted to learning manager.

    }

    public static String getFileExtension() {
        return "wlsys";
    }

    public static String getFileTypeDescription() {
        return "learning system";
    }

    public static String getDefaultLocation() {
        return "learningSystems";
    }

    /**
     * Get the value of trainResults
     *
     * @return the value of trainResults
     */
    public double[] getTrainResults() {
        return trainResults;
    }

    /**
     * Set the value of trainResults
     *
     * @param trainResults new value of trainResults
     */
    public void setTrainResults(double[] trainResults) {
        System.out.println("train done0");
        double[] oldTrainResults = null;
        if (this.trainResults != null) {
            oldTrainResults = new double[this.trainResults.length];
            for (int i = 0; i < oldTrainResults.length; i++) {
                oldTrainResults[i] = this.trainResults[i];
            }
        }

        this.trainResults = trainResults;
        propertyChangeSupport.firePropertyChange(PROP_TRAINRESULTS, oldTrainResults, trainResults);
    }

    /**
     * Get the value of trainResults at specified index
     *
     * @param index
     * @return the value of trainResults at specified index
     */
    public double getTrainResults(int index) {
        return this.trainResults[index];
    }

    /**
     * Set the value of trainResults at specified index.
     *
     * @param index
     * @param newTrainResults new value of trainResults at specified index
     */
    public void setTrainResults(int index, double newTrainResults) {
        System.out.println("train done");
        double oldTrainResults = this.trainResults[index];
        this.trainResults[index] = newTrainResults;
        propertyChangeSupport.fireIndexedPropertyChange(PROP_TRAINRESULTS, index, oldTrainResults, newTrainResults);
    }
    protected boolean isRunnable = false;
    public static final String PROP_ISRUNNABLE = "isRunnable";

    /**
     * Get the value of isRunnable
     *
     * @return the value of isRunnable
     */
    public boolean isIsRunnable() {
        return isRunnable;
    }

    /**
     * Set the value of isRunnable
     *
     * @param isRunnable new value of isRunnable
     */
    protected void setIsRunnable(boolean isRunnable) {
        boolean oldIsRunnable = this.isRunnable;
        this.isRunnable = isRunnable;
        propertyChangeSupport.firePropertyChange(PROP_ISRUNNABLE, oldIsRunnable, isRunnable);
    }

    /**
     * Get the value of cvResults
     *
     * @return the value of cvResults
     */
    public double[] getCvResults() {
        return cvResults;
    }

    /**
     * Set the value of cvResults
     *
     * @param cvResults new value of cvResults
     */
    public void setCvResults(double[] cvResults) {
        System.out.println("cv done");

        double[] oldCvResults = null;
        if (this.cvResults != null) {
            oldCvResults = new double[this.cvResults.length];
            for (int i = 0; i < oldCvResults.length; i++) {
                oldCvResults[i] = this.cvResults[i];
            }
        }

        this.cvResults = cvResults;
        propertyChangeSupport.firePropertyChange(PROP_CVRESULTS, oldCvResults, cvResults);
    }

    /**
     * Get the value of cvResults at specified index
     *
     * @param index
     * @return the value of cvResults at specified index
     */
    public double getCvResults(int index) {
        return this.cvResults[index];
    }

    /**
     * Set the value of cvResults at specified index.
     *
     * @param index
     * @param newCvResults new value of cvResults at specified index
     */
    public void setCvResults(int index, double newCvResults) {
        System.out.println("cv done");
        double oldCvResults = this.cvResults[index];
        this.cvResults[index] = newCvResults;
        propertyChangeSupport.fireIndexedPropertyChange(PROP_CVRESULTS, index, oldCvResults, newCvResults);
    }
    protected boolean isTrainable = false;
    public static final String PROP_ISTRAINABLE = "isTrainable";

    /**
     * Get the value of isTrainable
     *
     * @return the value of isTrainable
     */
    public boolean isIsTrainable() {
        return isTrainable;
    }

    /**
     * Set the value of isTrainable
     *
     * @param isTrainable new value of isTrainable
     */
    protected void setIsTrainable(boolean isTrainable) {
        boolean oldIsTrainable = this.isTrainable;
        this.isTrainable = isTrainable;
        propertyChangeSupport.firePropertyChange(PROP_ISTRAINABLE, oldIsTrainable, isTrainable);
    }


    /*   public enum LearningAlgorithmsInitializationState {
    NOT_INITIALIZED,
    SOME_INITIALIZED,
    ALL_INITIALIZED
    };

    public enum LearningSystemTrainingState {
    NOT_TRAINED,
    TRAINING,
    TRAINED,
    ERROR
    };

    public enum EvaluationState {
    NOT_EVALUATED,
    EVALUTATING,
    DONE_EVALUATING
    }; */
    public enum DatasetState {

        NO_DATA,
        HAS_DATA
    };

    protected enum EvaluationType {

        CV,
        TRAINING
    };

    /**
     * Get the value of evaluationState
     *
     * @return the value of evaluationState
     */
    /*  public EvaluationState getEvaluationState() {
    return evaluationState;
    } */
    /**
     * Set the value of evaluationState
     *
     * @param evaluationState new value of evaluationState
     */
    /*  public void setEvaluationState(EvaluationState evaluationState) {
    EvaluationState oldEvaluationState = this.evaluationState;
    this.evaluationState = evaluationState;
    propertyChangeSupport.firePropertyChange(PROP_EVALUATIONSTATE, oldEvaluationState, evaluationState);
    } */
    void addToTraining(double[] features, double[] params) {
        //Add to the training dataset.
        try {
            dataset.addInstance(features, params, paramMask, new Date());
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(LearningSystem.class.getName()).log(Level.SEVERE, "Invalid features or parameters");
        }
    }

    double[] classify(double[] features) {
        Instance[] instances = dataset.convertToClassifiableInstances(features);
        int next = 0;
        for (int i = 0; i < numParams; i++) {
            if (learners[i] != null && learners[i].getTrainingState() == LearningAlgorithm.TrainingState.TRAINED) { //classify whether or not learner enabled
                if (!paramUsingDistribution[i]) {
                    try {
                        outputs[next] = learners[i].classify(instances[i]);
                        next++;
                    } catch (Exception ex) {
                        next++;
                        Logger.getLogger(LearningSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    double[] dist = new double[0];
                    try {
                        dist = learners[i].distributionForInstance(instances[i]);
                        for (int j = 0; j < dist.length; j++) {
                            outputs[next++] = dist[j];
                        }
                    } catch (Exception ex) {
                        next += dist.length;
                        Logger.getLogger(LearningSystem.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } else { //learner not active : keep old values, but advance next
                if (!paramUsingDistribution[i]) {
                    next++;
                } else {
                    next += numMaxValsForParameter[i];
                }
            }
        }
        return outputs;
    }

    protected void train(int learnerNum) {
        // setSystemTrainingState(LearningSystemTrainingState.TRAINING);
        if (learners[learnerNum] != null) {
            try {
                learners[learnerNum].train(dataset.getClassifiableInstances(learnerNum));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Could not train", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(LearningSystem.class.getName()).log(Level.SEVERE, null, ex);
            //TODO lower priority: test this works when error actually occurs in learner
            }
        }
    }

    public void trainInBackground() {
        boolean[] which = new boolean[numParams];
        for (int i = 0; i < numParams; i++) {
            which[i] = true;
        }
        trainInBackground(which);
    }

    public void trainInBackground(final boolean[] whichLearners) {
        synchronized (this) {
            //See http://www.j2ee.me/javase/6/docs/api/javax/swing/SwingWorker.html
            if (trainingWorker != null && trainingWorker.getState() != SwingWorker.StateValue.DONE) {
                //TODO: Cancel?
                //trainingWorker.cancel(true);
                return;
            }
            trainingWorker = new SwingWorker<Integer, Void>() {

                //trainingWorker.
                @Override
                public Integer doInBackground() {
                    // train(); //TODO: Add status updates
                    int progress = 0;
                    //setProgress(progress);
                    int numToTrain = 0;
                    for (int i = 0; i < whichLearners.length; i++) {
                        if (whichLearners[i]) {
                            numToTrain++;
                        }
                    }
                    int numTrained = 0;
                    int numErr = 0;
                    setTrainingProgress(new TrainingStatus(numToTrain, numTrained, numErr, false));

                    for (int i = 0; i < whichLearners.length; i++) {
                        if (whichLearners[i] && learners[i] != null) {
                            try {
                                Instances ii = dataset.getClassifiableInstances(i);
                                if (ii.numInstances() == 0) {
                                    throw new Exception("Param " + i + " has no recorded examples");
                                }
                                learners[i].train(ii);
                                numTrained++;
                            } catch (InterruptedException ex) {
                                System.out.println("I was cancelled");
                                return new Integer(0);
                            } catch (Exception ex) {
                                numErr++;
                                JOptionPane.showMessageDialog(null, "Could not train model for parameter " + i + "\n" + ex.getMessage(), "Training error", JOptionPane.ERROR_MESSAGE);
                                Logger.getLogger(LearningSystem.class.getName()).log(Level.SEVERE, null, ex);
                                if (WekinatorRunner.isLogging()) {
                                    Plog.log(Msg.TRAINING_ERROR, "{" + ex.getMessage() + "}");
                                }
                            //TODO lower priority: test this works when error actually occurs in learner
                            }
                            //    setProgress(progress++);
                            setTrainingProgress(new TrainingStatus(numToTrain, numTrained, numErr, false));
                        // System.out.println("progress is " + progress);
                        }

                    }

                    return new Integer(0);
                }

                @Override
                public void done() {
                    //setProgress(numParams+1);
                    System.out.println("thread is done");
                    if (isCancelled()) {
                        TrainingStatus t = new TrainingStatus(trainingProgress.numToTrain, trainingProgress.numTrained, trainingProgress.numErrorsEncountered, true);
                        // trainingProgress.wasCancelled = true;
                        setTrainingProgress(t);
                        System.out.println("I was cancelled4");
                    }
                }
            };
            setIsTraining(true);
            trainingWorker.addPropertyChangeListener(trainingWorkerListener);
            trainingWorker.execute();
        }
    }

    public void trainInBackground(int paramNum) {
        boolean[] which = new boolean[numParams];
        which[paramNum] = true;
        trainInBackground(which);
    }

    public void forget() {
        for (int i = 0; i < numParams; i++) {
            if (learners[i] != null) {
                learners[i].forget();
            }
        }
        updateRunnable();
        updateTrainable();
    }

    /**
     * Get the value of systemTrainingState
     *
     * @return the value of systemTrainingState
     */
    /* public LearningSystemTrainingState getSystemTrainingState() {
    return systemTrainingState;
    } */
    /**
     * Set the value of systemTrainingState
     *
     * @param systemTrainingState new value of systemTrainingState
     */
    /* public void setSystemTrainingState(LearningSystemTrainingState systemTrainingState) {
    LearningSystemTrainingState oldSystemTrainingState = this.systemTrainingState;
    this.systemTrainingState = systemTrainingState;
    propertyChangeSupport.firePropertyChange(PROP_SYSTEMTRAININGSTATE, oldSystemTrainingState, systemTrainingState);
    } */
    /**
     * Get the value of paramMask
     *
     * @return the value of paramMask
     */
    public boolean[] isParamMask() {
        return paramMask;
    }

    /**
     * Set the value of paramMask
     *
     * @param paramMask new value of paramMask
     */
    public void setParamMask(boolean[] paramMask) {
        this.paramMask = paramMask;
    }

    /**
     * Get the value of paramMask at specified index
     *
     * @param index
     * @return the value of paramMask at specified index
     */
    public boolean isParamMask(int index) {
        return this.paramMask[index];
    }

    /**
     * Set the value of paramMask at specified index.
     *
     * @param index
     * @param newParamMask new value of paramMask at specified index
     */
    public void setParamMask(int index, boolean newParamMask) {
        this.paramMask[index] = newParamMask;
    }

    //Update this when initialization
    ///TODO problem: What if learner swapped mid-training?!?
    //should disallow this in GUI.
    protected void updateRunnable() {
        if (learners == null || learners.length == 0) {
            setIsRunnable(false);
        }

        int numTrained = 0;
        for (int i = 0; i < learners.length; i++) {
            if (learners[i] != null) {
                if (learners[i].getTrainingState() == LearningAlgorithm.TrainingState.TRAINED) {
                    numTrained++;
                }
            }
        }
        setIsRunnable(numTrained > 0);
    }


    /*    protected void updateTrainingState() {
    if (learners == null || learners.length == 0) {
    setSystemTrainingState(LearningSystemTrainingState.NOT_TRAINED);
    return;
    }

    int numTrained = 0;
    for (int i = 0; i < learners.length; i++) {
    if (learners[i] != null) {
    if (learners[i].getTrainingState() == LearningAlgorithm.TrainingState.TRAINED) {
    numTrained++;
    } else if (learners[i].getTrainingState() == LearningAlgorithm.TrainingState.TRAINING) {
    setSystemTrainingState(LearningSystemTrainingState.TRAINING);
    return;
    //If any one is training, we'return training overall, even if someone is in error
    } else if (learners[i].getTrainingState() == LearningAlgorithm.TrainingState.ERROR) {
    setSystemTrainingState(LearningSystemTrainingState.ERROR);
    return;
    }
    }
    }
    if (numTrained == 0) {
    setSystemTrainingState(LearningSystemTrainingState.NOT_TRAINED);
    return;
    } else {
    setSystemTrainingState(LearningSystemTrainingState.TRAINED); // could consider adding SOME_TRAINED state
    }
    } */
    protected void updateTrainable() {
        if (learners == null || learners.length == 0 || dataset == null || datasetState != DatasetState.HAS_DATA) {
            setIsTrainable(false);
            return;
        }

        int numInit = 0;
        for (int i = 0; i < learners.length; i++) {
            if (learners[i] != null) {
                numInit++;
            }
        }
        if (numInit >= 1) {
            setIsTrainable(true);
        }

    }

    /* protected void updateInitializationState() {
    if (learners == null || learners.length == 0) {
    setInitializationState(LearningAlgorithmsInitializationState.NOT_INITIALIZED);
    return;
    }

    int numInit = 0;
    for (int i = 0; i < learners.length; i++) {
    if (learners[i] != null) {
    numInit++;
    }
    }
    if (numInit == 0) {
    setInitializationState(LearningAlgorithmsInitializationState.NOT_INITIALIZED);
    } else if (numInit < numParams) {
    setInitializationState(LearningAlgorithmsInitializationState.SOME_INITIALIZED);
    } else {
    setInitializationState(LearningAlgorithmsInitializationState.ALL_INITIALIZED);
    }
    } */

    // protected LearningAlgorithmsInitializationState state = LearningAlgorithmsInitializationState.NOT_INITIALIZED;
    // public static final String PROP_STATE = "state";
    private void learnerPropertyChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(LearningAlgorithm.PROP_TRAININGSTATE)) {
            updateRunnable();
        }
    /* if (evt.getPropertyName().equals(LearningAlgorithm.PROP_TRAININGSTATE)) {
    LearningAlgorithm.TrainingState newState =
    (LearningAlgorithm.TrainingState) evt.getNewValue();
    LearningAlgorithm.TrainingState oldState =
    (LearningAlgorithm.TrainingState) evt.getOldValue();
    updateTrainingState();
    } */
    }

    /**
     * Get the value of learners
     *
     * @return the value of learners
     */
    public LearningAlgorithm[] getLearners() {
        return learners;
    }

    /**
     * Set the value of learners
     *
     * @param learners new value of learners
     */
    public void setLearners(LearningAlgorithm[] learners) {
        if (learners.length != numParams) {
            throw new IllegalArgumentException("learners length does not match number of parameters (" + numParams + " expected)");
        }

        if (this.learners != null) {
            for (int i = 0; i < this.learners.length; i++) {
                if (this.learners[i] != null) {
                    this.learners[i].removePropertyChangeListener(learnerChangeListener); //removed here?
                }
            }
        }
        this.learners = learners;
        if (this.learners != null) {
            for (int i = 0; i < this.learners.length; i++) {
                if (this.learners[i] != null) {
                    this.learners[i].addPropertyChangeListener(learnerChangeListener); //added here?
                }
            }
        }

        // updateInitializationState();
        updateTrainable();
        //updateTrainingState();
        updateRunnable();
        fireLearnerChanged();
    }

    /**
     * Get the value of learners at specified index
     *
     * @param index
     * @return the value of learners at specified index
     */
    public LearningAlgorithm getLearners(int index) {
        return this.learners[index];
    }

    /**
     * Set the value of learners at specified index.
     *
     * @param index
     * @param newLearners new value of learners at specified index
     */
    public void setLearners(int index, LearningAlgorithm newLearners) {
        if (learners[index] != null) {
            learners[index].removePropertyChangeListener(learnerChangeListener);
        }

        this.learners[index] = newLearners;
        if (this.learners[index] != null) {
            this.learners[index].addPropertyChangeListener(learnerChangeListener); //added here?
        }
        //   updateInitializationState();
        updateTrainable();
        // updateTrainingState();
        updateRunnable();
        fireLearnerChanged();
    }

   public LearningSystem(int numParams, boolean[] paramUsingDistribution, int[] maxValPerParam) {
        this.numParams = numParams;
        learners = new LearningAlgorithm[numParams];
        //  learnerEnabled = new boolean[numParams];
        //paramUsingDistribution = new boolean[numParams]; //Was causing to always be set to false!
        cvResults = new double[numParams];
        trainResults = new double[numParams];

        //tmp
        //ChuckSystem cs = ChuckSystem.getChuckSystem();

        numMaxValsForParameter = maxValPerParam;
       /* for (int i = 0; i < numParams; i++) {
            
                this.paramUsingDistribution[i] = paramUsingDistribution[i];
           
        }
 */
           this.paramUsingDistribution = paramUsingDistribution; //TODO: check!

       int numOutputs = 0;
       for (int i = 0; i < numParams; i++) {
            if (paramUsingDistribution[i]) {
                numOutputs += maxValPerParam[i] + 1;
            } else
                numOutputs++;
       }


        outputs = new double[numParams]; //*
        setOutputsArray(paramUsingDistribution, numMaxValsForParameter);
        paramMask = new boolean[numParams];
        for (int i = 0; i < numParams; i++) {
            //  learnerEnabled[i] = true;
            paramMask[i] = true;
        }

    //  setScore(new PlayalongScore(numParams));
    }


    public LearningSystem(int numParams) {
        this.numParams = numParams;
        learners = new LearningAlgorithm[numParams];
        //  learnerEnabled = new boolean[numParams];
        paramUsingDistribution = new boolean[numParams];
        cvResults = new double[numParams];
        trainResults = new double[numParams];

        //tmp
        ChuckSystem cs = ChuckSystem.getChuckSystem();

        numMaxValsForParameter = SynthProxy.paramMaxValues();
        int numOutputs = 0;
        for (int i = 0; i < numParams; i++) {
            if (SynthProxy.isParamDiscrete(i)) {
           // if (ChuckSystem.getChuckSystem().isIsParamDiscrete(i)) {
                paramUsingDistribution[i] = SynthProxy.isParamDistribution(i);
                if (paramUsingDistribution[i]) {
                    numOutputs += SynthProxy.paramMaxValue(i) + 1;
                } else {
                    numOutputs++;
                }
            } else {
                paramUsingDistribution[i] = false;
                numOutputs++;
            }
        }

        outputs = new double[numParams]; //*
        setOutputsArray(paramUsingDistribution, numMaxValsForParameter);
        paramMask = new boolean[numParams];
        for (int i = 0; i < numParams; i++) {
            //  learnerEnabled[i] = true;
            paramMask[i] = true;
        }

    //  setScore(new PlayalongScore(numParams));
    }

    /**
     * Get the value of numParams
     *
     * @return the value of numParams
     */
    public int getNumParams() {
        return numParams;
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
    //Properties: Num params, type of params, feature/param mask,
    //Other methods: train, compute accuracy (summary), compute individual accuracy, get learner types

    /**
     * Get the value of initializationState
     *
     * @return the value of initializationState
     */
    /*    public LearningAlgorithmsInitializationState getInitializationState() {
    return initializationState;
    } */
    /**
     * Set the value of initializationState
     *
     * @param initializationState new value of initializationState
     */
    /*   protected void setInitializationState(LearningAlgorithmsInitializationState initializationState) {
    LearningAlgorithmsInitializationState oldInitializationState = this.initializationState;
    this.initializationState = initializationState;
    propertyChangeSupport.firePropertyChange(PROP_INITIALIZATIONSTATE, oldInitializationState, initializationState);
    } */
    /**
     * Get the value of datasetState
     *
     * @return the value of datasetState
     */
    public DatasetState getDatasetState() {
        return datasetState;
    }

    /**
     * Set the value of datasetState
     *
     * @param datasetState new value of datasetState
     */
    public void setDatasetState(DatasetState datasetState) {
        DatasetState oldDatasetState = this.datasetState;
        this.datasetState = datasetState;
        propertyChangeSupport.firePropertyChange(PROP_DATASETSTATE, oldDatasetState, datasetState);
    }

    /**
     * Get the value of dataset
     *
     * @return the value of dataset
     */
    public SimpleDataset getDataset() {
        return dataset;
    }

    /**
     * Set the value of dataset
     *
     * @param dataset new value of dataset
     */
    public void setDataset(SimpleDataset dataset) {
        if (this.dataset != null) {
            this.dataset.removePropertyChangeListener(datasetListener);
        }
        if (dataset != null) {
            dataset.addPropertyChangeListener(datasetListener);
        }
        SimpleDataset oldDataset = this.dataset;
        this.dataset = dataset;

        propertyChangeSupport.firePropertyChange(PROP_DATASET, oldDataset, dataset);
        updateDatasetState();
        updateTrainable();

    }

    public void computeTrainingAccuracyInBackground() throws Exception {
        computeTrainingAccuracyInBackground(-1);
    }

    public void computeTrainingAccuracyInBackground(int paramNum) {
        synchronized (this) {
            if (isRunnable && !isEvaluating) {
                //    setEvaluationState(evaluationState.EVALUTATING);
                //isEvaluating = true;
                setIsEvaluating(true);
                evaluationWorker = new EvaluationWorker();
                evaluationWorker.addPropertyChangeListener(evalWorkerListener);

                learnerToEvaluate = paramNum;
                evaluationType = EvaluationType.TRAINING;
                evaluationWorker.execute();
                if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.EVAL_START_TRAIN, paramNum + "");
                }

            } else {
                //   throw new Exception("Cannot evaluate; either already evaluating, or not trained");
                System.out.println("Cannot evaluate");
            //TODO: popup
            }
        }
    }

    public void computeCVAccuracyInBackground(int numFolds) {
        computeCVAccuracyInBackground(-1, numFolds);
    }

    public void computeCVAccuracyInBackground(int paramNum, int numFolds) {
        synchronized (this) {
            if (isTrainable && !isEvaluating) {
                //  isEvaluating = true;
                setIsEvaluating(true);
                evaluationWorker = new EvaluationWorker();
                evaluationWorker.addPropertyChangeListener(evalWorkerListener);

                //   setEvaluationState(evaluationState.EVALUTATING);
                learnerToEvaluate = paramNum;
                this.numFolds = numFolds;
                evaluationType = EvaluationType.CV;
                evaluationWorker.execute();
                if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.EVAL_START_CV, paramNum + "," + numFolds);
                }
            } else {
                //throw new Exception("Cannot evaluate; either already evaluating, or not trained");
                System.out.println("Error: Cannot evaluate");
            //TODO: popup box
            }
        }
    }

    /*public void computeCVAccuracy(int numFolds) throws Exception {
    if (isRunnable) {
    //  setEvaluationState(EvaluationState.EVALUTATING);
    double[] a = new double[numParams];
    for (int i = 0; i < numParams; i++) {
    if (learners[i] != null) {
    a[i] = learners[i].computeCVAccuracy(numFolds, dataset.getClassifiableInstances(i));
    setCvResults(i, a[i]);
    }
    }
    //  setEvaluationState(EvaluationState.EVALUTATING.DONE_EVALUATING);
    } else {
    throw new Exception("Cannot evaluate; either already evaluating, or not trained");
    }
    } */
    protected void updateDatasetState() {
        if (dataset.isHasInstances()) {
            setDatasetState(DatasetState.HAS_DATA);
        } else {
            setDatasetState(DatasetState.NO_DATA);
        }

    }

    protected void datasetChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(SimpleDataset.PROP_HASINSTANCES)) {
            updateDatasetState();
            updateTrainable();
        }
    }

    public int getNumLearnersTrained() {
        int num = 0;
        for (int i = 0; i < learners.length; i++) {
            if (learners[i].getTrainingState() == LearningAlgorithm.TrainingState.TRAINED) {
                num++;
            }
        }
        return num;
    }

    public int[] getNumMaxValsForParameter() {
        return numMaxValsForParameter;
    }

    public void setNumMaxValsForParameter(int[] numMaxValsForParameter) {
        this.numMaxValsForParameter = numMaxValsForParameter;
        setOutputsArray(paramUsingDistribution, numMaxValsForParameter);
    }

    /**
     * Get the value of paramUsingDistribution
     *
     * @return the value of paramUsingDistribution
     */
    public boolean[] isParamUsingDistribution() {
        return paramUsingDistribution;
    }

    /**
     * Set the value of paramUsingDistribution
     *
     * @param paramUsingDistribution new value of paramUsingDistribution
     */
    public void setParamUsingDistribution(boolean[] paramUsingDistribution) {
        this.paramUsingDistribution = paramUsingDistribution;
        setOutputsArray(paramUsingDistribution, numMaxValsForParameter);
    }

    protected void setOutputsArray(boolean[] paramUsingDistribution, int[] maxParamVals) {
        int numOutputs = 0;
        for (int i = 0; i < numParams; i++) {
            if (paramUsingDistribution[i]) {
                numOutputs += maxParamVals[i] + 1; 
            } else {
                numOutputs++;
            }
        }
        if (numOutputs != outputs.length) {
            outputs = new double[numOutputs];
        }
    }

    /**
     * Get the value of paramUsingDistribution at specified index
     *
     * @param index
     * @return the value of paramUsingDistribution at specified index
     */
    public boolean isParamUsingDistribution(int index) {
        return this.paramUsingDistribution[index];
    }

    /**
     * Set the value of paramUsingDistribution at specified index.
     *
     * @param index
     * @param newParamUsingDistribution new value of paramUsingDistribution at specified index
     */
    public void setParamUsingDistribution(int index, boolean newParamUsingDistribution) {
        this.paramUsingDistribution[index] = newParamUsingDistribution;
       setOutputsArray(paramUsingDistribution, numMaxValsForParameter);

    }

    /**
     * Get the value of score
     *
     * @return the value of score
     */
    /*   public PlayalongScore getScore() {
    return score;
    } */
    /**
     * Set the value of score
     *
     * @param score new value of score
     */
    /*    public void setScore(PlayalongScore score) {
    PlayalongScore oldScore = this.score;
    this.score = score;
    propertyChangeSupport.firePropertyChange(PROP_SCORE, oldScore, score);
    } */
    public void addLearnerChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeLearnerChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireLearnerChanged() {
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

    /*  protected void fireCVDone() {
    Object[] listeners = cvListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
    if (listeners[i] == ChangeListener.class) {
    if (changeEvent == null) {
    changeEvent = new ChangeEvent(this);
    }
    ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
    }
    }
    } */

    /* protected void fireTrainEvalDone() {
    Object[] listeners = trListenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
    if (listeners[i] == ChangeListener.class) {
    if (changeEvent == null) {
    changeEvent = new ChangeEvent(this);
    }
    ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
    }
    }
    } */
    protected class EvaluationWorker extends SwingWorker<Integer, Void> {

        @Override
        protected Integer doInBackground() throws Exception {
            if (dataset.getNumDatapoints() > 0) {
                setProgress(1);

                int numErrs = 0;

                if (learnerToEvaluate == -1) {
                    int numDone = 0;
                    for (int i = 0; i < numParams; i++) {
                        //new EvalStatus(
                        setEvalStatus(new EvalStatus(-1, numParams, numDone, numErrs, (evaluationType == EvaluationType.CV), false));
                        double d;
                        try {
                            if (evaluationType == EvaluationType.CV) {
                                d = learners[i].computeCVAccuracy(numFolds, dataset.getClassifiableInstances(i));
                                setCvResults(i, d);
                            } else {
                                d = learners[i].computeAccuracy(dataset.getClassifiableInstances(i));
                                setTrainResults(i, d);
                            }
                            numDone++;
                        } catch (InterruptedException ex) {
                            System.out.println("I was cancelled3");
                            return new Integer(0);
                        } catch (Exception ex) {
                            numErrs++; //TODO: popup box
                            if (evaluationType == EvaluationType.CV) {
                                setCvResults(i, 0);
                            } else {
                                setTrainResults(i, 0);
                            }
                            System.out.println("ERROR encountered here 2" + ex.getMessage());
                        }
                    }
                    setEvalStatus(new EvalStatus(-1, numParams, numDone, numErrs, (evaluationType == EvaluationType.CV), false));
                    return new Integer(0);
                } else {
                    setEvalStatus(new EvalStatus(learnerToEvaluate, 1, 0, 0, (evaluationType == EvaluationType.CV), false));
                    // setEvalStatus(new EvalStatus);
                    double d;
                    try {
                        if (evaluationType == EvaluationType.CV) {
                            d = learners[learnerToEvaluate].computeCVAccuracy(numFolds, dataset.getClassifiableInstances(learnerToEvaluate));
                            setCvResults(learnerToEvaluate, d);

                        } else {
                            d = learners[learnerToEvaluate].computeAccuracy(dataset.getClassifiableInstances(learnerToEvaluate));
                            setTrainResults(learnerToEvaluate, d);
                        }
                        setEvalStatus(new EvalStatus(learnerToEvaluate, 1, 1, 0, (evaluationType == EvaluationType.CV), false));
                    } catch (InterruptedException ex) {
                        System.out.println("I was cancelled2");
                        return new Integer(0);
                    } catch (Exception ex) {
                        System.out.println("ERROR encountered here" + ex.getMessage());
                        setEvalStatus(new EvalStatus(learnerToEvaluate, 1, 0, 1, (evaluationType == EvaluationType.CV), false));
                        if (evaluationType == EvaluationType.CV) {
                            setCvResults(learnerToEvaluate, 0);
                        } else {
                            setTrainResults(learnerToEvaluate, 0);
                        }
                        //TODO: popup error box
                        System.out.println("error encountered");
                    }
                    return new Integer(0);
                }
            } else {
                System.out.println("Nothing to do: no instances yet"); //TODO log
                return new Integer(0);
            }
        }

        @Override
        protected void done() {
            if (isCancelled()) {
                //new EvalStatus(
                EvalStatus t = new EvalStatus(evalStatus.myParam, evalStatus.numModelsToEval, evalStatus.numModelsDone, evalStatus.numModelsWithErrors, evalStatus.isCV, true);
                setEvalStatus(t);
                System.out.println("I was cancelled1");
                System.out.println("State is " + getState());
            }

        }
    }

    public static LearningSystem readFromFile(File f) throws Exception {
        FileInputStream fin = new FileInputStream(f);
        ObjectInputStream i = new ObjectInputStream(fin);
        LearningSystem l;
        l = loadFromInputStream(i);
        i.close();
        fin.close();
        return l;
    }

    public void writeToFile(File f) throws IOException {
        FileOutputStream fout = new FileOutputStream(f);
        ObjectOutputStream o = new ObjectOutputStream(fout);
        writeToOutputStreamNew(o);
        o.close();
        fout.close();
    }

    public void writeToOutputStreamNew(ObjectOutputStream o) throws IOException {

        o.writeInt(numParams);
        o.writeObject(paramUsingDistribution);
        o.writeObject(numMaxValsForParameter);
        //   o.writeObject(learners);
        for (int i = 0; i < learners.length; i++) {
            if (learners[i] != null) {
                o.writeInt(1);
                learners[i].writeToOutputStream(o);
            }
        }

        if (dataset == null) {
            o.writeInt(0);
        } else {
            o.writeInt(1);
            dataset.writeToOutputStreamNew(o);
        }

    }

    public static LearningSystem loadFromInputStream(ObjectInputStream i) throws IOException, ClassNotFoundException {
        LearningSystem ls = null;
        int numParams = i.readInt();
       // ls = new LearningSystem(numParams);
//        ls.paramUsingDistribution = (boolean[]) i.readObject(); //TODO: may have to init this bit by bit...
        boolean[] paramUsingDistribution = (boolean[]) i.readObject(); //TODO: may have to init this bit by bit...

        int[] numMax = (int[]) i.readObject();
       // ls.setNumMaxValsForParameter(numMax);
        // LearningAlgorithm[] algs = (LearningAlgorithm[]) i.readObject();

        ls = new LearningSystem(numParams, paramUsingDistribution, numMax);


        LearningAlgorithm[] algs = new LearningAlgorithm[numParams];
        for (int j = 0; j < numParams; j++) {
            int hasLearner = i.readInt();
            if (hasLearner == 1) {
                algs[j] = LearningAlgorithm.readFromInputStream(i);
            } else {
                algs[j] = null;
            }
        }

        ls.setLearners(algs);
        int flag = i.readInt();
        if (flag == 0) {
            ls.setDataset(null);
        } else {
            SimpleDataset ds = SimpleDataset.loadFromInputStream(i);
            ls.setDataset(ds);
        }
        ls.updateDatasetState();
        ls.updateTrainable();
        ls.updateRunnable();
        return ls;

    }

    public static class TrainingStatus {

        protected int numToTrain = 0;
        protected int numTrained = 0;
        protected int numErrorsEncountered = 0;
        boolean wasCancelled = false;

        public TrainingStatus(int numToTrain, int numTrained, int numErr, boolean wasCancelled) {
            this.numToTrain = numToTrain;
            this.numTrained = numTrained;
            this.numErrorsEncountered = numErr;
            this.wasCancelled = wasCancelled;
        }

        public TrainingStatus() {
        }
    }

    public static class EvalStatus {

        public int myParam = -1;
        public int numModelsToEval = 0;
        public int numModelsDone = 0;
        // public int numFolds = 0;
        // public int numFoldsDone = 0;
        public int numModelsWithErrors = 0;
        public boolean wasCancelled = false;
        public boolean isCV = true;

        public EvalStatus(int myParam, int numModels, int numDone, int numErr, boolean isCV, boolean wasC) {
            this.myParam = myParam;
            this.numModelsToEval = numModels;
            this.numModelsDone = numDone;
            // this.numFolds = numFolds;
            // this.numFoldsDone = numFoldsDone;
            this.numModelsWithErrors = numErr;
            this.isCV = isCV;
            this.wasCancelled = wasC;
        }

        public EvalStatus() {
        }
    }
}

