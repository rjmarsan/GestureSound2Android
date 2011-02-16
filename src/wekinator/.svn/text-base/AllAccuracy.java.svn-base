/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AllAccuracy.java
 *
 * Created on Dec 12, 2009, 11:37:25 PM
 */
package wekinator;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;
import wekinator.LearningSystem.EvalStatus;
import wekinator.Plog.Msg;

/**
 *
 * @author rebecca
 */
public class AllAccuracy extends javax.swing.JPanel {
    //Get informed of everything by parent?

    protected LearningSystem learningSystem = null;
    protected ParameterMiniViewer[] paramPanels = null;
    protected int myParam = -1;

    public boolean isUsed = false;

    protected PropertyChangeListener learningSystemChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            learningSystemPropertyChanged(evt);
        }
    };
    
    public void setLearningSystem(LearningSystem ls) {
        System.out.println("setting learning system at " + myParam);
     //   System.out.println("null ? " + (ls == null));
        if (learningSystem == ls) {
            return;
        }
        //Remove old listeners
        if (learningSystem != null) {
            learningSystem.removePropertyChangeListener(learningSystemChangeListener);
        }

        this.learningSystem = ls;
        if (learningSystem != null) {
            learningSystem.addPropertyChangeListener(learningSystemChangeListener);
                    updateEvalStatus(ls.getEvalStatus());
                    updateParamPanelsForNumLearners();
        updateButtons();

        }

        
    }

    private void updateParamPanelsForNumLearners() {
        panelOutputs.removeAll();
        if (myParam == -1) {
            int numParams = learningSystem.getNumParams();
            paramPanels = new ParameterMiniViewer[numParams];
            SimpleDataset d = learningSystem.getDataset();
            for (int i = 0; i < numParams; i++) {
                if (d != null) {
                    paramPanels[i] = new ParameterMiniViewer(d.getParameterName(i), 0.0);
                } else {
                    paramPanels[i] = new ParameterMiniViewer("Param" + i, 0.0);
                }
                paramPanels[i].setPreferredSize(new Dimension(300, 28));
                panelOutputs.add(paramPanels[i]);
            }
        } else {
            paramPanels = new ParameterMiniViewer[1];
            SimpleDataset d = learningSystem.getDataset();
            if (d != null) {
                paramPanels[0] = new ParameterMiniViewer(d.getParameterName(myParam), 0.0);
            } else {
                paramPanels[0] = new ParameterMiniViewer("Param" + myParam, 0.0);
            }
            paramPanels[0].setPreferredSize(new Dimension(300, 28));
            panelOutputs.add(paramPanels[0]);
        }

    }

   private void updateButtons() {
        WekinatorLearningManager.Mode m = WekinatorLearningManager.getInstance().getMode();
        if (learningSystem == null) {
            System.out.println("null at " + myParam);
       //     return;
        }
        //} else {
           // System.out.println("NOT null at " + myParam);
        buttonComputeCV.setEnabled(learningSystem != null && m == WekinatorLearningManager.Mode.NONE && learningSystem.isTrainable);
        buttonComputeTrain.setEnabled(learningSystem != null && m == WekinatorLearningManager.Mode.NONE && learningSystem.isRunnable);
        buttonCancel.setEnabled(m == WekinatorLearningManager.Mode.EVALUATING);
        
       // }
        //TODO: add way to restrict cancellign to when it's me that is doing evaluating
    }

    public void updateResults(double[] results, boolean isCV) {
        if (results != null) {
             DecimalFormat dd = new DecimalFormat("#.##");
            if (myParam == -1) {
                if (results.length == paramPanels.length) {
                    for (int i = 0; i < paramPanels.length; i++) {
                        String s;
                        if (learningSystem.getLearners(i) instanceof NNLearningAlgorithm) {
                            s = " (RMS err)";
                            paramPanels[i].setValue(dd.format(results[i]) + s);
                        } else {
                            s = " %";
                            paramPanels[i].setValue(dd.format(results[i] * 100) + s);
                        }
                        
                    }
                }
            } else {
                if (results.length > myParam) {
                        String s;
                        if (learningSystem.getLearners(myParam) instanceof NNLearningAlgorithm) {
                            s = " (RMS err)";
                            paramPanels[0].setValue(dd.format(results[myParam]) + s);
                        } else {
                            s = " %";
                            paramPanels[0].setValue(dd.format(results[myParam] * 100) + s);
                        }
                }
            } 

        }
        if (isCV) {
            labelResultsHeader.setText("CV accuracy computed:");
        } else {
            labelResultsHeader.setText("Training accuracy computed: ");
        }
    }

   /* public void evaluationFinished() {
        //TODO: Also need to re-enable eval button from elsewhere.
     //   setGuiEvaluating(false);
    } */

    /** Creates new form AllAccuracy */
    public AllAccuracy() {
     //   initComponents();
     //   myParam = -1;
      //  setGuiEvaluating(false);
        this(-1);
    }

    public AllAccuracy(int paramNum) {
        System.out.println("NEW ALL ACCURACY CREATED");
            initComponents();
        myParam = paramNum;
      //  setGuiEvaluating(false);
       WekinatorLearningManager.getInstance().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                learningManagerPropertyChange(evt);
            }
        });
        updateButtons();

       WekinatorInstance.getWekinatorInstance().addPropertyChangeListener(new PropertyChangeListener() {

       public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(WekinatorInstance.PROP_LEARNINGSYSTEM)) {
                    setLearningSystem(WekinatorInstance.getWekinatorInstance().getLearningSystem());
                }

            }
        });
        setLearningSystem(WekinatorInstance.getWekinatorInstance().getLearningSystem());

    }

    private void learningManagerPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorLearningManager.PROP_MODE)) {
            updateButtons();
        }
    }

    public void setParamNum(int paramNum) {
        this.myParam = paramNum;
        updateParamPanelsForNumLearners();
    }

   /* protected void setGuiEvaluating(boolean evaluating) {
        buttonCompute.setEnabled(evaluating);
        progressBar.setIndeterminate(evaluating);
        if (evaluating) {
            labelModelStatus.setText("Evaluating accuracy...");
        } else {
            labelModelStatus.setText("");
        }
    } */

    private void learningSystemPropertyChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(LearningSystem.PROP_EVALSTATUS)) {
            updateEvalStatus(learningSystem.getEvalStatus());
            EvalStatus es = learningSystem.getEvalStatus();
            if (es.isCV) {
                double[] results = learningSystem.getCvResults();
                updateResults(results, true);
            } else {
             double[] results = learningSystem.getTrainResults();
                updateResults(results, false);
            }

        } else if (evt.getPropertyName().equals(LearningSystem.PROP_ISTRAINABLE)) {
            updateButtons();
        } else if (evt.getPropertyName().equals(LearningSystem.PROP_ISRUNNABLE)) {
            updateButtons();
        }


        /*else if (evt.getPropertyName().equals(LearningSystem.PROP_CVRESULTS)) {
            double[] results = learningSystem.getCvResults();
            updateResults(results, true);
           // for (int i = 0; i < numParams; i++) {
           //     learnerPanels[i].getAccuracyPanel().updateResults(results, true);
           // }
        } else if (evt.getPropertyName().equals(LearningSystem.PROP_TRAINRESULTS)) {
            double[] results = learningSystem.getTrainResults();
            updateResults(results, false);
        } */
    }

    private void updateEvalStatus(EvalStatus evalStatus) {
       // if (evalStatus.myParam != myParam)
       //     return;

        if (evalStatus.numModelsToEval == 0) {
            labelModelStatus.setText("");
            labelFoldStatus.setText("");
            labelErrorStatus.setText("");
            progressBar.setValue(0);
            return;
        }

        if (evalStatus.wasCancelled) {
          labelModelStatus.setText("Cancelled evaluation.");
          progressBar.setValue(0);

        } else if ((evalStatus.numModelsDone + evalStatus.numModelsWithErrors) == evalStatus.numModelsToEval) {
            labelModelStatus.setText("Finished evaluating models.");
            labelFoldStatus.setText("");
           // progressBar.setMaximum(1 + evalStatus.numModelsToEval * evalStatus.numFolds);
           // progressBar.setValue(1 + evalStatus.numModelsToEval * evalStatus.numFolds);
             progressBar.setMaximum(1 + evalStatus.numModelsToEval );
            progressBar.setValue(1 + evalStatus.numModelsToEval);

        } else {
            labelModelStatus.setText("Evaluating model " + (evalStatus.numModelsDone + evalStatus.numModelsWithErrors + 1) + " of " + evalStatus.numModelsToEval);
         /*   labelFoldStatus.setText("     Computing fold " + (evalStatus.numFoldsDone + 1) + " of " + evalStatus.numFolds);
            progressBar.setMaximum(1 + evalStatus.numModelsToEval * evalStatus.numFolds);
            progressBar.setValue(1 + (evalStatus.numModelsDone + evalStatus.numModelsWithErrors) * evalStatus.numFolds + evalStatus.numFoldsDone); */
            labelFoldStatus.setText("");
            progressBar.setMaximum(1 + evalStatus.numModelsToEval);
            progressBar.setValue(1 + (evalStatus.numModelsDone + evalStatus.numModelsWithErrors));
        }
        labelErrorStatus.setText(evalStatus.numModelsWithErrors + " models encountered errors.");
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        comboNumFolds = new javax.swing.JComboBox();
        buttonComputeTrain = new javax.swing.JButton();
        labelModelStatus = new javax.swing.JLabel();
        buttonCancel = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        labelResultsHeader = new javax.swing.JLabel();
        scrollOutputPanel = new javax.swing.JScrollPane();
        panelOutputs = new javax.swing.JPanel();
        parameterMiniViewer1 = new wekinator.ParameterMiniViewer();
        labelFoldStatus = new javax.swing.JLabel();
        labelErrorStatus = new javax.swing.JLabel();
        buttonComputeCV = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Evaluate Accuracy"));
        setMaximumSize(new java.awt.Dimension(1000, 1000));

        comboNumFolds.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2 folds", "5 folds", "10 folds" }));

        buttonComputeTrain.setText("Compute accuracy of trained model on training set");
        buttonComputeTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonComputeTrainActionPerformed(evt);
            }
        });

        labelModelStatus.setText("Computing model 1 of 15...");

        buttonCancel.setText("Cancel evaluation");
        buttonCancel.setEnabled(false);
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        labelResultsHeader.setText("Accuracy computed:");

        scrollOutputPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelOutputs.setLayout(new javax.swing.BoxLayout(panelOutputs, javax.swing.BoxLayout.Y_AXIS));
        panelOutputs.add(parameterMiniViewer1);

        scrollOutputPanel.setViewportView(panelOutputs);

        labelFoldStatus.setText("          Computing fold 2 of 10...");

        labelErrorStatus.setText("0 models encountered errors");

        buttonComputeCV.setText("Compute cross-validation accuracy");
        buttonComputeCV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonComputeCVActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(buttonComputeTrain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 358, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(scrollOutputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .add(labelFoldStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 273, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, labelErrorStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 273, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(labelModelStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 287, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(buttonComputeCV)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(comboNumFolds, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(buttonCancel)
                    .add(labelResultsHeader, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 339, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonComputeCV)
                    .add(comboNumFolds, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonComputeTrain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonCancel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(labelModelStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(labelFoldStatus)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(labelErrorStatus)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(labelResultsHeader, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(scrollOutputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonComputeTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonComputeTrainActionPerformed
        //Determine setup
       int numFolds = -1;

            //try {
                if (myParam == -1 && learningSystem != null) {
                   WekinatorLearningManager.getInstance().computeTrainingAccuracyInBackground();
                } else if (learningSystem != null) {
                    WekinatorLearningManager.getInstance().computeTrainingAccuracyInBackground(myParam);
                }
             //   setGuiEvaluating(true);
            //} catch (Exception ex) {
            //    Logger.getLogger(AllAccuracy.class.getName()).log(Level.SEVERE, null, ex);
           // }
           // setGuiEvaluating(true);
        

}//GEN-LAST:event_buttonComputeTrainActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        WekinatorLearningManager.getInstance().stopEvaluating();
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.BUTTON_EVAL_CANCELLED);
        }
}//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonComputeCVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonComputeCVActionPerformed
            int numFolds = 10;
            if (comboNumFolds.getSelectedIndex() == 0) {
                numFolds = 2;
            } else if (comboNumFolds.getSelectedIndex() == 1) {
                numFolds = 5;
            } else if (comboNumFolds.getSelectedIndex() == 2) {
                numFolds = 10;
            }
           // try {
                if (myParam == -1) {
                    if (learningSystem != null) {
                     //   learningSystem.computeCVAccuracyInBackground(numFolds);
                     WekinatorLearningManager.getInstance().computeCVAccuracyInBackground(numFolds);

                    }
                } else {
                    if (learningSystem != null) {
                       WekinatorLearningManager.getInstance().computeCVAccuracyInBackground(myParam, numFolds);
                    }
                }
               // setGuiEvaluating(true);
           // } catch (Exception ex) {
           //     Logger.getLogger(AllAccuracy.class.getName()).log(Level.SEVERE, null, ex);
           // }

}//GEN-LAST:event_buttonComputeCVActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonComputeCV;
    private javax.swing.JButton buttonComputeTrain;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox comboNumFolds;
    private javax.swing.JLabel labelErrorStatus;
    private javax.swing.JLabel labelFoldStatus;
    private javax.swing.JLabel labelModelStatus;
    private javax.swing.JLabel labelResultsHeader;
    private javax.swing.JPanel panelOutputs;
    private wekinator.ParameterMiniViewer parameterMiniViewer1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane scrollOutputPanel;
    // End of variables declaration//GEN-END:variables
}
