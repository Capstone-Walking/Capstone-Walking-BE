package com.walking.traffic.data.entity

enum class ApiSource(private val source: String) {
    /**
     * 서울시 제공 API - 차량 신호등
     * - certificationId : itstId & direction
     */
    SEOUL_CAR("seoul_car"),
}