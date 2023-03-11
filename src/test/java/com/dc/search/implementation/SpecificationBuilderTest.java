package com.dc.search.implementation;

import com.dc.search.application.entity.Person;
import com.dc.search.implementation.person.PersonSpecificationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecificationBuilderTest {

	private PersonSpecificationProvider specProvider;
	private SpecificationBuilder<Person> specBuilder;

	@BeforeEach
	void setUp() {
		specProvider = new PersonSpecificationProvider();
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
}