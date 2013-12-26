package ning.codelab.hello.persist.impl.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ning.codelab.hello.persist.HelloPersistence;
import ning.jmx.Managed;

/**
 * HashMap-based persistence for Hello.
 */
public class HelloPersistenceHashMapImpl implements HelloPersistence
{
    /** use a concurrent hash map */
    private final Map<String, String> store = new ConcurrentHashMap<String, String>();

    /** @see HelloPersistence#getHello(String) */
    @Managed
    public String getHello(String who)
    {
        return store.get(who);
    }

    /** @see HelloPersistence#putHello(String, String) */
    @Managed
    public void putHello(String who, String hello)
    {
        store.put(who, hello);
    }

    @Managed
    public void clear()
    {
        store.clear();
    }
}
