package com.walking.api.web.dto.request.path;

import com.walking.api.web.dto.request.validator.LatParam;
import com.walking.api.web.dto.request.validator.LngParam;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class FavoritePathBody {

	/* 즐겨찾기 경로 이름 */
	@Nullable private String name;

	/* 시작점 위도 */
	@LatParam private double startLat;

	/* 시작점 경도 */
	@LngParam private double startLng;

	/* 종료점 위도 */
	@LatParam private double endLat;

	/* 종료점 경도 */
	@LngParam private double endLng;

	/**
	 * 요청에 즐겨찾기 저장을 위한 이름이 있는지 확인합니다.
	 *
	 * @return 즐겨찾기 저장을 위한 이름이 없는 경우 true, 그렇지 않은 경우 false
	 */
	public boolean hasName() {
		return Objects.isNull(name) || name.isEmpty();
	}
}
