package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Origem;
import com.algaworks.brewer.model.Sabor;
import com.algaworks.brewer.repository.Cervejas;
import com.algaworks.brewer.repository.Estilos;
import com.algaworks.brewer.service.CadastroCervejaService;

@Controller
public class CervejasController {
	
	//private static final Logger logger = LoggerFactory.getLogger(CervejasController.class);
	
	@Autowired
	private Estilos estilos;
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private CadastroCervejaService cadastroCervejaService; 
	
	@GetMapping("/cervejas/novo")
	public ModelAndView novo(Cerveja cerveja) {
		ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
		mv.addObject("cerveja", cervejas.findOne(1L));
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());
		mv.addObject("estilo", estilos.findAll());
		return mv;
	}

	@PostMapping("/cervejas/novo")
	public ModelAndView cadastrar(@Valid Cerveja cerveja, BindingResult errors, 
			Model model, RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return novo(cerveja);
		}
		cadastroCervejaService.salvar(cerveja);
		/* no redirec o Model não funciona, para isto usamos o RedirectAttributes */
		redirectAttributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!!!");
		/* no redirect colocamos o url e não o nome da view */
		return new ModelAndView("redirect:/cervejas/novo");
	}
	
}
