package com.walking.api.repository.traffic;

import com.walking.data.entity.traffic.TrafficDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficDetailRepository extends JpaRepository<TrafficDetailEntity, Long> {}
