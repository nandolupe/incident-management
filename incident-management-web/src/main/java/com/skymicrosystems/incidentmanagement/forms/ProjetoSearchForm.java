package com.skymicrosystems.incidentmanagement.forms;

import com.skymicrosystems.incidentmanagement.model.Projeto;

public class ProjetoSearchForm extends DefaultSearchForm<Projeto> {

	public ProjetoSearchForm(Projeto data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
