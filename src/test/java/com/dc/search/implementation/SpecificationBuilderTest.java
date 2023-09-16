package com.dc.search.implementation;

import com.dc.search.application.entity.Person;
import com.dc.search.implementation.person.PersonSpecificationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecificationBuilderTest {

	private SpecificationBuilder<Person> specBuilder;

	@BeforeEach
	void setUp() {
		PersonSpecificationProvider specProvider = new PersonSpecificationProvider();
		specBuilder = new SpecificationBuilder<>(specProvider);
	}

	@Test
	void testParseSearch1() {
		String search = "firstName~equals~John~and~lastName~equals~Doe";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1~and~2", result);
		assertEquals("firstName~equals~John" , specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~equals~Doe" , specBuilder.parsedSearchMap.get(2));
	}

	@Test
	void testParseSearch2() {
		String search = "firstName~equals~John~and~lastName~equals~Doe~or~firstName~equals~Mia~and~lastName~equals~Jovo-vic";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1~and~2~or~3~and~4", result);
		assertEquals("firstName~equals~John" , specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~equals~Doe" , specBuilder.parsedSearchMap.get(2));
		assertEquals("firstName~equals~Mia" , specBuilder.parsedSearchMap.get(3));
		assertEquals("lastName~equals~Jovo-vic" , specBuilder.parsedSearchMap.get(4));
	}

	@Test
	void testParseSearch3() {
		String search = "(firstName~equals~John~or~lastName~equals~Doe)~and~(firstName~equals~Mia~or~lastName~equals~Jovo-vic)";
		String result = specBuilder.parseSearchString(search);

		assertEquals("5~and~6", result);
		assertEquals("1~or~2" ,specBuilder.parsedSearchMap.get(5));
		assertEquals("3~or~4" ,specBuilder.parsedSearchMap.get(6));
		assertEquals("firstName~equals~John" , specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~equals~Doe" , specBuilder.parsedSearchMap.get(2));
		assertEquals("firstName~equals~Mia" , specBuilder.parsedSearchMap.get(3));
		assertEquals("lastName~equals~Jovo-vic" , specBuilder.parsedSearchMap.get(4));
	}

	@Test
	void testParseSearch4() {
		String search = "((firstName~equals~John~or~lastName~equals~Doe)~or~firstName~equals~Mia)~or~lastName~equals~Jovo-vic";
		String result = specBuilder.parseSearchString(search);

		assertEquals("6~or~4", result);
		assertEquals("1~or~2" ,specBuilder.parsedSearchMap.get(5));
		assertEquals("5~or~3" ,specBuilder.parsedSearchMap.get(6));
		assertEquals("firstName~equals~John" , specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~equals~Doe" , specBuilder.parsedSearchMap.get(2));
		assertEquals("firstName~equals~Mia" , specBuilder.parsedSearchMap.get(3));
		assertEquals("lastName~equals~Jovo-vic" , specBuilder.parsedSearchMap.get(4));
	}

	@Test
	void testParseSearch5() {
		String search = "id~in~1,2,3,4,5";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1", result);
	}

	@Test
	void testParseSearch6() {
		String search = "age~greaterThan~25~and~age~lessThan~40";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1~and~2", result);
		assertEquals("age~greaterThan~25", specBuilder.parsedSearchMap.get(1));
		assertEquals("age~lessThan~40", specBuilder.parsedSearchMap.get(2));
	}

	@Test
	void testParseSearch7() {
		String search = "firstName~contains~doe";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1", result);
		assertEquals("firstName~contains~doe", specBuilder.parsedSearchMap.get(1));
	}

	@Test
	void testParseSearch8() {
		String search = "firstName~equals~john~or~lastName~equals~smith~or~age~lessThanOrEqual~30";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1~or~2~or~3", result);
		assertEquals("firstName~equals~john", specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~equals~smith", specBuilder.parsedSearchMap.get(2));
		assertEquals("age~lessThanOrEqual~30", specBuilder.parsedSearchMap.get(3));
	}

	@Test
	void testParseSearch9() {
		String search = "firstName~equals~john~and~(lastName~equals~smith~or~age~lessThanOrEqual~30)";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1~and~4", result);
		assertEquals("firstName~equals~john", specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~equals~smith", specBuilder.parsedSearchMap.get(2));
		assertEquals("age~lessThanOrEqual~30", specBuilder.parsedSearchMap.get(3));
	}

	@Test
	void testParseSearch10() {
		String search = "(firstName~equals~john~or~firstName~equals~jane)~and~lastName~equals~smith";
		String result = specBuilder.parseSearchString(search);

		assertEquals("4~and~3", result);
		assertEquals("1~or~2", specBuilder.parsedSearchMap.get(4));
		assertEquals("firstName~equals~john", specBuilder.parsedSearchMap.get(1));
		assertEquals("firstName~equals~jane", specBuilder.parsedSearchMap.get(2));
		assertEquals("lastName~equals~smith", specBuilder.parsedSearchMap.get(3));
	}

	@Test
	void testParseSearch11() {
		String search = "(firstName~startsWith~j~or~firstName~endsWith~n)~and~age~lessThanOrEqual~30";
		String result = specBuilder.parseSearchString(search);

		assertEquals("4~and~3", result);
		assertEquals("1~or~2", specBuilder.parsedSearchMap.get(4));
		assertEquals("firstName~startsWith~j", specBuilder.parsedSearchMap.get(1));
		assertEquals("firstName~endsWith~n", specBuilder.parsedSearchMap.get(2));
		assertEquals("age~lessThanOrEqual~30", specBuilder.parsedSearchMap.get(3));
	}

	@Test
	void testParseSearch12() {
		String search = "(firstName~contains~doe~or~lastName~contains~smith)~or~(age~greaterThanOrEqual~30~and~age~lessThanOrEqual~40)";
		String result = specBuilder.parseSearchString(search);

		assertEquals("5~or~6", result);
		assertEquals("1~or~2", specBuilder.parsedSearchMap.get(5));
		assertEquals("3~and~4", specBuilder.parsedSearchMap.get(6));
		assertEquals("firstName~contains~doe", specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~contains~smith", specBuilder.parsedSearchMap.get(2));
		assertEquals("age~greaterThanOrEqual~30", specBuilder.parsedSearchMap.get(3));
		assertEquals("age~lessThanOrEqual~40", specBuilder.parsedSearchMap.get(4));
	}

	@Test
	void testParseSearch13() {
		String search = "(firstName~equals~john~and~lastName~equals~doe)~or~(firstName~equals~jane~and~lastName~equals~smith)";
		String result = specBuilder.parseSearchString(search);

		assertEquals("5~or~6", result);
		assertEquals("1~and~2", specBuilder.parsedSearchMap.get(5));
		assertEquals("3~and~4", specBuilder.parsedSearchMap.get(6));
		assertEquals("firstName~equals~john", specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~equals~doe", specBuilder.parsedSearchMap.get(2));
		assertEquals("firstName~equals~jane", specBuilder.parsedSearchMap.get(3));
		assertEquals("lastName~equals~smith", specBuilder.parsedSearchMap.get(4));
	}

	@Test
	void testParseSearch14() {
		String search = "firstName~contains~n~or~lastName~contains~n~or~(age~greaterThan~40~and~age~lessThan~60)";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1~or~2~or~5", result);
		assertEquals("firstName~contains~n", specBuilder.parsedSearchMap.get(1));
		assertEquals("lastName~contains~n", specBuilder.parsedSearchMap.get(2));
		assertEquals("3~and~4", specBuilder.parsedSearchMap.get(5));
		assertEquals("age~greaterThan~40", specBuilder.parsedSearchMap.get(3));
		assertEquals("age~lessThan~60", specBuilder.parsedSearchMap.get(4));
	}

	@Test
	void testParseSearch15() {
		String search = "parentFolder~EQ~1~AND~immutable~EQ~false~and~(objectName~LIKE~asd~OR~creator~LIKE~asd~OR~type~LIKE~asd~OR~description~LIKE~asd)";
		String result = specBuilder.parseSearchString(search);

		assertEquals("1~AND~2~AND~7", result.toUpperCase());
		assertEquals("3~OR~4~OR~5~OR~6", specBuilder.parsedSearchMap.get(7));
	}
}