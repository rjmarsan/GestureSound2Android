/*
 * Bigger1.java
 *
 * Created on December 2, 2008, 11:10 AM
 */
//Update.
package wekinator;

import drawing.GraphDataViewFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import wekinator.ChuckRunner.ChuckRunnerState;
import wekinator.Plog.Msg;
import wekinator.util.Util;

/**
 *
 * @author  rebecca
 */
public class MainGUI extends javax.swing.JFrame {

    WekinatorInstance wek = WekinatorInstance.getWekinatorInstance();
    boolean isConnected = false;
    public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
    protected JDialog aboutBox, prefs;
    
    PropertyChangeListener hidSetupChangeListener = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent evt) {
            hidSetupPropertyChange(evt);
        }
    };

    /** Creates new form Bigger1 */
    public MainGUI() {
        initComponents();

        //Prefs stuff
        // set up a simple about box
        aboutBox = new JDialog(this, "About Wekinator");
        aboutBox.getContentPane().setLayout(new BorderLayout());
        aboutBox.getContentPane().add(new JLabel("Wekinator", JLabel.CENTER));
        aboutBox.getContentPane().add(new JLabel("a project by Rebecca Fiebrink, Princeton University, 2010", JLabel.CENTER), BorderLayout.SOUTH);

        aboutBox.setSize(460, 120);
        aboutBox.setResizable(false);

        // Preferences dialog lets you select the background color when displaying an image
        prefs = new JDialog(this, "Wekinator Preferences");
        prefs.getContentPane().setLayout(new BorderLayout());
        prefs.getContentPane().add(new JLabel("There is nothing to prefer at this time.", JLabel.CENTER));
        prefs.setSize(360, 120);
        prefs.setResizable(false);

        registerForMacOSXEvents();
        if (WekinatorRunner.isLogging()) {
            WekinatorInstance.getWekinatorInstance().setupPlog();
        } else {
            menuResetLog.setEnabled(false);
            menuFlushLog.setEnabled(false);
            menuPerformanceMode.setEnabled(false);
        }

        learningSystemConfigurationPanel.setMainGUI(this);
        //Anywhere we add a listener, also update to current property.

        // FeatureManager fm = wek.getFeatureManager();
        ChuckSystem.getChuckSystem().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                chuckSystemPropertyChange(evt);
            }
        });
        updateGUIforChuckSystem();

        Logger.getLogger(MainGUI.class.getName()).log(Level.INFO, "Here's some info");
        OscHandler.getOscHandler().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                oscHandlerPropertyChange(evt);
            }
        });
        updateGUIforOscStatus();
        //  fm.hidSetup = wek.getCurrentHidSetup(); //TODO: put in fm
        wek.getCurrentHidSetup().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                hidSetupPropertyChange(evt);
            }
        });
        wek.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                wekinatorInstancePropertyChangeEvent(evt);
            }
        });

        WekinatorLearningManager.getInstance().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                learningManagerPropertyChange(evt);
            }
        });
        ChuckRunner.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                runnerPropertyChange(evt);
            }
        });
        try {
            OscHandler.getOscHandler().setupOsc();
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        runOscIfNeeded();
        runChuckIfNeeded();
        updatePanels();
        updateMenus();
        if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.PANEL_CHUCK_VIEW);
         }
    }

     // Generic registration with the Mac OS X application menu
    // Checks the platform, then attempts to register with the Apple EAWT
    // See OSXAdapter.java to see how this is done without directly referencing any Apple APIs
    public void registerForMacOSXEvents() {
        if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[])null));
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
              //  OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }

    // General info dialog; fed to the OSXAdapter as the method to call when
    // "About OSXAdapter" is selected from the application menu
    public void about() {
        aboutBox.setLocation((int)this.getLocation().getX() + 22, (int)this.getLocation().getY() + 22);
        aboutBox.setVisible(true);
    }

    // General preferences dialog; fed to the OSXAdapter as the method to call when
    // "Preferences..." is selected from the application menu
    public void preferences() {
        prefs.setLocation((int)this.getLocation().getX() + 22, (int)this.getLocation().getY() + 22);
        prefs.setVisible(true);
    }

     // General quit handler; fed to the OSXAdapter as the method to call when a system quit event occurs
    // A quit event is triggered by Cmd-Q, selecting Quit from the application or Dock menu, or logging out
    public boolean quit() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            exit();
        }
        return (option == JOptionPane.YES_OPTION);
    }


    private void runOscIfNeeded() {
        if (WekinatorRunner.chuckFile == null &&
                WekinatorRunner.connectAutomatically &&
                OscHandler.getOscHandler().getConnectionState() == OscHandler.ConnectionState.NOT_CONNECTED) {

            connectOSC();
        }
    }

    private void runChuckIfNeeded() {
        if (WekinatorRunner.chuckFile != null &&
                ChuckRunner.getConfiguration() != null &&
                ChuckRunner.getConfiguration().isUsable() &&
                ChuckRunner.getRunnerState() == ChuckRunnerState.NOT_RUNNING) {
            try {
                ChuckRunner.run();
            } catch (IOException ex) {
                Logger.getLogger(ChuckRunnerPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void runnerPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ChuckRunner.PROP_RUNNERSTATE)) {
            updateRunnerState(ChuckRunner.getRunnerState());
        } else if (evt.getPropertyName().equals(ChuckRunner.PROP_CONFIGURATION)) {
            runChuckIfNeeded();
        }

    }

    private void updateRunnerState(ChuckRunner.ChuckRunnerState state) {
        if (state == ChuckRunnerState.RUNNING) {
            wek.useConfigurationNextSession();
        //also connect!
        //connectOSC(); //changed: We'll wait to hear from chuck.

        } else {
            try {
                OscHandler.getOscHandler().end();
                OscHandler.getOscHandler().setupOsc();
            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }

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

        buttonGroupClassifierSource = new javax.swing.ButtonGroup();
        buttonGroupSettingsSource = new javax.swing.ButtonGroup();
        buttonGroupProcessingSource = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        panelMainTabs = new javax.swing.JTabbedPane();
        panelOSC = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        chuckRunnerPanel1 = new wekinator.ChuckRunnerPanel();
        jPanel5 = new javax.swing.JPanel();
        buttonOscConnect = new javax.swing.JButton();
        buttonOscDisconnect = new javax.swing.JButton();
        labelOscStatus = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        panelTabFeatureConfiguration = new javax.swing.JPanel();
        featureConfigurationPanel1 = new wekinator.FeatureConfigurationPanel();
        panelTabLearningSystemConfiguration = new javax.swing.JPanel();
        learningSystemConfigurationPanel = new wekinator.LearningSystemConfigurationPanel();
        trainRunPanel1 = new wekinator.TrainRunPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        menuSaveLearningSystem = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        menuSaveDataset = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        menuItemViewConsole = new javax.swing.JMenuItem();
        menuItemViewFeatureViewer = new javax.swing.JMenuItem();
        menuItemViewDataset = new javax.swing.JMenuItem();
        menuItemViewGraphDataset = new javax.swing.JMenuItem();
        menuItemViewParamClipboard = new javax.swing.JMenuItem();
        actionMenu = new javax.swing.JMenu();
        menuEndGesture = new javax.swing.JMenuItem();
        menuAllGesture = new javax.swing.JMenuItem();
        menuResetLog = new javax.swing.JMenuItem();
        menuFlushLog = new javax.swing.JMenuItem();
        menuPerformanceMode = new javax.swing.JCheckBoxMenuItem();
        menuEnableOscControl = new javax.swing.JCheckBoxMenuItem();
        helpMenu1 = new javax.swing.JMenu();
        contentsMenuItem1 = new javax.swing.JMenuItem();
        aboutMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("The Wekinator");
        setMinimumSize(new java.awt.Dimension(300, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 600));

        jPanel1.setPreferredSize(new java.awt.Dimension(750, 750));

        panelMainTabs.setMinimumSize(new java.awt.Dimension(500, 500));
        panelMainTabs.setPreferredSize(new java.awt.Dimension(500, 500));
        panelMainTabs.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panelMainTabsComponentShown(evt);
            }
        });
        panelMainTabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                panelMainTabsStateChanged(evt);
            }
        });

        panelOSC.setPreferredSize(new java.awt.Dimension(500, 500));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Simple option: Run chuck backend from here"));

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(chuckRunnerPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 520, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(265, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(chuckRunnerPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Simple", jPanel8);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Advanced option: Connect to chuck backend running in terminal"));

        buttonOscConnect.setText("Connect");
        buttonOscConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOscConnectActionPerformed(evt);
            }
        });

        buttonOscDisconnect.setText("Disconnect");
        buttonOscDisconnect.setEnabled(false);
        buttonOscDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOscDisconnectActionPerformed(evt);
            }
        });

        labelOscStatus.setText("OSC Status: Not connected yet.");

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 2, 13));
        jLabel1.setText("Manually connect only if you're running ChucK from command line");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(buttonOscConnect)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(buttonOscDisconnect))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                        .add(23, 23, 23)
                        .add(labelOscStatus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
                    .add(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel1)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 181, Short.MAX_VALUE)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(buttonOscConnect)
                    .add(buttonOscDisconnect))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(labelOscStatus)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Advanced", jPanel5);

        org.jdesktop.layout.GroupLayout panelOSCLayout = new org.jdesktop.layout.GroupLayout(panelOSC);
        panelOSC.setLayout(panelOSCLayout);
        panelOSCLayout.setHorizontalGroup(
            panelOSCLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelOSCLayout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 818, Short.MAX_VALUE))
        );
        panelOSCLayout.setVerticalGroup(
            panelOSCLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        panelMainTabs.addTab("Chuck & OSC Setup", panelOSC);

        org.jdesktop.layout.GroupLayout panelTabFeatureConfigurationLayout = new org.jdesktop.layout.GroupLayout(panelTabFeatureConfiguration);
        panelTabFeatureConfiguration.setLayout(panelTabFeatureConfigurationLayout);
        panelTabFeatureConfigurationLayout.setHorizontalGroup(
            panelTabFeatureConfigurationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(featureConfigurationPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        panelTabFeatureConfigurationLayout.setVerticalGroup(
            panelTabFeatureConfigurationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(featureConfigurationPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        panelMainTabs.addTab("Features Setup", panelTabFeatureConfiguration);

        org.jdesktop.layout.GroupLayout panelTabLearningSystemConfigurationLayout = new org.jdesktop.layout.GroupLayout(panelTabLearningSystemConfiguration);
        panelTabLearningSystemConfiguration.setLayout(panelTabLearningSystemConfigurationLayout);
        panelTabLearningSystemConfigurationLayout.setHorizontalGroup(
            panelTabLearningSystemConfigurationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, learningSystemConfigurationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
        );
        panelTabLearningSystemConfigurationLayout.setVerticalGroup(
            panelTabLearningSystemConfigurationLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelTabLearningSystemConfigurationLayout.createSequentialGroup()
                .add(learningSystemConfigurationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 551, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        panelMainTabs.addTab("Learning Setup", panelTabLearningSystemConfiguration);
        panelMainTabs.addTab("Use it!", trainRunPanel1);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 750, Short.MAX_VALUE)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(panelMainTabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 750, Short.MAX_VALUE)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .add(panelMainTabs, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jScrollPane1.setViewportView(jPanel1);

        fileMenu.setText("File");

        menuSaveLearningSystem.setText("Save learning system");
        menuSaveLearningSystem.setEnabled(false);
        menuSaveLearningSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveLearningSystemActionPerformed(evt);
            }
        });
        fileMenu.add(menuSaveLearningSystem);

        jMenuItem2.setText("Save model(s)");
        jMenuItem2.setEnabled(false);
        fileMenu.add(jMenuItem2);

        menuSaveDataset.setText("Save dataset");
        menuSaveDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveDatasetActionPerformed(evt);
            }
        });
        fileMenu.add(menuSaveDataset);

        jMenuItem4.setText("Load global configuration");
        jMenuItem4.setEnabled(false);
        fileMenu.add(jMenuItem4);

        jMenuItem1.setText("Save global configuration");
        jMenuItem1.setEnabled(false);
        fileMenu.add(jMenuItem1);

        menuBar.add(fileMenu);

        viewMenu.setText("View");

        menuItemViewConsole.setText("Console");
        menuItemViewConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewConsoleActionPerformed(evt);
            }
        });
        viewMenu.add(menuItemViewConsole);

        menuItemViewFeatureViewer.setText("Feature viewer");
        menuItemViewFeatureViewer.setEnabled(false);
        menuItemViewFeatureViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewFeatureViewerActionPerformed(evt);
            }
        });
        viewMenu.add(menuItemViewFeatureViewer);

        menuItemViewDataset.setText("Examples (dataset)");
        menuItemViewDataset.setEnabled(false);
        menuItemViewDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewDatasetActionPerformed(evt);
            }
        });
        viewMenu.add(menuItemViewDataset);

        menuItemViewGraphDataset.setText("Graphical dataset editor");
        menuItemViewGraphDataset.setEnabled(false);
        menuItemViewGraphDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewGraphDatasetActionPerformed(evt);
            }
        });
        viewMenu.add(menuItemViewGraphDataset);

        menuItemViewParamClipboard.setText("Parameter clipboard");
        menuItemViewParamClipboard.setEnabled(false);
        menuItemViewParamClipboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewParamClipboardActionPerformed(evt);
            }
        });
        viewMenu.add(menuItemViewParamClipboard);

        menuBar.add(viewMenu);

        actionMenu.setText("Actions");

        menuEndGesture.setText("Label gesture ends");
        menuEndGesture.setEnabled(false);
        menuEndGesture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEndGestureActionPerformed(evt);
            }
        });
        actionMenu.add(menuEndGesture);

        menuAllGesture.setText("Label whole gestures");
        menuAllGesture.setEnabled(false);
        actionMenu.add(menuAllGesture);

        menuResetLog.setText("Reset log");
        menuResetLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuResetLogActionPerformed(evt);
            }
        });
        actionMenu.add(menuResetLog);

        menuFlushLog.setText("Flush log file");
        menuFlushLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFlushLogActionPerformed(evt);
            }
        });
        actionMenu.add(menuFlushLog);

        menuPerformanceMode.setText("Performance mode");
        menuPerformanceMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPerformanceModeActionPerformed(evt);
            }
        });
        actionMenu.add(menuPerformanceMode);

        menuEnableOscControl.setText("Enable OSC control of GUI");
        menuEnableOscControl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEnableOscControlActionPerformed(evt);
            }
        });
        actionMenu.add(menuEnableOscControl);

        menuBar.add(actionMenu);

        helpMenu1.setText("Help");
        helpMenu1.setEnabled(false);

        contentsMenuItem1.setText("Contents");
        helpMenu1.add(contentsMenuItem1);

        aboutMenuItem1.setText("About");
        aboutMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItem1ActionPerformed(evt);
            }
        });
        helpMenu1.add(aboutMenuItem1);

        menuBar.add(helpMenu1);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectOSC() {
        try {
            OscHandler.getOscHandler().startHandshake();
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

private void buttonOscConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOscConnectActionPerformed
    //try {
    //    OscHandler.getOscHandler().setupOsc();
    //} catch (IOException ex) {
    //    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    // }
    connectOSC();
}//GEN-LAST:event_buttonOscConnectActionPerformed

private void buttonOscDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOscDisconnectActionPerformed
    try {
        if (FeatureExtractionController.isExtracting()) {
            FeatureExtractionController.stopExtracting();
        }
        OscHandler.getOscHandler().end();
        OscHandler.getOscHandler().setupOsc();
    } catch (IOException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_buttonOscDisconnectActionPerformed

private void panelMainTabsComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelMainTabsComponentShown
    System.out.println("Component shown");
}//GEN-LAST:event_panelMainTabsComponentShown

private void panelMainTabsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_panelMainTabsStateChanged
   if (WekinatorRunner.isLogging()) {
        Component c = panelMainTabs.getSelectedComponent();
        if (c == panelOSC) {
            Plog.log(Msg.PANEL_CHUCK_VIEW);
        } else if (c == panelTabFeatureConfiguration) {
            Plog.log(Msg.PANEL_FEATURES_VIEW);
        } else if (c == panelTabLearningSystemConfiguration) {
            Plog.log(Msg.PANEL_LEARNING_VIEW);
        } else if (c == trainRunPanel1) {
            Plog.log(Msg.PANEL_USE_VIEW);
        }
    }

}//GEN-LAST:event_panelMainTabsStateChanged

