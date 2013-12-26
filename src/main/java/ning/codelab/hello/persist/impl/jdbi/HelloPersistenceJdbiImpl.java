package ning.codelab.hello.persist.impl.jdbi;

import ning.codelab.hello.persist.HelloPersistence;
import org.skife.jdbi.v2.DBI;

public class HelloPersistenceJdbiImpl implements HelloPersistence{
	
	private final DBI dbAccess;
	private final MyDao dao;
	
	public HelloPersistenceJdbiImpl() {
			this.dbAccess = new DBI("jdbc:mysql://localhost:3306/test", "", "");
			this.dao = dbAccess.open(MyDao.class);
			
			try {
				dao.checkCount();
			} catch (Exception e) {
				dao.createTable();
			}
}

	@Override
	public String getHello(String who) {
		return dao.findNameByWho(who);
	}

	@Override
	public void putHello(String who, String hello) {
		dao.insert(who, hello);
	}

	@Override
	public void clear() {
		dao.close();
	}

}
