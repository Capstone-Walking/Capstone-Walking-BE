package com.walking.api.repository.dao.member;

import com.walking.data.entity.member.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	boolean existsByIdAndDeletedFalse(Long id);

	Optional<MemberEntity> findByIdAndDeletedFalse(Long id);

	Optional<MemberEntity> findByCertificationIdAndDeletedFalse(String certificationId);
}
