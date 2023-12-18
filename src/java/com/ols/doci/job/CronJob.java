package com.ols.doci.job;

import com.ols.doci.EnvLoader;
import com.ols.doci.Log;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * Base class for creating a job.
 *
 * @author Brandon
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class CronJob implements Job
{
    public static final String JOB_LABEL = "label";
    public static final String EXECUTION_DELAY = "executionDelay";
    public static final String EXECUTION_COUNT = "executionCount";

    /**
     * Run a set of checks, to determine if the main process should be run.
     *
     * @param context
     * @return whether the job should run.
     */
    protected boolean hasWorkToDo(JobExecutionContext context)
    {
        return true;
    }

    /**
     * Process to be run by the job.
     *
     * @param context
     * @throws java.lang.Exception
     */
    protected abstract void main(JobExecutionContext context) throws Exception;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        JobDataMap data = context.getJobDetail().getJobDataMap();
        String label = data.getString(JOB_LABEL);
        int count = data.getInt(EXECUTION_COUNT);
        int delay = data.getInt(EXECUTION_DELAY);

        // delay the execution
        if (count < delay) {
            data.put(EXECUTION_COUNT, ++count);
            Log.log("====================== Delaying execution of [{0}] ({1} < {2})", label, count, delay);
            return;
        }

        EnvLoader.refresh();

        if (!this.hasWorkToDo(context)) {
            Log.log("====================== Avoiding execution of [{0}]", label);
            return;
        }

        try {
            Log.log("****** PROCESS [{0}] STARTED ******", label);
            this.main(context);
            Log.log("****** PROCESS [{0}] ENDED ******", label);
        } catch (Exception ex) {
            Log.error(ex, "Uncached exception on execution of [{0}]", label);
            Log.log("****** PROCESS [{0}] ABORTED ******");
        }
    }
}
