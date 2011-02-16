/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator.util;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import wekinator.WekinatorInstance;

/**
 *
 * @author rebecca
 */
public class Util {



    public static String getCanonicalPath(File f) {
            String s;
        try {
            s = f.getCanonicalPath();
        } catch (IOException ex) {
            s = f.getAbsolutePath();
        }
            return s;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    //Uses last file if not null, otherwise looks in dir
    //uses relative default directory
    public static File findLoadFile(String ext, String description, String defDir, Component c) {
       /* File defaultFile = null;
        if (defFile != null)
            defaultFile = new File(defFile); */
        String lastLoc = WekinatorInstance.getWekinatorInstance().getSettings().getLastLocation(ext);
        File defaultFile = null;
        if (lastLoc != null) {
            defaultFile = new File(lastLoc);
        }

        File defaultDir = null;
        if (defDir != null && defaultFile == null) {
            defaultDir = new File(WekinatorInstance.getWekinatorInstance().getSettings().getDefaultSettingsDirectory(), defDir);
        }
        
        FileChooserWithExtension fc = new FileChooserWithExtension(
                ext,
                description,
                defaultFile,
                defaultDir,
                false);
        
        File file = null;
        int returnVal = fc.showOpenDialog(c);
        if (returnVal == FileChooserWithExtension.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        return file;
    }

     public static File findSaveFile(String ext, String description, String defDir, Component c) {
        String lastLoc = WekinatorInstance.getWekinatorInstance().getSettings().getLastLocation(ext);
        File defaultFile = null;
        if (lastLoc != null) {
            defaultFile = new File(lastLoc);
        } else {
            defaultFile = new File(WekinatorInstance.getWekinatorInstance().getSettings().getDefaultSettingsDirectory() + File.separator + defDir,
                    "newFile." + ext);
        }

        FileChooserWithExtension fc = new FileChooserWithExtension(
                ext,
                description,
                defaultFile,
                null,
                true);

        File file = null;
        int returnVal = fc.showSaveDialog(c);
        if (returnVal == FileChooserWithExtension.APPROVE_OPTION) {
            file = fc.getSelectedFile();
           // fc.getCu
        }
        return file;

     }

    public static void setLastFile(String fileExtension, File file) {
        WekinatorInstance.getWekinatorInstance().getSettings().setLastLocation(fileExtension, getCanonicalPath(file));
    }


    

}
