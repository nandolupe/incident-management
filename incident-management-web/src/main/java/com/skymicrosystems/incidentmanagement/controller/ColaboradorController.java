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

import com.skymicrosystems.incidentmanagement.forms.ColaboradorSearchForm;
import com.skymicrosystems.incidentmanagement.model.Colaborador;
import com.skymicrosystems.incidentmanagement.services.ColaboradorServiceImpl;
import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class ColaboradorController {
	
	private static final String FORWARD_FORM = "manutencao-colaborador/form";
	private static final String FORWARD_LIST = "manutencao-colaborador/list";
	private static final String BASE_DOMAIN = "/colaborador";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private ColaboradorServiceImpl colaboradorServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new ColaboradorSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, ColaboradorSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new ColaboradorSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, ColaboradorSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		colaboradorServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Colaborador colaborador) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Colaborador colaborador, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(colaborador, model);
            return FORWARD_FORM;
        }
        
        Colaborador colaboradorSave = null;
        		
        try {
        
        	colaboradorSave = colaboradorServiceImpl.save(colaborador);
	        redirAttrs.addFlashAttribute("success", "manutencao.colaborador.label.save.success");
	        this.initForm(colaboradorSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(colaborador, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		
		this.initForm(new Colaborador(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") Long id, Model model) {
		
		Colaborador colaborador = colaboradorServiceImpl.findById(id).get();
		colaborador.setIdColaborador(null);
		
		this.initForm(colaborador, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid Colaborador colaborador, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	colaborador.setIdColaborador(id);
        	this.initForm(colaborador, model);
            return FORWARD_FORM;
        }
        
        Colaborador colaboradorSave = null;
        
        try {
            
        	colaboradorSave = colaboradorServiceImpl.update(colaborador);
	        redirAttrs.addFlashAttribute("success", "manutencao.colaborador.label.update.success");
	        this.initForm(colaboradorSave, model);
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(colaborador, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			colaboradorServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.colaborador.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Colaborador colaborador, Model model) {
		
		if (colaborador == null) {
			colaborador = new Colaborador();
			
		} else if (colaborador.getIdColaborador() != null) {
			colaborador = colaboradorServiceImpl.findById(colaborador.getIdColaborador()).get();
		}
		
		model.addAttribute("colaborador", colaborador);
	}
}
