/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author rebecca
 */
public class LearningSystemRecoverer {
    public static void recover(File in, File out) throws Exception {
        LearningSystem s = null;

        s = LearningSystem.readFromFile(in);
        if (s != null) {
                 try{
                     FileOutputStream fout = new FileOutputStream(out);
                     ObjectOutputStream o = new ObjectOutputStream(fout);
                     o.writeInt(s.numParams);
            o.writeObject(s.paramUsingDistribution);
            o.writeObject(s.numMaxValsForParameter);
           // o.writeObject(s.learners);
            if (s.learners != null) {
                o.writeInt(s.learners.length);
                for (int i = 0; i < s.learners.length; i++) {
                    
                }
            } else {
                o.writeInt(0);
            }

            if (s.dataset == null) {
                o.writeInt(0);
            } else {
                o.writeInt(1);
              //  s.dataset.writeToOutputStreamNew(o);
                SimpleDataset d = s.dataset;
                o.writeInt(d.getNumParameters());
                o.writeInt(d.getNumFeatures());
                o.writeObject(d.isParamDiscrete); /// for all!! TODO
                o.writeObject(d.getFeatureNames());
                o.writeObject(d.getParameterNames());
                o.writeObject(d.getMaxLegalDiscreteParamValues()); //Potential problem here: implementation of this method has changed 8/3/10
                o.writeObject(d.allInstances); 
                o.writeInt(d.nextID);
                o.writeObject(d.audioSegments);
                o.writeInt(d.currentTrainingRound);
                o.writeInt(d.numMetaData);
                o.writeInt(d.idIndex);
                o.writeInt(d.timestampIndex);
                o.writeInt(d.trainingIndex);
                o.writeObject(d.dateFormat);
                o.writeObject(d.prettyDateFormat);
                o.writeBoolean(d.hasInstances);
                if (d.featureLearnerConfiguration == null) {
                    o.writeInt(0);
                } else {
                    o.writeInt(1);
                    FeatureLearnerConfiguration f = d.featureLearnerConfiguration;
                    //featureLearnerConfiguration.writeToOutputStreamNew(o);

                     o.writeInt(f.numLearners);
                     o.writeInt(f.numFeatures);
                     o.writeObject(f.featureLists);
                }


            }


            o.close();
    }catch (Exception e){//Catch exception if any
      System.err.println("Error: " + e.getMessage());
    }
       /*     
    protected SimpleDataset dataset = null;
    protected FeatureLearnerConfiguration featureLearnerConfiguration = null;

*/


        }

    }

   /* public LearningSystem getFromIn(File in) {
make sure:
    *  protected LearningAlgorithmsInitializationState initializationState = LearningAlgorithmsInitializationState.NOT_INITIALIZED;
    protected DatasetState datasetState = DatasetState.NO_DATA;
    protected LearningSystemTrainingState systemTrainingState = LearningSystemTrainingState.NOT_TRAINED;
    protected EvaluationState evaluationState = EvaluationState.NOT_EVALUATED;
    } */


}
