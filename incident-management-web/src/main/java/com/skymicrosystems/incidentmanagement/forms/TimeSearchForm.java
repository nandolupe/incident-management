package com.skymicrosystems.incidentmanagement.forms;

import com.skymicrosystems.incidentmanagement.model.Time;

public class TimeSearchForm extends DefaultSearchForm<Time> {

	public TimeSearchForm(Time data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
