package com.dc.search.implementation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class BasicSearchSpecification {

	public static final String LIKE = "LIKE";
	public static final String EQUALS = "EQ";
	public static final String NOT_EQUALS = "NOT_EQ";
	public static final String GREATER = "GT";
	public static final String LESS = "LT";
	public static final String LESS_OR_EQUALS = "LTE";
	public static final String GREATER_OR_EQUALS = "GTE";
	public static final String IN = "IN";
	public static final String NOT_IN = "NOT_IN";


	public <T> Predicate toPredicateBasic(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb,
									  SearchCriteria criteria) {
		final String key = criteria.getKey();
		final Object value = getValue(root, key, criteria.getValue());

		final String operation = criteria.getOperation().toUpperCase();
		return switch (operation) {
			case GREATER -> withGreater(key, value, root, cb);
			case GREATER_OR_EQUALS -> withGreaterOrEquals(key, value, root, cb);
			case LESS -> withLess(key, value, root, cb);
			case LESS_OR_EQUALS -> withLessOrEquals(key, value, root, cb);
			case LIKE -> withLike(key, value, root, cb);
			case IN -> withIn(key, value, root, cb);
			case NOT_IN -> withNotIn(key, value, root, cb);
			case EQUALS -> withEquals(key, value, root, cb);
			case NOT_EQUALS -> withNotEquals(key, value, root, cb);
			default -> throw new RuntimeException("Unsupported search operation: " + operation);
		};
	}

	private <T> Object getValue(Root<T> root, String key, String value) {
		Class<?> javaType = extractEntity(key, root).getJavaType();

		if (value.contains(",") && javaType != Collection.class) {
			return Arrays.stream(value.split(","))
					.map(i -> getValue(root, key, i))
					.collect(Collectors.toList());
		}

		if (javaType == Boolean.class || javaType.getTypeName().equals("boolean")) {
			return Boolean.parseBoolean(value);
		} else if (Number.class.isAssignableFrom(javaType)) {
			return BigDecimal.valueOf(Double.parseDouble(value));
		} else if (javaType == UUID.class) {
			return UUID.fromString(value);
		} else if (javaType == LocalDate.class) {
			return Utils.parseDateFromString(value);
		} else if (javaType == List.class) {
			return Arrays.asList(value.split(","));
		} else if (javaType == Set.class) {
			return Set.of(value.split(","));
		} else {
			return value;
		}
	}

	private <T> Predicate withLike(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		// TODO: add formatting for local date
		return cb.like(extractEntity(key, root).as(String.class), "%" + value.toString() + "%");
	}

	private <T> Predicate withEquals(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		return cb.equal(extractEntity(key, root), value);
	}

	private <T> Predicate withNotEquals(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		return cb.notEqual(extractEntity(key, root), value);
	}

	private <T> Predicate withGreater(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		final Expression<Object> expression = extractEntity(key, root);
		final Class<?> javaType = expression.getJavaType();
		if (javaType == LocalDate.class) {
			return cb.greaterThan(expression.as(LocalDate.class), (LocalDate) value);
		} else if (Number.class.isAssignableFrom(javaType)) {
			return cb.greaterThan(expression.as(BigDecimal.class), (BigDecimal) value);
		} else if (javaType == String.class) {
			return cb.greaterThan(expression.as(String.class), (String) value);
		} else {
			throw new RuntimeException("Unsupported java type ({" + javaType.getTypeName() + "}) for GR operation");
		}
	}

	private <T> Predicate withGreaterOrEquals(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		final Expression<Object> expression = extractEntity(key, root);
		final Class<?> javaType = expression.getJavaType();
		if (javaType == LocalDate.class) {
			return cb.greaterThanOrEqualTo(expression.as(LocalDate.class), (LocalDate) value);
		} else if (Number.class.isAssignableFrom(javaType)) {
			return cb.greaterThanOrEqualTo(expression.as(BigDecimal.class), (BigDecimal) value);
		} else if (javaType == String.class) {
			return cb.greaterThanOrEqualTo(expression.as(String.class), (String) value);
		} else {
			throw new RuntimeException("Unsupported java type ({" + javaType.getTypeName() + "}) for GRE operation");
		}
	}

	private <T> Predicate withLess(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		final Expression<Object> expression = extractEntity(key, root);
		final Class<?> javaType = expression.getJavaType();
		if (javaType == LocalDate.class) {
			return cb.lessThan(expression.as(LocalDate.class), (LocalDate) value);
		} else if (Number.class.isAssignableFrom(javaType)) {
			return cb.lessThan(expression.as(BigDecimal.class), (BigDecimal) value);
		} else if (javaType == String.class) {
			return cb.lessThan(expression.as(String.class), (String) value);
		} else {
			throw new RuntimeException("Unsupported java type ({" + javaType.getTypeName() + "}) for LS operation");
		}
	}

	private <T> Predicate withLessOrEquals(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		final Expression<Object> expression = this.extractEntity(key, root);
		final Class<?> javaType = expression.getJavaType();
		if (javaType == LocalDate.class) {
			return cb.lessThanOrEqualTo(expression.as(LocalDate.class), (LocalDate) value);
		} else if (Number.class.isAssignableFrom(javaType)) {
			return cb.lessThanOrEqualTo(expression.as(BigDecimal.class), (BigDecimal) value);
		} else if (javaType == String.class) {
			return cb.lessThanOrEqualTo(expression.as(String.class), (String) value);
		} else {
			throw new RuntimeException("Unsupported java type ({" + javaType.getTypeName() + "}) for LSE operation");
		}
	}

	private <T> Predicate withIn(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		return cb.in(extractEntity(key, root)).value(value);
	}

	private <T> Predicate withNotIn(String key, Object value, Root<T> root, CriteriaBuilder cb) {
		return cb.not(cb.in(extractEntity(key, root)).value(value));
	}

	private <T> Expression<Object> extractEntity(String key, Root<T> root) {
		if (key.contains(".")) {
			String[] sections = key.split("\\.");
			Path<Object> path = root.get(sections[0]);
			for(int i=1; i<sections.length; i++) {
				path = path.get(sections[i]);
			}
			return path;
		}
		return root.get(key);
	}

}
