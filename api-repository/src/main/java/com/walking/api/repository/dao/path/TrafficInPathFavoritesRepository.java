package com.walking.api.repository.dao.path;

import com.walking.data.entity.path.TrafficInPathFavoritesEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficInPathFavoritesRepository
		extends JpaRepository<TrafficInPathFavoritesEntity, Long> {

	Optional<TrafficInPathFavoritesEntity> findById(Long pathFavoritesId);
}
