package com.walking.data.converter;

import com.walking.data.entity.path.TMapTrafficTurnType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

@Converter
public class TMapEnumListConverter implements AttributeConverter<List<TMapTrafficTurnType>, String> {
    private static final String SPLIT_CHAR = ", ";

    @Override
    public String convertToDatabaseColumn(List<TMapTrafficTurnType> stringList) {
        return String.join(SPLIT_CHAR, stringList.stream().map(Enum::name).toArray(String[]::new));
    }

    @Override
    public List<TMapTrafficTurnType> convertToEntityAttribute(String string) {
        return Arrays.asList(Arrays.stream(string.split(SPLIT_CHAR))
                .map(TMapTrafficTurnType::valueOf).toArray(TMapTrafficTurnType[]::new));

    }
}