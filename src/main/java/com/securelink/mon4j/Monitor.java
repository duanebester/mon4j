package com.securelink.mon4j;

import com.securelink.mon4j.services.CpuService;
import com.securelink.mon4j.services.DiskService;
import com.securelink.mon4j.services.EventLogService;
import com.securelink.mon4j.services.MemoryService;
import com.securelink.mon4j.services.PingService;
import com.securelink.mon4j.services.ServicesService;
import com.securelink.mon4j.util.NativeUtils;
import com.securelink.mon4j.util.Props;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
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
        if ( loadProps() && loadNatives() )
        {
            loadServices();

            final Engine engine = new Engine();

            log.info( "Monitor Started" );

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
            log.error( "Could not load..." );
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
                        log.error( "Could not load native libs" );
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

    private static boolean loadProps()
    {
        Properties props = new Properties();
        InputStream input = null;

        try
        {
            input = Monitor.class.getClassLoader().getResourceAsStream( "mon4j.properties" );

            // input = new FileInputStream( "mon4j.properties" );

            if ( input == null )
            {
                log.error( "Sorry, unable to find mon4j.properties" );
                System.exit( 1 );
            }

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

        if ( !testAllPropsExist( props ) )
        {
            log.error( "Not all keys have values in mon4j.properties!" );
            return false;
        }

        Props.getInstance().setProperties( props );

        return true;
    }

    private static void loadServices()
    {
        Services services = Services.getInstance();

        if ( "true".equals( Props.getInstance().getProperties().getProperty( "ram.on" ) ) )
        {
            services.addService( new MemoryService() );
        }
        if ( "true".equals( Props.getInstance().getProperties().getProperty( "disk.on" ) ) )
        {
            services.addService( new DiskService() );
        }
        if ( "true".equals( Props.getInstance().getProperties().getProperty( "cpu.on" ) ) )
        {
            services.addService( new CpuService() );
        }
        if ( "true".equals( Props.getInstance().getProperties().getProperty( "ping.on" ) ) )
        {
            services.addService( new PingService() );
        }
        if ( "true".equals( Props.getInstance().getProperties().getProperty( "services.on" ) ) )
        {
            services.addService( new ServicesService() );
        }
        if ( "true".equals( Props.getInstance().getProperties().getProperty( "eventLog.on" ) ) )
        {
            services.addService( new EventLogService() );
        }
    }

    private static boolean testAllPropsExist( Properties props )
    {
        for ( Object key : props.keySet() )
        {
            if ( StringUtils.isBlank( props.getProperty( (String) key ) ) )
            {
                return false;
            }
        }

        return true;

        // return props.keySet().stream().map( ( k ) -> ( String ) k).noneMatch( ( key ) -> ( StringUtils.isBlank(
        // props.getProperty( key ) ) ) );
    }
}
