package com.googlecode.jazure.sdk.task.tracker;

import java.util.List;

public interface PaginatedList<E> extends List<E> {

	Paginater getPaginater();

}
