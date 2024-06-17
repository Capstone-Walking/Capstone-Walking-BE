package com.walking.api.domain.path.dto;

import com.walking.data.entity.path.TrafficDirection;
import com.walking.data.entity.traffic.TrafficEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class PathTrafficData {

	private List<TrafficDirection> trafficDirections = new ArrayList<>();
	private List<TrafficEntity> trafficsInPath = new ArrayList<>();
	private List<TrafficEntity> allTraffics = new ArrayList<>();
}
