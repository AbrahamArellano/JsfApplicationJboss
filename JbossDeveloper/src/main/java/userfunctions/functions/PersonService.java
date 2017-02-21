package userfunctions.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import userfunctions.functions.beans.Person;

public class PersonService {

	public static List<Person> getListPersons() {
		List<Person> listPersons = new ArrayList<>();
		listPersons.add(personGenerator("1", "Kunstler", BigDecimal.valueOf(1000), "OK", "OK"));
		listPersons.add(personGenerator("2", "Programmer", BigDecimal.valueOf(200), "OK", "OK"));
		listPersons.add(personGenerator("3", "Briefträger", BigDecimal.valueOf(300), "OK", "OK"));
		listPersons.add(personGenerator("4", "Bäcker", BigDecimal.valueOf(400), "OK", "OK"));
		listPersons.add(personGenerator("5", "Klempner", BigDecimal.valueOf(500), "OK", "OK"));
		listPersons.add(personGenerator("6", "Schneider", BigDecimal.valueOf(500), "OK", "OK"));
		
		return listPersons;
	}

	private static Person personGenerator(String personId, String personArt, BigDecimal obligation, String shufaNK, String shufaMK) {
		Person person = new Person();
		person.setObligation(obligation);
		person.setPersonId(personId);
		person.setPersonTyp(personArt);
		person.setSchufaNK(shufaNK);
		person.setSchufaNM(shufaMK);
		return person;
	}
}
