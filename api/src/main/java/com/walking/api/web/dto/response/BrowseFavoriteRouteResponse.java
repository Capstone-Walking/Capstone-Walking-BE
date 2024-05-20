package com.walking.api.web.dto.response;

import com.walking.api.web.dto.response.detail.FavoriteRouteDetail;
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
public class BrowseFavoriteRouteResponse {

	private List<FavoriteRouteDetail> favoriteRoutes;
}
