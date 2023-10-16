package com.skymicrosystems.incidentmanagement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "TIME")
public class Time extends AuditModel {
	
	/*
	 * FIELDS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idTime;
	private String nomeTime;
	private String status;
	
	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "TimeColaborador",
            joinColumns = @JoinColumn(name = "idTime"),
            inverseJoinColumns = @JoinColumn(name = "idColaborador")
    )
    private List<Colaborador> colaboradores;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Time() {}
	
	public Time(Long idTime) {
		super();
		this.idTime = idTime;
	}

	public Time(String nomeTime, String status, Empresa empresa, List<Colaborador> colaboradores) {
		super();
		this.nomeTime = nomeTime;
		this.status = status;
		this.empresa = empresa;
		this.colaboradores = colaboradores;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	
	public Long getIdTime() {
		return idTime;
	}

	public void setIdTime(Long idTime) {
		this.idTime = idTime;
	}

	public String getNomeTime() {
		return nomeTime;
	}

	public void setNomeTime(String nomeTime) {
		this.nomeTime = nomeTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public List<Colaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}
	
	/*
	 * PROCESSORS
	 */

	@PrePersist
	@Override
	protected void prePersist() {
		super.prePersist();
		if (getEmpresa() == null)
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}
	
	@PreUpdate
	@Override
	protected void preUpdate() {
		super.preUpdate();
		
		if (getEmpresa() == null) 
			this.setEmpresa(BuildManagementUtils.getEmpresaAuthenticated());
	}
}
