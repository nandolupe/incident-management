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

import com.skymicrosystems.incidentmanagement.forms.TimeSearchForm;
import com.skymicrosystems.incidentmanagement.model.Time;
import com.skymicrosystems.incidentmanagement.services.ColaboradorServiceImpl;
import com.skymicrosystems.incidentmanagement.services.TimeServiceImpl;
import com.skymicrosystems.incidentmanagement.utils.BuildManagementUtils;

/**
 * @author luiz_pereira
 *
 */
@Controller
public class TimeController {
	
	private static final String FORWARD_FORM = "manutencao-time/form";
	private static final String FORWARD_LIST = "manutencao-time/list";
	private static final String BASE_DOMAIN = "/time";
	private static final String FORWARD_REDIRECT_LIST = "redirect:" + BASE_DOMAIN + "/list";

	@Autowired
	private TimeServiceImpl timeServiceImpl;
	
	@Autowired
	private ColaboradorServiceImpl colaboradorServiceImpl;
	
	@Autowired
	private BuildManagementUtils buildManagementUtils;
	
	@GetMapping(BASE_DOMAIN + "/list")
    public String showList(Model model, RedirectAttributes redirAttrs) {

		this.search(model, new TimeSearchForm(null, 
				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
				redirAttrs);
		
        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/search")
    public String search(Model model, TimeSearchForm searchForm, RedirectAttributes redirAttrs) {
		
        this.paginate(model, 
        		new TimeSearchForm(searchForm.getData(), 
        				buildManagementUtils.getPropertyInteger("default.system.initial-page"), 
        				buildManagementUtils.getPropertyInteger("default.system.size-per-page")),
        		redirAttrs);

        return FORWARD_LIST;
    }
	
	@PostMapping(BASE_DOMAIN + "/paginate")
    public String paginate(Model model, TimeSearchForm searchForm, RedirectAttributes redirAttrs) {
        
		try { 
		
	        model.addAttribute("principalObject", searchForm);
	        model.addAttribute("principalList", 
	        		timeServiceImpl.searchPaginate(
	        				searchForm.getData(), 
	        				PageRequest.of(searchForm.getPageNumber() - 1, searchForm.getSize())));
        
		 } catch (Exception e) {
	        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
		 }
        
        return FORWARD_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/show-form")
    public String showForm(Model model, Time time) {
		
		this.initForm(null, model);
		
		return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/add")
    public String add(@Valid Time time, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	this.initForm(time, model);
            return FORWARD_FORM;
        }
        
        try {
        
        	timeServiceImpl.save(time);
	        redirAttrs.addFlashAttribute("success", "manutencao.time.label.save.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(time, model);
        	return FORWARD_FORM;
        }
        
        return FORWARD_REDIRECT_LIST; 
    }
	
	@GetMapping(BASE_DOMAIN + "/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		
		this.initForm(new Time(id), model);
        
        return FORWARD_FORM;
    }
	
	@GetMapping(BASE_DOMAIN + "/copy/{id}")
    public String showCopyForm(@PathVariable("id") Long id, Model model) {
		
		Time time = timeServiceImpl.findById(id).get();
		time.setIdTime(null);
		
		this.initForm(time, model);
        
        return FORWARD_FORM;
    }
	
	@PostMapping(BASE_DOMAIN + "/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid Time time, BindingResult result, Model model, RedirectAttributes redirAttrs) {
        if (result.hasErrors()) {
        	time.setIdTime(id);
        	this.initForm(time, model);
            return FORWARD_FORM;
        }
        
        try {
            
        	timeServiceImpl.update(time);
	        redirAttrs.addFlashAttribute("success", "manutencao.time.label.update.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        	this.initForm(time, model);
        	return FORWARD_FORM;
        }
		
        return FORWARD_REDIRECT_LIST;
    }
	
	@GetMapping(BASE_DOMAIN + "/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model, RedirectAttributes redirAttrs) {
		
		try {
            
			timeServiceImpl.delete(id);
			redirAttrs.addFlashAttribute("success", "manutencao.time.label.exclude.success");
	        
        } catch (Exception e) {
        	redirAttrs.addFlashAttribute("error", e.getLocalizedMessage());
        }
		
        return FORWARD_REDIRECT_LIST;
    }

	private void initForm(Time time, Model model) {
		
		if (time == null) {
			time = new Time();
			
		} else if (time.getIdTime() != null) {
			time = timeServiceImpl.findById(time.getIdTime()).get();
		}
		
		model.addAttribute("time", time);
		
		model.addAttribute("colaboradores", colaboradorServiceImpl.searchPaginate(null, PageRequest.of(0, 10)));
	}
}
