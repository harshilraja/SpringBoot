package com.heowc;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SimpleJobLauncherController {

    private final JobLauncher jobLauncher;

    private final Job job;

    @Autowired
    public SimpleJobLauncherController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @RequestMapping("/jobLauncher.html")
    public void handle() throws Exception {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("create_date", new JobParameter(new Date()));
        jobLauncher.run(job, new JobParameters(map));
    }
}
