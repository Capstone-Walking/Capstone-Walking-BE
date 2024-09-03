package com.walking.traffic.batch.job.seoul

import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition

class SeoulLeftTimeItemWriter(jdbcTemplate: JdbcTemplate, private val txm: PlatformTransactionManager) :
    ItemWriter<SeoulLeftTimeChunk> {
    private val jdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

    override fun write(chunk: Chunk<out SeoulLeftTimeChunk>) {
        val transactionDef = DefaultTransactionDefinition()
        val status = txm.getTransaction(transactionDef)

        val bulkInsertParams = chunk.map { item ->
            MapSqlParameterSource()
                .addValue("itst_id", item.itstId)
                .addValue("direction", item.direction)
                .addValue("color", item.color)
                .addValue("left_time", item.leftTime)
                .addValue("reg_dt", item.leftTimeRegDt)
        }
        val sql = """
            INSERT INTO api.seoul_left_time(itst_id, direction, color, left_time, reg_dt, deleted)
            VALUES (:itst_id, :direction, :color, :left_time, :reg_dt, false)
        """.trimIndent()
        jdbcTemplate.batchUpdate(sql, bulkInsertParams.toTypedArray())

        txm.commit(status)
    }
}