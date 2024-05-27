package com.walking.api.service.path;

import com.walking.api.repository.path.PathFavoritesRepository;
import com.walking.api.repository.path.TrafficInPathFavoritesRepository;
import com.walking.api.repository.traffic.TrafficRepository;
import com.walking.data.entity.path.PathFavoritesEntity;
import com.walking.data.entity.path.TMapTrafficTurnType;
import com.walking.data.entity.path.TrafficInPathFavoritesEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalculatePathFavoritesTimeService {
    /**
     * 저장한 경로를 가져오고 이를 통해서 경로의 시간을 계산
     * 이때 출발 시간을 알려줄 수 있어야 한다.
     * 출발 시간은 첫 신호등 까지 걸리는 시간을 사이클의 합으로 나눈 나머지
     * untilFirstTrafficTime  % (red cycle + blue cycle)
     * untilFirstTrafficTime / (red cycle + blue cycle) 를 같이 이용한다.
     * 예시
     * 신호등까지 가는데 300초   현재 파란불 남은시간 10초 빨간불 사이클 60초 파란불 사이클 20초
     *
     * 300 % (60 + 20) = 300 % 80 = 60
     * 300 / (60 + 20) = 300 / 80 = 3
     *
     * 60초 후에 남은 신호 시간을 계산합니다. 이 시간은 바로 출발했을때 마주할 수 있는 신호등 시간입니다.
     *
     * 이 신호가 파란불인 경우
     * 최적의 출발 시간은
     * 남은 파란불 시간 + 빨간불 남은시간 +(빨간불 사이클 + 파란불 사이클)* x(횟수)  시간이 됩니다.
     * (x >= 300 / (60 + 20) )인 수 입니다. (등호 가능)
     *
     *
     * 이 신호가 빨간불인 경우
     * 최적의 출발 시간은
     * 남은 빨간불 시간 + (빨간불 사이클 + 파란불 사이클)* x(횟수) 시간이 됩니다.
     *
     * (x > 300 / (60 + 20) )인 수 입니다. (등호 불가능)
     *
     * 현재 파란불인 경우
     */

    private final PathFavoritesRepository pathFavoritesRepository;
    private final TrafficInPathFavoritesRepository trafficInPathFavoritesRepository;
    private final TrafficRepository trafficRepository;

    public void execute(Long MemberId, Long favoritesPathId) {
        Optional<PathFavoritesEntity> findPath = pathFavoritesRepository.findById(favoritesPathId);

        PathFavoritesEntity pathFavorites = checkPathFavoritesEntity(findPath);

        Optional<TrafficInPathFavoritesEntity> findTrafficInPathFavorites = trafficInPathFavoritesRepository.findByPathFavoritesId(pathFavorites.getId());

        TrafficInPathFavoritesEntity trafficInPathFavorites = checkTrafficInPathFavoritesEntity(findTrafficInPathFavorites);

        List<TMapTrafficTurnType> trafficTypes = trafficInPathFavorites.getTrafficTypes();
        List<Point> trafficPoints = trafficInPathFavorites.getTrafficPoints();
        
        //case 1 길에 신호등이 없는 경우 -> 그대로 출력 todo


        //case 2 길에 신호등이 있는 경우
        //1. 첫 신호등 위도 경도를 통해 가장 가까운 신호등 리스트를 찾는다.
        List<TrafficEntity> FirstClosetTraffic = trafficRepository.findClosetTrafficByLocation(trafficPoints.get(0).getX(), trafficPoints.get(0).getY());

        //2. 그 중 첫번째의 경우에 어떤 신호등인지 파악한다.
        for(TrafficEntity traffic : FirstClosetTraffic) {


        }



        

    }

    private TrafficInPathFavoritesEntity checkTrafficInPathFavoritesEntity(Optional<TrafficInPathFavoritesEntity> findTrafficInPathFavorites) {
        if (findTrafficInPathFavorites.isEmpty()) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }

        return findTrafficInPathFavorites.get();


    }

    private PathFavoritesEntity checkPathFavoritesEntity(Optional<PathFavoritesEntity> pathFavoritesEntity) {

        if (pathFavoritesEntity.isEmpty()) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }

        return pathFavoritesEntity.get();
    }


}
