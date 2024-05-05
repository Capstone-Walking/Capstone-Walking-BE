package com.walking.data.entity.traffic;


import com.walking.data.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "traffic")
@SQLDelete(sql = "UPDATE traffic SET deleted=true where id=?")
public class TrafficEntity extends BaseEntity {

    @Column(nullable = false , updatable = false)
    private String detail;

    @Column(nullable = false)
    private String name;

    @Column( columnDefinition = "POINT SRID 4326", nullable = false)
    private Point point;

}
