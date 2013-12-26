package test.ning.codelab.hello;

import org.testng.annotations.Test;

import ning.codelab.hello.HelloResource;

/**
 * HelloResource TestNG module.
 */
public class HelloResourceTest
{
    @Test
    public void testHelloDefault()
    {
        HelloResource theHello = GuiceHelper.useGuiceToInstantiateTheHelloResource();
        
        assert "hello, world".equals(theHello.getMessage("nobody 1"));
        assert "hello, world".equals(theHello.getMessage("nobody 2"));
    }

    @Test
    public void testHelloSystemProperty()
    {
        String helloPropertyName = "xn.hello.message"; // see MyConfig class
        String emergencyBroadcastSystem = "this is only a test";
        System.setProperty(helloPropertyName, emergencyBroadcastSystem);

        HelloResource theHello = GuiceHelper.useGuiceToInstantiateTheHelloResource();
        
        assert emergencyBroadcastSystem.equals(theHello.getMessage("nobody 1"));
        assert emergencyBroadcastSystem.equals(theHello.getMessage("nobody 2"));
    }
}