private void exit() {
     if (FeatureExtractionController.isExtracting()) {
        FeatureExtractionController.stopExtracting();
    }


    OscHandler.getOscHandler().end();

    if (ChuckRunner.getRunnerState() == ChuckRunner.ChuckRunnerState.RUNNING) {
        try {
            ChuckRunner.stop();
        } catch (IOException ex) {
        }
    }
    //Want to save settings here!
    wek.saveCurrentSettings();
        try {
          if (WekinatorRunner.isLogging()) {
              Plog.log(Msg.CLOSE);
            Plog.close();
          }
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    System.exit(0);

}


private void menuItemViewConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewConsoleActionPerformed
    Console c = Console.getInstance();
    if (c.isVisible()) {
        c.toFront();
    } else {
        c.setVisible(true);
    }
}//GEN-LAST:event_menuItemViewConsoleActionPerformed

private void aboutMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItem1ActionPerformed
    //SHow something about wekinator TODO
}//GEN-LAST:event_aboutMenuItem1ActionPerformed

private void menuItemViewDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewDatasetActionPerformed
    if ( WekinatorInstance.getWekinatorInstance().getLearningSystem() != null
            && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null) {
        WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset().showViewer();
    }
}//GEN-LAST:event_menuItemViewDatasetActionPerformed

