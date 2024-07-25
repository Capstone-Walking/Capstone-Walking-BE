package com.walking.api.data.entity.member;

import com.walking.api.data.entity.BaseEntity;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "traffic_favorites")
@SQLDelete(sql = "UPDATE traffic_favorites SET deleted=true where id=?")
public class TrafficFavoritesEntity extends BaseEntity {

	@Column(nullable = false)
	private Long memberFk;

	@Column(nullable = false)
	private Long trafficFk;

	@Column(nullable = false, length = 50)
	private String alias;

	// todo delete
	public TrafficFavoritesEntity updateAlias(String alias) {
		this.alias = alias;
		return this;
	}
}
