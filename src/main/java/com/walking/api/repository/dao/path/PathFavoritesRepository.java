package com.walking.api.repository.dao.path;

import com.walking.api.data.entity.path.PathFavoritesEntity;
import com.walking.api.repository.dao.dto.response.PathFavoritesVo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PathFavoritesRepository extends JpaRepository<PathFavoritesEntity, Long> {

	@Query(
			" select new  com.walking.api.repository.dao.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " order by pf.order desc ")
	List<PathFavoritesVo> findPathFavoritesEntitiesByMemberFkOrderByOrderDesc(
			@Param("memberFk") Long memberFk);

	@Query(
			" select new  com.walking.api.repository.dao.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " order by pf.createdAt")
	List<PathFavoritesVo> findPathFavoritesByMemberFkOrderByCreatedAt(
			@Param("memberFk") Long memberFk);

	@Query(
			" select new  com.walking.api.repository.dao.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " order by pf.name")
	List<PathFavoritesVo> findPathFavoritesByMemberFkOrderByName(@Param("memberFk") Long memberFk);

	@Query(
			" select new  com.walking.api.repository.dao.dto.response.PathFavoritesVo(pf.id,pf.startPoint,pf.endPoint,pf.startAlias,pf.endAlias,pf.name,pf.createdAt)"
					+ " from PathFavoritesEntity pf"
					+ " where pf.memberFk = :memberFk"
					+ " and ( pf.name like concat('%',:name,'%') or pf.startAlias like concat('%',:name,'%')  or pf.endAlias like concat('%',:name,'%')) "
					+ " order by pf.order desc")
	List<PathFavoritesVo> findPathFavoritesByMemberFkAndFilterName(
			@Param("memberFk") Long memberFk, @Param("name") String name);

	@Query("select coalesce(max(pf.order) ,0)" + "from PathFavoritesEntity pf")
	Long findMaxOrder();

	Optional<PathFavoritesEntity> findById(Long id);

	@Query(
			"update PathFavoritesEntity pf set pf.name = :name, pf.startAlias = :startAlias, pf.endAlias = :endAlias"
					+ " where pf.memberFk = :memberFk and pf.id = :pathId")
	Long updatePathName(
			@Param("memberFk") Long memberFk,
			@Param("pathId") Long pathId,
			@Param("name") String name,
			@Param("startAlias") String startAlias,
			@Param("endAlias") String endAlias);

	void deleteByMemberFkAndId(Long memberId, Long pathId);
}