private void menuItemViewParamClipboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewParamClipboardActionPerformed
   // LearningSystem learningSystem = WekinatorInstance.getWekinatorInstance().getLearningSystem();
    if (WekinatorInstance.getWekinatorInstance().getPlayalongScore() != null) {
        WekinatorInstance.getWekinatorInstance().getPlayalongScore().view();
    }

}//GEN-LAST:event_menuItemViewParamClipboardActionPerformed

private void menuItemViewFeatureViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewFeatureViewerActionPerformed
    FeatureExtractionController.showFeatureViewer();
}//GEN-LAST:event_menuItemViewFeatureViewerActionPerformed

private void menuItemViewGraphDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewGraphDatasetActionPerformed
    LearningSystem ls = WekinatorInstance.getWekinatorInstance().getLearningSystem();
    if (ls != null && ls.getDataset() != null) {
        new GraphDataViewFrame(ls.getDataset()).setVisible(true);
        if (WekinatorRunner.isLogging()) {
            Plog.log(Msg.GRAPHICAL_VIEWER_OPENED);
        }
    }

}//GEN-LAST:event_menuItemViewGraphDatasetActionPerformed

private void menuEndGestureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEndGestureActionPerformed
    boolean[] mask = trainRunPanel1.getMask();
    LearningSystem ls = WekinatorInstance.getWekinatorInstance().getLearningSystem();
    if (WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && ls.getDataset() != null) {
        ls.getDataset().processEndGestures(mask);
    }

}//GEN-LAST:event_menuEndGestureActionPerformed

