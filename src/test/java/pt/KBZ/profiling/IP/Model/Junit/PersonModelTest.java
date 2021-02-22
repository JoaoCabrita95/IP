package IP.Model.Junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import IP.Model.Person;
import IP.Model.SparqlEndPoint;
import IP.Model.Junit.Data.PersonTestData;
import IP.config.Configuration;

@Disabled
class PersonModelTest {

	Configuration cnf = new Configuration();
	PersonTestData pd = new PersonTestData();
	
	@Test
	@DisplayName("Saving Person data to server")
	void testSave() {
		
		cnf.LoadConfiguration();
		//Reset database
		SparqlEndPoint.deleteTriple("?s ?o ?p", "?s ?o ?p");
		//Make sure database has prefixes
		SparqlEndPoint.insertTTLFile("prefixes.ttl");
		
		List<Person> ps = pd.getPersons();
		for(Person p : ps) {
			try {
				p.Save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@Disabled("not yet implemented")
	void testGetPersons() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled("not yet implemented")
	void testGetPerson() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled("not yet implemented")
	void testGetPersonsByField() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled("not yet implemented")
	void testGetPersonByName() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled("not yet implemented")
	void testGetPersonByCV() {
		fail("Not yet implemented");
	}

}
