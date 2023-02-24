package com.dc.search.application.controller;

import com.dc.search.application.entity.Person;
import com.dc.search.application.repository.PersonRepository;
import com.dc.search.implementation.SpecificationBuilder;
import com.dc.search.implementation.person.PersonSpecificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/persons")
public class PersonController {

	private final PersonRepository personRepository;
	private final PersonSpecificationProvider provider;

	@GetMapping
	public List<Person> getAll(@RequestParam(required = false) String search) {
		if (search != null) {
			SpecificationBuilder<Person> builder = new SpecificationBuilder<>(provider);
			return personRepository.findAll(builder.parse(search));
		}

		return personRepository.findAll();
	}
}
