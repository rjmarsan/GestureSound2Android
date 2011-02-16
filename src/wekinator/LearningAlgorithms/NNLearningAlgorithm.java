/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator.LearningAlgorithms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

/**
 *
 * @author rebecca
 */
public class NNLearningAlgorithm extends ClassifierLearningAlgorithm {
//    protected transient NNSettingsPanel myPanel = null;

    public NNLearningAlgorithm() {
        initClassifier();
//        myPanel = new NNSettingsPanel(this);
    }

    protected NNLearningAlgorithm(MultilayerPerceptron c) {
        classifier = c;
        setTrainingState(TrainingState.TRAINED);
//        myPanel = new NNSettingsPanel(this);
    }

    protected void initClassifier() {
        classifier = new MultilayerPerceptron();
        ((MultilayerPerceptron)classifier).setHiddenLayers("i");
            //TODO: settings
    }

    public void setUseGui(boolean use) {
        getClassifier().setGUI(use);
    }

    @Override
    public MultilayerPerceptron getClassifier() {
        return (MultilayerPerceptron)classifier;
    }

    public LearningAlgorithm copy() {
        try {
            NNLearningAlgorithm la = new NNLearningAlgorithm();
            la.setTrainingState(trainingState);
            la.classifier = (MultilayerPerceptron) AbstractClassifier.makeCopy(classifier);
            return la;
        } catch (Exception ex) {
            Logger.getLogger(NNLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }

    protected static String name = "Neural Network";
    public String getName() {
        return name;
    }

//    public NNSettingsPanel getSettingsPanel() {
//                if (myPanel == null) {
//            myPanel = new NNSettingsPanel(this);
//        }
//        return myPanel;
//    }

   
    @Override
    public double computeAccuracy(Instances instances) throws Exception {
       boolean oldUseGui = getClassifier().getGUI();
       getClassifier().setGUI(false);
       //double accuracy =  computeAccuracy(instances);
       double accuracy = 0.0;
       if (getTrainingState() == TrainingState.TRAINED) {
         //   Thread.sleep(3000);

            Evaluation eval = new Evaluation(instances);
            eval.evaluateModel(getClassifier(), instances);
            //  return eval.correct() / instances.numInstances(); //BAD: lowers accuracy when class missing
            accuracy =  eval.errorRate();
        } else {
            throw new Exception("Cannot evaluate: Not trained");
        }

       getClassifier().setGUI(oldUseGui);
       return accuracy;
    }

            //written 1st in file to ID me
    public static String getFileIdentifier() {
        return "nn";
    }

    @Override
    public double computeCVAccuracy(int numFolds, Instances instances) throws Exception {
      //  return ClassifierLearningAlgorithmUtil.computeCVAccuracy(this, numFolds, instances);
        double results;
        Random r = new Random();
        //Instances randData = new Instances(instances);
       // randData.randomize(r);
       // randData.stratify(numFolds);
       // double sum = 0;
            Evaluation eval = new Evaluation(instances);

            boolean oldUseGui = getClassifier().getGUI();
            getClassifier().setGUI(false);
            eval.crossValidateModel(classifier, instances, numFolds, r);

            getClassifier().setGUI(oldUseGui);
            return eval.errorRate();
     /*   for (int j = 0; j < numFolds; j++) {



            Instances train = randData.trainCV(numFolds, j);
            Instances test = randData.testCV(numFolds, j);
            Classifier clsCopy = Classifier.makeCopy(c[classifierNum]);
            clsCopy.buildClassifier(train);
            eval.evaluateModel(clsCopy, test);
            // double a = eval.correct();
            double a = eval.errorRate();
            System.out.println("err rate is " + a);


            double b = (double) test.numInstances() / randData.numInstances(); //weight here
            sum += a * b;
        }
        results = sum;
        //}

        return results;

 */
    }

    public static NNLearningAlgorithm readFromInputStream(ObjectInputStream i, boolean idread) throws IOException, ClassNotFoundException {
        NNLearningAlgorithm a = new NNLearningAlgorithm();
        if (! idread) {
            Object o = i.readObject();
        }
        a.classifier = (MultilayerPerceptron) i.readObject();
        a.setTrainingState((TrainingState) i.readObject());
        return a;
    }

    @Override
    public String getSettingsDescription() {
        return "{}";
    }

}
