package com.skymicrosystems.incidentmanagement.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.incidentmanagement.model.Colaborador;
import com.skymicrosystems.incidentmanagement.model.Empresa;
import com.skymicrosystems.incidentmanagement.repositories.ColaboradorRepository;
import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

@Service
public class ColaboradorServiceImpl {
	
	@Autowired
    private ColaboradorRepository colaboradorRepository;
    
    public Page<Colaborador> searchPaginate(Colaborador colaborador, Pageable pageable) {
		
    	return colaboradorRepository.findAll(
				this.defaultSearchSpec(colaborador, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Colaborador> findById(Long id) {
    	return colaboradorRepository.findById(id);
	}
    
    public Colaborador save(Colaborador colaborador) {
        return colaboradorRepository.save(colaborador);
	}
    
    public Colaborador update(Colaborador colaborador) {
        return colaboradorRepository.saveAndFlush(colaborador);
	}
    
    public void delete(Long id) {
    	colaboradorRepository.findById(id).ifPresent(u -> colaboradorRepository.delete(u));
	}

	private Specification<Colaborador> defaultSearchSpec(Colaborador colaborador, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			if (colaborador != null) {
			
				if (StringUtils.isNotBlank(colaborador.getNomeColaborador())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("nomeColaborador")), 
											"%" + colaborador.getNomeColaborador().toUpperCase().trim() + "%"), 
									predicate);
				}
				
				if (StringUtils.isNotBlank(colaborador.getStatus())) {
					predicate = criteriaBuilder.and(
							criteriaBuilder.equal(
									root.get("status"), colaborador.getStatus().toUpperCase().trim()));
				}
				/*
				if (colaborador.getTimes() != null && colaborador.getTimes().size() > 0) {
					predicate = criteriaBuilder.and(
							criteriaBuilder.in(root.get("times.timeId"), colaborador.getTimes()));
				}
				*/
			}
			return predicate;
		};
	}
}
