/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator.LearningAlgorithms;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import weka.classifiers.Classifier;
import wekinator.LearningAlgorithms.LearningAlgorithm.TrainingState;

/**
 *
 * @author rebecca
 */
public class OtherClassifierLearningAlgorithm extends ClassifierLearningAlgorithm {
    protected transient OtherClassifierSettingsPanel myPanel = null;

    protected OtherClassifierLearningAlgorithm(Classifier c) {
        classifier = c;
        setTrainingState(TrainingState.TRAINED);
        myPanel = new OtherClassifierSettingsPanel(this);
    }
    

    public LearningAlgorithm copy() {
        try {
            Classifier newc = Classifier.makeCopy(classifier);
            OtherClassifierLearningAlgorithm la = new OtherClassifierLearningAlgorithm(newc);
            la.setTrainingState(trainingState);
            return la;
        } catch (Exception ex) {
            Logger.getLogger(OtherClassifierLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }

    public String getName() {
        return classifier.getClass().getSimpleName();
    }


    @Override
    public void forget() {
        setTrainingState(trainingState.NOT_TRAINED); //better than disallowing this
    }

    public OtherClassifierSettingsPanel getSettingsPanel() {
                if (myPanel == null) {
            myPanel = new OtherClassifierSettingsPanel(this);
        }
        return myPanel;
    }

    public static OtherClassifierLearningAlgorithm readFromInputStream(ObjectInputStream i, boolean idread) throws IOException, ClassNotFoundException {
        Classifier c = (Classifier) i.readObject();
        if (! idread) {
            Object o = i.readObject();
        }
        OtherClassifierLearningAlgorithm a = new OtherClassifierLearningAlgorithm(c);
        a.setTrainingState((TrainingState) i.readObject());
        return a;
    }

    @Override
    protected void initClassifier() {

    }
            //written 1st in file to ID me
    public static String getFileIdentifier() {
        return "other";
    }

    @Override
    public String getSettingsDescription() {
        return "{}";
    }

}
