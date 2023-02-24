package com.dc.search.implementation;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Log4j2
public class Utils {

	private static final List<String> supportedDateFormats = List.of("yyyy-MM-dd", "dd/MM/yyyy", "dd-MM-yyyy");
	public static LocalDate parseDateFromString(String date) {
		for (String format : supportedDateFormats) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				return LocalDate.parse(date, formatter);
			} catch (DateTimeParseException e) {
				log.debug("Date not of type: {}", format);
			}
		}
		return null;
	}
}
