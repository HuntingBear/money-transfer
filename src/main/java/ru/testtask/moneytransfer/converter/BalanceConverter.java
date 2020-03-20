package ru.testtask.moneytransfer.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

/**
 * Balance converter
 */
@Converter
public class BalanceConverter implements AttributeConverter<BigDecimal, Long> {
    /**
     * Converts balance value from entity value(dollars) to database value(cents)
     */
    @Override
    public Long convertToDatabaseColumn(BigDecimal attribute) {
        if (attribute == null) {
            return null;
        } else {
            return attribute.multiply(BigDecimal.valueOf(100)).longValue();
        }
    }

    /**
     * Converts balance value from database value(cents) to entity value(dollars)
     */
    @Override
    public BigDecimal convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        } else {
            return new BigDecimal(dbData).divide(BigDecimal.valueOf(100));
        }
    }
}
