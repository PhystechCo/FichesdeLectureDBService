package fichedbsvc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import co.phystech.aosorio.config.Constants;
import co.phystech.aosorio.controllers.CfgController;
import co.phystech.aosorio.controllers.IModel;
import co.phystech.aosorio.controllers.Sql2oModel;
import co.phystech.aosorio.models.Book;
import co.phystech.aosorio.models.Comment;
import co.phystech.aosorio.models.NewFichePayload;

public class SqlQueriesTest {

	CfgController dbConf = new CfgController(Constants.CONFIG_FILE);

	@Test
	public void queryWithAndTest() {

		String address = dbConf.getDbAddress();
		String dbUsername = dbConf.getDbUser();
		String dbPassword = dbConf.getDbPass();

		Sql2o sql2o = new Sql2o(address, dbUsername, dbPassword, new PostgresQuirks() {
			{
				// make sure we use default UUID converter.
				converters.put(UUID.class, new UUIDConverter());
			}
		});

		IModel model = new Sql2oModel(sql2o);

		//Add two fiches
		//1.
		NewFichePayload ficheOne = new NewFichePayload();
		Book bookOne = new Book();
		bookOne.setTitle("QWERTY");
		bookOne.setAuthor("xxx0xxx");
		//bookOne.setEditor("Edddddd");
		bookOne.setYearPub(12345);
		ficheOne.setId(1);
		ficheOne.setBook(bookOne);
			
		//2.
		NewFichePayload ficheTwo = new NewFichePayload();
		Book bookTwo = new Book();
		bookTwo.setTitle("QWERTY");
		bookTwo.setAuthor("xxx000xxx");
		bookTwo.setEditor("EddddddFFFF");
		//bookTwo.setYearPub(9876);
		ficheTwo.setId(2);
		ficheTwo.setBook(bookTwo);
		
		ArrayList<Comment> comments = new ArrayList<Comment>();
		
		UUID id = model.addFiche(1, bookOne, comments);
		
		assertTrue ( model.existFiche(ficheOne));
		
		assertFalse ( model.existFiche(ficheTwo));
				
		//... cleanup
		
		model.deleteFiche(id);
		
		
	}

}
