package com.googlecode.jazure.sdk.event;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.core.task.TaskExecutor;

import com.google.inject.Inject;
import com.google.inject.internal.Preconditions;
import com.googlecode.jazure.sdk.concurrent.ThreadPoolExecutor;


public class ConcurrentEventPublisher implements EventPublisher {

	private Set<EventListener> listeners = new HashSet<EventListener> ();

	private TaskExecutor mainExecutor = new ThreadPoolExecutor("JAzure - EventPublisher - main");

	private ConcurrentMap<EventListener, TaskExecutor> listenerExecutors = new ConcurrentHashMap<EventListener, TaskExecutor>();
	
	@Inject
	public ConcurrentEventPublisher(Set<EventListener> listeners) {
		this.listeners = listeners;
		initializeListenerExecutors();
	}

	private void initializeListenerExecutors() {
		for (EventListener listener : listeners) {
			listenerExecutors.putIfAbsent(
				listener, 
				new ThreadPoolExecutor("JAzure - EventPublisher - " + listener)
			);
		}
	}

	@Override
	public void publishEvent(final EventObject event) {
		mainExecutor.execute(new Runnable() {
			@Override
			public void run() {
				for (final EventListener listener: listeners) {
					executorFor(listener).execute(new Runnable() {
						@Override
						public void run() {
							listener.onEvent(event);
						}
					});
				}
			}
		});
	}

	private TaskExecutor executorFor(EventListener listener) {
		return Preconditions.checkNotNull(listenerExecutors.get(listener), "listener executor for [" + listener + "] missing");
	}


}
