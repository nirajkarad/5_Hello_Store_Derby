package test.ning.codelab.hello;

import org.testng.annotations.Test;

import ning.codelab.hello.HelloResource;

/**
 * HelloResource TestNG module.
 */
public class HelloResourceManagementTest
{
    @Test
    public void testHelloDefault()
    {
        HelloResource theHello = GuiceHelper.useGuiceToInstantiateTheHelloResource();
        assert "hello, world".equals(theHello.getMessage("nobody"));
        assert 1 == theHello.getHitCount();

        assert "hello, world".equals(theHello.getMessage("somebody"));
        assert 2 == theHello.getHitCount();

        theHello.resetHitCount();
        assert 0 == theHello.getHitCount();
    }
}
