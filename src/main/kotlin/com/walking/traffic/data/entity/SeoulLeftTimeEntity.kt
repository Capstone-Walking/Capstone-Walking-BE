package com.walking.traffic.data.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import java.time.LocalDateTime

/**
 * 차량 신호등 상세 정보
 * color와 leftTime 정보를 요청하는 API가 모두 성공하는 경우에만 저장
 */
@Entity
@Table(
    name = "seoul_left_time",
    indexes = [Index(name = "certification_idx", columnList = "itst_id, direction")]
)
@SQLDelete(sql = "UPDATE seoul_left_time SET deleted=true where id=?")
class SeoulLeftTimeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1,
    /** 서울시 제공 API의 차량 신호등 ID */
    @Column(nullable = false, name = "itst_id")
    val itstId: String,
    @Column(nullable = false, name = "direction")
    val direction: String,
    /** 보행자 신호등 색생 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "color")
    val trafficColor: TrafficColor,
    /** 보행자 신호등 남은 시간 */
    @Column(nullable = false, name = "left_time")
    val leftTime: Double,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(nullable = false, name = "reg_dt")
    val regDt: LocalDateTime,
) : BaseEntity()