package com.googlecode.jazure.sdk.endpoint;

import java.util.Collection;

import com.googlecode.jazure.sdk.core.Console;
import com.googlecode.jazure.sdk.event.EventListener;
import com.googlecode.jazure.sdk.lifecycle.LifeCycle;
import com.googlecode.jazure.sdk.task.TaskInvocation;

public interface QueueStorageEndpoint extends LifeCycle {

	void send(TaskInvocation task);

	TaskInvocation receive(String resultQueue);

	Collection<? extends EventListener> listeners();

	void setConsole(Console console);

}
