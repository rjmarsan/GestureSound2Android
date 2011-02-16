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
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import wekinator.LearningAlgorithms.LearningAlgorithm.TrainingState;


/**
 *
 * @author rebecca
 */
public class J48LearningAlgorithm extends ClassifierLearningAlgorithm {
    protected transient J48SettingsPanel myPanel = null;

    public J48LearningAlgorithm() {
        initClassifier();
        myPanel = new J48SettingsPanel(this);
    }

    protected J48LearningAlgorithm(J48 c) {
        classifier = c;
        setTrainingState(TrainingState.TRAINED);
        myPanel = new J48SettingsPanel(this);
    }

    protected void initClassifier() {
        classifier = new J48();
    }

    @Override
    public J48 getClassifier() {
        return (J48) classifier;
    }

    
    public LearningAlgorithm copy() {
        try {
            J48LearningAlgorithm la = new J48LearningAlgorithm();
            la.setTrainingState(trainingState);
            la.classifier = (J48) Classifier.makeCopy(classifier);
            return la;
        } catch (Exception ex) {
            Logger.getLogger(J48LearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }

    protected static String name = "J48 Decision Tree";
    public String getName() {
        return name;
    }

    public J48SettingsPanel getSettingsPanel() {
                if (myPanel == null) {
            myPanel = new J48SettingsPanel(this);
        }
        return myPanel;
    }

            //written 1st in file to ID me
    public static String getFileIdentifier() {
        return "j48";
    }

    public static J48LearningAlgorithm readFromInputStream(ObjectInputStream i, boolean idread) throws IOException, ClassNotFoundException {
        J48LearningAlgorithm a = new J48LearningAlgorithm();
        if (! idread) {
            Object o = i.readObject();
        }
        a.classifier = (J48) i.readObject();
        a.setTrainingState((TrainingState) i.readObject());
        return a;
    }

    @Override
    public String getSettingsDescription() {
        return "{}";
    }
}
