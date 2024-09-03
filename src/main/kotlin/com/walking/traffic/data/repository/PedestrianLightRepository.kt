package com.walking.traffic.data.repository

import com.walking.traffic.data.entity.PedestrianLightEntity
import org.locationtech.jts.geom.Polygon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PedestrianLightRepository : JpaRepository<PedestrianLightEntity, Long> {

    @Query(
        value = "SELECT * FROM predestrian_light WHERE ST_Contains(:polygon, point_value)",
        nativeQuery = true
    )
    fun findAllInBounds(
        @Param("polygon") polygon: Polygon,
    ): List<PedestrianLightEntity>

    @Modifying
    @Query(
        value = "UPDATE predestrian_light SET green_cycle = :greenCycle , updated_at = :updatedAt WHERE id = :id",
        nativeQuery = true
    )
    fun updateGreenCycle(
        @Param("id") id: Long,
        @Param("greenCycle") greenCycle: Double,
        @Param("updatedAt") updatedAt: LocalDateTime,
    )

    @Modifying
    @Query(
        value = "UPDATE predestrian_light SET red_cycle = :redCycle , updated_at = :updatedAt WHERE id = :id",
        nativeQuery = true
    )
    fun updateRedCycle(
        @Param("id") id: Long,
        @Param("redCycle") redCycle: Double,
        @Param("updatedAt") updatedAt: LocalDateTime,
    )
}