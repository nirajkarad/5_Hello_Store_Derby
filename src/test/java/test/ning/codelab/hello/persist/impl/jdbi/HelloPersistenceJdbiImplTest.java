package test.ning.codelab.hello.persist.impl.jdbi;

import ning.codelab.hello.persist.impl.jdbi.HelloPersistenceJdbiImpl;

import org.testng.annotations.Test;

public class HelloPersistenceJdbiImplTest {
	
	 @Test
	    public void testJdbiPersistence()
	    {
	        HelloPersistenceJdbiImpl persist = new HelloPersistenceJdbiImpl();
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
