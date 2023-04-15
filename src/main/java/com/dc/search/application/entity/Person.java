package com.dc.search.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

	@Id
	private Integer id;
	private String firstName;
	private String lastName;
	private String gender;
	private Integer age;
	private BigDecimal heightInMeters;
	private String email;
	private String ipAddress;
	private LocalDate birthdate;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;

	@OneToMany(mappedBy = "person")
	private List<Address> address = new ArrayList<>();

	public Person(Integer id, String firstName, String lastName, String gender, Integer age, BigDecimal heightInMeters
			, String email, String ipAddress, LocalDate birthdate, Country country) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.age = age;
		this.heightInMeters = heightInMeters;
		this.email = email;
		this.ipAddress = ipAddress;
		this.birthdate = birthdate;
		this.country = country;
	}
}