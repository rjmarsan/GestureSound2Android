/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator.LearningAlgorithms;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import wekinator.util.SerializedFileUtil;

/**
 *
 * @author rebecca
 */
public abstract class ClassifierLearningAlgorithm extends LearningAlgorithm {

    protected Classifier classifier = null;

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier c) {
        classifier = c;
    }

    protected abstract void initClassifier();

    public void forget() {
        initClassifier();
        setTrainingState(trainingState.NOT_TRAINED);
    }

    public void train(Instances instances) throws Exception {
        if (instances.numInstances() == 0) {
            return;
        }
        TrainingState backupState = this.trainingState;
        setTrainingState(TrainingState.TRAINING);
        Classifier backup = Classifier.makeCopy(this.classifier);
        //If classifier null, should we reset?
        
        try {
        //    Thread.sleep(3000);
            //ERROR: Null pointer exception here 8/6
            classifier.buildClassifier(instances);

            setTrainingState(TrainingState.TRAINED);
        } catch (Exception ex) {
            System.out.println("In exception caught... ");
            setClassifier(backup);
            setTrainingState(backupState);
           // Logger.getLogger(ClassifierLearningAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public double classify(Instance instance) throws Exception {
        if (getTrainingState() == TrainingState.TRAINED) {
            return getClassifier().classifyInstance(instance);
        } else {
            throw new Exception("Cannot classify: Not trained");
        }
    }

    public double computeAccuracy(Instances instances) throws Exception {
        if (getTrainingState() == TrainingState.TRAINED) {
         //   Thread.sleep(3000);

            Evaluation eval = new Evaluation(instances);
            eval.evaluateModel(getClassifier(), instances);
          //  eval.
          //  return eval.correct() / instances.numInstances(); //BAD: lowers accuracy when class missing
            return eval.correct() / (eval.correct() + eval.incorrect());
        } else {
            throw new Exception("Cannot evaluate: Not trained");
        }

    }

    public double computeCVAccuracy(int numFolds, Instances instances) throws Exception {
        if (instances.numInstances() == 0) {
            throw new Exception("Parameter has no recorded examples");
        }
        if (getTrainingState() != TrainingState.TRAINING) {
             //          Thread.sleep(3000);

            //TODO: is it necessary to copy here? Depends on implementation of SimpleDataset
            Random r = new Random();
            Instances randData = new Instances(instances);
            randData.randomize(r);
            randData.stratify(numFolds);

            double sum = 0;
            for (int j = 0; j < numFolds; j++) {    //TODO: more efficient way to do this?!
                Evaluation eval = new Evaluation(randData);
                Instances train = randData.trainCV(numFolds, j);
                Instances test = randData.testCV(numFolds, j);
                Classifier clsCopy = Classifier.makeCopy(getClassifier());
                clsCopy.buildClassifier(train);
                eval.evaluateModel(clsCopy, test);
                double a = eval.correct();
                int b = test.numInstances();
                sum += a / b;
            }
            return sum / (double) numFolds;

        } else {
            throw new Exception("Cannot evaluate: Not trained");
        }
    }

    public double[] distributionForInstance(Instance instance) throws Exception {
        if (getTrainingState() == TrainingState.TRAINED) {
            return getClassifier().distributionForInstance(instance);
        } else {
            throw new Exception("Cannot classify: Not trained");
        }
    }

    public void writeToOutputStream(ObjectOutputStream o) throws IOException {
        o.writeObject(this.getClass().getName());
        o.writeObject(classifier);
        o.writeObject(trainingState);
    }

    public void saveAsSerializedWekaClassifier(File f) throws Exception {
        SerializedFileUtil.writeToFile(f, classifier);
    }

    public static ClassifierLearningAlgorithm loadFromSerializedWekaClassifier(File f) throws ClassCastException, Exception {
        Object o = SerializedFileUtil.readFromFile(f);
        ClassifierLearningAlgorithm la = null;
        Classifier c;
        try {
            c = (Classifier) o;
        } catch (ClassCastException ex) {
            throw new ClassCastException("File does not contain a Weka classifier");
        }

        if (c instanceof IBk) {
            la = new IbkLearningAlgorithm((IBk) c);
        } else if (c instanceof AdaBoostM1) {
            la = new AdaboostM1LearningAlgorithm((AdaBoostM1) c);
        } else if (c instanceof J48) {
            la = new J48LearningAlgorithm((J48) c);
        } else if (c instanceof SMO) {
            la = new SMOLearningAlgorithm((SMO) c);
        } else if (c instanceof MultilayerPerceptron) {
            la = new NNLearningAlgorithm((MultilayerPerceptron) c);
        } else {
            la = new OtherClassifierLearningAlgorithm(c);
            Logger.getLogger(ClassifierLearningAlgorithm.class.getName()).log(Level.SEVERE, null, "Created other learning algorithm of type " + la.getName());
        }
        return la;
    }
}
