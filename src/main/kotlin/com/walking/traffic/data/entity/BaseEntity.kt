package com.walking.traffic.data.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@JsonIgnoreProperties(value = ["createdAt, modifiedAt"], allowGetters = true)
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    /** 생성일 */
    @CreatedDate
    @Column(name = "created_at", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    var createdAt: LocalDateTime = LocalDateTime.now()

    /** 수정일 */
    @LastModifiedDate
    @Column(
        name = "modified_at",
        columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    )
    var modifiedAt: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false, name = "deleted")
    var deleted: Boolean = false
}