package com.walking.data.entity.traffic;

import com.walking.data.entity.BaseEntity;
import com.walking.data.entity.traffic.constant.TrafficColor;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Table(name = "traffic_detail")
@SQLDelete(sql = "UPDATE traffic_detail SET deleted=true where id=?")
public class TrafficDetailEntity extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TrafficEntity traffic;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrafficColor color;

	@Column(nullable = false)
	private Float timeLeft;
}
