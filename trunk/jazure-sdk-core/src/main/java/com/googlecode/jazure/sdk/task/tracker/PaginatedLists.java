package com.googlecode.jazure.sdk.task.tracker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaginatedLists {

	private static Logger logger = LoggerFactory.getLogger(PaginatedLists.class);
	
	protected static Method PAGINAGER_GETTER_METHOD = null;
	
	static {
		try {
			PAGINAGER_GETTER_METHOD = PaginatedList.class.getMethod("getPaginater", new Class[] { });
		} catch (Exception e) {
			logger.error("Method getPaginater not found in " + PaginatedList.class, e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <E> PaginatedList<E> proxy(final List<E> target, final Paginater paginater) {
		return (PaginatedList<E>) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {PaginatedList.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
				
				if (!method.equals(PAGINAGER_GETTER_METHOD)) {
					return method.invoke(target, args);
				}
				return paginater;
			}
		});
	}

}
