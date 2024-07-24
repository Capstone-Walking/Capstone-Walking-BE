package com.walking.api.domain.path.service.dto;

import com.walking.api.data.entity.path.TrafficDirection;
import com.walking.api.data.entity.traffic.TrafficEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PathTrafficVO {

	private List<TrafficDirection> trafficDirections = new ArrayList<>();
	private List<TrafficEntity> trafficsInPath = new ArrayList<>();
	private List<TrafficEntity> allTraffics = new ArrayList<>();
}
