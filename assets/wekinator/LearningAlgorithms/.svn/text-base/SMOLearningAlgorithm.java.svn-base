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
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import wekinator.LearningAlgorithms.LearningAlgorithm.TrainingState;


/**
 *
 * @author rebecca
 */
public class SMOLearningAlgorithm extends ClassifierLearningAlgorithm {
    protected transient SMOSettingsPanel myPanel = null;
//    protected int defaultNumRounds = 100; Any default params?

    public SMOLearningAlgorithm() {
        initClassifier();
        myPanel = new SMOSettingsPanel(this);
    }

    protected SMOLearningAlgorithm(SMO c) {
        classifier = c;
        setTrainingState(TrainingState.TRAINED);
        myPanel = new SMOSettingsPanel(this);
    }

    protected void initClassifier() {
        classifier = new SMO();
        PolyKernel k = new PolyKernel();
        k.setExponent(2.0);
        ((SMO)classifier).setKernel(k);
    }

    @Override
    public SMO getClassifier() {
        return (SMO)classifier;
    }


    public SMOLearningAlgorithm copy() {
        try {
            SMOLearningAlgorithm la = new SMOLearningAlgorithm();
            la.setTrainingState(trainingState);
            la.classifier = (SMO) Classifier.makeCopy(classifier);
            return la;
        } catch (Exception ex) {
            Logger.getLogger(SMOLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }

    public String getName() {
        return "Support Vector Machine";
    }

    public SMOSettingsPanel getSettingsPanel() {
                if (myPanel == null) {
            myPanel = new SMOSettingsPanel(this);
        }
        return myPanel;
    }

    
    public void setLinearKernel() {
        Kernel k = getClassifier().getKernel();
        if (k instanceof PolyKernel && ((PolyKernel)k).getExponent() == 1.0) {
            return; // already got it
        }
        else {
            PolyKernel nk = new PolyKernel();
            nk.setExponent(1.0);
            getClassifier().setKernel(nk);
        }
    }

    public void setPolyKernel(double e, boolean useLowerOrder) {
        Kernel k = getClassifier().getKernel();
        if (k instanceof PolyKernel) {
            ((PolyKernel)k).setExponent(e);
            ((PolyKernel)k).setUseLowerOrder(useLowerOrder);
            return;
        }
        else {
            PolyKernel nk = new PolyKernel();
            nk.setExponent(e);
            nk.setUseLowerOrder(useLowerOrder);
            getClassifier().setKernel(nk);
        }
    }

    public void setRbfKernel(double gamma) {
        Kernel k = getClassifier().getKernel();
        if (k instanceof RBFKernel) {
            ((RBFKernel)k).setGamma(gamma);
            return;
        }
        else {
            RBFKernel nk = new RBFKernel();
            nk.setGamma(gamma);
            getClassifier().setKernel(nk);
        }
    }

    public static SMOLearningAlgorithm readFromInputStream(ObjectInputStream i, boolean idread) throws IOException, ClassNotFoundException {
        SMOLearningAlgorithm a = new SMOLearningAlgorithm();
        if (! idread) {
            Object o = i.readObject();
        }
        a.classifier = (SMO) i.readObject();
        a.setTrainingState((TrainingState) i.readObject());
        return a;
    }

            //written 1st in file to ID me
    public static String getFileIdentifier() {
        return "smo";
    }

    @Override
    public String getSettingsDescription() {
        String s = "{";
        Kernel k = ((SMO)classifier).getKernel();
        if (k instanceof RBFKernel) {
            s += "RBF,gamma=" + ((RBFKernel)k).getGamma() + ",";
        } else if (k instanceof PolyKernel) {
            double e = ((PolyKernel)k).getExponent();
            if (e == 1.0) {
                s += "Linear,";
            } else {
                s += "Poly,exp=" + e + "," + "lowerorder=";
                s += ((PolyKernel)k).getUseLowerOrder() + ",";
            }
        } else {
            s += "Other";
        }
        s += "C=" + ((SMO)classifier).getC() + "}";
        return s;
    }
}
