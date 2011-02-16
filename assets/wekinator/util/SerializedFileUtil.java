/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rebecca
 */
public class SerializedFileUtil {
    public static Object readFromFile(File f) throws Exception {
        FileInputStream instream = null;
        ObjectInputStream objin = null;
        Object o = null;
        boolean err = false;
        Exception myEx = new Exception();
        try {
            instream = new FileInputStream(f);
            objin = new ObjectInputStream(instream);
            o = objin.readObject();
        } catch (Exception ex) {
            myEx = ex;
            err = true;
            Logger.getLogger(SerializedFileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (objin != null) {
                    objin.close();
                }
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SerializedFileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (err) {
            throw myEx;
        }
        return o;
    }

     public static void writeToFile(File f, Object o) throws Exception {
        FileOutputStream outstream = null;
        ObjectOutputStream objout = null;
        boolean success = false;
        Exception myEx = new Exception();
        try {
            outstream = new FileOutputStream(f);
            objout = new ObjectOutputStream(outstream);
            objout.writeObject(o);
            success = true;
        } catch (Exception ex) {
            success = false;
            myEx = new Exception(ex.getMessage());
            Logger.getLogger(SerializedFileUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (objout != null) {
                    objout.close();
                }
                if (outstream != null) {
                    outstream.close();
                }
            } catch (IOException ex) {

                Logger.getLogger(SerializedFileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!success)
            throw myEx;

    }
}
