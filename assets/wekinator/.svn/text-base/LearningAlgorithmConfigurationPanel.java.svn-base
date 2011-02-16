/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LearningAlgorithmConfigurationPanel.java
 *
 * Created on Dec 2, 2009, 4:04:46 PM
 */
package wekinator;

import javax.swing.event.ChangeEvent;
import wekinator.LearningAlgorithms.NNLearningAlgorithm;
import wekinator.LearningAlgorithms.ClassifierLearningAlgorithm;
import wekinator.LearningAlgorithms.AdaboostM1LearningAlgorithm;
import wekinator.LearningAlgorithms.J48LearningAlgorithm;
import wekinator.LearningAlgorithms.IbkLearningAlgorithm;
import wekinator.LearningAlgorithms.LearningAlgorithm;
import wekinator.LearningAlgorithms.SMOLearningAlgorithm;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.OptionalDataException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import wekinator.LearningAlgorithms.HmmLearningAlgorithm;
import wekinator.Plog.Msg;
import wekinator.util.Util;

/**
 *
 * @author rebecca
 */
public class LearningAlgorithmConfigurationPanel extends javax.swing.JPanel {

    protected FeatureConfiguration featureConfiguration;
    protected int paramNum = 0;
    protected String paramName = null;
    protected boolean discrete = true;
    protected int maxVals = 0;
    protected LearningAlgorithm currentLearningAlgorithm = null;
    protected LearningAlgorithm loadedLearningAlgorithm = null;
    protected boolean[] featureSelected = null;
    protected int[] loadedFeatureMapping = null;
    protected File fileToLoadFrom = null;
    protected boolean hasUsableLoadedFile = false;
    protected int[] myFeatureMapping = null;
    protected ChangeListener featureNamesListener = new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
            featureNamesChanged();
        }
    };

    public boolean isHasUsableLoadedFile() {
        return hasUsableLoadedFile;
    }

    public void setHasUsableLoadedFile(boolean hasUsableLoadedFile) {
        if (!this.hasUsableLoadedFile && hasUsableLoadedFile) {
            checkDisabled.setSelected(true);
        }

        this.hasUsableLoadedFile = hasUsableLoadedFile;
        setLoadedAlgorithmInfo();
    }
    //TODO Problem here!! What if learner loaded from file, then selected & used, then user wants to reload?!
    protected PropertyChangeListener learningChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            learningPropertyChange(evt);  //def this happens
        }
    };
    protected JCheckBox[] featureBoxes = null;
    private Logger logger = Logger.getLogger(LearningAlgorithmConfigurationPanel.class.getName());

    /**
     * Get the value of currentLearningAlgorithm
     *
     * @return the value of currentLearningAlgorithm
     */
    public LearningAlgorithm getCurrentLearningAlgorithm() {
        return currentLearningAlgorithm;
    }

    public boolean getDisabled() {
        return checkDisabled.isSelected();
    }

    //MappingToCommit should never be null
    protected void commitFeatureMapping(int[] mappingToCommit) {
        if (mappingToCommit == null) {
            return;
        }

        if (myFeatureMapping == null || myFeatureMapping.length != mappingToCommit.length) {
            myFeatureMapping = new int[mappingToCommit.length];
        }

        for (int i = 0; i < mappingToCommit.length; i++) {
            myFeatureMapping[i] = mappingToCommit[i];
        }
    }

    //TODO: Fix so also is feature mapping
    //6/29: This is the problem: By the time this is called (post-commit), button has already changed to "use current"
    public int[] getFeatureMapping() {
        return myFeatureMapping;

    /* ButtonModel bb = buttonGroup1.getSelection();
    if (radioUseNew.isSelected()) { //false here??

    if (featureSelected == null) {
    System.out.println("Should not be null here - fix this");
    return null;
    } else {
    int numSelected = 0;
    for (int i = 0; i < featureSelected.length; i++) {
    if (featureSelected[i]) {
    numSelected++;
    }
    }
    int mapping[] = new int[numSelected];
    int n = 0;
    for (int i = 0; i < featureSelected.length; i++) {
    if (featureSelected[i]) {
    mapping[n++] = i;
    }
    }
    return mapping;
    }

    } else if (radioUseFile.isSelected()) {
    return loadedFeatureMapping;
    } else {
    return null; //TODO: Handle more gracefully.
    } */
    }

    /**
     * Set the value of currentLearningAlgorithm
     *
     * @param currentLearningAlgorithm new value of currentLearningAlgorithm
     */
    public void setCurrentLearningAlgorithm(LearningAlgorithm learningAlgorithm) {
        //NEW 6/29:
        boolean isAlgorithmNewlyNotNull = (this.currentLearningAlgorithm == null && learningAlgorithm != null);

        if (this.currentLearningAlgorithm != null) {
            this.currentLearningAlgorithm.removePropertyChangeListener(learningChangeListener);
        }
        this.currentLearningAlgorithm = learningAlgorithm;
        if (this.currentLearningAlgorithm != null) {
            this.currentLearningAlgorithm.addPropertyChangeListener(learningChangeListener);
        }

        //6/29: Error is happening here on 1st non-null:
        setCurrentAlgorithmInfo(isAlgorithmNewlyNotNull);
    }

    /**
     * Get the value of discrete
     *
     * @return the value of discrete
     */
    public boolean isDiscrete() {
        return discrete;
    }

    /**
     * Set the value of discrete
     *
     * @param discrete new value of discrete
     */
    public void setDiscrete(boolean discrete) {
        this.discrete = discrete;
        comboSelectClassifier.setVisible(discrete);
        if (discrete) {
            radioUseNew.setText("Select new algorithm");
        } else {
            radioUseNew.setText("Use new neural network");
        }

        setParamNameLabel();
    }

    /**
     * Get the value of paramName
     *
     * @return the value of paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * Set the value of paramName
     *
     * @param paramName new value of paramName
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
        setParamNameLabel();
    }

    public void setMaxVals(int m) {
        maxVals = m;
    }

    public int getMaxVals() {
        return maxVals;
    }

    /**
     * Get the value of paramNum
     *
     * @return the value of paramNum
     */
    public int getParamNum() {
        return paramNum;
    }

    /**
     * Set the value of paramNum
     *
     * @param paramNum new value of paramNum
     */
    public void setParamNum(int paramNum) {
        this.paramNum = paramNum;
        setParamNameLabel();
    }

    /** Creates new form LearningAlgorithmConfigurationPanel */
    public LearningAlgorithmConfigurationPanel() {
        initComponents();
    /*
    setLoadedAlgorithmInfo();
    setParamName(paramName);
    setDiscrete(discrete);
    setLoadedAlgorithmInfo();
    setCurrentLearningAlgorithm(currentLearningAlgorithm); */
    //Construct w/ some defaults:
    // this(0, "Param_0", true, null);
    }

    public LearningAlgorithmConfigurationPanel(int paramNum,
            String paramName,
            boolean discrete,
            int maxVals,
            LearningAlgorithm currentLearningAlgorithm,
            boolean learnerEnabled,
            FeatureConfiguration fc) {

        initComponents();
        updateFeatureConfiguration(fc);
        setMaxVals(maxVals);
        setParamName(paramName);
        setParamNum(paramNum);
        setDiscrete(discrete);
        setHasUsableLoadedFile(false);
        setCurrentLearningAlgorithm(currentLearningAlgorithm);
        if (currentLearningAlgorithm != null) {
            setCurrentLearningAlgorithmSelected();
        } else {
            setNewLearningAlgorithmSelected();
        }
        checkDisabled.setSelected(!learnerEnabled);
        //   this.featureConfiguration = fc;
        WekinatorInstance.getWekinatorInstance().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                wekInstChanged(evt);
            }
        });
        updateFeatures();
    }

    private void updateFeatureConfiguration(FeatureConfiguration newfc) {
        if (newfc == featureConfiguration) {
            return;
        }

        if (featureConfiguration != null) {
            featureConfiguration.removeFeatureNamesChangeListener(featureNamesListener);
        }

        featureConfiguration = newfc;
        if (newfc != null) {
            featureConfiguration.addFeatureNamesChangeListener(featureNamesListener);
        }


    }

    private void featureNamesChanged() {
        updateFeatures();
    }

    private void wekInstChanged(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorInstance.PROP_FEATURECONFIGURATION)) {
            //TODO: update feature name listeners on feature configuration
            updateFeatures();
        }
    }

    public void setCurrentLearningAlgorithmSelected() {
        radioUseCurrent.setSelected(true);
        radioSelectionChanged();
    }

    public void setNewLearningAlgorithmSelected() {
        radioUseNew.setSelected(true);
        radioSelectionChanged();

    }

    private File chooseLoadFile() {
        WekinatorInstance wek = WekinatorInstance.getWekinatorInstance();
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String location = WekinatorInstance.getWekinatorInstance().getSettings().getLastLocation(LearningAlgorithm.getFileExtension());
        if (location == null || location.equals("")) {
            location = HidSetup.getDefaultLocation();
        }
        fc.setCurrentDirectory(new File(location)); //TODO: Could set directory vs file here according to above results

        boolean success = true;
        File file = null;
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        if (file != null) {
            WekinatorInstance.getWekinatorInstance().getSettings().setLastLocation(LearningAlgorithm.getFileExtension(), Util.getCanonicalPath(file));
        }
        return file;
    }

    private int computeNumFeaturesSelected() {
        int n = 0;
        if (featureBoxes == null) {
            return n;
        }

        for (int i = 0; i < featureBoxes.length; i++) {
            if (featureBoxes[i].isSelected()) {
                n++;
            }
        }
        return n;
    }

    private void loadLearnerFromNewFile() {
        /* File f = chooseLoadFile();
        if (f != null) {
        loadLearnerFromFile(f);
        } */
        File file = Util.findLoadFile(LearningAlgorithm.getFileExtension(),
                LearningAlgorithm.getFileTypeDescription(),
                LearningAlgorithm.getDefaultLocation(),
                this);
        if (file != null) {
            loadLearnerFromFile(file);
        }
    }

    private void loadLearnerFromFile(File f) {
        LearningAlgorithm l = null;
        try {
            l = LearningAlgorithm.readFromFile(f);
        //   l = (LearningAlgorithm) SerializedFileUtil.readFromFile(f);
        } catch (OptionalDataException ex) {
            Logger.getLogger(LearningAlgorithmConfigurationPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Invalid learning algorithm file", "Could not load algorithm from file", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            //Show warning box.
            Logger.getLogger(LearningAlgorithmConfigurationPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not load algorithm from file", JOptionPane.ERROR_MESSAGE);

        }
        if (l != null) {
            //Make sure it's the type we need
            if (l instanceof ClassifierLearningAlgorithm || l instanceof HmmLearningAlgorithm) {
                if (discrete) {
                    fileToLoadFrom = f;

                    setLoadedLearningAlgorithm(l);
                    setHasUsableLoadedFile(true);
                    Util.setLastFile(LearningAlgorithm.getFileExtension(), f);

                } else {
                    JOptionPane.showMessageDialog(this, "A discrete learning algorithm may only be loaded for a discrete parameter", "Could not load algorithm from file", JOptionPane.ERROR_MESSAGE);
                }
            } else if (l instanceof NNLearningAlgorithm) {
                if (!discrete) {
                    fileToLoadFrom = f;

                    setLoadedLearningAlgorithm(l);
                    setHasUsableLoadedFile(true);
                    Util.setLastFile(LearningAlgorithm.getFileExtension(), f);
                } else {
                    JOptionPane.showMessageDialog(this, "A real-valued learning algorithm may only be loaded for a real-valued parameter", "Could not load algorithm from file", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "The file does not contain a valid learning algorithm", "Could not load algorithm from file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void setLoadedLearningAlgorithm(LearningAlgorithm l) {
        this.loadedLearningAlgorithm = l;

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
        featureMapFrame = new javax.swing.JFrame();
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
        jPanel8 = new javax.swing.JPanel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jCheckBox9 = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        jCheckBox10 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jCheckBox12 = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        jCheckBox13 = new javax.swing.JCheckBox();
        jPanel15 = new javax.swing.JPanel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jCheckBox15 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();
        buttonOK = new javax.swing.JButton();
        labelParamName = new javax.swing.JLabel();
        labelLearnerStatus = new javax.swing.JLabel();
        comboSelectClassifier = new javax.swing.JComboBox();
        buttonLoadFile = new javax.swing.JButton();
        radioUseNew = new javax.swing.JRadioButton();
        radioUseCurrent = new javax.swing.JRadioButton();
        radioUseFile = new javax.swing.JRadioButton();
        labelFile = new javax.swing.JLabel();
        labelFeatures = new javax.swing.JLabel();
        buttonFeatures = new javax.swing.JButton();
        checkDisabled = new javax.swing.JCheckBox();
        buttonLoadedFeatures = new javax.swing.JButton();

        featureMapFrame.setTitle("Select features to use for this parameter");

        scrollPaneFeatures.setMinimumSize(new java.awt.Dimension(100, 100));

        panelFeatures.setLayout(new java.awt.GridLayout(0, 4));

        jPanel4.setAlignmentX(0.0F);

        jCheckBox3.setText("jCheckBox2");
        jCheckBox3.setAlignmentX(0.5F);
        jCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

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
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

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
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

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
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

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

        jPanel8.setAlignmentX(0.0F);

        jCheckBox7.setText("jCheckBox2");
        jCheckBox7.setAlignmentX(0.5F);
        jCheckBox7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox7ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox7)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox7)
        );

        panelFeatures.add(jPanel8);

        jPanel9.setAlignmentX(0.0F);

        jCheckBox8.setText("jCheckBox2");
        jCheckBox8.setAlignmentX(0.5F);
        jCheckBox8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox8)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox8)
        );

        panelFeatures.add(jPanel9);

        jPanel10.setAlignmentX(0.0F);

        jCheckBox9.setText("jCheckBox2");
        jCheckBox9.setAlignmentX(0.5F);
        jCheckBox9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox9ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox9)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox9)
        );

        panelFeatures.add(jPanel10);

        jPanel11.setAlignmentX(0.0F);

        jCheckBox10.setText("jCheckBox2");
        jCheckBox10.setAlignmentX(0.5F);
        jCheckBox10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox10ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox10)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox10)
        );

        panelFeatures.add(jPanel11);

        jPanel12.setAlignmentX(0.0F);

        jCheckBox11.setText("jCheckBox2");
        jCheckBox11.setAlignmentX(0.5F);
        jCheckBox11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox11ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox11)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox11)
        );

        panelFeatures.add(jPanel12);

        jPanel13.setAlignmentX(0.0F);

        jCheckBox12.setText("jCheckBox2");
        jCheckBox12.setAlignmentX(0.5F);
        jCheckBox12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox12ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox12)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox12)
        );

        panelFeatures.add(jPanel13);

        jPanel14.setAlignmentX(0.0F);

        jCheckBox13.setText("jCheckBox2");
        jCheckBox13.setAlignmentX(0.5F);
        jCheckBox13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel14Layout = new org.jdesktop.layout.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox13)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox13)
        );

        panelFeatures.add(jPanel14);

        jPanel15.setAlignmentX(0.0F);

        jCheckBox14.setText("jCheckBox2");
        jCheckBox14.setAlignmentX(0.5F);
        jCheckBox14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox14ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox14)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox14)
        );

        panelFeatures.add(jPanel15);

        jPanel16.setAlignmentX(0.0F);

        jCheckBox15.setText("jCheckBox2");
        jCheckBox15.setAlignmentX(0.5F);
        jCheckBox15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox15ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox15)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jCheckBox15)
        );

        panelFeatures.add(jPanel16);

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

        org.jdesktop.layout.GroupLayout featureMapFrameLayout = new org.jdesktop.layout.GroupLayout(featureMapFrame.getContentPane());
        featureMapFrame.getContentPane().setLayout(featureMapFrameLayout);
        featureMapFrameLayout.setHorizontalGroup(
            featureMapFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(featureMapFrameLayout.createSequentialGroup()
                .addContainerGap()
                .add(featureMapFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scrollPaneFeatures, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                    .add(featureMapFrameLayout.createSequentialGroup()
                        .add(buttonCancel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(buttonOK))
                    .add(featureMapFrameLayout.createSequentialGroup()
                        .add(jButton3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton4)))
                .addContainerGap())
        );
        featureMapFrameLayout.setVerticalGroup(
            featureMapFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, featureMapFrameLayout.createSequentialGroup()
                .add(scrollPaneFeatures, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(featureMapFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton3)
                    .add(jButton4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(featureMapFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonCancel)
                    .add(buttonOK)))
        );

        labelParamName.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        labelParamName.setText("Parameter 0 - discrete");

        labelLearnerStatus.setText("Adaboost.M1, not yet trained, using 6 features");
        labelLearnerStatus.setEnabled(false);

        comboSelectClassifier.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AdaBoostM.1", "Decision Tree", "K-Nearest Neighbor", "Support Vector Machine" }));
        comboSelectClassifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboSelectClassifierActionPerformed(evt);
            }
        });

        buttonLoadFile.setText("Choose file...");
        buttonLoadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadFileActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioUseNew);
        radioUseNew.setSelected(true);
        radioUseNew.setText("Select new algorithm");
        radioUseNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioUseNewActionPerformed(evt);
            }
        });
        radioUseNew.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                radioUseNewPropertyChange(evt);
            }
        });

        buttonGroup1.add(radioUseCurrent);
        radioUseCurrent.setText("Use current:");
        radioUseCurrent.setEnabled(false);
        radioUseCurrent.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        radioUseCurrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioUseCurrentActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioUseFile);
        radioUseFile.setText("Load model from file:");
        radioUseFile.setEnabled(false);
        radioUseFile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        radioUseFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioUseFileActionPerformed(evt);
            }
        });
        radioUseFile.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                radioUseFilePropertyChange(evt);
            }
        });

        labelFile.setText("Decision Tree, trained, using 5 features");
        labelFile.setEnabled(false);

        labelFeatures.setText("Using all features");

        buttonFeatures.setText("View & choose features...");
        buttonFeatures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFeaturesActionPerformed(evt);
            }
        });

        checkDisabled.setText("Disable re-training (use current version only)");
        checkDisabled.setEnabled(false);
        checkDisabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkDisabledActionPerformed(evt);
            }
        });

        buttonLoadedFeatures.setText("View & choose features...");
        buttonLoadedFeatures.setEnabled(false);
        buttonLoadedFeatures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadedFeaturesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(labelParamName)
                    .add(checkDisabled)
                    .add(layout.createSequentialGroup()
                        .add(radioUseCurrent)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(labelLearnerStatus))
                    .add(layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(buttonLoadFile)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(buttonLoadedFeatures))
                    .add(layout.createSequentialGroup()
                        .add(radioUseFile)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(labelFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 290, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(radioUseNew)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(comboSelectClassifier, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 164, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(27, 27, 27)
                        .add(labelFeatures)
                        .add(18, 18, 18)
                        .add(buttonFeatures)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(labelParamName)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(checkDisabled)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(radioUseCurrent)
                    .add(labelLearnerStatus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(7, 7, 7)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(radioUseFile)
                    .add(labelFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonLoadFile)
                    .add(buttonLoadedFeatures))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(radioUseNew)
                    .add(comboSelectClassifier, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(labelFeatures)
                    .add(buttonFeatures))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonLoadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadFileActionPerformed
        loadLearnerFromNewFile();
    }//GEN-LAST:event_buttonLoadFileActionPerformed

    private void buttonFeaturesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonFeaturesActionPerformed
        featureMapFrame.setVisible(true);
        featureMapFrame.toFront();
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.FEATURE_CHOOSER_OPENED);
        }
    }//GEN-LAST:event_buttonFeaturesActionPerformed

    private void buttonLoadedFeaturesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadedFeaturesActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_buttonLoadedFeaturesActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        if (featureBoxes == null || featureSelected == null || featureBoxes.length != featureSelected.length) {
            featureMapFrame.setVisible(false);
            return;

        }

        for (int i = 0; i < featureBoxes.length; i++) {
            featureBoxes[i].setSelected(featureSelected[i]);
        }
        featureMapFrame.setVisible(false);
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.FEATURE_VIEWER_CLOSED);
        }

}//GEN-LAST:event_buttonCancelActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (featureBoxes == null) {
            return;
        }

        for (int i = 0; i < featureBoxes.length; i++) {
            featureBoxes[i].setSelected(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOKActionPerformed
        if (featureBoxes == null) {
            featureMapFrame.setVisible(false);
            return;
        }
        int numSelected = computeNumFeaturesSelected();
        if (numSelected == 0) {
            //Error box; don't close
            //TODO: low priority: Give focus back to frame when done.
            JOptionPane.showMessageDialog(this, "Must use at least one feature for each parameter", "Error", JOptionPane.ERROR_MESSAGE);
            featureMapFrame.toFront();
            //JOptionPane.showMessage
            return;
        }

        if (featureSelected == null || featureSelected.length != featureBoxes.length) {
            featureSelected = new boolean[featureBoxes.length];
        }

        for (int i = 0; i < featureBoxes.length; i++) {
            //AAA
            featureSelected[i] = featureBoxes[i].isSelected();
        } //this does its job 6/28
        featureMapFrame.setVisible(false);
        if (numSelected == featureBoxes.length) {
            labelFeatures.setText("Using all features");
        } else {
            labelFeatures.setText("Using " + numSelected + " features");
        }

        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.FEATURE_CHOOSER_CLOSED);
        }

}//GEN-LAST:event_buttonOKActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (featureBoxes == null) {
            return;
        }

        for (int i = 0; i < featureBoxes.length; i++) {
            featureBoxes[i].setSelected(false);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox6ActionPerformed

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

    private void radioUseNewPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_radioUseNewPropertyChange
    }//GEN-LAST:event_radioUseNewPropertyChange

    private void radioUseFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioUseFileActionPerformed
        radioSelectionChanged();
    }//GEN-LAST:event_radioUseFileActionPerformed

    private void radioUseFilePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_radioUseFilePropertyChange
    }//GEN-LAST:event_radioUseFilePropertyChange

    private void radioUseNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioUseNewActionPerformed
        radioSelectionChanged();


    }//GEN-LAST:event_radioUseNewActionPerformed

    private void radioUseCurrentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioUseCurrentActionPerformed
        //TODO: doublecheck this logic - disabled changes state when it needs to
        radioSelectionChanged();
    }//GEN-LAST:event_radioUseCurrentActionPerformed

    private void radioSelectionChanged() {
        //    checkDisabled.setEnabled(!radioUseNew.isSelected());
        checkDisabled.setEnabled(false);

        if (radioUseNew.isSelected()) {
            checkDisabled.setSelected(false);
        }

        labelFeatures.setEnabled(radioUseNew.isSelected());
        buttonFeatures.setEnabled(radioUseNew.isSelected());

        buttonLoadedFeatures.setEnabled(radioUseFile.isSelected());
    }

    private void checkDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkDisabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkDisabledActionPerformed

    private void comboSelectClassifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboSelectClassifierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboSelectClassifierActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonFeatures;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonLoadFile;
    private javax.swing.JButton buttonLoadedFeatures;
    private javax.swing.JButton buttonOK;
    private javax.swing.JCheckBox checkDisabled;
    private javax.swing.JComboBox comboSelectClassifier;
    private javax.swing.JFrame featureMapFrame;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel labelFeatures;
    private javax.swing.JLabel labelFile;
    private javax.swing.JLabel labelLearnerStatus;
    private javax.swing.JLabel labelParamName;
    private javax.swing.JPanel panelFeatures;
    private javax.swing.JRadioButton radioUseCurrent;
    private javax.swing.JRadioButton radioUseFile;
    private javax.swing.JRadioButton radioUseNew;
    private javax.swing.JScrollPane scrollPaneFeatures;
    // End of variables declaration//GEN-END:variables

    protected void learningPropertyChange(PropertyChangeEvent evt) {
        setCurrentAlgorithmInfo(false);
    }

    protected void setCurrentAlgorithmInfo(boolean isAlgorithmNewlyNotNull) {
        if (isAlgorithmNewlyNotNull) {
            radioUseCurrent.setSelected(true);
        }

        if (currentLearningAlgorithm == null) { //only null @ first load of pane, before config done
            labelLearnerStatus.setText("None");
            radioUseNew.setSelected(true);
            radioUseCurrent.setEnabled(false);
            labelLearnerStatus.setEnabled(false);
        } else {
            String s = currentLearningAlgorithm.getName(); //TODO: add ability to view selected features
            //Ultimately might want to encode (lA, DB) pairs outside either class - a moderator for getting data...
           /* switch (currentLearningAlgorithm.getTrainingState()) {
            case NOT_TRAINED:
            s += ", not trained";
            break;
            case TRAINED:
            s += ", trained";
            break;
            case TRAINING:
            s += ", training in process";
            break;
            default:
            logger.log(Level.SEVERE, "Unknown learning algorithm state: " + currentLearningAlgorithm.getTrainingState());

            }*/
            labelLearnerStatus.setText(s);
            radioUseCurrent.setEnabled(true);
            labelLearnerStatus.setEnabled(true);
        }
    }

    private void setLoadedAlgorithmInfo() {
        if (hasUsableLoadedFile == false) {
            labelFile.setText("No file chosen");
            labelFile.setEnabled(false);
            radioUseFile.setEnabled(false);
            buttonLoadedFeatures.setEnabled(true);
        } else {
            labelFile.setEnabled(true);
            radioUseFile.setEnabled(true);
            radioUseFile.setSelected(true);
            buttonLoadedFeatures.setEnabled(true);
            labelFile.setText("Using file " + fileToLoadFrom.getName());
        }
    }

    private void setParamNameLabel() {
        String s;
        String name;
        if (paramName == null) {
            name = "Param_" + paramNum;
        } else {
            name = paramName;
        }

        s = name + (discrete ? " - Discrete classifier" : " - Neural network outputting real values");
        labelParamName.setText(s);
        featureMapFrame.setTitle("Select features to use for " + name);
    }

    public LearningAlgorithm commitAndGetSelectedAlgorithm() throws Exception {
        commitCurrentConfiguration();
        return currentLearningAlgorithm;
    }

    public LearningAlgorithm getProposedLearningAlgorithmNoncommittal() throws Exception {
        LearningAlgorithm selected = null;

        if (radioUseCurrent.isSelected()) {
            selected = currentLearningAlgorithm;
        } else if (radioUseFile.isSelected()) {
            if (hasUsableLoadedFile) {
                if (loadedLearningAlgorithm != null) {
                    selected = loadedLearningAlgorithm;
                } else {
                    loadLearnerFromFile(fileToLoadFrom);
                    selected = loadedLearningAlgorithm;
                }
            } else {
                throw new Exception("Couldn't load from file");
            }
        } else if (radioUseNew.isSelected()) {
            if (isDiscrete()) {

                int i = comboSelectClassifier.getSelectedIndex();
                if (i == 0) { //adaboost
                    selected = new AdaboostM1LearningAlgorithm();
                } else if (i == 1) {
                    selected = new J48LearningAlgorithm();
                } else if (i == 2) {
                    selected = new IbkLearningAlgorithm();
                } else if (i == 3) {
                    selected = new SMOLearningAlgorithm();
                } else if (i == 4) {
                    selected = new HmmLearningAlgorithm(maxVals);
                }
            } else {
                selected = new NNLearningAlgorithm();
            }
        }

        if (selected == null) {
            logger.log(Level.SEVERE, "Invalid learning algorithm configuration: Should not happen!");
            throw new Exception("Problem committing selection");
        }
        return selected;

    }

    public void commitCurrentConfiguration() throws Exception {
        LearningAlgorithm selected = getProposedLearningAlgorithmNoncommittal();
        setMyMapping();
        setCurrentLearningAlgorithm(selected);
        setLoadedLearningAlgorithm(null);
        setCurrentLearningAlgorithmSelected();
    }

    protected void setMyMapping() {
        if (radioUseCurrent.isSelected()) {
            //do nothing
        } else if (radioUseFile.isSelected()) {
            commitFeatureMapping(loadedFeatureMapping); //Will have to initialize this somewhere.
        } else if (radioUseNew.isSelected()) {
            int numSelected = 0;
            for (int i = 0; i < featureSelected.length; i++) {
                if (featureSelected[i]) {
                    numSelected++;
                }
            }
            int mapping[] = new int[numSelected];
            int n = 0;
            for (int i = 0; i < featureSelected.length; i++) {
                if (featureSelected[i]) {
                    mapping[n++] = i;
                }
            }
            commitFeatureMapping(mapping);
        }
    }

    public void setDisabled(boolean b) {
        checkDisabled.setSelected(b); //TODO: error check?
    }

    public static void main(String[] args) {
        FeatureConfiguration fc = new FeatureConfiguration();
        fc.setUseFFT(true);
        fc.setUseMotionSensor(true);

        WekinatorInstance.getWekinatorInstance().setFeatureConfiguration(fc);

        JFrame frame = new JFrame();
        // LearningAlgorithmConfigurationPanel panel = new LearningAlgorithmConfigurationPanel(0, "My param", true, null);
        LearningAlgorithmConfigurationPanel panel = new LearningAlgorithmConfigurationPanel(0, "p1", false, 2, null, true, fc);
        frame.add(panel);
        frame.setVisible(true);

    }

    //TODO: Hack: Ultimately we want this to be responsive to changes in registered feature configuration
    //Not sure whether to listen here or be notified elsewhere... e.g. listen for learning system
    //prop change.
    protected void updateFeatures() {
        panelFeatures.removeAll();
//        FeatureConfiguration fc = WekinatorLearningManager.getInstance().getFeatureConfiguration();
        FeatureConfiguration featureConfiguration = WekinatorInstance.getWekinatorInstance().getFeatureConfiguration();
        if (featureConfiguration != null) {
            String[] featureNames = featureConfiguration.getAllEnabledFeatureNames();
            if (featureSelected == null || featureSelected.length != featureNames.length) {
                featureSelected = new boolean[featureNames.length];
                featureBoxes = new JCheckBox[featureNames.length];

                for (int i = 0; i < featureNames.length; i++) {

                    featureSelected[i] = true;
                    if (WekinatorRunner.isKbow() && i == 0) {
                        featureSelected[i] = false;
                    }
                }
            }

            for (int i = 0; i < featureNames.length; i++) {
                JPanel next = new JPanel();
                // JLabel nextName= new JLabel(featureNames[i]);
                // JCheckBox tmp = new JCheckBox
                featureBoxes[i] = new JCheckBox(featureNames[i], featureSelected[i]);
                next.add(featureBoxes[i]);
                next.setAlignmentX(1.0F);
                panelFeatures.add(next);

            }


        }
        //if (featureM.isVisible()) {
        featureMapFrame.setSize(500, 300);
        featureMapFrame.repaint();
    //}
    }
}
