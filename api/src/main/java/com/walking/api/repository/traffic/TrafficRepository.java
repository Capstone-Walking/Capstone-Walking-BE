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

	@Query(
			value =
					"SELECT * FROM traffic "
							+ "WHERE ST_Equals(point_value, "
							+ "ST_PointFromText(CONCAT('POINT(', :lat, ' ', :lng, ')'), 4326))",
			nativeQuery = true)
	List<TrafficEntity> findByLocation(@Param("lat") Double lat, @Param("lng") Double lng);

	@Query(
			"SELECT t FROM TrafficEntity t "
					+ "WHERE FUNCTION('ST_Distance_Sphere', t.point, "
					+ "FUNCTION('ST_PointFromText', CONCAT('POINT(', :lat, ' ', :lng, ')'), 4326)) < :distance")
	List<TrafficEntity> findByLocationAndDistance(
			@Param("lat") Float lat, @Param("lng") Float lng, @Param("distance") Integer distance);

	@Query(
			value =
					"SELECT * FROM traffic "
							+ "WHERE ST_Contains("
							+ "    ST_SRID("
							+ "        ST_MakeEnvelope("
							+ "            POINT(:blLng, :blLat), "
							+ "            POINT(:trLng, :trLat)"
							+ "        ), 4326"
							+ "    ), point_value"
							+ ")",
			nativeQuery = true)
	List<TrafficEntity> findTrafficWithinBounds(
			@Param("blLng") float blLng,
			@Param("blLat") float blLat,
			@Param("trLng") float trLng,
			@Param("trLat") float trLat);
}
