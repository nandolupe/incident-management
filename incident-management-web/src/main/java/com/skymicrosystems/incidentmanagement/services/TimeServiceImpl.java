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
import com.skymicrosystems.incidentmanagement.model.Time;
import com.skymicrosystems.incidentmanagement.repositories.TimeRepository;
import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

@Service
public class TimeServiceImpl {
	
	@Autowired
    private TimeRepository timeRepository;
    
    public Page<Time> searchPaginate(Time time, Pageable pageable) {
		
    	return timeRepository.findAll(
				this.defaultSearchSpec(time, 
						BuildManagementUtils.getEmpresaAuthenticated()), 
				pageable);
	}
    
    public Optional<Time> findById(Long id) {
    	return timeRepository.findById(id);
	}
    
    public Time save(Time time) {
        return timeRepository.save(time);
	}
    
    public Time update(Time time) {
        return timeRepository.saveAndFlush(time);
	}
    
    public void delete(Long id) {
    	timeRepository.findById(id).ifPresent(u -> timeRepository.delete(u));
	}

	private Specification<Time> defaultSearchSpec(Time time, Empresa empresa) {
		return (root, query, criteriaBuilder) -> {
			
			Predicate predicate = null;
			
			predicate = criteriaBuilder.equal(root.get("empresa"), empresa);
			
			if (time != null) {
				
				if (StringUtils.isNotBlank(time.getNomeTime())) {
					predicate = criteriaBuilder
							.and(criteriaBuilder
									.like(criteriaBuilder.upper(
											root.get("nomeTime")), 
											"%" + time.getNomeTime().toUpperCase().trim() + "%"), 
									predicate);
				}
			}
			return predicate;
		};
	}
}
