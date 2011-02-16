/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wekinator.util;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Prompts the user when overwriting.
 *
 * @author rebecca
 */
public class OverwritePromptingFileChooser extends JFileChooser {

    @Override
    public void approveSelection() {
        File selected = getSelectedFile();
        if (selected != null && selected.exists()) {
            int lResponse = JOptionPane.showConfirmDialog(this,
                    "The file " + selected.getName() + " already exists. Do you " +
                    "want to replace the existing file?", "",
                    JOptionPane.YES_NO_OPTION);

            if (lResponse != JOptionPane.YES_OPTION) {
                return;
            }
        }
        super.approveSelection();
    }
}
