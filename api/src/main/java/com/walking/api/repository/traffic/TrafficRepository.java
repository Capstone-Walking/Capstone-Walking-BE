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


	// 주변 1km의 Polygon을 만들어 인덱스를 타도록
	@Query(
			value =
					" WITH temp AS ( "
							+ " SELECT *, ST_DISTANCE(t.point_value, ST_SRID(POINT(:longitude, :latitude), 4326)) AS distance "
							+ " FROM traffic t "
							+ " WHERE ST_Contains( "
							+ " ST_GeomFromText(CONCAT('POLYGON((', "
							+ ":latitude - 0.0113, ' ', :longitude + 0.009, ', ', "
							+ ":latitude + 0.0113, ' ', :longitude + 0.009, ', ', "
							+ ":latitude + 0.0113, ' ', :longitude - 0.009, ', ', "
							+ ":latitude - 0.0113, ' ', :longitude - 0.009, ', ', "
							+ ":latitude - 0.0113, ' ', :longitude + 0.009, '))'), 4326), t.point_value)) "
							+ " SELECT * FROM temp t1 "
							+ " WHERE distance = ("
							+ " SELECT MIN(t2.distance) "
							+ "  FROM temp t2)",
			nativeQuery = true)
	List<TrafficEntity> findClosetTrafficByLocation(
			@Param("longitude") Double longitude, @Param("latitude") Double latitude);

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