private void menuSaveDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveDatasetActionPerformed
    LearningSystem ls = WekinatorInstance.getWekinatorInstance().getLearningSystem();
    if (ls != null && ls.getDataset() != null) {

        File file = Util.findSaveFile("arff",
                "arff file",
                SimpleDataset.getDefaultLocation(),
                this);
        if (file != null) {
            try {
                ls.getDataset().writeInstancesToArff(file);
                if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.MENU_SAVE_ARFF, file.getAbsolutePath() + "/" + file.getName());
                }
            // ls.writeToFile(file); //TODOTODOTODO: update last path on this.
             Util.setLastFile(SimpleDataset.getFileExtension(), file);
            } catch (Exception ex) {
                Logger.getLogger(TrainRunPanel.class.getName()).log(Level.INFO, null, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not save to file", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}//GEN-LAST:event_menuSaveDatasetActionPerformed

private void menuSaveLearningSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveLearningSystemActionPerformed
    //Save learning system
    if (WekinatorInstance.getWekinatorInstance().getLearningSystem() == null)
        return;

    File file = Util.findSaveFile(LearningSystem.getFileExtension(),
                LearningSystem.getFileTypeDescription(),
                LearningSystem.getDefaultLocation(),
                this);
        if (file != null) {
            try {
                WekinatorInstance.getWekinatorInstance().getLearningSystem().writeToFile(file); //TODOTODOTODO: update last path on this.
                Util.setLastFile(LearningSystem.getFileExtension(), file);
            } catch (Exception ex) {
                Logger.getLogger(TrainRunPanel.class.getName()).log(Level.INFO, null, ex);
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not save to file", JOptionPane.ERROR_MESSAGE);
            }
        }

}//GEN-LAST:event_menuSaveLearningSystemActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    System.out.println("closing");
    exit();
}//GEN-LAST:event_formWindowClosing

