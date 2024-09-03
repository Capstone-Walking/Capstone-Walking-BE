package com.walking.traffic.batch.scheduler

import org.quartz.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SeoulLeftTimeJobRunner(
    @Value("\${api.seoul.interval}") private val interval: Int,
    private val scheduler: Scheduler,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        runScheduledJob()
    }

    @Scheduled(cron = "0 0 7 * * ?")
    private fun runScheduledJob() {
        val jobDetail = JobBuilder.newJob(SeoulLeftTimeJob::class.java)
            .withIdentity("seoulLeftTimeJob", "batch")
            .build()

        val trigger: Trigger = TriggerBuilder.newTrigger()
            .withIdentity("seoulLeftTimeTrigger", "batch")
            .startAt(DateBuilder.todayAt(7, 0, 0))
            .endAt(DateBuilder.todayAt(23, 59, 59))
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever()
            )
            .build()
        try {
            scheduler.scheduleJob(jobDetail, trigger)
        } catch (e: SchedulerException) {
            e.printStackTrace()
        }
    }
}