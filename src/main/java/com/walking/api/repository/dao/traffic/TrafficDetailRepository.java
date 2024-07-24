package com.walking.api.repository.dao.traffic;

import com.walking.api.data.entity.traffic.TrafficDetailEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficDetailRepository extends JpaRepository<TrafficDetailEntity, Long> {

	@Query(
			value =
					"SELECT * FROM traffic_detail WHERE traffic_detail.traffic_id = :trafficId ORDER BY traffic_detail.time_left_reg_dt DESC",
			nativeQuery = true)
	List<TrafficDetailEntity> findTopWhereTrafficIdOrderByTimeLeftRegDtDesc(Long trafficId);
}
