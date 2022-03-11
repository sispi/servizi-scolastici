package it.kdm.docer.conservazione.provider.parer;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

public class EntePasswordManagerTest {
	
	@Test
	public void test() throws Exception {
		
		File file = new File("src/main/resources/users");
		
		EntePasswordManager entePasswordManager = new EntePasswordManager(file);
		//Assert.assertEquals("username", entePasswordManager.getUsername("ente", "aoo1"));
		//Assert.assertEquals("Привет!", entePasswordManager.getPassword("ente1", "aoo"));
		System.out.println(entePasswordManager.getUsername("regione_emilia-romagna", ""));
		System.out.println(entePasswordManager.getPassword("regione_emilia-romagna", ""));

	}
	
}
