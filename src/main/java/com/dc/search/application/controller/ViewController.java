package com.dc.search.application.controller;

import com.dc.search.application.entity.Person;
import com.dc.search.application.repository.PersonRepository;
import com.dc.search.implementation.SpecificationBuilder;
import com.dc.search.implementation.person.PersonSpecificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

	private final PersonRepository personRepository;

	private final PersonSpecificationProvider provider;

	@GetMapping(value = "/")
	private String listPersons(Model model, String search){
		if (search != null) {
			SpecificationBuilder<Person> builder = new SpecificationBuilder<>(provider);
			model.addAttribute("persons", personRepository.findAll(builder.parse(search)));
			model.addAttribute("search", search);
		} else {
			model.addAttribute("persons", personRepository.findAll());
		}
		return "persons-view";
	}
}
