package com.skymicrosystems.incidentmanagement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "COLABORADOR")
public class Colaborador extends AuditModel {
	
	/*
	 * FIELDS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idColaborador;
	private String nomeColaborador;
	private String cpf;
	private String status;
	
	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idContato")
	private Contato contato;
	
	@ManyToMany(mappedBy = "colaboradores", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Time> times;
	
	/*
	 * CONSTRUCTORS
	 */
	public Colaborador() {}
	
	public Colaborador(Long idColaborador) {
		super();
		this.idColaborador = idColaborador;
	}
	
	public Colaborador(String nomeColaborador, String cpf, String status, Empresa empresa, Contato contato) {
		super();
		this.nomeColaborador = nomeColaborador;
		this.cpf = cpf;
		this.status = status;
		this.empresa = empresa;
		this.contato = contato;
	}
	
	/*
	 * PROCESSORS
	 */

	public Long getIdColaborador() {
		return idColaborador;
	}

	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}

	public String getNomeColaborador() {
		return nomeColaborador;
	}

	public void setNomeColaborador(String nomeColaborador) {
		this.nomeColaborador = nomeColaborador;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

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
