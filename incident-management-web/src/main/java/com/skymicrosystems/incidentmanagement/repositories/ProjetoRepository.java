package com.skymicrosystems.incidentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.skymicrosystems.incidentmanagement.model.Projeto;

@Component
public interface ProjetoRepository extends JpaRepository<Projeto, Long> , JpaSpecificationExecutor<Projeto>{

}
