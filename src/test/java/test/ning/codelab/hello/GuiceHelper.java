package test.ning.codelab.hello;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ning.codelab.hello.HelloResource;
import ning.codelab.hello.HelloServerModule;
import ning.codelab.hello.persist.HelloPersistence;

/**
 * A little helper class to eliminate undue copy and paste of Guice instantiation
 * in the tests.
 */
public class GuiceHelper
{
    public static HelloResource useGuiceToInstantiateTheHelloResource()
    {
        Injector injector = Guice.createInjector(new HelloServerModule());

        HelloResource theHello = injector.getInstance(HelloResource.class);

        HelloPersistence persist = injector.getInstance(HelloPersistence.class);
 
        persist.clear();

        return theHello;
    }

}
