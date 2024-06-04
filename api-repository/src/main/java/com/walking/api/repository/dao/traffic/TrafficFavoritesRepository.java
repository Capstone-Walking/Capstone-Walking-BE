package com.walking.api.repository.traffic;

import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficFavoritesRepository extends JpaRepository<TrafficFavoritesEntity, Long> {

	List<TrafficFavoritesEntity> findByMemberFk(MemberEntity memberFk);

	Optional<TrafficFavoritesEntity> findByIdAndDeletedFalse(Long id);

	Optional<TrafficFavoritesEntity> findByIdAndMemberFkAndDeletedFalse(
			Long id, MemberEntity memberFk);
}
