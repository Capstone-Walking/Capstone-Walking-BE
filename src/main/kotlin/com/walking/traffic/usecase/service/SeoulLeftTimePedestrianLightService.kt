package com.walking.traffic.usecase.service

import com.walking.traffic.data.entity.PedestrianLightEntity
import com.walking.traffic.data.repository.SeoulLeftTimeRepository
import com.walking.traffic.data.support.SeoulCarCertificationMapper
import com.walking.traffic.usecase.model.PedestrianTrafficLight
import com.walking.traffic.usecase.model.left.PedestrianLeftTimeInfo
import com.walking.traffic.usecase.model.left.RecentPedestrianLeftTimes
import com.walking.traffic.usecase.service.left.SeoulLeftTimeCalculateService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeoulLeftTimePedestrianLightService(
    @Value("\${api.seoul.interval}") private val interval: Int,
    private val seoulLeftTimeRepository: SeoulLeftTimeRepository,
    private val seoulCertificationMapper: SeoulCarCertificationMapper,
    private val seoulLeftTimeCalculateService: SeoulLeftTimeCalculateService,
) : PedestrianLightService {

    @Transactional(readOnly = true)
    override fun execute(sources: List<PedestrianLightEntity>): List<PedestrianTrafficLight> {
        val idMappedSeoulLeftTime = sources.groupBy {
            it.id
        }.mapValues { it ->
            val certification =
                seoulCertificationMapper.toSeoulCertification(it.value.first().sourceCertification)

            seoulLeftTimeRepository.findTopWhereTrafficIdOrderByRegDtDesc(
                itstId = certification.itstId,
                direction = certification.direction
            )
                .map {
                    PedestrianLeftTimeInfo(
                        id = it.itstId,
                        color = it.trafficColor,
                        leftTime = it.leftTime,
                        createdAt = it.createdAt
                    )
                }
                .let {
                    RecentPedestrianLeftTimes(interval, it)
                }
        }

        return seoulLeftTimeCalculateService.execute(
            sources,
            idMappedSeoulLeftTime
        )
    }
}