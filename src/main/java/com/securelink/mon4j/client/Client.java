package com.securelink.mon4j.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.DefaultCookie;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class Client
{
    private final URI uri;

    private final Logger log = LoggerFactory.getLogger( Client.class );

    public Client( URI uri )
    {
        this.uri = uri;
    }

    public void run()
        throws InterruptedException
    {
        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "localhost" : uri.getHost();
        int port = uri.getPort();

        log.info( "scheme={}, host={}, port={}", scheme, host, port );

        if ( port == -1 )
        {
            if ( "http".equalsIgnoreCase( scheme ) )
            {
                port = 80;
            }
            else if ( "https".equalsIgnoreCase( scheme ) )
            {
                port = 443;
            }
        }

        if ( !"http".equalsIgnoreCase( scheme ) && !"https".equalsIgnoreCase( scheme ) )
        {
            log.error( "Only HTTP(S) is supported." );
            return;
        }

        boolean ssl = "https".equalsIgnoreCase( scheme );

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap();
            b.group( group ).channel( NioSocketChannel.class ).handler( new HttpClientInitializer( ssl ) );

            // Make the connection attempt.
            Channel ch = b.connect( host, port ).sync().channel();

            // Prepare the HTTP request.
            HttpRequest request = new DefaultFullHttpRequest( HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath() );
            request.headers().set( HttpHeaders.Names.HOST, host );
            request.headers().set( HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE );
            request.headers().set( HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP );

            // Set some example cookies.
            // request.headers().set( HttpHeaders.Names.COOKIE, ClientCookieEncoder.encode( new DefaultCookie(
            // "my-cookie", "foo" ), new DefaultCookie( "another-cookie", "bar" ) ) );

            // Send the HTTP request.
            ch.writeAndFlush( request );

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
        }
        finally
        {
            // Shut down executor threads to exit.
            group.shutdownGracefully();
        }
    }
}
