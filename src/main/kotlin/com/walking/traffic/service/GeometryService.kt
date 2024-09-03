package com.walking.traffic.service

import org.locationtech.jts.geom.*
import org.springframework.stereotype.Service

@Service
class GeometryService(
    val geometryFactory: GeometryFactory = GeometryFactory(PrecisionModel(), 4326),
) {

    fun createPolygon(coordinates: Array<Coordinate>): Polygon {
        return geometryFactory.createPolygon(coordinates)
    }
}