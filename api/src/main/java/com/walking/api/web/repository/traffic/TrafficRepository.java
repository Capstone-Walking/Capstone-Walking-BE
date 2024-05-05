package com.walking.api.web.repository.traffic;

import com.walking.data.entity.traffic.TrafficEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficRepository extends JpaRepository<TrafficEntity, Long> {}
