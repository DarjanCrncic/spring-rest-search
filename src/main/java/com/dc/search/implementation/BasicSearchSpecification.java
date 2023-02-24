package com.dc.search.implementation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public abstract class BasicSearchSpecification {
	
	public static final String EQUALS = ":";
	public static final String NOT_EQUALS_OPERATION = "!";
	public static final String GREATER = ">";
	public static final String LESS = "<";
	public static final String LESS_OR_EQUALS = "<=";
	public static final String GREATER_OR_EQUALS = ">=";

    public Predicate toPredicateBasic(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder, SearchCriteria criteria) {
		final Class<?> javaType = root.get(criteria.getKey()).getJavaType();
		final Object value = getValue(root, criteria);

		if (criteria.getOperation().equalsIgnoreCase(GREATER)) {
			if (javaType == LocalDate.class) {
				return builder.greaterThan(root.get(criteria.getKey()), (LocalDate) value);
			} else if (javaType == BigDecimal.class) {
				return builder.greaterThan(root.get(criteria.getKey()), (BigDecimal) value);
			}
        }
        else if (criteria.getOperation().equalsIgnoreCase(GREATER_OR_EQUALS)) {
			if (javaType == LocalDate.class) {
				return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (LocalDate) value);
			} else if (javaType == BigDecimal.class) {
				return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (BigDecimal) value);
			}
        }
        else if (criteria.getOperation().equalsIgnoreCase(LESS)) {
			if (javaType == LocalDate.class) {
				return builder.lessThan(root.get(criteria.getKey()), (LocalDate) value);
			} else if (javaType == BigDecimal.class) {
				return builder.lessThan(root.get(criteria.getKey()), (BigDecimal) value);
			}
        }
        else if (criteria.getOperation().equalsIgnoreCase(LESS_OR_EQUALS)) {
			if (javaType == LocalDate.class) {
				return builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDate) value);
			} else if (javaType == BigDecimal.class) {
				return builder.lessThanOrEqualTo(root.get(criteria.getKey()), (BigDecimal) value);
			}
        }
        else {
			if (criteria.getOperation().equalsIgnoreCase(EQUALS)) {
				if (javaType == String.class) {
					return builder.like(root.get(criteria.getKey()), (String) value);
				} else if (javaType == LocalDate.class) {
					LocalDate val = (LocalDate) value;
					Predicate less = builder.lessThan(root.get(criteria.getKey()), val.plus(1, ChronoUnit.DAYS));
					// for some reason greater than will also include that day
					Predicate greater = builder.greaterThan(root.get(criteria.getKey()), val);
					return builder.and(less, greater);
				} else {
					return builder.equal(root.get(criteria.getKey()), value);
				}
			}
			else if (criteria.getOperation().equalsIgnoreCase(NOT_EQUALS_OPERATION)) {
				if (javaType == String.class) {
					return builder.notLike(root.get(criteria.getKey()), (String) value);
				} else if (javaType == LocalDate.class) {
					LocalDate val = (LocalDate) value;
					Predicate less = builder.lessThan(root.get(criteria.getKey()), val);
					// for some reason greater than will also include that day
					Predicate greater = builder.greaterThan(root.get(criteria.getKey()), val.plus(1, ChronoUnit.DAYS));
					return builder.or(less, greater);
				} else {
					return builder.notEqual(root.get(criteria.getKey()), value);
				}
			}
		}
        return null;
    }

	private Object getValue(Root<?> root, SearchCriteria criteria) {
		Class<?> javaType = root.get(criteria.getKey()).getJavaType();
		if (javaType == String.class) {
			return "%" + criteria.getValue() + "%";
		} else if (javaType == Boolean.class || javaType.getTypeName().equals("boolean")) {
			return Boolean.parseBoolean(criteria.getValue().toString());
		} else if (javaType == UUID.class) {
			return UUID.fromString(criteria.getValue().toString());
		} else if (javaType == LocalDate.class) {
			return Utils.parseDateFromString(criteria.getValue().toString());
		} else {
			return criteria.getValue();
		}
	}

}
