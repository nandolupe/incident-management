package com.skymicrosystems.incidentmanagement.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.skymicrosystems.incidentmanagement.enums.StatusEnum;
import com.skymicrosystems.incidentmanagement.enums.TipoAcessoEnum;
import com.skymicrosystems.incidentmanagement.enums.TipoClienteEnum;
import com.skymicrosystems.incidentmanagement.model.Cliente;
import com.skymicrosystems.incidentmanagement.model.Colaborador;
import com.skymicrosystems.incidentmanagement.model.Contato;
import com.skymicrosystems.incidentmanagement.model.Empresa;
import com.skymicrosystems.incidentmanagement.model.Endereco;
import com.skymicrosystems.incidentmanagement.model.Papel;
import com.skymicrosystems.incidentmanagement.model.Time;
import com.skymicrosystems.incidentmanagement.model.Usuario;
import com.skymicrosystems.incidentmanagement.repositories.ClienteRepository;
import com.skymicrosystems.incidentmanagement.repositories.EmpresaRepository;
import com.skymicrosystems.incidentmanagement.repositories.PapelRepository;
import com.skymicrosystems.incidentmanagement.repositories.UsuarioRepository;

@Service
public class DatabaseInitializingServiceImpl {
	
	private final static Logger logger = LoggerFactory.getLogger(DatabaseInitializingServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ColaboradorServiceImpl colaboradorServiceImpl;
	
	@Autowired
	private TimeServiceImpl timeServiceImpl;
	
	public void initialEssentialDataBase() {
		
		logger.info("######################################################################");
		logger.info("################## INITIALINZING ESSENTIAL DATABASE ##################");
		logger.info("######################################################################");
		
		logger.info("Creating initial datas for Papel...");

		Papel INTERNO_ADMIN_SAVED = this.criarRole("ROLE_INTERNO_ADMIN", "ADMINISTRATIVO", "PERMISSAO COM TODOS OS ACESSOS LIBERADOS PARA TODO SISTEMA", TipoAcessoEnum.INTERNO, StatusEnum.ATIVO);
		
		Papel ROLE_EMPRESA_ADMIN = this.criarRole("ROLE_EMPRESA_ADMIN", "ADMINISTRATIVO EMPRESA", "PERMISSAO COM TODOS OS ACESSOS LIBERADOS PARA EMPRESA" , TipoAcessoEnum.EMPRESA, StatusEnum.ATIVO);
		
		Papel ROLE_MANAGER = this.criarRole("ROLE_MANAGER", "ACESSO MANAGER", "PERMISSAO PARA CARGO DE LIDERANÇA" , TipoAcessoEnum.EMPRESA, StatusEnum.ATIVO);
		
		Papel ROLE_ANALYST = this.criarRole("ROLE_ANALYST", "ACESSO ANALISTA", "PERMISSAO PARA CARGO DOS ANALISTAS" , TipoAcessoEnum.EMPRESA, StatusEnum.ATIVO);
		
		logger.info("Creating initial datas for Usuario...");
		
		Usuario usuario = new Usuario(
				TipoAcessoEnum.INTERNO, 
				null, 
				null, 
				null, 
				env.getProperty("default.system.user.administrator.name"), 
				env.getProperty("default.system.user.administrator.login"), 
				env.getProperty("default.system.user.administrator.email"), 
				env.getProperty("default.system.user.administrator.password"), 
				Boolean.parseBoolean(env.getProperty("default.system.user.administrator.locked")), 
				Boolean.parseBoolean(env.getProperty("default.system.user.administrator.enabled")), 
				Collections.singletonList(INTERNO_ADMIN_SAVED), 
				null);
		
		usuario.setCriadoPor(env.getProperty("default.system.username"));
		usuario.setDtCriacao(LocalDateTime.now());
		usuarioRepository.save(usuario);
		
		usuario = new Usuario(
				TipoAcessoEnum.INTERNO, 
				null, 
				null, 
				null, 
				"User 1", 
				"user1", 
				"administrador@test.com", 
				"1234", 
				Boolean.FALSE, 
				Boolean.TRUE, 
				Collections.singletonList(INTERNO_ADMIN_SAVED), 
				null);
		
		usuario.setCriadoPor(env.getProperty("default.system.username"));
		usuario.setDtCriacao(LocalDateTime.now());
		usuarioRepository.save(usuario);
		
		logger.info("######################################################################");
		logger.info("############# FINISH INITIALINZING ESSENTIAL DATABASE ################");
		logger.info("######################################################################");
		
	}

	public void initialMockDataBase() {
		
		logger.info("######################################################################");
		logger.info("################## INITIALINZING MOCK DATABASE #######################");
		logger.info("######################################################################");
		
		logger.info("Creating mock datas for Empresa e Acessos...");
		
		List<Papel> papeisEmpresa = papelRepository.findByNome("ROLE_EMPRESA_ADMIN");
		
		this.criarEmpresa("EMPRESA TESTE 1 LTDA", "21.111.111/0001-80", "empresa1", "1234", papeisEmpresa);
		
		this.criarEmpresa("EMPRESA TESTE 2 LTDA", "20.000.000/0001-90", "empresa2", "1234", papeisEmpresa);
		
		logger.info("######################################################################");
		logger.info("############### FINISH INITIALINZING MOCK DATABASE ###################");
		logger.info("######################################################################");
	}
	
	private Papel criarRole(String nomePapel, String descricaoResumida, String descricaoDetalhada, TipoAcessoEnum tipoAcesso, StatusEnum status) {
		Papel papel =  new Papel(nomePapel, descricaoResumida, descricaoDetalhada, tipoAcesso, status);
		papel.setCriadoPor(env.getProperty("default.system.username"));
		papel.setDtCriacao(LocalDateTime.now());
		
		return papelRepository.save(papel);
	}
	
	private void criarEmpresa(String nome, String cnpj, String login, String senha,  List<Papel> papel) {
		
		logger.info("Creating mock datas for Empresa e Acessos...");
		
		Endereco endereco = new Endereco("Av. Paulista", "125", "Bela Vista", "São Paulo", "SP", "00000-555", null);
		endereco.setDtCriacao(LocalDateTime.now());
		endereco.setCriadoPor(env.getProperty("default.system.username"));
		
		Contato contato = new Contato("Teste", "(11) 87980-7879", "(11) 8798-7879", "teste@teste.com");
		contato.setDtCriacao(LocalDateTime.now());
		contato.setCriadoPor(env.getProperty("default.system.username"));
		
		Empresa empresaSave = this.criarEmpresa(nome, cnpj, endereco, contato);
		
		this.criarUsuario(nome, login, senha, papel, empresaSave);
		
		logger.info("Creating mock datas for CLiente...");
		
		this.criarCliente("CLIENTE TESTE 1", "11.555.879/0001/75", TipoClienteEnum.PJ, "ATIVO", empresaSave);
		this.criarCliente("CLIENTE TESTE 2", "22.222.666/0001/75", TipoClienteEnum.PJ, "ATIVO", empresaSave);
		
		Colaborador criarColaborador1 = this.criarColaborador("GUILHERME OLIVEIRA SILVA", "374.456.111-87", "ATIVO", empresaSave);
		Colaborador criarColaborador2 = this.criarColaborador("BRUNA SILVA", "222.333.111-55", "ATIVO", empresaSave);
		Colaborador criarColaborador3 = this.criarColaborador("ROBERTO DA SILVA LOPES", "366.663.153-55", "ATIVO", empresaSave);
		Colaborador criarColaborador4 = this.criarColaborador("LUCIANA PERES DE SOUZA", "655.466.565-66", "ATIVO", empresaSave);
		
		this.criarTime("SUPORTE OPERACOES - N1", "ATIVO", empresaSave, Arrays.asList(criarColaborador1, criarColaborador2));
		this.criarTime("SUPORTE FINANCEIRO - N2", "ATIVO", empresaSave, Arrays.asList(criarColaborador3, criarColaborador4));
		this.criarTime("INFRAESTRUTURA - N3", "ATIVO", empresaSave, Arrays.asList(criarColaborador1, criarColaborador4));
		
	}
	
	private Time criarTime(String nomeTime, String status, Empresa empresa, List<Colaborador> colaboradores) {
		
		Time time = new Time(nomeTime, status, empresa, colaboradores);
		time.setDtCriacao(LocalDateTime.now());
		time.setCriadoPor(env.getProperty("default.system.username"));
		
		return timeServiceImpl.save(time );
		
	}

	private Colaborador criarColaborador(String nomeColaborador, String cpf, String status, Empresa empresa) {
		
		Contato contato = new Contato("Teste", "(11) 00000-0000", "(11) 1111-1111", "teste@teste.com");
		contato.setDtCriacao(LocalDateTime.now());
		contato.setCriadoPor(env.getProperty("default.system.username"));
		
		Colaborador colaborador = new Colaborador(nomeColaborador, cpf, status, empresa, contato);
		colaborador.setCriadoPor(env.getProperty("default.system.username"));
		colaborador.setDtCriacao(LocalDateTime.now());
		
		return colaboradorServiceImpl.save(colaborador);
	}

	private Empresa criarEmpresa(String nome, String cnpj, Endereco endereco, Contato contato) {
		Empresa empresa1 = new Empresa(
				cnpj, 
				nome, 
				nome, 
				LocalDate.now(), 
				LocalDate.now(), 
				"ATIVO", 
				endereco, 
				contato, 
				null);
		
		empresa1.setCriadoPor(env.getProperty("default.system.username"));
		empresa1.setDtCriacao(LocalDateTime.now());
		return empresaRepository.save(empresa1);
	}
	
	private Usuario criarUsuario(String nome, String login, String senha, List<Papel> papel, Empresa empresa1Save) {
		Usuario usuario = new Usuario(
				TipoAcessoEnum.EMPRESA, 
				null, 
				null, 
				null, 
				nome, 
				login, 
				login + "@test.com", 
				senha, 
				Boolean.FALSE, 
				Boolean.TRUE, 
				papel, 
				empresa1Save);
		
		usuario.setCriadoPor(env.getProperty("default.system.username"));
		usuario.setDtCriacao(LocalDateTime.now());
		return usuarioRepository.save(usuario);
	}

	private Cliente criarCliente(String nomeCliente, String cnpjCpf, TipoClienteEnum tipoCliente, String status, Empresa empresa) {
		
		Endereco endereco = new Endereco("Rua. Benjanmin Constant", "125", "Centro", "Suzano", "SP", "00000-555", null);
		endereco.setDtCriacao(LocalDateTime.now());
		endereco.setCriadoPor(env.getProperty("default.system.username"));
		
		Contato contato = new Contato("Teste", "(11) 00000-0000", "(11) 1111-1111", "teste@teste.com");
		contato.setDtCriacao(LocalDateTime.now());
		contato.setCriadoPor(env.getProperty("default.system.username"));
		
		Cliente cliente = new Cliente(null, nomeCliente, cnpjCpf, tipoCliente, status, empresa, endereco, contato);
		cliente.setDtCriacao(LocalDateTime.now());
		cliente.setCriadoPor(env.getProperty("default.system.username"));
		
		return clienteRepository.save(cliente);
	}
}
