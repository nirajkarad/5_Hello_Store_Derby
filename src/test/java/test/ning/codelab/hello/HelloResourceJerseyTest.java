package test.ning.codelab.hello;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ning.codelab.hello.HelloResource;

/**
 * HelloResource TestNG module.
 */
public class HelloResourceJerseyTest
{
    /** see MyConfig class */
    private static final String XN_HELLO_MESSAGE_PROPERTY_KEY = "xn.hello.message";

    @BeforeMethod
    public void setUp()
    {
        System.clearProperty(XN_HELLO_MESSAGE_PROPERTY_KEY);
    }

    @Test
    public void testHelloDefaultPlaintext()
    {
        HelloResource theHello = GuiceHelper.useGuiceToInstantiateTheHelloResource();

        assert "hello, world".equals(theHello.getPlainText("somebody"));
        
        theHello.putHello("somebody", "knock knock");
        assert "knock knock".equals(theHello.getPlainText("somebody"));
        
        assert "hello, world".equals(theHello.getPlainText("nobody"));
    }

    @Test
    public void testHelloDefaultJson()
    {
        HelloResource theHello = GuiceHelper.useGuiceToInstantiateTheHelloResource();

        assert "hello, world".equals(theHello.getJson("nobody").getMessage());
        assert "hello, world".equals(theHello.getJson("somebody").getMessage());
        
        theHello.putHello("somebody", "knock knock again");
        
        assert "knock knock again".equals(theHello.getJson("somebody").getMessage());
        assert "hello, world".equals(theHello.getJson("nobody").getMessage());
    }
}
