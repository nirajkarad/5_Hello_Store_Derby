package ning.codelab.hello.persist;

/**
 * Abstract persistence interface for hello string storage and retrieval.
 */
public interface HelloPersistence
{
    /**
     * Get the hello message for a given "who"
     * 
     * @param who
     * @return String hello message
     */
    public String getHello(String who);

    /**
     * Set the hello message for a given "who"
     * 
     * @param who
     * @param hello
     */
    public void putHello(String who, String hello);

    /**
     * Remove all entries from storage.
     */
    public void clear();
}
