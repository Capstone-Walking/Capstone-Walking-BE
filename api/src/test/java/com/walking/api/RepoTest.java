package com.walking.api;

import com.walking.api.web.repository.member.MemberRepository;
import com.walking.api.web.repository.member.PathFavoritesRepository;
import com.walking.api.web.repository.member.TrafficFavoritesRepository;
import com.walking.api.web.repository.traffic.TrafficRepository;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.PathFavoritesEntity;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

@Slf4j
public class RepoTest extends RepositoryTest {

	@Autowired EntityManager em;

	@Autowired MemberRepository memberRepository;

	@Autowired TrafficRepository trafficRepository;

	@Autowired PathFavoritesRepository pathFavoritesRepository;
	@Autowired TrafficFavoritesRepository trafficFavoritesRepository;

	@Nested
	class MemberEntityTest {

		@Test
		// @Rollback(value = false)
		public void saveMember() {
			MemberEntity memberEntity = MemberEntity.builder().memberId("124").password("123").build();

			MemberEntity saved = memberRepository.save(memberEntity);

			Assertions.assertThat(memberEntity.getId()).isEqualTo(saved.getId());
		}

		@Test
		// @Rollback(value = false)
		public void deleteMember() {
			MemberEntity memberEntity = MemberEntity.builder().memberId("123").password("123").build();

			MemberEntity saved = memberRepository.save(memberEntity);
		}

		@Test
		// @Rollback(value = false)
		public void saveTraffic() {

			GeometryFactory gf = new GeometryFactory();
			Point point = gf.createPoint(new Coordinate(123, 321));

			TrafficEntity traffic =
					TrafficEntity.builder().detail("123-L").name("제법이 집 앞").point(point).build();

			TrafficEntity savedTraffic = trafficRepository.save(traffic);

			Optional<TrafficEntity> findTraffic = trafficRepository.findById(savedTraffic.getId());

			Assertions.assertThat(traffic.getId()).isEqualTo(findTraffic.get().getId());
		}

		@Test
		@Rollback(value = false)
		public void saveTrafficFavorites() {

			GeometryFactory gf = new GeometryFactory();
			Point point = gf.createPoint(new Coordinate(123, 321));

			MemberEntity memberEntity = MemberEntity.builder().memberId("123").password("123").build();

			TrafficEntity traffic =
					TrafficEntity.builder().detail("123-L").name("제법이 집 앞").point(point).build();

			em.persist(memberEntity);
			em.persist(traffic);
			em.flush();

			TrafficFavoritesEntity trafficFavorites =
					TrafficFavoritesEntity.builder()
							.memberEntity(memberEntity)
							.trafficEntity(traffic)
							.alias("123")
							.build();

			TrafficFavoritesEntity save = trafficFavoritesRepository.save(trafficFavorites);

			TrafficFavoritesEntity trafficFavoritesEntity =
					trafficFavoritesRepository.findById(save.getId()).get();

			Assertions.assertThat(save.getId()).isEqualTo(trafficFavoritesEntity.getId());
		}

		@Test
		@Rollback(value = false)
		public void savePathFavorites() {

			GeometryFactory gf = new GeometryFactory();
			Coordinate[] coordinates = new Coordinate[3];
			for (int i = 0; i < 3; i++) {
				coordinates[i] = new Coordinate(i * 10, i * 20);
			}
			LineString lineString = gf.createLineString(coordinates);

			Point startPoint = gf.createPoint(coordinates[0]);
			Point endPoint = gf.createPoint(coordinates[2]);

			MemberEntity memberEntity = MemberEntity.builder().memberId("123").password("123").build();

			em.persist(memberEntity);
			em.flush();

			PathFavoritesEntity build =
					PathFavoritesEntity.builder()
							.memberEntity(memberEntity)
							.startPoint(startPoint)
							.startAlias("시작")
							.endPoint(endPoint)
							.endAlias("끝")
							.path(lineString)
							.build();

			PathFavoritesEntity save = pathFavoritesRepository.save(build);

			PathFavoritesEntity pathFavoritesEntity =
					pathFavoritesRepository.findById(save.getId()).get();

			Assertions.assertThat(save.getId()).isEqualTo(pathFavoritesEntity.getId());
		}
	}
}
