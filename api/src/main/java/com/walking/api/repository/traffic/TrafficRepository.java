package com.walking.api.repository.traffic;

import com.walking.data.entity.traffic.TrafficEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficRepository extends JpaRepository<TrafficEntity, Long> {

	@Query("SELECT t FROM TrafficEntity t where t.id IN :ids")
	List<TrafficEntity> findByIds(@Param("ids") List<Long> ids);
}
