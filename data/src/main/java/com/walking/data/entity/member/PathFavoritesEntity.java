package com.walking.data.entity.member;

import com.walking.data.entity.BaseEntity;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "path_favorites")
@SQLDelete(sql = "UPDATE path_favorites SET deleted=true where id=?")
public class PathFavoritesEntity extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private MemberEntity memberEntity;

	@Column(columnDefinition = "POINT")
	private Point startPoint;

	@Column(columnDefinition = "POINT")
	private Point endPoint;

	@Column(columnDefinition = "LINESTRING")
	private LineString path;

	@Column(nullable = false)
	private String startAlias;

	@Column(nullable = false)
	private String endAlias;
}
