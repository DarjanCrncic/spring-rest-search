package com.dc.search.implementation.person;

import com.dc.search.application.entity.Person;
import com.dc.search.implementation.BasicSearchSpecification;
import com.dc.search.implementation.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;


@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonSpecification extends BasicSearchSpecification implements Specification<Person> {
	private SearchCriteria criteria;

	@Override
	public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		return super.toPredicateBasic(root, query, builder, criteria);
	}

}
