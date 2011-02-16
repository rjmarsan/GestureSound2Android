/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TrainPanel.java
 *
 * Created on Dec 6, 2009, 9:54:58 PM
 */
package wekinator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;
import wekinator.LearningSystem.TrainingStatus;
import wekinator.Plog.Msg;

/**
 *
 * @author rebecca
 */
public class TrainPanel extends javax.swing.JPanel {

    protected LearningSystem learningSystem = null;
    protected JCheckBox[] learnerBoxes = null;

    protected PropertyChangeListener learningSystemChangeListener = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            learningSystemPropertyChanged(evt);
        }
    };

    private ChangeListener learnerChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            learningAlgorithmsChanged(e);
        }
    };

    private void learningAlgorithmsChanged(ChangeEvent e) {
            updateGuiForNN();
    }

    private void learningSystemPropertyChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(LearningSystem.PROP_TRAININGPROGRESS)) {
            updateTrainingProgress(learningSystem.getTrainingProgress());
        } else if (evt.getPropertyName().equals(LearningSystem.PROP_ISTRAINABLE)) {
            updateButtons();
        }
    }

    /** Creates new form TrainPanel */
    public TrainPanel() {
        initComponents();
        WekinatorLearningManager.getInstance().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                learningManagerPropertyChange(evt);
            }
        });
    }

    private void updateButtons() {
        WekinatorLearningManager.Mode m = WekinatorLearningManager.getInstance().getMode();
        buttonTrain.setEnabled(m == WekinatorLearningManager.Mode.NONE && learningSystem.isIsTrainable());
        buttonSelectModels.setVisible(learningSystem != null && learningSystem.getNumParams() > 1);
        buttonUntrain.setEnabled(m == WekinatorLearningManager.Mode.NONE && learningSystem != null && learningSystem.isIsRunnable());
        buttonCancelTrain.setEnabled(m == WekinatorLearningManager.Mode.TRAINING);
    }

    private void learningManagerPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorLearningManager.PROP_MODE)) {
            updateButtons();
        } else if (evt.getPropertyName().equals(WekinatorLearningManager.PROP_TRAININGMASK)) {
            updateGuiForEnabled();
        }
    }

    private int computeNumLearnersSelected() {
        int n = 0;
        if (learnerBoxes == null) {
            return n;
        }

        for (int i = 0; i < learnerBoxes.length; i++) {
            if (learnerBoxes[i].isSelected()) {
                n++;
            }
        }
        return n;
    }

    private void tryUpdateSelectionFrameForTrainingMask() {
        boolean[] mask = WekinatorLearningManager.getInstance().getTrainingMask();
        if (mask != null && learningSystem != null && mask.length == learningSystem.getNumParams()) {
            for (int i = 0; i < mask.length; i++) {
                learnerBoxes[i].setEnabled(mask[i]);
            }
        }
    }

    private void updateLearnerSelectionFrameForLearningSystem() {
        panelFeatures.removeAll();
//        FeatureConfiguration fc = WekinatorLearningManager.getInstance().getFeatureConfiguration();
        if (learningSystem != null) {
            String[] parameterNames = learningSystem.getDataset().getParameterNames();
            if (parameterNames != null) {
                learnerBoxes = new JCheckBox[parameterNames.length];
                for (int i = 0; i < parameterNames.length; i++) {
                    JPanel next = new JPanel();
                    // JLabel nextName= new JLabel(featureNames[i]);
                    // JCheckBox tmp = new JCheckBox
                    learnerBoxes[i] = new JCheckBox(parameterNames[i], true);
                    next.setAlignmentX(1.0F);
                    learnerBoxes[i].setAlignmentX(0.0F);
                    learnerBoxes[i].setAlignmentY(0.0F);

                    next.add(learnerBoxes[i]);

                    //next.setAl
                    panelFeatures.add(next);

                }
            } else {
                System.out.println("log: error: param names null");


            }
            tryUpdateSelectionFrameForTrainingMask();
        }
        //if (featureM.isVisible()) {
        learnerMaskFrame.setSize(500, 300);
        learnerMaskFrame.repaint();
    //}

    }

    void setLearningSystem(LearningSystem ls) {
        if (learningSystem == ls) {
            return;
        }
        //Remove old listeners
        if (learningSystem != null) {
            learningSystem.removePropertyChangeListener(learningSystemChangeListener);
            learningSystem.removeLearnerChangeListener(learnerChangeListener);
        }

        this.learningSystem = ls;

        if (ls != null) {
        learningSystem.addPropertyChangeListener(learningSystemChangeListener);
        learningSystem.addLearnerChangeListener(learnerChangeListener);

        updateLearnerSelectionFrameForLearningSystem();


        updateGuiForEnabled();
        updateGuiForNN();
        updateLearnersForNN();
        updateButtons();
        updateTrainingProgress(learningSystem.getTrainingProgress());
        }
    }

    protected void updateLearnersForNN() {
        LearningAlgorithm[] algs = learningSystem.getLearners();
        for (int i = 0; i < algs.length; i++) {
            if (algs[i] instanceof NNLearningAlgorithm) {
                ((NNLearningAlgorithm) algs[i]).setUseGui(checkNNGui.isSelected());
            }
        }
    }

    public boolean isUseNNGui() {
        return checkNNGui.isSelected();
    }

    protected void updateGuiForEnabled() {
        boolean[] mask = WekinatorLearningManager.getInstance().getTrainingMask();
        if (mask == null || mask.length == 0) {
            labelSelected.setText("No models to train");
            return;
        }

        int numEnabled = 0;
        for (int i = 0; i < mask.length; i++) {
            if (mask[i]) {
                numEnabled++;
            }
        }

        labelSelected.setText(numEnabled + " of " + mask.length + " will be trained");
    }

    protected void updateGuiForNN() {
        int numNNs = 0;
        for (int i = 0; i < learningSystem.getNumParams(); i++) {
            if (learningSystem.getLearners(i) instanceof NNLearningAlgorithm) {
                numNNs++;
            }
        }
        checkNNGui.setVisible(numNNs > 0);

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
        learnerMaskFrame = new javax.swing.JFrame();
        scrollPaneFeatures = new javax.swing.JScrollPane();
        panelFeatures = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jCheckBox9 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jCheckBox10 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        jCheckBox12 = new javax.swing.JCheckBox();
        jPanel15 = new javax.swing.JPanel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jPanel17 = new javax.swing.JPanel();
        jCheckBox15 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonOK = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        labelTrainingStatus1 = new javax.swing.JLabel();
        buttonCancelTrain = new javax.swing.JButton();
        labelTrainingStatus2 = new javax.swing.JLabel();
        labelTrainingStatus3 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        buttonTrain = new javax.swing.JButton();
        checkNNGui = new javax.swing.JCheckBox();
        buttonSelectModels = new javax.swing.JButton();
        labelSelected = new javax.swing.JLabel();
        buttonUntrain = new javax.swing.JButton();

        learnerMaskFrame.setTitle("Select models to be trained");

        scrollPaneFeatures.setMinimumSize(new java.awt.Dimension(100, 100));

        panelFeatures.setLayout(new java.awt.GridLayout(0, 1));

        jPanel4.setAlignmentX(0.0F);

        jCheckBox3.setText("jCheckBox2");
        jCheckBox3.setAlignmentX(0.5F);
        jCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox3)
        );

        panelFeatures.add(jPanel4);

        jPanel5.setAlignmentX(0.0F);

        jCheckBox4.setText("jCheckBox2");
        jCheckBox4.setAlignmentX(0.5F);
        jCheckBox4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox4)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox4)
        );

        panelFeatures.add(jPanel5);

        jPanel6.setAlignmentX(0.0F);

        jCheckBox5.setText("jCheckBox2");
        jCheckBox5.setAlignmentX(0.5F);
        jCheckBox5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox5)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox5)
        );

        panelFeatures.add(jPanel6);

        jPanel7.setAlignmentX(0.0F);

        jCheckBox6.setText("jCheckBox2");
        jCheckBox6.setAlignmentX(0.5F);
        jCheckBox6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox6)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox6)
        );

        panelFeatures.add(jPanel7);

        jPanel9.setAlignmentX(0.0F);

        jCheckBox7.setText("jCheckBox2");
        jCheckBox7.setAlignmentX(0.5F);
        jCheckBox7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox7)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox7)
        );

        panelFeatures.add(jPanel9);

        jPanel10.setAlignmentX(0.0F);

        jCheckBox8.setText("jCheckBox2");
        jCheckBox8.setAlignmentX(0.5F);
        jCheckBox8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox8)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox8)
        );

        panelFeatures.add(jPanel10);

        jPanel11.setAlignmentX(0.0F);

        jCheckBox9.setText("jCheckBox2");
        jCheckBox9.setAlignmentX(0.5F);
        jCheckBox9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox9ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox9)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox9)
        );

        panelFeatures.add(jPanel11);

        jPanel12.setAlignmentX(0.0F);

        jCheckBox10.setText("jCheckBox2");
        jCheckBox10.setAlignmentX(0.5F);
        jCheckBox10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox10)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox10)
        );

        panelFeatures.add(jPanel12);

        jPanel13.setAlignmentX(0.0F);

        jCheckBox11.setText("jCheckBox2");
        jCheckBox11.setAlignmentX(0.5F);
        jCheckBox11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox11)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox11)
        );

        panelFeatures.add(jPanel13);

        jPanel14.setAlignmentX(0.0F);

        jCheckBox12.setText("jCheckBox2");
        jCheckBox12.setAlignmentX(0.5F);
        jCheckBox12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox12)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox12)
        );

        panelFeatures.add(jPanel14);

        jPanel15.setAlignmentX(0.0F);

        jCheckBox13.setText("jCheckBox2");
        jCheckBox13.setAlignmentX(0.5F);
        jCheckBox13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox13)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox13)
        );

        panelFeatures.add(jPanel15);

        jPanel16.setAlignmentX(0.0F);

        jCheckBox14.setText("jCheckBox2");
        jCheckBox14.setAlignmentX(0.5F);
        jCheckBox14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox14ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox14)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox14)
        );

        panelFeatures.add(jPanel16);

        jPanel17.setAlignmentX(0.0F);

        jCheckBox15.setText("jCheckBox2");
        jCheckBox15.setAlignmentX(0.5F);
        jCheckBox15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox15ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel17Layout = new org.jdesktop.layout.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox15)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox15)
        );

        panelFeatures.add(jPanel17);

        scrollPaneFeatures.setViewportView(panelFeatures);

        jButton3.setText("Select All");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Select none");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonOK.setText("OK");
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout learnerMaskFrameLayout = new org.jdesktop.layout.GroupLayout(learnerMaskFrame.getContentPane());
        learnerMaskFrame.getContentPane().setLayout(learnerMaskFrameLayout);
        learnerMaskFrameLayout.setHorizontalGroup(
            learnerMaskFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(learnerMaskFrameLayout.createSequentialGroup()
                .addContainerGap()
                .add(learnerMaskFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(learnerMaskFrameLayout.createSequentialGroup()
                        .add(buttonCancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(buttonOK))
                    .add(learnerMaskFrameLayout.createSequentialGroup()
                        .add(jButton3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton4))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, scrollPaneFeatures, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
                .addContainerGap())
        );
        learnerMaskFrameLayout.setVerticalGroup(
            learnerMaskFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, learnerMaskFrameLayout.createSequentialGroup()
                .addContainerGap()
                .add(scrollPaneFeatures, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(learnerMaskFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton3)
                    .add(jButton4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(learnerMaskFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonCancel)
                    .add(buttonOK)))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Training Progress"));

        labelTrainingStatus1.setText("Training model 3 of 4...");

        buttonCancelTrain.setText("Cancel");
        buttonCancelTrain.setEnabled(false);
        buttonCancelTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelTrainActionPerformed(evt);
            }
        });

        labelTrainingStatus2.setText("2 models trained");

        labelTrainingStatus3.setText("0 errors encountered");

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labelTrainingStatus1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(labelTrainingStatus3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel8Layout.createSequentialGroup()
                                .add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(18, 18, 18)
                                .add(buttonCancelTrain))
                            .add(labelTrainingStatus2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 360, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(labelTrainingStatus1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(labelTrainingStatus2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(labelTrainingStatus3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(buttonCancelTrain)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(12, 12, 12))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Train models"));

        buttonTrain.setText("Train now");
        buttonTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTrainActionPerformed(evt);
            }
        });

        checkNNGui.setText("View NN GUIs");
        checkNNGui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNNGuiActionPerformed(evt);
            }
        });

        buttonSelectModels.setText("Select models to train...");
        buttonSelectModels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSelectModelsActionPerformed(evt);
            }
        });

        labelSelected.setText("None selected");

        buttonUntrain.setText("Untrain these models");
        buttonUntrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUntrainActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                        .add(labelSelected, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(checkNNGui))
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(buttonSelectModels))
                    .add(buttonTrain, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(buttonUntrain)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(checkNNGui)
                    .add(labelSelected, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonSelectModels)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonTrain, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(buttonUntrain))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonCancelTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelTrainActionPerformed
       WekinatorLearningManager.getInstance().stopTraining();
       if (WekinatorRunner.isLogging()) {
           Plog.trainCancelled();
       }
}//GEN-LAST:event_buttonCancelTrainActionPerformed

    private void jCheckBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox7ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox7ActionPerformed

    private void jCheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jCheckBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox9ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox9ActionPerformed

    private void jCheckBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox10ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox10ActionPerformed

    private void jCheckBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox11ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox11ActionPerformed

    private void jCheckBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox12ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox12ActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jCheckBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox14ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox14ActionPerformed

    private void jCheckBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox15ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jCheckBox15ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (learnerBoxes == null) {
            return;
        }

        for (int i = 0; i < learnerBoxes.length; i++) {
            learnerBoxes[i].setSelected(true);
        }
}//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (learnerBoxes == null) {
            return;
        }

        for (int i = 0; i < learnerBoxes.length; i++) {
            learnerBoxes[i].setSelected(false);
        }
}//GEN-LAST:event_jButton4ActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        boolean[] mask = WekinatorLearningManager.getInstance().getTrainingMask();
        if (learnerBoxes == null || learnerBoxes.length == 0 || mask == null || learnerBoxes.length != mask.length) {
            learnerMaskFrame.setVisible(false);
            return;
        }

        for (int i = 0; i < learnerBoxes.length; i++) {
            learnerBoxes[i].setSelected(mask[i]);
        }
        learnerMaskFrame.setVisible(false);
    }//GEN-LAST:event_buttonCancelActionPerformed

    //TODO: what if # params can change while this is open?
    private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
        if (learnerBoxes == null) {
            learnerMaskFrame.setVisible(false);
            return;
        }
        int numSelected = computeNumLearnersSelected();
        if (numSelected == 0) {
            JOptionPane.showMessageDialog(this, "Must select at least one parameter to train", "Error", JOptionPane.ERROR_MESSAGE);
            learnerMaskFrame.toFront();
            return;
        }
        boolean[] newMask = new boolean[learnerBoxes.length];

        for (int i = 0; i < learnerBoxes.length; i++) {
            newMask[i] = learnerBoxes[i].isSelected();
        }
        learnerMaskFrame.setVisible(false);
        WekinatorLearningManager.getInstance().setTrainingMask(newMask);
    // updateGuiForEnabled();
    }//GEN-LAST:event_buttonOKActionPerformed

    private void buttonSelectModelsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSelectModelsActionPerformed
        learnerMaskFrame.setVisible(true);
        learnerMaskFrame.toFront();
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.BUTTON_TRAIN_MODEL_SELECT);
        }
    }//GEN-LAST:event_buttonSelectModelsActionPerformed

    private void checkNNGuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkNNGuiActionPerformed
        updateLearnersForNN();
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.NN_GUI_PREF_SET, "set=" + checkNNGui.isSelected());
        }
    }//GEN-LAST:event_checkNNGuiActionPerformed

    private void buttonTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTrainActionPerformed
        //  learningSystem.setLearnersEnabled(learnerSelected);
        WekinatorLearningManager.getInstance().startTraining();
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.BUTTON_TRAIN_HIT);
        }
    }//GEN-LAST:event_buttonTrainActionPerformed

    private void buttonUntrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUntrainActionPerformed
        learningSystem.forget();
        if (WekinatorRunner.isLogging()) {
            if (WekinatorRunner.isLogging()) {
                Plog.log(Msg.BUTTON_UNTRAIN);
            }
        }
}//GEN-LAST:event_buttonUntrainActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonCancelTrain;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonOK;
    private javax.swing.JButton buttonSelectModels;
    private javax.swing.JButton buttonTrain;
    private javax.swing.JButton buttonUntrain;
    private javax.swing.JCheckBox checkNNGui;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel labelSelected;
    private javax.swing.JLabel labelTrainingStatus1;
    private javax.swing.JLabel labelTrainingStatus2;
    private javax.swing.JLabel labelTrainingStatus3;
    private javax.swing.JFrame learnerMaskFrame;
    private javax.swing.JPanel panelFeatures;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JScrollPane scrollPaneFeatures;
    // End of variables declaration//GEN-END:variables

    private void updateTrainingProgress(TrainingStatus trainingProgress) {
        if (trainingProgress.numToTrain == 0) {
            labelTrainingStatus1.setText("");
            labelTrainingStatus2.setText("");
            labelTrainingStatus3.setText("");
            progressBar.setValue(0);
            return;
        }

        if (trainingProgress.wasCancelled) {
          labelTrainingStatus1.setText("Cancelled training models.");
          progressBar.setValue(0);

        } else if ((trainingProgress.numTrained + trainingProgress.numErrorsEncountered) == trainingProgress.numToTrain) {
            labelTrainingStatus1.setText("Finished training models.");
            progressBar.setMaximum(1 + trainingProgress.numToTrain);
            progressBar.setValue(1 + trainingProgress.numToTrain);
        } else {
            labelTrainingStatus1.setText("Training model " + (trainingProgress.numTrained + trainingProgress.numErrorsEncountered + 1) + " of " + trainingProgress.numToTrain);
            progressBar.setMaximum(1 + trainingProgress.numToTrain);
            progressBar.setValue(1 + trainingProgress.numTrained + trainingProgress.numErrorsEncountered);
        }
        labelTrainingStatus2.setText(trainingProgress.numTrained + " models successfully trained.");
        labelTrainingStatus3.setText(trainingProgress.numErrorsEncountered + " models encountered errors.");
    }
}
