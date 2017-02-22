package userfunctions.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import userfunctions.functions.beans.Person;

/**
 * Service that provides access to different lists of persons.
 * 
 * @author aarellan
 *
 */
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
	
	public static List<Person> getListOfPersonsByJob(String jobName) {
		List<Person> listPersonsKunstler = new ArrayList<>();
		listPersonsKunstler.add(personGenerator("1", "Kunstler", BigDecimal.valueOf(1000), "OK", "OK"));
		listPersonsKunstler.add(personGenerator("2", "Kunstler", BigDecimal.valueOf(200), "OK", "OK"));
		listPersonsKunstler.add(personGenerator("3", "Kunstler", BigDecimal.valueOf(300), "OK", "OK"));
		
		List<Person> listPersonsProgrammer = new ArrayList<>();
		listPersonsProgrammer.add(personGenerator("4", "Programmer", BigDecimal.valueOf(1000), "OK", "OK"));
		listPersonsProgrammer.add(personGenerator("5", "Programmer", BigDecimal.valueOf(200), "OK", "OK"));
		listPersonsProgrammer.add(personGenerator("6", "Programmer", BigDecimal.valueOf(300), "OK", "OK"));
		
		List<Person> listPersonsSchneider = new ArrayList<>();
		listPersonsSchneider.add(personGenerator("7", "Schneider", BigDecimal.valueOf(1000), "OK", "OK"));
		listPersonsSchneider.add(personGenerator("8", "Schneider", BigDecimal.valueOf(200), "OK", "OK"));
		listPersonsSchneider.add(personGenerator("9", "Schneider", BigDecimal.valueOf(300), "OK", "OK"));
		
		Map<String, List<Person>> mapOfPersonsByJob = new HashMap<>(4);
		mapOfPersonsByJob.put("Kunstler", listPersonsKunstler);
		mapOfPersonsByJob.put("Programmer", listPersonsProgrammer);
		mapOfPersonsByJob.put("Schneider", listPersonsSchneider);
		
		return mapOfPersonsByJob.get(jobName);
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
