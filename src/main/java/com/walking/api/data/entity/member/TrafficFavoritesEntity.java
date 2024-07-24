package com.walking.api.data.entity.member;

import com.walking.api.data.entity.BaseEntity;
import com.walking.api.data.entity.traffic.TrafficEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "traffic_favorites")
@SQLDelete(sql = "UPDATE traffic_favorites SET deleted=true where id=?")
public class TrafficFavoritesEntity extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MemberEntity memberFk;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TrafficEntity trafficFk;

	@Column(nullable = false, length = 50)
	private String alias;

	public TrafficFavoritesEntity(MemberEntity memberFk, TrafficEntity trafficFk, String alias) {
		this.memberFk = memberFk;
		this.trafficFk = trafficFk;
		this.alias = alias;
	}

	// todo delete
	public TrafficFavoritesEntity updateAlias(String alias) {
		this.alias = alias;
		return this;
	}
}
