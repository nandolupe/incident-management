package com.skymicrosystems.incidentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.skymicrosystems.incidentmanagement.model.Empresa;

@Component
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
