package com.walking.api.data.entity.path;

import com.walking.api.data.entity.BaseEntity;
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

	@Column(nullable = false)
	private Long memberFk;

	@Column(nullable = false, columnDefinition = "POINT SRID 4326")
	private Point startPoint;

	@Column(nullable = false, columnDefinition = "POINT SRID 4326")
	private Point endPoint;

	@Column(nullable = false, columnDefinition = "LINESTRING SRID 4326")
	private LineString path;

	@Column(nullable = false, length = 50)
	private String startAlias;

	@Column(nullable = false, length = 50)
	private String endAlias;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, name = "orders")
	private Long order;

	@Column(nullable = false)
	private Integer untilFirstTrafficTime;

	@Column(nullable = false)
	private Integer totalTime;

	@Column(nullable = false)
	private Integer totalDistance;
}
