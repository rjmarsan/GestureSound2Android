/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rebecca
 */
public class FeatureLearnerConfiguration {

    protected int numLearners = 0;
    protected int numFeatures = 0;
    protected List<int[]> featureLists = null; //Each list indicates which features are used by each learner, in order
    //e.g. [3,0,1] indicates that a learner uses global features 3, 0, and 1 in that order.

    public FeatureLearnerConfiguration(int numLearners, int numFeatures) {
        this.numLearners = numLearners;
        this.numFeatures = numFeatures;
        featureLists = new ArrayList<int[]>(numLearners);

        for (int i = 0; i < numLearners; i++) {
            int featureList[] = new int[numFeatures];
            for (int j = 0; j < numFeatures; j++) {
                featureList[j] = j;
            }
            featureLists.add(featureList);
        }
    }

    public int[] getFeatureMappingForLearner(int l) {
        return featureLists.get(l);
    }

    public void setFeatureMappingForLearner(int l, int[] featureList) throws Exception {
        if (featureList == null || featureList.length > numFeatures || l < 0 || l >= numLearners) {
            throw new Exception("Invalid feature list or index");
        }

        int[] m = new int[featureList.length];
        HashSet<Integer> used = new HashSet<Integer>();
        for (int i = 0; i < m.length; i++) {
            if (featureList[i] < 0 || featureList[i] >= numFeatures) {
                throw new Exception("Invalid featureList number " + featureList[i] + ": must be between 0 and numFeatures-1");
            }
            if (used.contains(featureList[i])) {
                throw new Exception("Invalid feature list: contains duplicate feature " + featureList[i] + " at index " + i);
            }
            used.add(featureList[i]);

            m[i] = featureList[i];
        }

        featureLists.set(l, m);
    }

    /*  public Reorder getReorderFilterForLearner(int l) {
    if (l < 0 || l >= numLearners) {
    System.out.println("Error - bad learner #");
    return null;
    }

    Reorder r = new Reorder();
    try {
    r.setAttributeIndicesArray(featureLists.get(l));
    } catch (Exception ex) {
    //Badness... means that there is an error in this class.
    Logger.getLogger(FeatureLearnerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
    }

    return r;
    } */
    public void writeToOutputStreamNew(ObjectOutputStream o) {
        try {
            o.writeInt(numLearners);
            o.writeInt(numFeatures);
            o.writeObject(featureLists);
        } catch (IOException ex) {
            Logger.getLogger(FeatureLearnerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static FeatureLearnerConfiguration loadFromInputStream(ObjectInputStream i) throws IOException, ClassNotFoundException {
        int numLearners = i.readInt();
        int numFeatures = i.readInt();
        List<int[]> featureLists = (List<int[]>) i.readObject();

        FeatureLearnerConfiguration flc = new FeatureLearnerConfiguration(numLearners, numFeatures);
        for (int j = 0; j < numLearners; j++) {
            try {
                flc.setFeatureMappingForLearner(j, featureLists.get(j));
            } catch (Exception ex) {
                Logger.getLogger(FeatureLearnerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException("Invalid feature learner configuration feature list");
            }
        }
        return flc;
    }
}
