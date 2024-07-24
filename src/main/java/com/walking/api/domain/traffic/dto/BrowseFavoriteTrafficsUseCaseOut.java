package com.walking.api.domain.traffic.dto;

import com.walking.api.domain.traffic.dto.detail.FavoriteTrafficDetail;
import java.util.List;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class BrowseFavoriteTrafficsUseCaseOut {

	private List<FavoriteTrafficDetail> traffics;
}
