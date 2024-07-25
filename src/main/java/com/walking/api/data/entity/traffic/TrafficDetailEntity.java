package com.walking.api.data.entity.traffic;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
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

	@Column(name = "traffic_id", nullable = false)
	private Long traffic;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TrafficColor color;

	@Column(nullable = false)
	private Float timeLeft;

	@Enumerated(EnumType.STRING)
	private Direction direction;

	@Column(nullable = false)
	private OffsetDateTime colorRegDt;

	@Column(nullable = false)
	private OffsetDateTime timeLeftRegDt;
}
