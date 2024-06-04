package com.walking.data.entity.member;

import com.walking.data.entity.BaseEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "traffic_favorites")
@SQLDelete(sql = "UPDATE traffuc_favorites SET deleted=true where id=?")
public class TrafficFavoritesEntity extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MemberEntity memberFk;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TrafficEntity trafficFk;

	@Column(nullable = false, length = 50)
	private String alias;

	// todo delete
	public TrafficFavoritesEntity updateAlias(String alias) {
		this.alias = alias;
		return this;
	}
}
