/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator.LearningAlgorithms;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import wekinator.LearningAlgorithms.LearningAlgorithm.TrainingState;
import weka.classifiers.lazy.IBk;

/**
 *
 * @author rebecca
 */
public class IbkLearningAlgorithm extends ClassifierLearningAlgorithm {
    protected transient IbkSettingsPanel myPanel = null;
    protected int defaultNumNeighbors = 10;

    public IbkLearningAlgorithm() {
        initClassifier();
        myPanel = new IbkSettingsPanel(this);
    }

    protected IbkLearningAlgorithm(IBk c) {
        classifier = c;
        setTrainingState(TrainingState.TRAINED);
        myPanel = new IbkSettingsPanel(this);
    }

    protected void initClassifier() {
        classifier = new IBk();
        ((IBk)classifier).setKNN(defaultNumNeighbors);
    }

    //written 1st in file to ID me
    public static String getFileIdentifier() {
        return "ibk";
    }

    @Override
    public IBk getClassifier() {
        return (IBk)classifier;
    }

    public LearningAlgorithm copy() {
        try {
            IbkLearningAlgorithm la = new IbkLearningAlgorithm();
            la.setTrainingState(trainingState);
            la.classifier = (IBk) Classifier.makeCopy(classifier);
            return la;
        } catch (Exception ex) {
            Logger.getLogger(IbkLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

   protected static String name = "k-nearest neighbor";
    public String getName() {
        return name;
    }

    public IbkSettingsPanel getSettingsPanel() {
            if (myPanel == null) {
            myPanel = new IbkSettingsPanel(this);
        }
            return myPanel;
    }

    public static IbkLearningAlgorithm readFromInputStream(ObjectInputStream i, boolean idread) throws IOException, ClassNotFoundException {
        IbkLearningAlgorithm a = new IbkLearningAlgorithm();
        if (! idread) {
            Object o = i.readObject();
        }
        a.classifier = (IBk) i.readObject();
        a.setTrainingState((TrainingState) i.readObject());
        return a;
    }

    @Override
    public String getSettingsDescription() {
        String s = "{" + ((IBk)classifier).getKNN() + "}";
        return s;
    }

}
