package com.ols.doci.job;

import com.ols.doci.Log;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Base class to schedule jobs.
 *
 * @author Luis Brayan
 */
public class CronScheduler
{
    public static Scheduler init()
    {
        try {
            SchedulerFactory factory = new StdSchedulerFactory();
            SCHEDULER = factory.getScheduler();
            return SCHEDULER;
        } catch (SchedulerException ex) {
            Log.error(ex, "On CronScheluder");
            return null;
        }
    }

    protected static Scheduler SCHEDULER;

    public static Scheduler getScheduler()
    {
        return SCHEDULER;
    }

    public static void start() throws SchedulerException
    {
        SCHEDULER.start();
    }

    public static JobDetail prepareJob(Class<? extends CronJob> clazz, String name)
    {
        // specify the job details..
        JobDetail job = JobBuilder.newJob(clazz)
                .withIdentity(name)
                .build();

        // set initial state for the job
        JobDataMap data = job.getJobDataMap();
        data.put(CronJob.JOB_LABEL, name);
        data.put(CronJob.EXECUTION_DELAY, 1);
        data.put(CronJob.EXECUTION_COUNT, 0);

        return job;
    }

    public static Trigger forever(JobDetail job, int interval, CronIntervalUnits unit)
    {
        // specify the interval
        SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule()
                .repeatForever();

        switch (unit) {
            case SECONDS:
                schedule.withIntervalInSeconds(interval);
                break;

            case MINUTES:
                schedule.withIntervalInMinutes(interval);
                break;

            case HOURS:
                schedule.withIntervalInHours(interval);
                break;
        }

        // specify the running period of the job
        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(schedule)
                .build();

        //schedule the job
        try {
            SCHEDULER.scheduleJob(job, trigger);
            return trigger;
        } catch (SchedulerException ex) {
            Log.error(ex, "On CronScheluder");
            return null;
        }
    }
}
