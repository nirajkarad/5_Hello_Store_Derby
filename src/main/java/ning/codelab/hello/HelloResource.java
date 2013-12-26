package ning.codelab.hello;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ning.codelab.hello.persist.HelloPersistence;
import ning.jmx.Managed;

import com.google.inject.Inject;

/**
 * Initially, a small class that implements a "message service." For
 * simplification, the service delegates to a message configured by the
 * configuration object. In the future, more complex logic may be enabled.
 * 
 * Now, we extend the HelloResource class to be a Jersey endpoint.
 * This is a jax-rs resource class. The @Path annotation says it
 * matches / (in the sense of http://localhost:9999/ )
 *
 * Different methods are annotated to handle different methods (only GET here),
 * and different Accept header media type thingamabobs.
 *
 * See http://jersey.dev.java.net/ for Jersey documentation
 * 
 * Lastly, we extend the class with management methods to get and reset a
 * hit counter.
 */
@Path("/")
public class HelloResource
{
    private final MyConfig config;
    private final HelloPersistence persist;
    
    private final AtomicLong hitCount = new AtomicLong(0);
    
    @Inject
    public HelloResource(MyConfig config, HelloPersistence persist)
    {
        this.config = config;
        this.persist = persist;
    }

    @Managed
    public long getHitCount()
    {
        return hitCount.get();
    }
    
    @Managed
    public void resetHitCount()
    {
        hitCount.set(0);
    }
    
    @GET
    @Path("/{who}/plaintext")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlainText(final @PathParam("who") String who)
    {
        return this.getMessage(who);
    }
    
    @GET
    @Path("/{who}/json")
    @Produces(MediaType.APPLICATION_JSON)
    public HelloMessage getJson(final @PathParam("who") String who)
    {
        /**
         * an inner class is used here to put an envelope around the
         * message for JSON formatting, if we just returned the message the
         * response would be:
         * 
         * "hello, world"
         *
         * with the object envelope it becomes:
         *
         * {"message":"hello, world"}
         *
         * which may or may not matter, but we want something like that often
         * enough that an example is reasonable to provide.
         */
        return new HelloMessage()
        {
            public String getMessage()
            {
                return HelloResource.this.getMessage(who);
            }
        };
    }
    
    @POST
    @Path("/{who}/{hello}")
    @Produces(MediaType.TEXT_PLAIN)
    public String putHello(final @PathParam("who") String who, final @PathParam("hello") String hello)
    {
        putHelloImpl(who, hello);
        return who +" : "+hello+"\n";
    }

    /**
     * The original "message service" implementation.
     */
    public String getMessage(String who)
    {
        hitCount.incrementAndGet();
        
        String storedResult = persist.getHello(who);
        
        return (storedResult != null) ? storedResult : config.getMessage();
    }

    /**
     * Implements "put hello"
     * 
     * @param who
     * @param hello
     */
    public void putHelloImpl(String who, String hello)
    {
        persist.putHello(who, hello);
    }

    /**
     * An inner class "response object" so that we can test more easily.
     */
    public abstract static class HelloMessage
    {
        public abstract String getMessage();
    }
}
