package com.walking.data.entity.traffic;

import com.walking.data.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(name = "traffic", indexes = @Index(name = "point_idx", columnList = "point_value"))
@SQLDelete(sql = "UPDATE traffic SET deleted=true where id=?")
public class TrafficEntity extends BaseEntity {

	@Column(columnDefinition = "JSON", nullable = false)
	private String detail = "{}";

	@Column(nullable = false)
	private String name;

	@Column(columnDefinition = "POINT SRID 4326", nullable = false, name = "point_value")
	private Point point;

	public double getLat() {
		return point.getY();
	}

	public double getLng() {
		return point.getX();
	}
}
