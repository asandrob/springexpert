package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Cerveja;

@Controller
public class CervejasController {

	@GetMapping("/cervejas/novo")
	public String novo(Cerveja cerveja, Model model) {
		model.addAttribute("cerveja", cerveja);
		return "cerveja/CadastroCerveja";
	}

	@PostMapping("/cervejas/novo")
	public String cadastrar(@Valid Cerveja cerveja, BindingResult errors, 
			Model model, RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return novo(cerveja, model);
		}
		/* no redirec o Model não funciona, para isto usamos o RedirectAttributes */
		redirectAttributes.addFlashAttribute("mensagem", "Cerveja salva com sucesso!!!");
		/* no redirect colocamos o url e não o nome da view */
		return "redirect:/cervejas/novo";
	}
}
