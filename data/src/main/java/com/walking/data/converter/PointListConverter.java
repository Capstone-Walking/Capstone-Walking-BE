package com.walking.data.converter;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;

public class PointListConverter implements AttributeConverter<List<Point>, String> {


    private static final String SPLIT_CHAR = " - ";

    @Override
    public String convertToDatabaseColumn(List<Point> pointList) {
        return String.join(SPLIT_CHAR, pointList.stream().map(Point::toString).toArray(String[]::new));
    }

    @Override
    public List<Point> convertToEntityAttribute(String string) {
        GeometryFactory geometryFactory = new GeometryFactory();
        List<Point> pointList = Arrays.asList(Arrays.stream(string.split(SPLIT_CHAR)).
                map(s -> s.replace("POINT (", "").replace(")", "")).
                map(s -> {
                    String[] split = s.split(" ");
                    return geometryFactory.createPoint(new Coordinate(Double.parseDouble(split[0]), Double.parseDouble(split[1])));
                }).toArray(Point[]::new));

        return pointList;
    }

}
