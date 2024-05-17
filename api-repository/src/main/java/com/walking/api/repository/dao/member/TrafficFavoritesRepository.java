package com.walking.api.repository.dao.member;

import com.walking.data.entity.member.TrafficFavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficFavoritesRepository extends JpaRepository<TrafficFavoritesEntity, Long> {}
