package com.walking.data.entity.member;

import com.walking.data.entity.BaseEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "traffic_favorites")
@SQLDelete(sql = "UPDATE traffuc_favorites SET deleted=true where id=?")
public class TrafficFavoritesEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @OneToOne(fetch = FetchType.LAZY)
    private TrafficEntity trafficEntity;

    @Column(nullable = false)
    private String alias;

}
