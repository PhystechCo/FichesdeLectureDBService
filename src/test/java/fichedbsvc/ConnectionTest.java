package fichedbsvc;

import java.sql.SQLException;
import java.util.UUID;

import org.junit.Test;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.controllers.CfgController;

public class ConnectionTest {

	@Test
	public void connectSql2o() {

		CfgController dbConf = new CfgController(Constants.CONFIG_FILE);
		
		String address = dbConf.getDbAddress();
		String dbUsername = dbConf.getDbUser();
		String dbPassword = dbConf.getDbPass();

		Sql2o sql2o = new Sql2o(address, dbUsername, dbPassword, new PostgresQuirks() {
			{
				// make sure we use default UUID converter.
				converters.put(UUID.class, new UUIDConverter());
			}
		});
		
		try {
			sql2o.getDataSource().getConnection();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

	}

}
