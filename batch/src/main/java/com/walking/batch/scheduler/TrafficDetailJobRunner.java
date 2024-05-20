package com.walking.batch.scheduler;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrafficDetailJobRunner extends JobRunner {

	private final Scheduler scheduler;

	@Value("${walking.batch.schedular.interval}")
	private int interval;

	@Override
	protected void doRun(ApplicationArguments args) {
		JobDetail jobDetail =
				buildJobDetail(TrafficDetailSchJob.class, "trafficDetailJob", "batch", new HashMap<>());
		// 트리거 설정: 매일 06:00에 시작, 23:59:59에 종료, 1분 10초(70초) 간격으로 반복
		Trigger trigger =
				TriggerBuilder.newTrigger()
						.withIdentity("trafficDetailTrigger", "batch")
						.startAt(DateBuilder.todayAt(6, 0, 0)) // 오늘 06:00에 시작
						.endAt(DateBuilder.todayAt(23, 59, 59)) // 오늘 23:59:59에 종료
						.withSchedule(
								SimpleScheduleBuilder.simpleSchedule()
										.withIntervalInSeconds(interval) // 70초 간격으로 실행
										.repeatForever()) // 무한 반복
						.build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
