package com.googlecode.jazure.sdk.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;


public class CategoryBufferedMap<C, V> {
	
	private ConcurrentMap<C, Queue<V>> buffer = new ConcurrentHashMap<C, Queue<V>>();
	
	public void put(C category, V singleValue) {
		buffer.putIfAbsent(category, new ConcurrentLinkedQueue<V>());
		buffer.get(category).add(singleValue);
	}
	
	public V remove(C category) {
		Queue<V> queue = buffer.get(category);
		if (queue == null) {
			return null;
		}
		
		return queue.poll();
	}
	
	public void clear() {
		buffer.clear();
	}

}
