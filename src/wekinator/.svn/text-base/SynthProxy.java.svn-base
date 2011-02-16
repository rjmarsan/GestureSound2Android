/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator;

/**
 *
 * @author rebecca
 */
public class SynthProxy {
    protected static final OscSynthProxy ref = new OscSynthProxy();

    static int getNumParams() {
     if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().getNumParams();
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getNumParams();
        }
    }

    static boolean isParamDiscrete(int i) {
        if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().isIsParamDiscrete(i);
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getIsDiscrete()[i];
        }
    }

    static boolean[] isParamDiscretes() {
    if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().isIsParamDiscrete();
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getIsDiscrete();
        }
    }

    static boolean isParamDistribution(int i) {
        if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().isDoesParamUseDistribution(i);
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getIsDistribution()[i];
        }
    }

    static boolean[] isParamDistribution() {
        if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().isDoesParamUseDistribution();
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getIsDistribution();
        }
    }

    static int paramMaxValue(int i) {
        if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().getNumSynthMaxParamVals(i);
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getMaxValue()[i];
        }
    }

    static int[] paramMaxValues() {
        if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().getNumSynthMaxParamVals();
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getMaxValue();
        }
    }

    static String paramName(int i) {
        if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().getParamNames(i);
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getParamNames()[i];
        }
    }

        static String[] paramNames() {
        if (WekinatorInstance.getWekinatorInstance().getConfiguration().isUseChuckSynthClass()) {
            return ChuckSystem.getChuckSystem().getParamNames();
        } else {
            return WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration().getParamNames();
        }
    }
}
