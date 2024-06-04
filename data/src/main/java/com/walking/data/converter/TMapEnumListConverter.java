package com.walking.data.converter;

import com.walking.data.entity.path.TrafficDirection;
import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TMapEnumListConverter implements AttributeConverter<List<TrafficDirection>, String> {
	private static final String SPLIT_CHAR = ", ";

	@Override
	public String convertToDatabaseColumn(List<TrafficDirection> stringList) {
		return String.join(SPLIT_CHAR, stringList.stream().map(Enum::name).toArray(String[]::new));
	}

	@Override
	public List<TrafficDirection> convertToEntityAttribute(String string) {
		return Arrays.asList(
				Arrays.stream(string.split(SPLIT_CHAR))
						.map(TrafficDirection::valueOf)
						.toArray(TrafficDirection[]::new));
	}
}
