package com.walking.traffic.data.entity

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.locationtech.jts.geom.Point

@Entity
@Table(
    name = "predestrian_light",
    indexes = [Index(name = "location_point_idx", columnList = "location_point")]
)
@SQLDelete(sql = "UPDATE predestrian_light SET deleted=true where id=?")
class PedestrianLightEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1,
    /** 보행자 신호등 이름 */
    @Column(nullable = false, name = "location_name")
    val locationName: String,
    /** 보행자 신호등 위치 */
    @Column(columnDefinition = "POINT SRID 4326", nullable = false, name = "location_point")
    val locationPoint: Point,
    /** 보행자 녹색 신호등 주기 */
    @Column(nullable = true, name = "green_cycle")
    val greenCycle: Double = 0.0,
    /** 보행자 빨간색 신호등 주기 */
    @Column(nullable = true, name = "red_cycle")
    val redCycle: Double = 0.0,
    @Column(nullable = false, name = "source")
    @Enumerated(EnumType.STRING)
    val source: ApiSource,
    @Column(nullable = false, columnDefinition = "JSON", name = "source_certification")
    val sourceCertification: String,
) : BaseEntity()