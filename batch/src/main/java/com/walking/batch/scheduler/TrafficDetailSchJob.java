package com.walking.batch.scheduler;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrafficDetailSchJob extends QuartzJobBean {

	private final Job trafficDetailJob; // Job 주입

	private final JobLauncher jobLauncher; // Job 실행기 주입

	@SneakyThrows
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobParameters jobParameters =
				new JobParametersBuilder()
						.addLong("id", new Date().getTime()) // JobParameter 를 달리하여 계속 실행할 수 있도록 파리미터 추가
						.toJobParameters();

		jobLauncher.run(trafficDetailJob, jobParameters);
	}
}
