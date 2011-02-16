/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator;

import java.util.LinkedList;
import wekinator.FeatureConfiguration.Feature;

/**
 *
 * @author rebecca
 */
public abstract class MetaFeature {

    protected Feature myFeature = null;
    protected String myName = "DefaultMetaFeature";

/*    protected String readableName = "DefaultMetaFeature";

    public String getReadableName() {
        return readableName;
    } */

    public enum Type {

        DELTA_1s,
        DELTA_2,
        SMOOTH_1,
        HISTORY
    };

    public static String nameForType(Type type) {
        switch (type) {
            case DELTA_1s:
                return "1stDiff";
            case DELTA_2:
                return "2ndDiff";
            case SMOOTH_1:
                return "Smooth1";
            case HISTORY:
                return "History";
        }
        return "Other";
    }

    public abstract Type getType();

    public static MetaFeature createForType(Type type, Feature f) {
        switch (type) {
            case DELTA_1s:
                return new Delta1(f);
            case DELTA_2:
                return new Delta2(f);
            case SMOOTH_1:
                return new Smooth1(f);
            case HISTORY:
                return new History(f);
        }
        return null;

    }

    public String getOperationName() {
        return myName;
    }

    public String getFeatureName() {
        return getOperationName() + "_" + myFeature.name;
    }

    public Feature getFeature() {
        return myFeature;
    }

    public abstract double[] computeForNextFeature(double[] f, int startIndex);

    public int getSize() {
        return 1;
    }
}

class Delta1 extends MetaFeature {

    double last = 0.0;


    protected Delta1(Feature f) {
        this.myFeature = f;
        this.myName = MetaFeature.nameForType(Type.DELTA_1s);
    }

    @Override
    public Type getType() {
        return Type.DELTA_1s;
    }

    @Override
    public double[] computeForNextFeature(double[] f, int startIndex) {
        double[] val = new double[1];
        val[0] = f[startIndex] - last;
        last = f[startIndex];
        return val;
    }
}

class Delta2 extends MetaFeature {

    double lastValue = 0.0;
    double lastDiff = 0.0;

    public double[] computeForNextFeature(double[] f, int startIndex) {
        double[] val = new double[1];
        double thisDiff = f[startIndex] - lastValue;
        lastValue = f[startIndex];
        val[0] = thisDiff - lastDiff;
        lastDiff = thisDiff;
        return val;
    }

    protected Delta2(Feature f) {
        this.myFeature = f;
        this.myName = MetaFeature.nameForType(Type.DELTA_2);
    }

    @Override
    public Type getType() {
        return Type.DELTA_2;
    }
}

class Smooth1 extends MetaFeature {

    double last = 0.0;

    protected Smooth1(Feature f) {
        this.myFeature = f;
        this.myName = MetaFeature.nameForType(Type.SMOOTH_1);
    }

    @Override
    public Type getType() {
        return Type.SMOOTH_1;
    }

    @Override
    public double[] computeForNextFeature(double[] f, int startIndex) {
        double[] val = new double[1];
        val[0] = (f[startIndex] + last) * .5;
        last = f[startIndex];
        return val;
    }
}

class History extends MetaFeature {

    LinkedList<Double> history;
    public static int n = 2; //hack

    protected History(Feature f) {
         this.myFeature = f;
        this.myName = MetaFeature.nameForType(Type.HISTORY);
       // this.n = n;
        history = new LinkedList<Double>();
        initList();
    }

    public int getSize() {
        return n;
    }

    //Hack: Update list when global size is changed
    public void updateSize() {
        initList();
    }

    private void initList() {
        while (history.size() < n) {
            history.add(0.);
        }
        while (history.size() > n) {
            history.remove();
        }
    }

   /* public void setN(int n) {
        History.n = n;
        initList();
    } */

    @Override
    public Type getType() {
        return Type.HISTORY;
    }

    @Override
    public double[] computeForNextFeature(double[] f, int startIndex) {
        double[] val = new double[n];
       // val[0] = f[startIndex] - last;
        //history.remove();
        

        //history.add(f[startIndex]);
        int i = 0;
        for (Double d : history) {
            val[i] = d;
            i++;
        }
        history.removeLast();
        history.addFirst(f[startIndex]);
        return val;
    }
}