private void menuResetLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuResetLogActionPerformed
    int lResponse = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset the log?\n"
            + "Please do this ONLY at the very beginning of Part A / Part B of your PLOrk assignment.\n" 
            + "If you do it later, it will erase needed logging info.", "", JOptionPane.YES_NO_OPTION);
                if (lResponse == JOptionPane.YES_OPTION) {
                    if (WekinatorRunner.isLogging()) {
                        Plog.startPlog();
                    }
                }

    
}//GEN-LAST:event_menuResetLogActionPerformed

private void menuFlushLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFlushLogActionPerformed
    if (WekinatorRunner.isLogging()) {
        Plog.flush();
    }
}//GEN-LAST:event_menuFlushLogActionPerformed

private void menuPerformanceModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPerformanceModeActionPerformed
    if (menuPerformanceMode.isSelected()) {
        int lResponse = JOptionPane.showConfirmDialog(this, "Are you sure you want set to performance mode?\n"
            + "This will turn off some logging, so please don't do this while working on your plork assignment.\n", "", JOptionPane.YES_NO_OPTION);
                if (lResponse == JOptionPane.YES_OPTION) {
                    if (WekinatorRunner.isLogging()) {
                        Plog.performanceMode = true;
                    }
                }

    } else {
        if (WekinatorRunner.isLogging()) {
            Plog.performanceMode = false;
        }
    }
}//GEN-LAST:event_menuPerformanceModeActionPerformed

