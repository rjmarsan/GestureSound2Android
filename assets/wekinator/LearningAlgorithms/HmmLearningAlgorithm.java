/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator.LearningAlgorithms;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;

/**
 *
 * @author rebecca
 */
public class HmmLearningAlgorithm extends LearningAlgorithm {
//    protected HmmSettingsPanel myPanel = null;
    protected int numStates = 5;
   // protected Instance[] buffer = null;
    protected LinkedList<ObservationVector> buffer = new LinkedList<ObservationVector>();
    protected int buffSize = 10;
    protected List<Hmm<ObservationVector>> hmms = null;
    protected int numClasses = 1;
    protected int minLength = 5;

    public HmmLearningAlgorithm(int numClasses) {
        this.numClasses = numClasses;
        hmms = new ArrayList<Hmm<ObservationVector>>(numClasses);
        for (int i = 0; i < numClasses; i++) {
            hmms.add(null);
        }
//      myPanel = new HmmSettingsPanel(this);

    }

    @Override
    public LearningAlgorithm copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
//    public LearningAlgorithmSettingsPanel getSettingsPanel() {
////        return myPanel;
//    }

    @Override
    public String getName() {
        return "HMM";
    }

    @Override
    public double classify(Instance instance) throws Exception {
        double[] d = distributionForInstance(instance);
        double max = -1.0;
        int maxIndex = 0;
        for (int i = 0; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    protected void updateBuffer(LinkedList<ObservationVector> b, Instance instance) {
        //For now, never clear buffer.
        if (b.size() == buffSize) {
            b.removeFirst();
        }
        ObservationVector v = getOvForInstance(instance);
        b.addLast(v);
    }

    protected ObservationVector getOvForInstance(Instance instance) {
        //Instance n = (Instance)instance.copy();
        //n.deleteAttributeAt(n.classIndex());
        double[] d = new double[instance.numAttributes() - 1];
        for (int i = 0; i < instance.numAttributes()-1; i++) {
            d[i] = instance.value(i);
        }

        ObservationVector v = new ObservationVector(d);
        return v;
    }

    @Override
    public double[] distributionForInstance(Instance instance) throws Exception {
        double[] probs = new double[hmms.size()];
        updateBuffer(buffer, instance);
        for (int i = 0; i < probs.length; i++) {
            if (hmms.get(i) != null) {
                //updateBuffer(buffer, instance);
                probs[i] = hmms.get(i).probability(buffer);
            } else {
                probs[i] = 0;
            }
        }
        return probs;
    }

    protected int getNumClassValues(Instances i) {
        Attribute a = i.classAttribute();
        Enumeration e = a.enumerateValues();
        int max = 0;
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            Integer n = Integer.parseInt(s);
            if (n > max) {
                max = n;
            }
        }
        System.out.println("hmm detected " + (max + 1) + " att values");
        return max + 1;
    }

    @Override
    public void train(Instances instances) throws Exception {
        if (instances.numInstances() == 0) {
            return;
        }
        int numFeats = instances.numAttributes() - 1;
        

        TrainingState backupState = this.trainingState;
        setTrainingState(TrainingState.TRAINING);
        List<Hmm<ObservationVector>> backups = new ArrayList<Hmm<ObservationVector>>(numClasses);
        for (int i = 0; i < hmms.size(); i++) {
              backups.add(hmms.get(i));
        }

        try {
            List<List<List<ObservationVector>>> sequences = getSequences(instances);
            for (int i = 0; i < numClasses; i++) {
                //System.out.println("**** SEQUENCE FOR PARAM=" + i + ":");
                //printSequences(sequences.get(i));
                List<List<ObservationVector>> thisSequence = sequences.get(i);
                if (thisSequence.size() > 0) {

                Hmm<ObservationVector> initHmm = new Hmm<ObservationVector>(numStates, new OpdfMultiGaussianFactory(numFeats));
                BaumWelchLearner bwl = new BaumWelchLearner();
                hmms.set(i, bwl.learn(initHmm, thisSequence));
                }
            }

            setTrainingState(TrainingState.TRAINED);
        } catch (Exception ex) {
            hmms = backups;
            setTrainingState(backupState);
            throw ex;
        }
    }

    //Ensure before calling that instances size > 0
    protected List<List<List<ObservationVector>>> getSequences(Instances instances) {
        // List<vector> is a single sequence
        // List<List<vector>> is a set of sequences
        // we have a set of sequences for each param class

        //// return new LinkedList<new LinkedList<ObservationVector>>();
        List<List<List<ObservationVector>>> allSequences = new LinkedList<List<List<ObservationVector>>>();
        int numSequences = getNumClassValues(instances);
        for (int i = 0; i < numSequences; i++) {
            allSequences.add(new LinkedList<List<ObservationVector>>());
        }
        
        LinkedList<ObservationVector> currentBuffer = new LinkedList<ObservationVector>();

        //How do we want to do this?
        //Buffer of any length, but only 1 class?
        //int minBufLength = 10;
        double lastClass = -1;
        boolean isFirst = true;
        for (int i = 0; i < instances.numInstances(); i++) {
            if (instances.instance(i).classIsMissing()) {
                updateBuffer(currentBuffer, instances.instance(i));
            } else {
                if (isFirst) {
                    lastClass = instances.instance(i).classValue();
                    isFirst = false;
                }
                double thisClass = instances.instance(i).classValue();
                if (lastClass == thisClass) {
                    updateBuffer(currentBuffer, instances.instance(i));
                    //ADD TO APPROPRIATE SEQUENCE:
                    if (currentBuffer.size() >= minLength) {
                        List<ObservationVector> bufferCopy = (List<ObservationVector>)currentBuffer.clone(); // I think shallow is OK?
                        int intClass = (int)thisClass;
                        allSequences.get(intClass).add(bufferCopy);
                    }
                } else {
                    currentBuffer.clear();
                    lastClass = thisClass;
                }
            }
        }

        return allSequences;
    }

    @Override
    public void forget() {
        hmms = null;
        setTrainingState(trainingState.NOT_TRAINED);
    }

    @Override
    public double computeCVAccuracy(int numFolds, Instances instances) throws Exception {
      /*  if (getTrainingState() == TrainingState.TRAINED) {
             //          Thread.sleep(3000);

            //TODO: is it necessary to copy here? Depends on implementation of SimpleDataset
            Random r = new Random();
            Instances randData = new Instances(instances);
            randData.randomize(r);
            randData.stratify(numFolds);
            List<Hmm<ObservationVector>> backup = new ArrayList<Hmm<ObservationVector>>(numClasses);
            for (int i= 0; i < numClasses; i++) {
                backup.add(hmms.get(i).clone());
            }
            int numFeats = instances.numAttributes() - 1;

            double sum = 0;
            for (int j = 0; j < numFolds; j++) {    //TODO: more efficient way to do this?!
                Instances train = randData.trainCV(numFolds, j);
                Instances test = randData.testCV(numFolds, j);
                List<Hmm<ObservationVector>> thisFoldHmms = new ArrayList<Hmm<ObservationVector>>(numClasses);

                List<List<List<ObservationVector>>> sequences = getSequences(instances);
                for (int i = 0; i < numClasses; i++) {
                    //System.out.println("**** SEQUENCE FOR PARAM=" + i + ":");
                    //printSequences(sequences.get(i));
                    List<List<ObservationVector>> thisSequence = sequences.get(i);
                    if (thisSequence.size() > 0) {

                        Hmm<ObservationVector> initHmm = new Hmm<ObservationVector>(numStates, new OpdfMultiGaussianFactory(numFeats));
                        BaumWelchLearner bwl = new BaumWelchLearner();
                        thisFoldHmms.set(i, bwl.learn(initHmm, thisSequence));
                    }
                }

                //now evaluate
                double[] probs = new double[hmms.size()];
                updateBuffer(buffer, instance);
        for (int i = 0; i < probs.length; i++) {
            if (hmms.get(i) != null) {
                //updateBuffer(buffer, instance);
                probs[i] = hmms.get(i).probability(buffer);
            } else {
                probs[i] = 0;
            }
        }
                
                
                double a = eval.correct();
                int b = test.numInstances();
                sum += a / b;
            }
            return sum / (double) numFolds;

        } else {
            throw new Exception("Cannot evaluate: Not trained");
        } */
        return .413;
    }

    @Override
    public void writeToOutputStream(ObjectOutputStream o) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNumStates() {
        return numStates;
    }

    public void setNumStates(int n) {
        numStates = n;
    }

    public int getBuffSize() {
        return buffSize;
    }

    public void setBuffSize(int b) {
        buffSize = b;
        buffer = new LinkedList<ObservationVector>();
    }

    @Override
    public boolean implementsFastAccurate() {
        return false;
    }
    static LearningAlgorithm readFromInputStream(ObjectInputStream i, boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void printSequences(List<List<ObservationVector>> ss) {
        for (int i= 0; i < ss.size(); i++) {
            System.out.println("  Sequence " + i);
            List<ObservationVector> veclist = ss.get(i);
            for (int j = 0; j < veclist.size(); j++) {
                System.out.println("    t" + j + ":");
                ObservationVector v = veclist.get(j);
                for (int k = 0; k < v.dimension(); k++) {
                    System.out.print(" " + v.value(k));
                }
                System.out.println("");
            }

        }

    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int l) {
            if (l > 1) {
                minLength = l;
            }
    }

    @Override
    public double computeAccuracy(Instances instances) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSettingsDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
