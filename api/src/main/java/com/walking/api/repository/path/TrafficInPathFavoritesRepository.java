package com.walking.api.repository.path;


import com.walking.data.entity.path.TrafficInPathFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrafficInPathFavoritesRepository extends JpaRepository<TrafficInPathFavoritesEntity, Long> {

    Optional<TrafficInPathFavoritesEntity> findByPathFavoritesId(Long pathFavoritesId);

}
