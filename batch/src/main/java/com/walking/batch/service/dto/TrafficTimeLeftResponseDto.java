package com.walking.batch.service.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrafficTimeLeftResponseDto {
	private Long itstId; // 교차로 ID
	private Float ntPdsgRmdrCs; // 북쪽
	private Float etPdsgRmdrCs; // 동쪽
	private Float stPdsgRmdrCs; // 남쪽
	private Float wtPdsgRmdrCs; // 서쪽
	private Float nePdsgRmdrCs; // 북동
	private Float sePdsgRmdrCs; // 남동
	private Float swPdsgRmdrCs; // 남서
	private Float nwPdsgRmdrCs; // 북서
	private OffsetDateTime regDt; // 등록일자
}
