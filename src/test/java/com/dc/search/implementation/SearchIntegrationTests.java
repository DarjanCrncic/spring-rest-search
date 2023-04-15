package com.dc.search.implementation;

import com.dc.search.application.entity.Address;
import com.dc.search.application.entity.Country;
import com.dc.search.application.entity.Person;
import com.dc.search.application.repository.AddressRepository;
import com.dc.search.application.repository.CountryRepository;
import com.dc.search.application.repository.PersonRepository;
import com.dc.search.implementation.person.PersonSpecificationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SearchIntegrationTests {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private PersonSpecificationProvider provider;

	@Autowired
	private AddressRepository addressRepository;

	private SpecificationBuilder<Person> builder;

	@BeforeEach
	public void setUp() {
		addressRepository.deleteAll();
		personRepository.deleteAll();
		countryRepository.deleteAll();

		Country canada = countryRepository.save(new Country(1, "Canada", "CA"));
		Country usa = countryRepository.save(new Country(2, "USA", "US"));
		Country germany = countryRepository.save(new Country(3, "Germany", "DE"));
		Country france = countryRepository.save(new Country(4, "France", "FR"));

		Person person1 = personRepository.save(new Person(1, "John", "Doe", "male", 25, BigDecimal.valueOf(1.8), "john.doe@example.com",
				"192.168.0.1", LocalDate.parse("1997-05-03"), canada));
		Person person2 = personRepository.save(new Person(2, "Jane", "Doe", "female", 20, BigDecimal.valueOf(1.6), "jane.doe@example.com",
				"192.168.0.2", LocalDate.parse("2001-01-01"), usa));
		Person person3 = personRepository.save(new Person(3, "Alice", "Smith", "female", 30, BigDecimal.valueOf(1.7), "alice.smith@example.com",
				"192.168.0.3", LocalDate.parse("1990-02-14"), canada));
		Person person4 = personRepository.save(new Person(4, "Bob", "Johnson", "male", 50, BigDecimal.valueOf(1.75), "bob.johnson@example.com",
				"192.168.0.4", LocalDate.parse("1972-12-31"), usa));
		Person person5 = personRepository.save(new Person(5, "Emma", "Miller", "female", 22, BigDecimal.valueOf(1.68), "emma.miller@example.com",
				"192.168.0.5", LocalDate.parse("2000-05-10"), germany));
		Person person6 = personRepository.save(new Person(6, "Max", "Meyer", "male", 42, BigDecimal.valueOf(1.83), "max.meyer@example.com",
				"192.168.0.6", LocalDate.parse("1980-11-27"), germany));
		Person person7 = personRepository.save(new Person(7, "Sophie", "Leclerc", "female", 35, BigDecimal.valueOf(1.72), "sophie.leclerc@example.com",
				"192.168.0.7", LocalDate.parse("1987-09-18"), france));
		Person person8 = personRepository.save(new Person(8, "Antoine", "Dupont", "male", 28, BigDecimal.valueOf(1.76), "antoine.dupont@example.com",
				"192.168.0.8", LocalDate.parse("1994-02-23"), france));

		addressRepository.saveAll(List.of(
				new Address(1, "123 Main St", "", 10, person1),
				new Address(2, "456 Elm St", "Apt 2B", 20, person2),
				new Address(3, "789 Oak Ave", "Suite 100", 30, person3),
				new Address(4, "111 Maple Rd", "Unit 5", 40, person4),
				new Address(5, "222 Pine St", "Apt 3C", 50, person5),
				new Address(6, "333 Cedar Ave", "Suite 200", 60, person6),
				new Address(7, "444 Birch Rd", "Unit 7", 70, person7),
				new Address(8, "555 Spruce St", "Apt 4D", 80, person8)));

		builder = new SpecificationBuilder<>(provider);
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
		String search = "((country.name~eq~Canada)~OR~(country.name~eq~USA))~AND~(age~gt~25)~AND~(heightInMeters~gte~1.7)" +
				"~AND~((firstName~like~John)~OR~(firstName~like~Alice))~AND~(birthdate~lt~1995-01-01)~AND~" +
				"(email~like~example.com)";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("Alice", results.get(0).getFirstName());
	}

	@Test
	public void testChildEntitiesSearch() {
		String search = "address.line1~like~Main";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(1, results.size());
	}

	@Test
	public void testChildEntitiesSearch2() {
		String search = "address.id~eq~1~or~address.id~eq~2";
		List<Person> results = personRepository.findAll(builder.parse(search));
		assertNotNull(results);
		assertEquals(2, results.size());
	}
}