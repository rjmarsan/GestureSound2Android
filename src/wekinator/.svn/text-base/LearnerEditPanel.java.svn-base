/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LearnerEditPanel.java
 *
 * Created on Dec 13, 2009, 4:33:32 PM
 */
package wekinator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import wekinator.LearningAlgorithms.AdaboostM1LearningAlgorithm;
import wekinator.LearningAlgorithms.ClassifierLearningAlgorithm;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.LearningAlgorithm.TrainingState;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;
import wekinator.Plog.Msg;
import wekinator.util.*;

/**
 *
 * @author rebecca
 */
public class LearnerEditPanel extends javax.swing.JPanel {

    protected int paramNum = 0;
    protected LearningSystem ls = null;
    protected LearningAlgorithm al = null;
    protected ChangeListener learningSystemChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            learningSystemLearnersChanged(e);
        }
    };
    protected PropertyChangeListener lsPropertyChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            learningSystemPropertyChanged(evt);
        }
    };
    protected PropertyChangeListener learningAlgChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            learningAlgorithmPropertyChanged(evt);
        }
    };

    public AllAccuracy getAccuracyPanel() {
        return accuracyPanel;
    }

    /** Creates new form LearnerEditPanel */
    public LearnerEditPanel() {
        initComponents();
        accuracyPanel.isUsed = true;
        WekinatorInstance.getWekinatorInstance().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                wekinatorInstPropertyChange(evt);
            }
        });
        setLearningSystem(WekinatorInstance.getWekinatorInstance().getLearningSystem());
    }

    private void wekinatorInstPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorInstance.PROP_LEARNINGSYSTEM)) {
            setLearningSystem(WekinatorInstance.getWekinatorInstance().getLearningSystem());
        }
    }

    public LearnerEditPanel(LearningSystem ls, int paramNum) {
        initComponents();
        setLearningSystemAndNumber(ls, paramNum);

    }

    public void setLearningSystem(LearningSystem ls) {
        if (this.ls == ls) {
            return;
        }

        if (this.ls != null) {
            this.ls.removeLearnerChangeListener(learningSystemChangeListener);
            ls.removePropertyChangeListener(lsPropertyChangeListener);
            if (al != null) {
                al.removePropertyChangeListener(learningAlgChangeListener);
            }
        }

        this.ls = ls;
        if (ls != null) {
            this.al = ls.getLearners(paramNum);
            ls.addLearnerChangeListener(learningSystemChangeListener);
            ls.addPropertyChangeListener(lsPropertyChangeListener);
            if (al != null) {
                al.addPropertyChangeListener(learningAlgChangeListener);
            }
        } else {
            al = null;
        }
        updateGuiForAlgorithm();
    }

    public void setLearningSystemAndNumber(LearningSystem ls, int paramNum) {
        this.paramNum = paramNum;
        accuracyPanel.setParamNum(paramNum);
        setLearningSystem(ls);
    }

    protected void updateGuiForAlgorithm() {
        System.out.println("updating gui");
        labelLearnerDescription.setText(al.getName()); //TODO: + ", using " + "AAA features");
        panelLearnerSettings.removeAll();
        JPanel p = al.getSettingsPanel().getPanel();
        panelLearnerSettings.add(p);
        checkNNGui.setVisible(al instanceof NNLearningAlgorithm);
        panelLearnerSettings.repaint();
        updateForTrainingStatus();
    }

    private void learningSystemPropertyChanged(PropertyChangeEvent evt) {
        //TODO: respond to learning system changes...?
        //   System.out.println("here");
    }

    private void learningSystemLearnersChanged(ChangeEvent e) {
        System.out.println("updating learner edit panel " + paramNum);
        LearningAlgorithm newal = ls.getLearners(paramNum);
        if (al != null) {
            al.removePropertyChangeListener(learningAlgChangeListener);
        }
        if (newal != null) {
            newal.addPropertyChangeListener(learningAlgChangeListener);
        }

        al = newal;
        updateGuiForAlgorithm();

    }

    private void updateForTrainingStatus() {
        TrainingState state = al.getTrainingState();
        if (state == TrainingState.TRAINING) {
            buttonRetrain.setEnabled(false);
            labelTrainingStatus.setText("Training...");

        } else {
            buttonRetrain.setEnabled(true);
            if (state == TrainingState.NOT_TRAINED) {
                labelTrainingStatus.setText("Not trained");
            } else if (state == TrainingState.TRAINED) {
                labelTrainingStatus.setText("Trained");
            //}// else if (state == TrainingState.ERROR) {
            //    labelTrainingStatus.setText("Encountered error while training");
            } else {
                labelTrainingStatus.setText("Unknown training status.");
            }

        }

    }

    private void learningAlgorithmPropertyChanged(PropertyChangeEvent evt) {
        String s = evt.getPropertyName();
        if (s.equals(LearningAlgorithm.PROP_TRAININGSTATE)) {
            updateForTrainingStatus();
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        labelLearnerDescription = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        buttonRetrain = new javax.swing.JButton();
        buttonSaveLearner = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        checkNNGui = new javax.swing.JCheckBox();
        panelLearnerSettings = new javax.swing.JPanel();
        sMOSettingsPanel1 = new wekinator.LearningAlgorithms.SMOSettingsPanel();
        labelTrainingStatus = new javax.swing.JLabel();
        buttonSaveLearner1 = new javax.swing.JButton();
        accuracyPanel = new wekinator.AllAccuracy();

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Edit model settings"));

        labelLearnerDescription.setText("Using kNN, 3 features");

        jButton15.setText("Change model type or load...");
        jButton15.setEnabled(false);

        buttonRetrain.setText("Apply these settings");
        buttonRetrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRetrainActionPerformed(evt);
            }
        });

        buttonSaveLearner.setText("Save learner...");
        buttonSaveLearner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveLearnerActionPerformed(evt);
            }
        });

        jButton1.setText("Undo changes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        checkNNGui.setText("View NN GUIs");
        checkNNGui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNNGuiActionPerformed(evt);
            }
        });

        panelLearnerSettings.setLayout(new javax.swing.BoxLayout(panelLearnerSettings, javax.swing.BoxLayout.LINE_AXIS));
        panelLearnerSettings.add(sMOSettingsPanel1);

        labelTrainingStatus.setText("jLabel1");

        buttonSaveLearner1.setText("Export classifier...");
        buttonSaveLearner1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveLearner1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labelLearnerDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 344, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(buttonRetrain)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton1))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(checkNNGui)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(labelTrainingStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(panelLearnerSettings, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 326, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(buttonSaveLearner)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(buttonSaveLearner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .add(labelLearnerDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonSaveLearner)
                    .add(buttonSaveLearner1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelLearnerSettings, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(buttonRetrain))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(checkNNGui)
                    .add(labelTrainingStatus))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, accuracyPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(accuracyPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRetrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRetrainActionPerformed
        try {
            al.getSettingsPanel().applySettings();
            if (al instanceof NNLearningAlgorithm) {
                ((NNLearningAlgorithm) al).setUseGui(checkNNGui.isSelected());
            }
            if (WekinatorRunner.isLogging()) {
                Plog.log(Msg.LEARNER_SETTINGS_EDITED, "" + paramNum);
                Plog.log(Msg.LEARNER_SETTINGS_NEW_VALUES, al.getSettingsDescription());
            }
        //  WekinatorLearningManager.getInstance().startTraining(paramNum);
        } catch (Exception ex) {
            Logger.getLogger(LearnerEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
}//GEN-LAST:event_buttonRetrainActionPerformed

    private void buttonSaveLearnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveLearnerActionPerformed
        //TODO: Show file chooser
        /*File f = new File("test.out");
        try {
        al.writeToFile(f);
        } catch (Exception ex) {
        //TODO: display warning popup
        Logger.getLogger(LearnerEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        } */
        File file = Util.findSaveFile(LearningAlgorithm.getFileExtension(),
                LearningAlgorithm.getFileTypeDescription(),
                LearningAlgorithm.getDefaultLocation(),
                this);
        if (file != null) {
            try {
                al.writeToFile(file); //TODOTODOTODO: update last path on this.
                Util.setLastFile(LearningAlgorithm.getFileExtension(), file);
                if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.LEARNING_ALGORITHM_SAVED, file.getAbsolutePath() + "/" + file.getName());
                }
            } catch (Exception ex) {
                Logger.getLogger(LearnerEditPanel.class.getName()).log(Level.INFO, null, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not save to file", JOptionPane.ERROR_MESSAGE);
            }
        }
}//GEN-LAST:event_buttonSaveLearnerActionPerformed

    private void checkNNGuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNNGuiActionPerformed
}//GEN-LAST:event_checkNNGuiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        al.getSettingsPanel().reset();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonSaveLearner1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveLearner1ActionPerformed
       if (al instanceof ClassifierLearningAlgorithm) {
        File file = Util.findSaveFile("classifier",
                "Serialized weka classifier",
                LearningAlgorithm.getDefaultLocation(),
                this);
        if (file != null) {
            try {
                ((ClassifierLearningAlgorithm)al).saveAsSerializedWekaClassifier(file);
                Util.setLastFile(LearningAlgorithm.getFileExtension(), file);
                if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.LEARNING_ALGORITHM_SAVED, file.getAbsolutePath() + "/" + file.getName());
                }
            } catch (Exception ex) {
                Logger.getLogger(LearnerEditPanel.class.getName()).log(Level.INFO, null, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not save to file", JOptionPane.ERROR_MESSAGE);
            }
        }
       }
    }//GEN-LAST:event_buttonSaveLearner1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private wekinator.AllAccuracy accuracyPanel;
    private javax.swing.JButton buttonRetrain;
    private javax.swing.JButton buttonSaveLearner;
    private javax.swing.JButton buttonSaveLearner1;
    private javax.swing.JCheckBox checkNNGui;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton15;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelLearnerDescription;
    private javax.swing.JLabel labelTrainingStatus;
    private javax.swing.JPanel panelLearnerSettings;
    private wekinator.LearningAlgorithms.SMOSettingsPanel sMOSettingsPanel1;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        JFrame f = new JFrame();
        ChuckSystem.getChuckSystem().setNumParams(1);

        LearningSystem ls = new LearningSystem(1);
        ls.setLearners(0, new AdaboostM1LearningAlgorithm());
        LearnerEditPanel p = new LearnerEditPanel(ls, 0);
        f.add(p);
        f.setVisible(true);


    }
}
