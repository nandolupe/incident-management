package com.skymicrosystems.incidentmanagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

@Entity
@Table(name = "PROJETO")
public class Projeto extends AuditModel {

	/*
	 * FIELDS
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProjeto;
	
	private String projetoAbreviado;
	
	private String nomeProjeto;
	
	private String descricaoProjeto;
	
	private Integer diasRetencaoTickets;
	
	private Integer criticidadeSLA;
	
	private String status;

	@ManyToOne
    @JoinColumn(name="idEmpresa", nullable = false)
    private Empresa empresa;
	
	@ManyToOne
    @JoinColumn(name="idCliente", nullable = false)
	private Cliente cliente;
	
	@ManyToOne
    @JoinColumn(name="idColaborador", nullable = false)
	private Colaborador colaboradorResponsavel;
	
	@ManyToOne
    @JoinColumn(name="idTime", nullable = false)
	private Time timeResponsavel;
	
	/*
	 * CONSTRUCTORS
	 */
	
	public Projeto() {
	}
	
	public Projeto(Long idProjeto) {
		super();
		this.idProjeto = idProjeto;
	}

	public Projeto(String projetoAbreviado, String nomeProjeto, String descricaoProjeto, Integer diasRetencaoTickets,
			Integer criticidadeSLA, String status, Empresa empresa,
			Cliente cliente) {
		super();
		this.projetoAbreviado = projetoAbreviado;
		this.nomeProjeto = nomeProjeto;
		this.descricaoProjeto = descricaoProjeto;
		this.diasRetencaoTickets = diasRetencaoTickets;
		this.criticidadeSLA = criticidadeSLA;
		this.status = status;
		this.empresa = empresa;
		this.cliente = cliente;
	}

	/*
	 * GETTERS AND SETTERS
	 */
	
	public Long getIdProjeto() {
		return idProjeto;
	}

	public String getProjetoAbreviado() {
		return projetoAbreviado;
	}

	public void setProjetoAbreviado(String projetoAbreviado) {
		this.projetoAbreviado = projetoAbreviado;
	}

	public void setIdProjeto(Long idProjeto) {
		this.idProjeto = idProjeto;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getDescricaoProjeto() {
		return descricaoProjeto;
	}

	public void setDescricaoProjeto(String descricaoProjeto) {
		this.descricaoProjeto = descricaoProjeto;
	}

	public Integer getDiasRetencaoTickets() {
		return diasRetencaoTickets;
	}

	public void setDiasRetencaoTickets(Integer diasRetencaoTickets) {
		this.diasRetencaoTickets = diasRetencaoTickets;
	}

	public Integer getCriticidadeSLA() {
		return criticidadeSLA;
	}

	public void setCriticidadeSLA(Integer criticidadeSLA) {
		this.criticidadeSLA = criticidadeSLA;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Colaborador getColaboradorResponsavel() {
		return colaboradorResponsavel;
	}

	public void setColaboradorResponsavel(Colaborador colaboradorResponsavel) {
		this.colaboradorResponsavel = colaboradorResponsavel;
	}
	
	public Time getTimeResponsavel() {
		return timeResponsavel;
	}

	public void setTimeResponsavel(Time timeResponsavel) {
		this.timeResponsavel = timeResponsavel;
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
