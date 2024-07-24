package com.walking.api.data.entity.support.listener;

import com.walking.api.data.entity.traffic.TrafficEntity;
import javax.persistence.PreRemove;

public class TrafficEntitySoftDeleteListener {

	@PreRemove
	private void preRemove(TrafficEntity entity) {
		entity.delete();
	}
}
