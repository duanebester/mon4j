package link.duane.mon4j.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:duane@securelink.com">Duane Bester</a>
 */

public class HttpClientHandler
    extends SimpleChannelInboundHandler<HttpObject>
{
    private final Logger log = LoggerFactory.getLogger( HttpClientHandler.class );

    @Override
    public void messageReceived( ChannelHandlerContext ctx, HttpObject msg )
        throws Exception
    {
        if ( msg instanceof HttpResponse )
        {
            HttpResponse response = (HttpResponse) msg;

            log.info( "STATUS: " + response.getStatus() );
            log.info( "VERSION: " + response.getProtocolVersion() );
            log.info( "\n" );

            if ( !response.headers().isEmpty() )
            {
                for ( String name : response.headers().names() )
                {
                    for ( String value : response.headers().getAll( name ) )
                    {
                        log.info( "HEADER: " + name + " = " + value );
                    }
                }
                log.info( "\n" );
            }

            // if (!response.headers().isEmpty()) {
            // response.headers().names().stream().forEach((name) -> {
            // response.headers().getAll(name).stream().forEach((value) -> {
            // log.info("HEADER: " + name + " = " + value);
            // });
            // });
            // log.info( "\n" );
            // }

            if ( HttpHeaders.isTransferEncodingChunked( response ) )
            {
                log.info( "CHUNKED CONTENT {" );
            }
            else
            {
                log.info( "CONTENT {" );
            }
        }
        if ( msg instanceof HttpContent )
        {
            HttpContent content = (HttpContent) msg;

            log.info( content.content().toString( CharsetUtil.UTF_8 ) );

            if ( content instanceof LastHttpContent )
            {
                log.info( "} END OF CONTENT" );
            }
        }
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause )
        throws Exception
    {
        log.info( cause.getMessage() );
        ctx.close();
    }
}