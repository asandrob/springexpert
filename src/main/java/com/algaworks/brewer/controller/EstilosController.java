package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.service.CadastroEstiloService;

@Controller
public class EstilosController {

	@Autowired
	private CadastroEstiloService cadastroEstiloService;
	
	@GetMapping("/estilos/novo")
	public ModelAndView novo(Estilo estilo) {
		ModelAndView mv = new ModelAndView("estilo/CadastroEstilo");
		mv.addObject(estilo);
		return mv;
	}
	
	@PostMapping("/estilos/novo")
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult errors, 
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return novo(estilo);
		}
		cadastroEstiloService.salvar(estilo);
		/* no redirec o Model não funciona, para isto usamos o RedirectAttributes */
		redirectAttributes.addFlashAttribute("mensagem", "Estilo salvo com sucesso!!!");
		/* no redirect colocamos o url e não o nome da view */
		return new ModelAndView("redirect:/estilos/novo");
	}

}
