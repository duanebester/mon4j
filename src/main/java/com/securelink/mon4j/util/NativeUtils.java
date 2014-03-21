package com.securelink.mon4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class NativeUtils {
    
    public static final Logger log = LoggerFactory.getLogger( NativeUtils.class );
    
    public static void loadLibraryFromJar(String libName) throws IOException
    {
        log.info("lib" + File.separator + "sigar-native-deps-1.6.4.jar");
        
        File lib = new File("lib");
        
        log.info( "lib exists: {}, is directory: {}, can read: {}", lib.exists(), lib.isDirectory(), lib.canRead());
        
        File ourjar = new File("lib" + File.separator + "sigar-native-deps-1.6.4.jar");
        
        log.info( "jar exists: {}, is directory: {}, can read: {}", ourjar.exists(), ourjar.isDirectory(), ourjar.canRead());
        
        JarFile jar = new JarFile("lib" + File.separator + "sigar-native-deps-1.6.4.jar");
        
        jar.stream().forEach((entry) -> {
            log.info(entry.toString());
        });
        
        
        log.info(jar.toString());
        
        log.info("LibName: {}", libName);
            
        JarEntry myLib = jar.getJarEntry(libName);

        if ( myLib != null )
        {
            File file = File.createTempFile("dep",  null);
            FileOutputStream resourceOS = new FileOutputStream(file);
            byte[] byteArray = new byte[1024];
            int i;
            InputStream is = jar.getInputStream(myLib);

            while ((i = is.read(byteArray)) > 0) 
            {
                //Write the bytes to the output stream
                resourceOS.write(byteArray, 0, i);
            }

            //Close streams to prevent errors
            is.close();
            resourceOS.close();

            System.load(file.getAbsolutePath());
        }
        else
        {
            log.error("null myLib");
        }
    } 
}
