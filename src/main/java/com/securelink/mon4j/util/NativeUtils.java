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
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class NativeUtils
{

    public static final Logger log = LoggerFactory.getLogger( NativeUtils.class );

    public static void loadLibraryFromJar( String libName )
        throws IOException
    {
        JarFile jar = new JarFile( "lib" + File.separator + "sigar-native-deps-1.6.4.jar" );

        JarEntry myLib = jar.getJarEntry( libName );

        if ( myLib != null )
        {
            File file = File.createTempFile( "dep", null );
            FileOutputStream resourceOS = new FileOutputStream( file );
            byte[] byteArray = new byte[1024];
            int i;
            InputStream is = jar.getInputStream( myLib );

            while ( ( i = is.read( byteArray ) ) > 0 )
            {
                // Write the bytes to the output stream
                resourceOS.write( byteArray, 0, i );
            }

            // Close streams to prevent errors
            is.close();
            resourceOS.close();

            System.load( file.getAbsolutePath() );
        }
        else
        {
            log.error( "----- No native dependencies jar :( -----" );
        }
    }
}
