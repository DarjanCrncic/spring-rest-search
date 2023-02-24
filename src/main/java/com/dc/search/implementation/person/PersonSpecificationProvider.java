package com.dc.search.implementation.person;

import com.dc.search.application.entity.Person;
import com.dc.search.implementation.Providable;
import com.dc.search.implementation.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PersonSpecificationProvider implements Providable<Person> {
	@Override
	public Specification<Person> getNewInstance(SearchCriteria criteria) {
		return new PersonSpecification(criteria);
	}
}
