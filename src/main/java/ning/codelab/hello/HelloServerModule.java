package ning.codelab.hello;

import static com.google.common.collect.ImmutableMap.of;
import ning.codelab.hello.json.JacksonJsonProviderWrapper;
import ning.codelab.hello.persist.HelloPersistence;
import ning.codelab.hello.persist.impl.map.HelloPersistenceHashMapImpl;
import ning.configamajig.v1.ConfigModule;
import ning.guice.lifecycle.LifecycleModule;
import ning.jackson.guice.CustomObjectMapperProvider;
import ning.jackson.serializers.DateTimeSerializer;
import ning.jmx.JMXModule;
import ning.jmx.ManagedJmxExportScope;
import ning.log.jmx.LoggingModule;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.container.filter.GZIPContentEncodingFilter;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * This is the root module for the Hello server. It will install other modules
 * and configure what is needed for the hello core. See
 * <a href=http://code.google.com/p/google-guice/>http://code.google.com/p/google-guice/</a>
 * for more information on Google Guice.
 */
public class HelloServerModule extends ServletModule
{
    protected void configureServlets()
    {
        /*
         * Install the Configamajig module which populates objects using System
         * properties (or defaults provided in the annotations).
         */
        install(new ConfigModule());

        /*
         * The config class works with the ConfigModule to convert system
         * properties in java objects via Configamajig
         * (http://git.source.ninginc.com/ning-libs-configamajig.git).
         * The "EagerSingleton" just tells Guice "do this NOW" (as
         * opposed to later in the request cycle).
         */
        bind(MyConfig.class).toProvider(ConfigModule.provide(MyConfig.class)).asEagerSingleton();

        /* Install the Ning JMX module */
        install(new JMXModule());
        
        /* Install the Ning Logging module */
        install(new LoggingModule());
        
        /* Install the Ning Lifecycle module */
        install(new LifecycleModule());

        /* Bind Hello Persistence to Map implementation */
        bind(HelloPersistence.class).toProvider(
            ConfigModule.provide(HelloPersistenceHashMapImpl.class)
            ).in(new ManagedJmxExportScope("ning.codelab.hello.persist:name=HelloPersistence"));
        
        /*
         * These next two bindings configure Jackson
         * ( http://jackson.codehaus.org/ ) for generating JSON, which is our
         * most commonly used representation media type for resources.
         */
        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderWrapper.class)
            .asEagerSingleton();

        /*
         * This configures the Jackson object mapper to convert JodaTime
         * DateTime instances into ISO date/time formats.
         * 
         * ( http://joda-time.sourceforge.net/ )
         */
        bind(ObjectMapper.class).toProvider(new CustomObjectMapperProvider().addGenericSerializer(DateTime.class, new DateTimeSerializer())).asEagerSingleton();

        /*
         * This instructs the filter to serve all requests through
         * GuiceContainer, which is actually just Jersey-via-Guice, and to
         * enable gzip compression in and out.
         */
        serve("*").with(GuiceContainer.class,
            of("com.sun.jersey.spi.container.ContainerRequestFilters", GZIPContentEncodingFilter.class.getName(),
               "com.sun.jersey.spi.container.ContainerResponseFilters", GZIPContentEncodingFilter.class.getName()));

        /*
         * Bind a JMX-enabled HelloResource.
         */
        bind(HelloResource.class).in(new ManagedJmxExportScope("ning.codelab.hello:name=HelloResource"));
    }
}
