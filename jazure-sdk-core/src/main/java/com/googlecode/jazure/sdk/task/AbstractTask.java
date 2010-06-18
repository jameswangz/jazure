package com.googlecode.jazure.sdk.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTask implements Task {

	private static final long serialVersionUID = -407453809185888472L;

	protected Map<String, Serializable> parameters = new HashMap<String, Serializable>();

	@Override
	public Task addParameter(String key, Serializable value) {
		parameters.put(key, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends Serializable> V getParameter(String key, Class<V> valueType) {
		return (V) parameters.get(key);
	}

	@Override
	public Map<String, Serializable> getParameters() {
		return parameters;
	}

	/**
	 * Convenient implementation for {@link Searchable}, use parameters to construct search keyValues,
	 * this is a common sense for most scenarios, concreate classes can override this default implementation
	 * if they have their own business.
	 * 
	 * @see Searchable
	 * @see #getParameters()
	 */
	@Override
	public Map<String, String> keyValues() {
		Map<String, String> keyValues = new HashMap<String, String>();
		
		for (Map.Entry<String, Serializable> entry : parameters.entrySet()) {
			keyValues.put(entry.getKey(), entry.getValue().toString());
		}
		
		return keyValues;
	}
	
	
	
}
