package com.skymicrosystems.incidentmanagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.skymicrosystems.incidentmanagement.forms.ProjetoSearchForm;
import com.skymicrosystems.incidentmanagement.model.Projeto;
import com.skymicrosystems.incidentmanagement.services.ClienteServiceImpl;
import com.skymicrosystems.incidentmanagement.services.ColaboradorServiceImpl;
import com.skymicrosystems.incidentmanagement.services.ProjetoServiceImpl;
import com.skymicrosystems.incidentmanagement.services.TimeServiceImpl;
import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class ProjetoController {
	
	private static final String FORWARD_FORM = "manutencao-projeto/form";
	private static final String FORWARD_LIST = "manutencao-projeto/list";
	private static final String BASE_DOMAIN = "/projeto";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private ProjetoServiceImpl projetoServiceImpl;
	
	@Autowired
	private TimeServiceImpl timeServiceImpl;
	
	@Autowired
	private ColaboradorServiceImpl colaboradorServiceImpl;
	
	@Autowired
	private ClienteServiceImpl clienteServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new ProjetoSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, ProjetoSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new ProjetoSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, ProjetoSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		projetoServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Projeto projeto) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Projeto projeto, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(projeto, model);
            return FORWARD_FORM;
        }
        
        Projeto projetoSave = null;
        		
        try {
        
        	projetoSave = projetoServiceImpl.save(projeto);
	        redirAttrs.addFlashAttribute("success", "manutencao.projeto.label.save.success");
	        this.initForm(projetoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(projeto, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		
		this.initForm(new Projeto(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") Long id, Model model) {
		
		Projeto projeto = projetoServiceImpl.findById(id).get();
		projeto.setIdProjeto(null);
		
		this.initForm(projeto, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid Projeto projeto, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	projeto.setIdProjeto(id);
        	this.initForm(projeto, model);
            return FORWARD_FORM;
        }
        
        Projeto projetoSave = null;
        
        try {
            
        	projetoSave = projetoServiceImpl.update(projeto);
	        redirAttrs.addFlashAttribute("success", "manutencao.projeto.label.update.success");
	        this.initForm(projetoSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(projeto, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			projetoServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.projeto.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Projeto projeto, Model model) {
		
		if (projeto == null) {
			projeto = new Projeto();
			
		} else if (projeto.getIdProjeto() != null) {
			projeto = projetoServiceImpl.findById(projeto.getIdProjeto()).get();
		}
		
		model.addAttribute("projeto", projeto);
		model.addAttribute("availableResponsaveis", colaboradorServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
		model.addAttribute("availableTimes", timeServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
		model.addAttribute("availableClientes", clienteServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
		
	}
}
