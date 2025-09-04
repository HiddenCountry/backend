package com.example.hiddencountry.place.domain;

import com.example.hiddencountry.place.domain.type.AreaCode;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class AreaCodeConverter implements AttributeConverter<AreaCode, String> {

	@Override
	public String convertToDatabaseColumn(AreaCode attribute) {
		return attribute != null ? attribute.getCode() : null;
	}

	@Override
	public AreaCode convertToEntityAttribute(String dbData) {
		return dbData != null ? AreaCode.fromCode(dbData) : null;
	}
}