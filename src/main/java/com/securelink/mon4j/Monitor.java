package com.securelink.mon4j;

import com.securelink.mon4j.services.CpuService;
import com.securelink.mon4j.services.DiskService;
import com.securelink.mon4j.services.MemoryService;
import com.securelink.mon4j.util.NativeUtils;
import com.securelink.mon4j.util.Props;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class Monitor
{
    public static final Logger log = LoggerFactory.getLogger( Monitor.class );

    public static void main( String args[] )
    {
        log.info( "Monitor Started" );

        loadProps();

        if ( loadNatives() )
        {
            Services services = Services.getInstance();
            services.addService( new MemoryService() );
            services.addService( new DiskService() );
            services.addService( new CpuService() );

            Engine engine = new Engine();

            engine.start();

            Runtime.getRuntime().addShutdownHook( new Thread()
            {
                @Override
                public void run()
                {
                    log.info( "Shutting down Engine..." );

                    if ( engine != null )
                    {
                        engine.stop();
                    }
                }
            } );
        }
        else
        {
            log.error( "Could not load native libs" );
            System.exit( 1 );
        }
    }

    private static boolean loadNatives()
    {
        try
        {
            String osName = System.getProperty( "os.name" ).replaceAll( " ", "" ).replaceAll( "\"", "" ).replaceAll( "[0-9]", "" ).toLowerCase().trim();
            String osArch = System.getProperty( "os.arch" ).replaceAll( "amd64", "x86_64" ).replaceAll( "i386", "x86_64" ).trim();

            Props.getInstance().getProperties().setProperty( "os.name", osName );
            Props.getInstance().getProperties().setProperty( "os.arch", osArch );

            log.info( "------------------------------------------------" );
            log.info( "        OS-Name: {} OS-Arch: {}", osName, osArch );
            log.info( "------------------------------------------------\n" );

            StringBuilder sb = new StringBuilder();

            sb.append( "native" ).append( "/" ).append( osName ).append( "/" ).append( osArch ).append( "/" );

            if ( null != osName )
            {
                switch ( osName )
                {
                    case "windows":
                        if ( "x86_64".equals( osArch ) )
                            NativeUtils.loadLibraryFromJar( sb.append( "sigar-amd64-winnt.dll" ).toString() );
                        else
                        {
                            // Load multiple
                            String natives = sb.toString();
                            NativeUtils.loadLibraryFromJar( natives + "sigar-x86-winnt.dll" );
                            NativeUtils.loadLibraryFromJar( natives + "sigar-x86-winnt.lib" );
                        }
                        break;
                    case "linux":
                        if ( "x86_64".equals( osArch ) )
                            NativeUtils.loadLibraryFromJar( sb.append( "libsigar-ia64-linux.so" ).toString() );
                        else
                            NativeUtils.loadLibraryFromJar( sb.append( "libsigar-x86-linux.so" ).toString() );
                        break;
                    case "macosx":
                        if ( "x86_64".equals( osArch ) )
                            NativeUtils.loadLibraryFromJar( sb.append( "libsigar-universal64-macosx.dylib" ).toString() );
                        else
                            NativeUtils.loadLibraryFromJar( sb.append( "libsigar-universal-macosx.dylib" ).toString() );
                        break;
                    default:
                        log.error( "Invalid OS name?" );
                        return false;
                }
            }

            return true;

        }
        catch ( IOException ex )
        {
            log.error( ex.getMessage() );
            return false;
        }
    }

    private static void loadProps()
    {
        Properties props = new Properties();
        InputStream input = null;

        try
        {
            // input = Monitor.class.getClassLoader().getResourceAsStream( "mon4j.properties" );

            input = new FileInputStream( "mon4j.properties" );

            // if ( input == null )
            // {
            // log.error( "Sorry, unable to find mon4j.properties" );
            // System.exit( 1 );
            // }

            props.load( input );
        }
        catch ( IOException ex )
        {
            log.error( ex.getMessage() );
            System.exit( 1 );
        }
        finally
        {
            if ( input != null )
            {
                try
                {
                    input.close();
                }
                catch ( IOException e )
                {
                    log.error( e.getMessage() );
                    System.exit( 1 );
                }
            }
        }

        Props.getInstance().setProperties( props );
    }
}
