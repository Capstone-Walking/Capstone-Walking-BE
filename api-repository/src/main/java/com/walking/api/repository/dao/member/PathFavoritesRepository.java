package com.walking.api.repository.dao.member;

import com.walking.data.entity.path.PathFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PathFavoritesRepository extends JpaRepository<PathFavoritesEntity, Long> {}
