package com.securelink.mon4j;

import com.securelink.mon4j.services.CpuService;
import com.securelink.mon4j.services.DiskService;
import com.securelink.mon4j.services.MemoryService;
import com.securelink.mon4j.util.NativeUtils;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 *
 */

public class Monitor 
{
    public static final Logger log = LoggerFactory.getLogger( Monitor.class );
    
    public static void main( String args[] )
    {
        boolean loaded;
        
        try
        {
            String osName = System.getProperty( "os.name" ).replaceAll( " ", "" ).replaceAll( "\"", "" ).toLowerCase().trim();
            String osArch = System.getProperty("os.arch").replaceAll("amd64", "x86_64").replaceAll("i386", "x86_64").trim();
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("native").append(File.separator).append(osName).append(File.separator).append(osArch).append(File.separator);
            
            if ( null != osName )
            {
                switch (osName) 
                {
                    case "windows":
                        if ( "x86_64".equals( osArch ) )
                            NativeUtils.loadLibraryFromJar(sb.append("sigar-amd64-linux.dll").toString());
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
                            NativeUtils.loadLibraryFromJar(sb.append("libsigar-ia64-linux.so").toString());
                        else
                            NativeUtils.loadLibraryFromJar(sb.append("libsigar-x86-linux.so" ).toString());
                        break;
                    case "macosx":
                        if ( "x86_64".equals( osArch ) )
                            NativeUtils.loadLibraryFromJar(sb.append("libsigar-universal64-macosx.dylib").toString());
                        else
                            NativeUtils.loadLibraryFromJar(sb.append("libsigar-universal-macosx.dylib").toString());
                        break;
                }
            }

            loaded = true;
            
        } catch (IOException ex) 
        {
            log.error(ex.getMessage());
            loaded = false;
        }
        
        
        if ( loaded )
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
                    log.info("Shutting down Engine...");

                    if ( engine != null )
                    {
                        engine.stop();
                    }
                }
            });
        }
    }
}
