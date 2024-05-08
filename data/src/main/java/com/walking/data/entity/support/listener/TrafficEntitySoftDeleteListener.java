package com.walking.data.entity.support.listener;

import com.walking.data.entity.traffic.TrafficEntity;
import javax.persistence.PreRemove;

public class TrafficEntitySoftDeleteListener {

	@PreRemove
	private void preRemove(TrafficEntity entity) {
		entity.delete();
	}
}
