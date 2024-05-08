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
public class TrafficTimeLeftDto {
	private Long itstId; // 교차로 ID
	private String ntPdsgRmdrCs; // 북쪽
	private String etPdsgRmdrCs; // 동쪽
	private String stPdsgRmdrCs; // 남쪽
	private String wtPdsgRmdrCs; // 서쪽
	private String nePdsgRmdrCs; // 북동
	private String sePdsgRmdrCs; // 남동
	private String swPdsgRmdrCs; // 남서
	private String nwPdsgRmdrCs; // 북서
	private OffsetDateTime regDt; // 등록일자
}
