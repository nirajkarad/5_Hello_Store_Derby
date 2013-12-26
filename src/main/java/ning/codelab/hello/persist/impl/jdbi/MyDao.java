package ning.codelab.hello.persist.impl.jdbi;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.binders.Bind;

public interface MyDao {
	
	  @SqlUpdate("create table demoTable (who varchar(100) primary key, hello varchar(100))")
	  void createTable();
	  
	  @SqlQuery("select count(*) from demoTable")
	  int checkCount();

	  @SqlUpdate("insert into demoTable (who, hello) values (:who, :hello)")
	  void insert(@Bind("who") String who_name, @Bind("hello") String hello_name);

	  @SqlQuery("select hello from demoTable where who = :who")
	  String findNameByWho(@Bind("who") String who_name);

	  @SqlUpdate("delete from demoTable")
	  void close();
}
