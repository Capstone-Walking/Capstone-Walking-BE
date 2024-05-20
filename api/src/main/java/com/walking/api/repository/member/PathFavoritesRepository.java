package com.walking.api.repository.member;

import com.walking.api.repository.dto.response.PathFavoritesVo;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.PathFavoritesEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PathFavoritesRepository extends JpaRepository<PathFavoritesEntity, Long> {

	@Query(
			" select new  com.walking.api.repository.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " order by pf.order desc ")
	List<PathFavoritesVo> findPathFavoritesEntitiesByMemberFkOrderByOrderDesc(
			@Param("memberFk") MemberEntity memberFk);

	@Query(
			" select new  com.walking.api.repository.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " order by pf.createdAt")
	List<PathFavoritesVo> findPathFavoritesByMemberFkOrderByCreatedAt(
			@Param("memberFk") MemberEntity memberFk);

	@Query(
			" select new  com.walking.api.repository.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " order by pf.name")
	List<PathFavoritesVo> findPathFavoritesByMemberFkOrderByName(
			@Param("memberFk") MemberEntity memberFk);

	@Query(
			" select new  com.walking.api.repository.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " and ( pf.name like concat('%',:name,'%') or pf.startAlias like concat('%',:name,'%')  or pf.endAlias like concat('%',:name,'%')) "
					+ " order by pf.order desc")
	List<PathFavoritesVo> findPathFavoritesByMemberFkAndFilterName(
			@Param("memberFk") MemberEntity memberFk, @Param("name") String name);

	@Query("select max(pf.order)" + "from PathFavoritesEntity pf")
	Long findMaxOrder();

	void deleteByMemberFkAndId(MemberEntity memberId, Long pathId);
}
