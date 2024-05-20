package com.walking.batch.chunk.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class TrafficApiDetailDto {

	private Long itstId; // 교차로 ID
	private String ntPdsgStatNm; // 북쪽
	private String etPdsgStatNm; // 동쪽
	private String stPdsgStatNm; // 남쪽
	private String wtPdsgStatNm; // 서쪽
	private String nePdsgStatNm; // 북동
	private String sePdsgStatNm; // 남동
	private String swPdsgStatNm; // 남서
	private String nwPdsgStatNm; // 북서

	private Float ntPdsgRmdrCs; // 북쪽
	private Float etPdsgRmdrCs; // 동쪽
	private Float stPdsgRmdrCs; // 남쪽
	private Float wtPdsgRmdrCs; // 서쪽
	private Float nePdsgRmdrCs; // 북동
	private Float sePdsgRmdrCs; // 남동
	private Float swPdsgRmdrCs; // 남서
	private Float nwPdsgRmdrCs; // 북서

	private OffsetDateTime colorRegDt; // 색상 정보 등록일자

	private OffsetDateTime timeLeftRegDt; // 잔여 시간 등록일자

	public List<TrafficDetailDto> getTrafficDetailDtos() {
		List<TrafficDetailDto> detailDtos = new ArrayList<>();

		generateTrafficDetailDto(ntPdsgStatNm, ntPdsgRmdrCs, "nt").ifPresent(detailDtos::add);
		generateTrafficDetailDto(etPdsgStatNm, etPdsgRmdrCs, "et").ifPresent(detailDtos::add);
		generateTrafficDetailDto(stPdsgStatNm, stPdsgRmdrCs, "st").ifPresent(detailDtos::add);
		generateTrafficDetailDto(wtPdsgStatNm, wtPdsgRmdrCs, "wt").ifPresent(detailDtos::add);
		generateTrafficDetailDto(nePdsgStatNm, nePdsgRmdrCs, "ne").ifPresent(detailDtos::add);
		generateTrafficDetailDto(sePdsgStatNm, sePdsgRmdrCs, "se").ifPresent(detailDtos::add);
		generateTrafficDetailDto(swPdsgStatNm, swPdsgRmdrCs, "sw").ifPresent(detailDtos::add);
		generateTrafficDetailDto(nwPdsgStatNm, nwPdsgRmdrCs, "nw").ifPresent(detailDtos::add);
		return detailDtos;
	}

	private Optional<TrafficDetailDto> generateTrafficDetailDto(
			String pdsgStatNm, Float pdsgRmdrCs, String direction) {
		if (pdsgStatNm == null || pdsgRmdrCs == null) {
			return Optional.empty();
		}
		return Optional.of(
				TrafficDetailDto.builder()
						.itstId(itstId)
						.direction(direction)
						.color(convertToColor(pdsgStatNm))
						.timeLeft(pdsgRmdrCs)
						.colorRegDt(colorRegDt)
						.timeLeftRegDt(timeLeftRegDt)
						.build());
	}

	private String convertToColor(String pdsgStatNm) {
		if (pdsgStatNm.equals("stop-And-Remain")) {
			return "RED";
		}
		if (pdsgStatNm.equals("protected-Movement-Allowed")
				|| pdsgStatNm.equals("permissive-Movement-Allowed")
				|| pdsgStatNm.equals("permissive-clearance")) {
			return "GREEN";
		}
		if (pdsgStatNm.equals("dark")) {
			return "DARK";
		}
		throw new IllegalArgumentException(
				"itstId " + itstId + "의 전달 값 [" + pdsgStatNm + "]은(는) 잘못된 타입 입니다.");
	}
}
