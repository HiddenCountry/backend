package com.example.hiddencountry.entity.place.domain;

import com.example.hiddencountry.entity.place.domain.type.ContentType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class ContentTypeConverter implements AttributeConverter<ContentType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ContentType attribute) {
		return (attribute != null) ? attribute.getCode() : null;
	}

	@Override
	public ContentType convertToEntityAttribute(Integer dbData) {
		return (dbData != null) ? ContentType.fromCode(dbData) : null;
	}
}