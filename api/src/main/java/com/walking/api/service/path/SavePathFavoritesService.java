package com.walking.api.service.path;

import com.walking.api.repository.path.PathFavoritesRepository;
import com.walking.api.repository.path.TrafficInPathFavoritesRepository;
import com.walking.api.service.dto.TMapPathFavoritesTrafficInfo;
import com.walking.api.service.dto.request.FavoritePathRequestDto;
import com.walking.api.web.client.TMapClient;
import com.walking.api.web.client.dto.request.TMapRequestDto;
import com.walking.api.web.client.dto.response.TMapResponseDto;
import com.walking.api.web.client.dto.response.detail.Feature;
import com.walking.api.web.client.dto.response.detail.Geometry;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.path.PathFavoritesEntity;
import com.walking.data.entity.path.TMapTrafficTurnType;
import com.walking.data.entity.path.TrafficInPathFavoritesEntity;
import lombok.RequiredArgsConstructor;

import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavePathFavoritesService {

    private final PathFavoritesRepository pathFavoritesRepository;
    private final TrafficInPathFavoritesRepository trafficInPathFavoritesRepository;

    private final TMapClient tMapClient;

    public void execute(FavoritePathRequestDto favoritePathRequestDto, Long MemberId) {


        TMapResponseDto tMapPathData = getTMapPathData(favoritePathRequestDto.getStartLat(), favoritePathRequestDto.getStartLng(),
                favoritePathRequestDto.getEndLat(), favoritePathRequestDto.getEndLng());

        // 신호등 좌표 추출
        TMapPathFavoritesTrafficInfo tMapPathFavoritesTrafficInfo = extractAllTrafficPoints(tMapPathData);



        LineString lineString = extractLineString(tMapPathData);

        // PathFavoritesEntity 생성
        savePathFavoritesAndTrafficInFavorites(favoritePathRequestDto, MemberId, tMapPathFavoritesTrafficInfo, lineString);


    }

    //todo 다른 객체로 분리
    @Transactional
    public void savePathFavoritesAndTrafficInFavorites(FavoritePathRequestDto favoritePathRequestDto, Long MemberId,TMapPathFavoritesTrafficInfo  trafficPoints, LineString lineString) {
        PathFavoritesEntity savedPathFavorites = pathFavoritesRepository.save(PathFavoritesEntity.builder().path(lineString).memberFk(MemberEntity.builder().id(MemberId).build())
                .startPoint(createPoint(favoritePathRequestDto.getStartLng(), favoritePathRequestDto.getStartLat()))
                .endPoint(createPoint(favoritePathRequestDto.getEndLng(), favoritePathRequestDto.getEndLat()))
                .startAlias(favoritePathRequestDto.getStartName())
                .endAlias(favoritePathRequestDto.getEndName())
                .name(favoritePathRequestDto.getName())
                .order(pathFavoritesRepository.findMaxOrder() + 1)
                .build());

        trafficInPathFavoritesRepository.save(TrafficInPathFavoritesEntity.builder().
                pathFk(savedPathFavorites)
                .trafficPoints(trafficPoints.getPoints())
                .trafficTypes(trafficPoints.getTrafficTypes()).build());

    }

    private TMapResponseDto getTMapPathData(double startLat, double startLng, double endLat, double endLng){
                                 //가져와야될 것 -> 신호등 어떤게 있는지, 길  정보

        return tMapClient.TMapDetailPathSearch(TMapRequestDto.builder()
                .startX(startLng)
                .startY(startLat)
                .endX(endLng)
                .endY(endLat)
                .startName("출발지")
                .endName("도착지")
                .build());

    }

    private LineString extractLineString(TMapResponseDto responseDto) {
        List<Coordinate> coordinates = new ArrayList<>();
        GeometryFactory geometryFactory = new GeometryFactory();

        for (Feature feature : responseDto.getFeatures()) {
            if ("LineString".equals(feature.getGeometry().getType())) {
                List<List<Double>> points = feature.getGeometry().getCoordinates();
                for (List<Double> point : points) {
                    coordinates.add(new Coordinate(point.get(0), point.get(1)));
                }
            }
        }

        if (coordinates.isEmpty()) {
            return null;
        }

        Coordinate[] coordinatesArray = coordinates.toArray(new Coordinate[0]);
        return geometryFactory.createLineString(coordinatesArray);
    }

    private TMapPathFavoritesTrafficInfo extractAllTrafficPoints(TMapResponseDto responseDto) {
        List<Point> points = new ArrayList<>();
        List<TMapTrafficTurnType> trafficTypes = new ArrayList<>();

        // TMapResponseDto 객체에서 Feature 리스트를 반복하며 각 Feature의 Geometry를 검사합니다.
        for (Feature feature : responseDto.getFeatures()) {
            Geometry geometry = feature.getGeometry();
            List<List<Double>> coordinates = geometry.getCoordinates();

            // 신호등은 Point 타입만 존재한다.
            if ("Point".equals(geometry.getType()) && feature.getProperties().getFacilityType().equals("15")) {
                // Point 타입의 좌표 처리
                points.add(createPoint(coordinates.get(0).get(0), coordinates.get(0).get(1)));
                trafficTypes.add(TMapTrafficTurnType.findByNumber(feature.getProperties().getTurnType()));
            }
        }

        return new TMapPathFavoritesTrafficInfo(points, trafficTypes);
    }

    private Point createPoint(double lng, double lat) {
        GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 4326);
        return gf.createPoint(new Coordinate(lng, lat));
    }


}
