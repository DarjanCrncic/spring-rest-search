package com.dc.search.implementation;

import com.dc.search.application.entity.Country;
import com.dc.search.application.entity.Person;
import com.dc.search.application.repository.CountryRepository;
import com.dc.search.application.repository.PersonRepository;
import com.dc.search.implementation.person.PersonSpecificationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class SearchIntegrationTests {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private PersonSpecificationProvider provider;

	private SpecificationBuilder<Person> builder;

	@BeforeEach
	public void setUp() {
		personRepository.deleteAll();
		countryRepository.deleteAll();
		Country usa = countryRepository.save(new Country(2, "USA", "US"));
		Country canada = countryRepository.save(new Country(1, "Canada", "CA"));
		Country germany = countryRepository.save(new Country(3, "Germany", "DE"));
		Country france = countryRepository.save(new Country(4, "France", "FR"));

		builder = new SpecificationBuilder<>(provider);

		personRepository.saveAll(Arrays.asList(
				new Person(1, "John", "Doe", "male", 25, BigDecimal.valueOf(1.8), "john.doe@example.com", "192.168.0.1", LocalDate.parse("1997-05-03"), canada),
				new Person(2, "Jane", "Doe", "female", 20, BigDecimal.valueOf(1.6), "jane.doe@example.com", "192.168.0.2", LocalDate.parse("2001-01-01"), usa),
				new Person(3, "Alice", "Smith", "female", 30, BigDecimal.valueOf(1.7), "alice.smith@example.com", "192.168.0.3", LocalDate.parse("1990-02-14"), canada),
				new Person(4, "Bob", "Johnson", "male", 50, BigDecimal.valueOf(1.75), "bob.johnson@example.com", "192.168.0.4", LocalDate.parse("1972-12-31"), usa),
				new Person(5, "Emma", "Miller", "female", 22, BigDecimal.valueOf(1.68), "emma.miller@example.com", "192.168.0.5", LocalDate.parse("2000-05-10"), germany),
				new Person(6, "Max", "Meyer", "male", 42, BigDecimal.valueOf(1.83), "max.meyer@example.com", "192.168.0.6", LocalDate.parse("1980-11-27"), germany),
				new Person(7, "Sophie", "Leclerc", "female", 35, BigDecimal.valueOf(1.72), "sophie.leclerc@example.com", "192.168.0.7", LocalDate.parse("1987-09-18"), france),
				new Person(8, "Antoine", "Dupont", "male", 28, BigDecimal.valueOf(1.76), "antoine.dupont@example.com", "192.168.0.8", LocalDate.parse("1994-02-23"), france)
		));
	}

	@Test
	public void testFindByGenderAndAge() {
		String search = "gender~eq~female~AND~age~lt~25";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(2, results.size());
		assertEquals("Jane", results.get(0).getFirstName());
	}

	@Test
	public void testFindByCountryAndAgeRange() {
		String search = "country.name~eq~Canada~AND~age~gt~20~AND~age~lt~30";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("John", results.get(0).getFirstName());
	}

	@Test
	public void testFindByBirthdateOrGender() {
		String search = "(birthdate~lt~1990-01-01)~OR~(gender~eq~male)";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(5, results.size());
	}

	@Test
	public void testFindByHeightAndAge() {
		String search = "heightInMeters~gte~1.7~AND~age~lt~30";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(2, results.size());
		assertEquals("John", results.get(0).getFirstName());
	}

	@Test
	public void testFindAllPersons() {
		String search = "";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(8, results.size());
	}

	@Test
	public void testFindByFirstNameContainingAndAgeGreaterThan() {
		String search = "(firstName~like~John)~AND~(age~gt~18)";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(1, results.size());
	}

	@Test
	public void testFindByHeightInMetersGreaterThanOrGenderEquals() {
		String search = "heightInMeters~gte~1.8~AND~gender~eq~male";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(2, results.size());
	}

	@Test
	public void testFindByBirthdateLessThanEqualsOrEmailContaining() {
		String search = "(birthdate~lt~2000-01-01)~OR~(email~like~example.com)";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(8, results.size());
	}

	@Test
	public void testFindByCountryNameEquals() {
		String search = "country.name~eq~Canada";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(2, results.size());
	}

	@Test
	public void testFindByComplexSearch() {
		String search = "((firstName~like~John)~AND~(age~gt~18))~OR~((gender~eq~male)~AND~(heightInMeters~gte~1.8))";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(2, results.size());
	}

	@Test
	public void testFindByMultipleCountries() {
		String search = "country.name~in~Canada,USA";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(4, results.size());
	}

	@Test
	public void testFindByFirstNameAndLastName() {
		String search = "(firstName~eq~John)~AND~(lastName~eq~Doe)";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(1, results.size());
	}

	@Test
	public void testComplex2() {
		String search = "((country.name~eq~Canada)~OR~(country.name~eq~USA))~AND~(age~gt~25)~AND~(heightInMeters~gte~1.7)~AND~((firstName~like~John)~OR~(firstName~like~Alice))~AND~(birthdate~lt~1995-01-01)~AND~(email~like~example.com)";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("Alice", results.get(0).getFirstName());
	}
}