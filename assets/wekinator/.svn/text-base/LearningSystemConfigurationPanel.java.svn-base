/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LearningSystemConfigurationPanel.java
 *
 * Created on Dec 2, 2009, 9:06:13 PM
 */
package wekinator;

import wekinator.LearningAlgorithms.LearningAlgorithm;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;
import wekinator.util.Util;

/**
 *
 * @author rebecca
 */
public class LearningSystemConfigurationPanel extends javax.swing.JPanel {

    protected int numParams = 0;
    protected String[] paramNames = new String[0];
    protected boolean[] isParamDiscrete = new boolean[0];
    protected int[] maxNumParamVals = new int[0];
    protected FeatureConfiguration featureConfiguration = null;
    protected LearningSystem learningSystem = null;
    protected Logger logger = Logger.getLogger(LearningSystemConfigurationPanel.class.getName());
    protected MainGUI mainGui = null;

    //Call this when chuck system set & reset (this panel can be persistant)
    public void configure(int numParams, String[] paramNames,
            boolean[] isParamDiscrete, int[] maxNumParamVals, FeatureConfiguration featureConfiguration) {
        this.numParams = numParams;
        this.paramNames = paramNames;
        this.maxNumParamVals = maxNumParamVals;
        this.isParamDiscrete = isParamDiscrete;
        this.featureConfiguration = featureConfiguration;
        this.setLearningSystem(new LearningSystem(numParams));
    }

    public void setMainGUI(MainGUI m) {
        mainGui = m;
    }
    public LearningSystem getLearningSystem() {
        return learningSystem;
    }

    public void setLearningSystem(LearningSystem learningSystem) {
        // if (this.learningSystem != null) {
        //     this.learningSystem.removePropertyChangeListener(learningSystemChangeListener);
        // }
        this.learningSystem = learningSystem;
        // if (this.learningSystem != null) {
        //     this.learningSystem.addPropertyChangeListener(learningSystemChangeListener);
        // }
        setFormFromLearningSystem(); //should update learner for each parameter
    }

