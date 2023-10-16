package com.skymicrosystems.incidentmanagement.forms;

import com.skymicrosystems.incidentmanagement.model.Empresa;

public class EmpresaSearchForm extends DefaultSearchForm<Empresa> {

	public EmpresaSearchForm(Empresa data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
