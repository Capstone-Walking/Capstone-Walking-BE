package com.walking.traffic.data.repository

import com.walking.traffic.data.entity.SeoulLeftTimeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SeoulLeftTimeRepository : JpaRepository<SeoulLeftTimeEntity, Long> {
    @Query(
        value = "SELECT * FROM seoul_left_time WHERE seoul_left_time.itst_id = :itstId and seoul_left_time.direction = :direction ORDER BY seoul_left_time.reg_dt DESC LIMIT :limit",
        nativeQuery = true
    )
    fun findTopWhereTrafficIdOrderByRegDtDesc(
        itstId: String,
        direction: String,
        limit: Int? = 10,
    ): List<SeoulLeftTimeEntity>
}