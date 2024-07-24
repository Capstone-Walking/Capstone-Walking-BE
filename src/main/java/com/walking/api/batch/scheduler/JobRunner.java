package com.walking.api.batch.scheduler;

import java.util.Map;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public abstract class JobRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		doRun(args);
	}

	/** 자식 클래스마다 배치를 어떻게 돌릴지 구현 */
	protected abstract void doRun(ApplicationArguments args);

	/** Trigger를 가져온다. */
	public Trigger buildJobTrigger(String scheduleExp) {
		return TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
				.build();
	}

	/** JobDetail을 가져온다. */
	public JobDetail buildJobDetail(Class job, String name, String group, Map params) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(params);

		// newJob(job): 실제 job을 가져오는 단계
		return JobBuilder.newJob(job).withIdentity(name, group).usingJobData(jobDataMap).build();
	}
}
