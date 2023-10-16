package com.skymicrosystems.incidentmanagement.forms;

import com.skymicrosystems.incidentmanagement.model.Cliente;

public class ClienteSearchForm extends DefaultSearchForm<Cliente> {

	public ClienteSearchForm(Cliente data, Integer pageNumber, Integer size) {
		super.setData(data);
		super.setPageNumber(pageNumber);
		super.setSize(size);
	}
	
}
