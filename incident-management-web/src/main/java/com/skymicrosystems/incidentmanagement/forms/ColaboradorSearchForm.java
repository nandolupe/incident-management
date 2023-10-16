package com.skymicrosystems.incidentmanagement.forms;

import com.skymicrosystems.incidentmanagement.model.Colaborador;

public class ColaboradorSearchForm extends DefaultSearchForm<Colaborador> {

	public ColaboradorSearchForm(Colaborador data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
