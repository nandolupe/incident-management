package com.skymicrosystems.incidentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.incidentmanagement.model.Cliente;

@Component
public interface ClienteRepository extends JpaRepository<Cliente, String> , JpaSpecificationExecutor<Cliente>{

}
