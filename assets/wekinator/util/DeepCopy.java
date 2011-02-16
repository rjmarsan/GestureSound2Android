/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wekinator.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * http://javatechniques.com/blog/faster-deep-copies-of-java-objects/
 */
public class DeepCopy {

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
     // http://javatechniques.com/blog/faster-deep-copies-of-java-objects/
    public static Object copy(Object orig) throws IOException, ClassNotFoundException {
        Object obj = null;
        
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
       
        
        return obj;
    }


}
