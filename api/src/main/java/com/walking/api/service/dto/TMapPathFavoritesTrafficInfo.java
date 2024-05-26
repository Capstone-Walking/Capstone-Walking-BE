package com.walking.api.service.dto;

import com.walking.data.entity.path.TMapTrafficTurnType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class TMapPathFavoritesTrafficInfo {

    List<Point> points;
    List<TMapTrafficTurnType> trafficTypes;


}
