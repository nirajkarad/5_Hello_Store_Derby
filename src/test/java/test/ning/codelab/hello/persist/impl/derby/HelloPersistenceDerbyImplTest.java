package test.ning.codelab.hello.persist.impl.derby;

import org.testng.annotations.Test;

import ning.codelab.hello.persist.impl.derby.HelloPersistenceDerbyImpl;

public class HelloPersistenceDerbyImplTest
{
    @Test
    public void testDerbyPersistence()
    {
        HelloPersistenceDerbyImpl persist = new HelloPersistenceDerbyImpl();
        persist.clear();
        
        assert null == persist.getHello("nonexistent");
        assert null == persist.getHello("a_user");
        
        persist.putHello("a_user", "a_hello");
        assert "a_hello".equals(persist.getHello("a_user"));

        assert null == persist.getHello("nonexistent");
        assert null == persist.getHello("another_nonexistent");
        
        persist.clear();
        assert null == persist.getHello("a_user");
    }
}
