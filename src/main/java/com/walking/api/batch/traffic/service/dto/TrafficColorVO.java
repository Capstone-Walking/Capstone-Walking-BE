package com.walking.api.batch.traffic.service.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrafficColorVO {
	private Long itstId; // 교차로 ID
	private String ntPdsgStatNm; // 북쪽
	private String etPdsgStatNm; // 동쪽
	private String stPdsgStatNm; // 남쪽
	private String wtPdsgStatNm; // 서쪽
	private String nePdsgStatNm; // 북동
	private String sePdsgStatNm; // 남동
	private String swPdsgStatNm; // 남서
	private String nwPdsgStatNm; // 북서
	private OffsetDateTime regDt; // 등록일자
}
