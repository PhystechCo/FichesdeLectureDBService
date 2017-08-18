/**
 * 
 */
package co.phystech.aosorio.controllers;

import java.util.UUID;

import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import co.phystech.aosorio.config.Constants;


/**
 * @author AOSORIO
 *
 */
public class SqlController {
	
	private static SqlController instance = null;
	private static Sql2o sql2o = null;

	protected SqlController() {
		
		CfgController dbConf = new CfgController(Constants.CONFIG_FILE);
		
		String address = dbConf.getDbAddress();
		String dbUsername = dbConf.getDbUser();
		String dbPassword = dbConf.getDbPass();
		
		sql2o = new Sql2o(address, dbUsername, dbPassword, new PostgresQuirks() {
			{
				// make sure we use default UUID converter.
				converters.put(UUID.class, new UUIDConverter());
			}
		});
		
	}
	
	public static SqlController getInstance() {
		if (instance == null) {
			instance = new SqlController();
		}
		return instance;
	}

	public Sql2o getAccess() {
		return sql2o;
	}

}
