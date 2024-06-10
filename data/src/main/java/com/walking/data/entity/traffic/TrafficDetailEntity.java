package com.walking.data.entity.traffic;

import com.walking.data.entity.traffic.constant.Direction;
import com.walking.data.entity.traffic.constant.TrafficColor;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder(toBuilder = true)
@Table(
		name = "traffic_detail",
		indexes = @Index(name = "traffic_id_idx", columnList = "traffic_id"))
@ToString
public class TrafficDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TrafficEntity traffic;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrafficColor color;

	@Column(nullable = false)
	private Float timeLeft;

	@Enumerated(EnumType.STRING)
	private Direction direction;

	private OffsetDateTime colorRegDt; // 색상 정보 등록일자

	private OffsetDateTime timeLeftRegDt; // 잔여 시간 등록일자
}
