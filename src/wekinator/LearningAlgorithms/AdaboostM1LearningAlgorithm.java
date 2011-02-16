/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator.LearningAlgorithms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;

/**
 *
 * @author rebecca
 */
public class AdaboostM1LearningAlgorithm extends ClassifierLearningAlgorithm {

//    protected AdaboostM1SettingsPanel myPanel = null;
    protected int defaultNumRounds = 100;
    protected boolean isBaseTree = true;

    public AdaboostM1LearningAlgorithm() {
        initClassifier();
    }

    protected AdaboostM1LearningAlgorithm(AdaBoostM1 adaBoostM1) {
        classifier = adaBoostM1;
        setTrainingState(TrainingState.TRAINED);
    }

    protected void initClassifier() {
        classifier = new AdaBoostM1();
       // ((AdaBoostM1) classifier).setClassifier(new DecisionStump());
        ((AdaBoostM1)classifier).setClassifier(new J48());
        ((AdaBoostM1) classifier).setNumIterations(defaultNumRounds);
        AdaBoostM1 tmp = new AdaBoostM1();
    }

    public LearningAlgorithm copy() {
        try {
            AdaboostM1LearningAlgorithm la = new AdaboostM1LearningAlgorithm();
            la.setTrainingState(trainingState);
            la.classifier = (AdaBoostM1) AbstractClassifier.makeCopy(classifier);
            return la;
        } catch (Exception ex) {
            Logger.getLogger(AdaboostM1LearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void setFastAccurate(double value) {
        if (value >= 0 && value <= 1) {
            ((AdaBoostM1) classifier).setNumIterations((int) (value * 490 + 10));
        }
    }

    @Override
    public boolean implementsFastAccurate() {
        return true;
    }

    @Override
    public AdaBoostM1 getClassifier() {
        return (AdaBoostM1) classifier;
    }

//    public AdaboostM1SettingsPanel getSettingsPanel() {
//        if (myPanel == null) {
//            myPanel = new AdaboostM1SettingsPanel(this);
//        }
//        return myPanel;
//    }

    public static AdaboostM1LearningAlgorithm readFromInputStream(ObjectInputStream i, boolean idread) throws IOException, ClassNotFoundException {
        AdaboostM1LearningAlgorithm a = new AdaboostM1LearningAlgorithm();
        if (! idread) {
            Object o = i.readObject();
        }

        a.classifier = (AdaBoostM1) i.readObject();
        Classifier base = ((AdaBoostM1)a.classifier).getClassifier();
        if (base instanceof J48)
            a.isBaseTree = true;
        else
            a.isBaseTree = false;
        
        a.setTrainingState((TrainingState) i.readObject());
        return a;
    }

    @Override
    public String getName() {
        return "Adaboost.M1";
    }

    @Override
    public String getSettingsDescription() {
        String s = "{";
        s += "numRounds=" + ((AdaBoostM1)classifier).getNumIterations() + ","
                + "tree=" + (((AdaBoostM1)classifier).getClassifier() instanceof J48) + "}";
        return s;
    }
}
