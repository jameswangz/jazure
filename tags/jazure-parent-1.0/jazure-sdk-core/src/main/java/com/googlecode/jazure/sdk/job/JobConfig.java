package com.googlecode.jazure.sdk.job;

import java.io.Serializable;

import com.googlecode.jazure.sdk.core.Console;
import com.googlecode.jazure.sdk.job.exception.DuplicateJobException;

public interface JobConfig extends Serializable {
	
	/**
	 * Provide an unique job configuration id in specified {@link Console}, enterprise side must
	 * ensure this otherwise a {@link DuplicateJobException} will be raised when invoke 
	 * {@link Console#addEventDrivenJobConfig(JobConfig)} or {@link Console#addPollingJobConfig(JobConfig)}.
	 * @return a unique job configuration id
	 * @see Console#addEventDrivenJobConfig(JobConfig)
	 * @see Console#addPollingJobConfig(JobConfig)
	 * @see DuplicateJobException
	 */
	String getId();


}
