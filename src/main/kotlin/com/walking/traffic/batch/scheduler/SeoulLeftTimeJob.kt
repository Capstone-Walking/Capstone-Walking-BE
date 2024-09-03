package com.walking.traffic.batch.scheduler

import org.quartz.*
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import java.util.*

@Component
class SeoulLeftTimeJob(
    private val trafficDetailJob: Job,
    private val jobLauncher: JobLauncher,
) : QuartzJobBean() {

    override fun executeInternal(context: JobExecutionContext) {
        val jobParameters = JobParametersBuilder()
            .addLong("id", Date().time)
            .toJobParameters()
        jobLauncher.run(trafficDetailJob, jobParameters)
    }
}