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

	@Query(value = "WITH ClosestSet  As (" +
			"SELECT * , ST_Distance_Sphere(t.point, POINT(:longitude, :latitude) AS distance " +
			" FROM TrafficEntity t" +
			" ORDER BY distance ASC" +
			" LIMIT 1 )" +
			" select t.* " +
			"from ClosestSet cs, TrafficEntity t" +
			" where cs.point = t.point )"
	, nativeQuery = true)
	List<TrafficEntity> findClosetTrafficByLocation(@Param("latitude") Double latitude, @Param("longitude") Double longitude);

}