private void menuEnableOscControlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEnableOscControlActionPerformed
    OscController.setOscControllable(menuEnableOscControl.isSelected());
}//GEN-LAST:event_menuEnableOscControlActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem1;
    private javax.swing.JMenu actionMenu;
    private javax.swing.ButtonGroup buttonGroupClassifierSource;
    private javax.swing.ButtonGroup buttonGroupProcessingSource;
    private javax.swing.ButtonGroup buttonGroupSettingsSource;
    private javax.swing.JButton buttonOscConnect;
    private javax.swing.JButton buttonOscDisconnect;
    private wekinator.ChuckRunnerPanel chuckRunnerPanel1;
    private javax.swing.JMenuItem contentsMenuItem1;
    private wekinator.FeatureConfigurationPanel featureConfigurationPanel1;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelOscStatus;
    private wekinator.LearningSystemConfigurationPanel learningSystemConfigurationPanel;
    private javax.swing.JMenuItem menuAllGesture;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem menuEnableOscControl;
    private javax.swing.JMenuItem menuEndGesture;
    private javax.swing.JMenuItem menuFlushLog;
    private javax.swing.JMenuItem menuItemViewConsole;
    private javax.swing.JMenuItem menuItemViewDataset;
    private javax.swing.JMenuItem menuItemViewFeatureViewer;
    private javax.swing.JMenuItem menuItemViewGraphDataset;
    private javax.swing.JMenuItem menuItemViewParamClipboard;
    private javax.swing.JCheckBoxMenuItem menuPerformanceMode;
    private javax.swing.JMenuItem menuResetLog;
    private javax.swing.JMenuItem menuSaveDataset;
    private javax.swing.JMenuItem menuSaveLearningSystem;
    private javax.swing.JTabbedPane panelMainTabs;
    private javax.swing.JPanel panelOSC;
    private javax.swing.JPanel panelTabFeatureConfiguration;
    private javax.swing.JPanel panelTabLearningSystemConfiguration;
    private wekinator.TrainRunPanel trainRunPanel1;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables

    private void oscHandlerPropertyChange(PropertyChangeEvent evt) {
        updateGUIforOscStatus();
        if (evt.getPropertyName().equals(OscHandler.PROP_CONNECTIONSTATE)) {
            OscHandler.ConnectionState o = (OscHandler.ConnectionState) evt.getOldValue();
            OscHandler.ConnectionState n = (OscHandler.ConnectionState) evt.getNewValue();

            if (n == OscHandler.ConnectionState.CONNECTED) {
               // panelMainTabs.setSelectedComponent(panelTabFeatureConfiguration);
                showFeatureConfigurationPanel();
                if (WekinatorRunner.getFeatureFile() != null) {
                    try {
                        FeatureConfiguration fc = FeatureConfiguration.readFromFile(WekinatorRunner.getFeatureFile());
                        featureConfigurationPanel1.setFormFromConfiguration(fc);

                        // Thread.sleep(5000);
                        //  fc.validate(); //does this do it? ABC
                        WekinatorInstance.getWekinatorInstance().setFeatureConfiguration(fc);
                    } catch (Exception ex) {
                        // Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, "Unable to load feature configuration from file");
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, "Unable to load feature configuration from file");
                        Logger.getLogger(MainGUI.class.getName()).log(Level.WARNING, null, ex);
                    }

                //configuration = ChuckConfiguration.readFromFile(new File(cLoc));
                //WekinatorInstance.getWekinatorInstance().setFeatureConfiguration(featureConfiguration);

                }

            }

        }
    }

    public void showOscPanel() {
        panelMainTabs.setSelectedComponent(panelOSC);
        if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.PANEL_CHUCK_VIEW);
         }
    }

    public void showTrainRunPanel() {
        panelMainTabs.setSelectedComponent(trainRunPanel1);
        if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.PANEL_USE_VIEW);
         }
    }

    public void showFeatureConfigurationPanel() {
        panelMainTabs.setSelectedComponent(panelTabFeatureConfiguration);
        if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.PANEL_FEATURES_VIEW);
         }
    }

    public void showLearningSystemPanel() {
        panelMainTabs.setSelectedComponent(panelTabLearningSystemConfiguration);
        if (WekinatorRunner.isLogging()) {
                    Plog.log(Msg.PANEL_LEARNING_VIEW);
         }
    }

    protected void updatePanels() {
        //TODO:
        //make sure changes upstream invalidate downstream?
        //check each step -- that changing feat config necessitates valid update to learning system
        //Also make sure that panel changes automatically

        OscHandler h = OscHandler.getOscHandler();
        boolean connected = (h.getConnectionState() == OscHandler.ConnectionState.CONNECTED);
        boolean featValid = WekinatorInstance.getWekinatorInstance().getFeatureConfiguration() != null && (ChuckSystem.getChuckSystem().state == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID);
        boolean learnValid = WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && (ChuckSystem.getChuckSystem().state == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID);
        setFeatureConfigurationPanelEnabled(connected);
        setLearningSystemConfigurationPanelEnabled(connected && featValid);
        setTrainRunPanelEnabled(isConnected && featValid && learnValid);
    }

    protected void updateMenus() {
        //Enable appropriate menus: TODO
        OscHandler h = OscHandler.getOscHandler();
        boolean connected = (h.getConnectionState() == OscHandler.ConnectionState.CONNECTED);
        boolean featValid = WekinatorInstance.getWekinatorInstance().getFeatureConfiguration() != null && (ChuckSystem.getChuckSystem().state == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID);
        boolean learnValid = WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && (ChuckSystem.getChuckSystem().state == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID);
        menuSaveLearningSystem.setEnabled(WekinatorInstance.getWekinatorInstance().getLearningSystem() != null);
        menuSaveDataset.setEnabled(WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null);
        menuItemViewFeatureViewer.setEnabled(featValid);
        menuItemViewDataset.setEnabled(WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null);
        menuItemViewParamClipboard.setEnabled(learnValid);
        
        if (!WekinatorRunner.isPlork()) {
            menuEndGesture.setEnabled(WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null);
            menuAllGesture.setEnabled(WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null);
        } else {
             menuEndGesture.setEnabled(false);
             menuAllGesture.setEnabled(false);
        }

        if (! WekinatorRunner.isPlork) {
            menuItemViewGraphDataset.setEnabled(WekinatorInstance.getWekinatorInstance().getLearningSystem() != null && WekinatorInstance.getWekinatorInstance().getLearningSystem().getDataset() != null);
        } else {
           menuItemViewGraphDataset.setEnabled(false);
        }

        setFeatureConfigurationPanelEnabled(connected);
        setLearningSystemConfigurationPanelEnabled(connected && featValid);
        setTrainRunPanelEnabled(isConnected && featValid && learnValid);
    }

    protected void updateGUIforOscStatus() {


        OscHandler h = OscHandler.getOscHandler();
        labelOscStatus.setText("OSC status: " + h.getStatusMessage());
        if (h.getConnectionState() == OscHandler.ConnectionState.CONNECTED ||
                h.getConnectionState() == OscHandler.ConnectionState.CONNECTING) {
            buttonOscDisconnect.setEnabled(true);
            buttonOscConnect.setEnabled(false);
            if (h.getConnectionState() == OscHandler.ConnectionState.CONNECTED) {
                isConnected = true;
            } else {
                isConnected = false;
            }
        } else {
            isConnected = false;
            buttonOscDisconnect.setEnabled(false);
            buttonOscConnect.setEnabled(true);
        }

        /*  setFeatureConfigurationPanelEnabled(isConnected);
        if (!isConnected) {
        //  setLearningSystemConfigurationPanelEnabled(false);
        //   setTrainRunPanelEnabled(false);
        } */
        updatePanels();
        updateMenus();


    }

    //TODO: get rid of this.
    private void hidSetupPropertyChange(PropertyChangeEvent evt) {
        System.out.println("GUI RECVD HID SETUP change w/ name: " + evt.getPropertyName());
    }

    private void wekinatorInstancePropertyChangeEvent(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorInstance.PROP_CURRENTHIDSETUP)) {

            //TODO: handle null here!
            ((HidSetup) evt.getOldValue()).removePropertyChangeListener(hidSetupChangeListener);
            ((HidSetup) evt.getNewValue()).addPropertyChangeListener(hidSetupChangeListener);
        } else if (evt.getPropertyName().equals(WekinatorInstance.PROP_FEATURECONFIGURATION)) {
            // boolean e = (WekinatorInstance.getWekinatorInstance().getFeatureConfiguration() != null);
            //  System.out.println("enabling feat " + e);
            //  menuItemViewFeatures.setEnabled(e);
            updatePanels();
            updateMenus();
        } else if (evt.getPropertyName().equals(WekinatorInstance.PROP_LEARNINGSYSTEM)) {
            System.out.println("learning system changed");
            if (WekinatorInstance.getWekinatorInstance().getLearningSystem() != null) {
                showTrainRunPanel();
            }
            updatePanels();
            updateMenus();
        }
    }

    //TODO: Get all this logic out of the gui! 
    private void chuckSystemPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ChuckSystem.PROP_STATE)) {
            ChuckSystem cs = ChuckSystem.getChuckSystem();
            updateGUIforChuckSystem();
            updatePanels();
            updateMenus();
            // panelTabLearningSystemConfiguration.setEnabled(cs.getState() == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID);
            if (evt.getOldValue() != ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID && evt.getNewValue() == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID) {
                //  learningSystemConfigurationPanel.configure(cs.getNumParams(), cs.getParamNames(), cs.isIsParamDiscrete(), WekinatorInstance.getWekinatorInstance().getFeatureConfiguration());
                //WekinatorInstance.getWekinatorInstance().setNumParams(cs.getNumParams());
                //WekinatorInstance.getWekinatorInstance().setParamNames(

                //This was causing problem when feature config changed but learning system became invalid!
                if (WekinatorInstance.getWekinatorInstance().getLearningSystem() == null) {
                   // panelMainTabs.setSelectedComponent(panelTabLearningSystemConfiguration);
                   showLearningSystemPanel();
                    if (WekinatorRunner.getLearningSystemFile() != null) {
                        try {
                            LearningSystem ls = LearningSystem.readFromFile(WekinatorRunner.getLearningSystemFile());
                            if (WekinatorInstance.getWekinatorInstance().canUse(ls)) {
                                WekinatorInstance.getWekinatorInstance().setLearningSystem(ls);

                                learningSystemConfigurationPanel.setLearningSystem(ls);
                                panelMainTabs.setSelectedComponent(trainRunPanel1);
                                if (WekinatorRunner.runAutomatically) {
                                    //trainRunPanel1.
                                    //if can run:
                                    //TODO
                                    if (trainRunPanel1.canRun()) {
                                        trainRunPanel1.startAutoRun(); //put elsewhere
                                        if (WekinatorRunner.isMinimizeOnRun()) {
                                            this.setState(Frame.ICONIFIED);
                                        }
                                    } else {
                                        System.out.println("Cannot run automatically: learning system not ready");
                                    }

                                }
                            } else {
                                //TODO: more info
                                System.out.println("This learning system is not configured correctly");
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, "Could not load learning system from file");

                            Logger.getLogger(MainGUI.class.getName()).log(Level.WARNING, null, ex);
                        }
                    }
                } else {
                    showTrainRunPanel();
                }
            }
        }

    }

    private void updateGUIforChuckSystem() {
        //  setLearningSystemConfigurationPanelEnabled(ChuckSystem.getChuckSystem().getState() == ChuckSystem.ChuckSystemState.CONNECTED_AND_VALID); //TODO ABC
        //  setLearningSystemConfigurationPanelEnabled(true);
    }

    private void setFeatureConfigurationPanelEnabled(boolean enabled) {
        //panelMainTabs.setEnabledAt(panelMainTabs.indexOfComponent(panelTabFeatureConfiguration), enabled);
        panelMainTabs.setEnabledAt(panelMainTabs.indexOfComponent(panelTabFeatureConfiguration), enabled);

    }

    private void setLearningSystemConfigurationPanelEnabled(boolean enabled) {
        panelMainTabs.setEnabledAt(panelMainTabs.indexOfComponent(panelTabLearningSystemConfiguration), enabled);
    }

    private void setTrainRunPanelEnabled(boolean enabled) {
        panelMainTabs.setEnabledAt(panelMainTabs.indexOfComponent(trainRunPanel1), enabled);

    }

    private void learningManagerPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WekinatorInstance.PROP_LEARNINGSYSTEM)) {
            boolean e = (WekinatorInstance.getWekinatorInstance().getLearningSystem() != null);
            System.out.println("enabling otf data " + e);
            menuItemViewParamClipboard.setEnabled(e);
            menuItemViewDataset.setEnabled(e);

        }

    }
    // private WekaOperator w;
    int sendPort, receivePort;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*    System.setProperty("apple.laf.useScreenMenuBar", "true");
        try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                MainGUI b = new MainGUI();
                b.setVisible(true);
            }
        });
    }
}