    /** Creates new form LearningSystemConfigurationPanel */
    public LearningSystemConfigurationPanel() {
        initComponents();
        setLearningSystem(null);
        //For now:
        paneTabSimpleAdvanced.setSelectedIndex(1);
        ChuckSystem.getChuckSystem().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                chuckSystemUpdated(evt);
            }
        });

    }

    private void chuckSystemUpdated(PropertyChangeEvent evt) {
        ChuckSystem cs = ChuckSystem.getChuckSystem();
        if (evt.getPropertyName().equals(ChuckSystem.PROP_STATE)) {
            if (evt.getOldValue() != ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID && evt.getNewValue() == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID) {
                if (!WekinatorInstance.getWekinatorInstance().getConfiguration().isUseOscSynth()) {

                this.configure(SynthProxy.getNumParams(),
                        SynthProxy.paramNames(),
                        SynthProxy.isParamDiscretes(),
                        SynthProxy.paramMaxValues(),
                        WekinatorInstance.getWekinatorInstance().getFeatureConfiguration());
                } else {
                   OscSynthConfiguration config = WekinatorInstance.getWekinatorInstance().getConfiguration().getOscSynthConfiguration();
                   this.configure(config.getNumParams(), config.getParamNames(), config.isDiscrete, config.getMaxValue(), WekinatorInstance.getWekinatorInstance().getFeatureConfiguration());
                }
            }
        }
    }

    protected DatasetLoadingPanel myDatasetPanel = null;
    protected LearningAlgorithmConfigurationPanel[] myAlgorithmPanels = new LearningAlgorithmConfigurationPanel[0];

    private File findLearningSystemFile() {
        JFileChooser fc = new JFileChooser();
        String location = WekinatorInstance.getWekinatorInstance().getSettings().getLastLocation(LearningSystem.getFileExtension());
        if (location == null || location.equals("")) {
            location = LearningSystem.getDefaultLocation();
        }
        fc.setCurrentDirectory(new File(location)); //TODO: Could set directory vs file here according to above results
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        boolean success = true;
        File file = null;
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }

        if (file != null) {
            WekinatorInstance.getWekinatorInstance().getSettings().setLastLocation(LearningSystem.getFileExtension(), Util.getCanonicalPath(file));
        }
        return file;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paneTabSimpleAdvanced = new javax.swing.JTabbedPane();
        panelSimpleTab = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelAdvancedTab = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelAdvancedParent = new javax.swing.JPanel();
        datasetLoadingPanel1 = new wekinator.DatasetLoadingPanel();
        learningAlgorithmConfigurationPanel2 = new wekinator.LearningAlgorithmConfigurationPanel();
        learningAlgorithmConfigurationPanel1 = new wekinator.LearningAlgorithmConfigurationPanel();
        buttonLoad = new javax.swing.JButton();
        buttonUndo = new javax.swing.JButton();
        buttonGo = new javax.swing.JButton();
        labelLearningSystemStatus = new javax.swing.JLabel();

        paneTabSimpleAdvanced.setEnabled(false);

        panelSimpleTab.setEnabled(false);

        jLabel1.setText("Coming soon.");

        org.jdesktop.layout.GroupLayout panelSimpleTabLayout = new org.jdesktop.layout.GroupLayout(panelSimpleTab);
        panelSimpleTab.setLayout(panelSimpleTabLayout);
        panelSimpleTabLayout.setHorizontalGroup(
            panelSimpleTabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSimpleTabLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addContainerGap(387, Short.MAX_VALUE))
        );
        panelSimpleTabLayout.setVerticalGroup(
            panelSimpleTabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSimpleTabLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addContainerGap(374, Short.MAX_VALUE))
        );

        paneTabSimpleAdvanced.addTab("Simple", panelSimpleTab);

        panelAdvancedParent.setMinimumSize(new java.awt.Dimension(130, 573));
        panelAdvancedParent.setPreferredSize(new java.awt.Dimension(130, 400));
        panelAdvancedParent.setLayout(new javax.swing.BoxLayout(panelAdvancedParent, javax.swing.BoxLayout.Y_AXIS));

        datasetLoadingPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelAdvancedParent.add(datasetLoadingPanel1);

        learningAlgorithmConfigurationPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelAdvancedParent.add(learningAlgorithmConfigurationPanel2);
        panelAdvancedParent.add(learningAlgorithmConfigurationPanel1);

        jScrollPane1.setViewportView(panelAdvancedParent);

        org.jdesktop.layout.GroupLayout panelAdvancedTabLayout = new org.jdesktop.layout.GroupLayout(panelAdvancedTab);
        panelAdvancedTab.setLayout(panelAdvancedTabLayout);
        panelAdvancedTabLayout.setHorizontalGroup(
            panelAdvancedTabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAdvancedTabLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelAdvancedTabLayout.setVerticalGroup(
            panelAdvancedTabLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
        );

        paneTabSimpleAdvanced.addTab("Advanced", panelAdvancedTab);

        buttonLoad.setText("Load learning system from file...");
        buttonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadActionPerformed(evt);
            }
        });

        buttonUndo.setText("Undo changes");
        buttonUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUndoActionPerformed(evt);
            }
        });

        buttonGo.setText("Go!");
        buttonGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGoActionPerformed(evt);
            }
        });

        labelLearningSystemStatus.setText("No learning configuration set.");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(paneTabSimpleAdvanced, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(buttonLoad)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(buttonUndo))
                    .add(layout.createSequentialGroup()
                        .add(buttonGo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(labelLearningSystemStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 379, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(paneTabSimpleAdvanced, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonLoad, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(buttonUndo))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonGo)
                    .add(labelLearningSystemStatus))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadActionPerformed

        File file = Util.findLoadFile(LearningSystem.getFileExtension(),
                LearningSystem.getFileTypeDescription(),
                LearningSystem.getDefaultLocation(),
                this);
        if (file != null) {
            LearningSystem newS = null;

            try {
                //  newS = LearningSystem.readFromFile(file);
                newS = LearningSystem.readFromFile(file);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid learning system file", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(LearningSystemConfigurationPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (newS != null) {
                //TODO TODO TODO:
                // check newS param types
                //check newS feature names & param names (warn if disagree)
                if (!WekinatorInstance.getWekinatorInstance().canUse(newS)) {
                    JOptionPane.showMessageDialog(this, "Incompatible learning system", "Cannot load", JOptionPane.ERROR_MESSAGE);

                    logger.log(Level.WARNING, "Incompatible learning system");
                    return;
                }

                setLearningSystem(newS); //TODO: Problematic: What do we do w/ backup?; make usre to deal with hid there
                //New: Allow change of dist
                for (int i = 0; i < SynthProxy.getNumParams(); i++) {
                    newS.setParamUsingDistribution(i, SynthProxy.isParamDistribution(i));
                }

                WekinatorInstance.getWekinatorInstance().setLearningSystem(newS);
                labelLearningSystemStatus.setText("Learning system loaded successfully.");

                System.out.println("Sanity check:");
                for (int i = 0; i < newS.getNumParams(); i++) {
                    int[] mapping = newS.getDataset().getFeatureLearnerConfiguration().getFeatureMappingForLearner(i);
                    System.out.println("mapping " + i + " is: ");
                    for (int j = 0; j < mapping.length; j++) {
                        System.out.print(mapping[j] + " ");
                    }
                    System.out.println("");
                }

                Util.setLastFile(LearningSystem.getFileExtension(), file);
            }
        }


}//GEN-LAST:event_buttonLoadActionPerformed
//TODO: enable GO button depending on init state.

    private void buttonUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUndoActionPerformed
        reset();
}//GEN-LAST:event_buttonUndoActionPerformed

    protected void reset() {
        //Set from current learning system, or new...
        LearningSystem current = WekinatorInstance.getWekinatorInstance().getLearningSystem();
        if (current != null) {

            setLearningSystem(current);
        } else {
            //TODO: How do we want to do this??
            setLearningSystem(new LearningSystem(numParams));
        }
    }

    private void buttonGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGoActionPerformed

        //First, prompt the user to overwrite
        if (WekinatorInstance.getWekinatorInstance().getLearningSystem() != null) {
            int lResponse = JOptionPane.showConfirmDialog(this, "Are you sure you want to change your model configurations?\n" + "This could destroy your existing trained models...", "", JOptionPane.YES_NO_OPTION);
            if (lResponse != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            //Zeroth, check this is valid
            setLearningSystemFromForm();

        } catch (Exception ex) {
            Logger.getLogger(FeatureConfigurationPanel.class.getName()).log(Level.INFO, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid feature configuration", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //If no errors, apply changes globally
        //error here?
        if (learningSystem != WekinatorInstance.getWekinatorInstance().getLearningSystem()) {
            WekinatorInstance.getWekinatorInstance().setLearningSystem(learningSystem);
            //ABC: set mask NOW.
            boolean[] trainMask = getMaskFromForm();
            WekinatorLearningManager.getInstance().setTrainingMask(trainMask);
        }

        labelLearningSystemStatus.setText("Learning system configured.");

        setFormFromLearningSystem();

    //try {
    //  LearningSystem next = (LearningSystem) DeepCopy.copy(learningSystem); //DO I REALLY want to do this?!?! Really want shallow copy.  TODO TODO TODO
    //} catch (IOException ex) {
    //    Logger.getLogger(FeatureConfigurationPanel.class.getName()).log(Level.SEVERE, null, ex);
    //} catch (ClassNotFoundException ex) {
    //    Logger.getLogger(FeatureConfigurationPanel.class.getName()).log(Level.SEVERE, null, ex);
    // }
        
        if (WekinatorLearningManager.getInstance().getMode() == WekinatorLearningManager.Mode.RUNNING) {
            WekinatorLearningManager.getInstance().stopRunning();
                OscHandler.getOscHandler().stopSound();
        } else if (WekinatorLearningManager.getInstance().getMode() == WekinatorLearningManager.Mode.EVALUATING) {
            WekinatorLearningManager.getInstance().stopEvaluating();

        } else if (WekinatorLearningManager.getInstance().getMode() == WekinatorLearningManager.Mode.TRAINING) {
            WekinatorLearningManager.getInstance().stopTraining();
        }

        mainGui.showTrainRunPanel();

    }//GEN-LAST:event_buttonGoActionPerformed

    private boolean[] getMaskFromForm() {
        boolean[] m = new boolean[myAlgorithmPanels.length];
        for (int i = 0; i < m.length; i++) {
            m[i] = !myAlgorithmPanels[i].getDisabled();
        }
        return m;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonGo;
    private javax.swing.JButton buttonLoad;
    private javax.swing.JButton buttonUndo;
    private wekinator.DatasetLoadingPanel datasetLoadingPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelLearningSystemStatus;
    private wekinator.LearningAlgorithmConfigurationPanel learningAlgorithmConfigurationPanel1;
    private wekinator.LearningAlgorithmConfigurationPanel learningAlgorithmConfigurationPanel2;
    private javax.swing.JTabbedPane paneTabSimpleAdvanced;
    private javax.swing.JPanel panelAdvancedParent;
    private javax.swing.JPanel panelAdvancedTab;
    private javax.swing.JPanel panelSimpleTab;
    // End of variables declaration//GEN-END:variables

    private void setFormFromLearningSystem() {
        //TODO: Don't erase every time!!! TODO TODO TODO
        //RAF: When responding to change fire & new learning system is same last, just look for learners that have changed

        if (learningSystem == null) {
            panelAdvancedParent.removeAll();
            return;
        }

        SimpleDataset s = learningSystem.getDataset(); //will be null if learning system new


        if (myDatasetPanel == null) {
            myDatasetPanel = new DatasetLoadingPanel(s);
            myDatasetPanel.setBorder(new EtchedBorder());
            panelAdvancedParent.add(myDatasetPanel);
        } else {
            myDatasetPanel.setCurrentDataset(s);
            myDatasetPanel.setCurrentDatasetSelected();
        }

        LearningAlgorithm algs[] = learningSystem.getLearners(); //help: contents is null.
        if (algs == null) {
            if (myAlgorithmPanels != null) {
                for (int i = 0; i < myAlgorithmPanels.length; i++) {
                    panelAdvancedParent.remove(myAlgorithmPanels[i]);
                }
            }
            myAlgorithmPanels = new LearningAlgorithmConfigurationPanel[0];
        } else {
            //if (myAlgorithmPanels == null || myAlgorithmPanels.length != algs.length) {
                //Reset from scratch
                if (myAlgorithmPanels != null) {
                    for (int i = 0; i < myAlgorithmPanels.length; i++) {
                        panelAdvancedParent.remove(myAlgorithmPanels[i]);
                    }
                }
                myAlgorithmPanels = new LearningAlgorithmConfigurationPanel[algs.length];


                for (int i = 0; i < algs.length; i++) {
                    LearningAlgorithmConfigurationPanel p =
                            new LearningAlgorithmConfigurationPanel(
                            i, //paramNum
                            paramNames[i],
                            isParamDiscrete[i],
                            maxNumParamVals[i],
                            algs[i],
                            true,
                            featureConfiguration);
                    myAlgorithmPanels[i] = p;
                    p.setBorder(new EtchedBorder());
                    panelAdvancedParent.add(p);
                }
         /*   } else {
                //Just update the panels we've already got
                for (int i = 0; i < algs.length; i++) {
                    //verify panels[i] will never be null here
                    //Problem, what if I used to be discrete and now I'm not??
                    if (myAlgorithmPanels[i].discrete == isParamDiscrete[i]) {

                        myAlgorithmPanels[i].setCurrentLearningAlgorithm(algs[i]);
                        if (algs[i] != null) {
                            myAlgorithmPanels[i].setCurrentLearningAlgorithmSelected();
                        } else {
                            myAlgorithmPanels[i].setNewLearningAlgorithmSelected();
                        }
                    } else {
                        panelAdvancedParent.remove(myAlgorithmPanels[i]);
                        LearningAlgorithmConfigurationPanel p =
                            new LearningAlgorithmConfigurationPanel(
                            i, //paramNum
                            paramNames[i],
                            isParamDiscrete[i],
                            maxNumParamVals[i],
                            algs[i],
                            true,
                            featureConfiguration);
                        myAlgorithmPanels[i] = p;
                        p.setBorder(new EtchedBorder());
                        panelAdvancedParent.add(p);

                    }
                //   myAlgorithmPanels[i].setDisabled(!learningSystem.getLearnerEnabled(i));
                }

            } */
        }

        int height = myDatasetPanel.getPreferredSize().height;
        if (myAlgorithmPanels != null && myAlgorithmPanels.length > 0 && myAlgorithmPanels[0] != null) {
            height += (myAlgorithmPanels[0].getPreferredSize().height) * myAlgorithmPanels.length;
        }
        System.out.println("Setting size");
        //panelAdvancedParent.setSize(panelAdvancedParent.getSize().width, height);
//panelAdvancedParent.setSize(panelAdvancedParent.getSize().width, 1000);
        panelAdvancedParent.setPreferredSize(new Dimension(panelAdvancedParent.getSize().width, height));
        repaint();
    }

    private void currentLearningSystemChanged(PropertyChangeEvent evt) {
        //TODO TODO TODO
        //Update for learning system change: esp. for training state etc.
        //Don't handle individual learners here: leave this up to my learner panels.
        //BUT do need to make sure that if any learner ID changes, we change that here too.
    }

    private void setLearningSystemFromForm() throws Exception {
        SimpleDataset s = myDatasetPanel.commitAndGetSelectedDataset();

        if (s != learningSystem.getDataset()) {
            learningSystem.setDataset(s);
        }

        for (int i = 0; i < myAlgorithmPanels.length; i++) {
            LearningAlgorithm a = myAlgorithmPanels[i].commitAndGetSelectedAlgorithm();
            learningSystem.setLearners(i, a);
            // learningSystem.setLearnerEnabled(i, !disabled);
            int[] mapping = myAlgorithmPanels[i].getFeatureMapping();
            if (mapping != null) {
                try {
                    learningSystem.getDataset().setMappingForLearner(i, mapping);
                } catch (Exception ex) {
                    //TODO: log
                    System.out.println("Error with setting mapping for learner " + i);
                }
            }
         //   Plog.logFeatureMap(i);
        }
    }

    private LearningSystem getLearningSystemFromFormToSave() throws Exception {
        LearningSystem ls = new LearningSystem(learningSystem.getNumParams());
        SimpleDataset s = myDatasetPanel.getProposedDatasetNoncommittal();
        ls.setDataset(s);

        for (int i = 0; i < myAlgorithmPanels.length; i++) {
            LearningAlgorithm a = myAlgorithmPanels[i].getProposedLearningAlgorithmNoncommittal();
            ls.setLearners(i, a);
            boolean disabled = myAlgorithmPanels[i].getDisabled();
            // ls.setLearnerEnabled(i, !disabled);
            int[] mapping = myAlgorithmPanels[i].getFeatureMapping();
            if (mapping != null) {
                try {

                    ls.getDataset().setMappingForLearner(i, mapping);
                } catch (Exception ex) {
                    //TODO: log
                    System.out.println("Error with setting mapping for learner " + i);
                }
            }

        }
        return ls;
    }

    public static void main(String[] args) {
        FeatureConfiguration fc = new FeatureConfiguration();
        fc.setUseFFT(true);
        fc.setUseMotionSensor(true);

        WekinatorInstance.getWekinatorInstance().setFeatureConfiguration(fc);


        int[] maxVals = {3, 5};
        boolean[] isUseDist = {false, false, false};
        String[] names = {"my aparam 1", "my bparam 2", "p3"};
        boolean[] isDiscrete = {true, true, false};
        JFrame frame = new JFrame();
        // LearningAlgorithmConfigurationPanel panel = new LearningAlgorithmConfigurationPanel(0, "My param", true, null);
        LearningSystemConfigurationPanel panel = new LearningSystemConfigurationPanel();
        // LearningSystem ls = new LearningSystem(2); //only allocates space, doesn't init anything to defaults

        //  LearningSystem ls = new LearningSystem(2);
        //How to get settings re: param types?!

        panel.configure(3, names, isDiscrete, maxVals, fc);
        frame.add(panel);
        frame.setVisible(true);

    }
}
