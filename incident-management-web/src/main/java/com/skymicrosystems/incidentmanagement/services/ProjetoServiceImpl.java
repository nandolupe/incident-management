package com.skymicrosystems.incidentmanagement.services;

import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.skymicrosystems.incidentmanagement.model.Empresa;
import com.skymicrosystems.incidentmanagement.model.Projeto;
import com.skymicrosystems.incidentmanagement.repositories.ProjetoRepository;
import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

@Service
public class ProjetoServiceImpl {
	
	@Autowired
    private ProjetoRepository projetoRepository;
    
    public Page<Projeto> searchPaginate(Projeto projeto, Pageable pageable) {
		
    	return projetoRepository.findAll(
				this.defaultSearchSpec(projeto, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Projeto> findById(Long id) {
    	return projetoRepository.findById(id);
	}
    
    public Projeto save(Projeto projeto) {
        return projetoRepository.save(projeto);
	}
    
    public Projeto update(Projeto projeto) {
        return projetoRepository.saveAndFlush(projeto);
	}
    
    public void delete(Long id) {
    	projetoRepository.findById(id).ifPresent(u -> projetoRepository.delete(u));
	}

	private Specification<Projeto> defaultSearchSpec(Projeto projeto, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			if (projeto != null) {
				
				if (StringUtils.isNotBlank(projeto.getNomeProjeto())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("nomeProjeto")), 
											"%" + projeto.getNomeProjeto().toUpperCase().trim() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
}
