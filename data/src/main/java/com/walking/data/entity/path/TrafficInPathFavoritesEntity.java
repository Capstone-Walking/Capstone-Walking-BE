package com.walking.data.entity.path;

import com.walking.data.converter.PointListConverter;
import com.walking.data.converter.TMapEnumListConverter;
import com.walking.data.entity.BaseEntity;
import java.util.List;
import javax.persistence.*;
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
@Table(name = "traffic_in_path_favorites")
@SQLDelete(sql = "UPDATE traffic_in_path_favorites SET deleted=true where id=?")
public class TrafficInPathFavoritesEntity extends BaseEntity {

	@Convert(converter = PointListConverter.class)
	private List<Point> trafficPoints;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private PathFavoritesEntity pathFk;

	@Convert(converter = TMapEnumListConverter.class)
	private List<TrafficDirection> trafficTypes;
}
