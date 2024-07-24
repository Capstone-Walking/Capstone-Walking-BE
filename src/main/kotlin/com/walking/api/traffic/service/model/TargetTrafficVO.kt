package com.walking.api.traffic.service.model

import org.locationtech.jts.geom.Point

data class TargetTrafficVO(val id: Long, val name: String, val point: Point, val detail: String)