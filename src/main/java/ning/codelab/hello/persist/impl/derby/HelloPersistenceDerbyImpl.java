package ning.codelab.hello.persist.impl.derby;

import java.io.File;
import java.util.Map;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.tweak.HandleCallback;

import ning.codelab.hello.persist.HelloPersistence;
import ning.jmx.Managed;

/**
 * Derby embedded-DB-based persistence for Hello.
 */
public class HelloPersistenceDerbyImpl implements HelloPersistence
{
    private final DBI dbAccess;

    public HelloPersistenceDerbyImpl()
    {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setCreateDatabase("create");
        ds.setDatabaseName(new File("hello_db").getAbsolutePath());

        this.dbAccess = new DBI(ds);

        dbAccess.withHandle(new HandleCallback<Void>() {
            public Void withHandle(Handle handle) throws Exception
            {
                try {
                    handle.select("select COUNT(*) from hellos");
                }
                catch (Exception e) {
                    handle.execute("create table hellos (who varchar(128) primary key, hello varchar(1024))");
                }

                return null;
            }
        });
    }

    /** @see HelloPersistence#getHello(String) */
    @Managed
    public String getHello(final String who)
    {
        return dbAccess.withHandle(new HandleCallback<String>() {
            public String withHandle(Handle handle) throws Exception
            {
                Map<String, Object> firstRow = handle.createQuery("select * from hellos where who = :who").bind("who", who).first();

                if (firstRow != null && firstRow.containsKey("hello")) {
                    return (String) firstRow.get("hello");
                }

                return null;
            }
        });
    }

    /** @see HelloPersistence#putHello(String, String) */
    @Managed
    public void putHello(final String who, final String hello)
    {
        dbAccess.withHandle(new HandleCallback<Boolean>() {
            public Boolean withHandle(Handle handle) throws Exception
            {
                // this would be nicer if derby had a "replace into" stmt
                handle.createStatement("delete from hellos where who = :who").bind("who", who).execute();

                handle.createStatement("insert into hellos (who, hello) values (:who, :hello)").bind("who", who).bind("hello", hello).execute();

                return true;
            }
        });
    }

    /* used in unit tests */
    @Managed
    public void clear()
    {
        dbAccess.withHandle(new HandleCallback<Boolean>() {
            public Boolean withHandle(Handle handle) throws Exception
            {
                handle.createStatement("delete from hellos").execute();

                return true;
            }
        });
    }
}
