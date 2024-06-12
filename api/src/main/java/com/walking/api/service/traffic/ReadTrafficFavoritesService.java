package com.walking.api.service.traffic;

import com.walking.api.repository.dao.traffic.TrafficFavoritesRepository;
import com.walking.data.entity.member.MemberEntity;
import com.walking.data.entity.member.TrafficFavoritesEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadTrafficFavoritesService {

	private final TrafficFavoritesRepository trafficFavoritesRepository;

	public List<TrafficFavoritesEntity> executeByMemberFk(MemberEntity member) {
		return trafficFavoritesRepository.findByMemberFkAndDeletedFalse(member);
	}
}
