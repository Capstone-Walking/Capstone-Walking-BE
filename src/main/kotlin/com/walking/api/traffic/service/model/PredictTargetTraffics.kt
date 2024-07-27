package com.walking.api.traffic.service.model

import java.time.OffsetDateTime

class PredictTargetTraffics {

    private val traffics: MutableList<PredictTargetTraffic> = ArrayList()

    fun addTraffic(traffic: PredictTargetTraffic) {
        traffics.add(traffic)
    }

    fun doAllPredict(standardTime: OffsetDateTime): Map<Long, PredictTargetTraffic> {
        val predictedMap: MutableMap<Long, PredictTargetTraffic> = HashMap()
        for (predictTargetTraffic in traffics) {
            if (predictTargetTraffic.doPredict(standardTime)) {
                predictedMap[predictTargetTraffic.traffic.id] = predictTargetTraffic
            }
        }
        return predictedMap
    }
}