package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.filter.EstiloFilter;
import com.algaworks.brewer.service.CadastroEstiloService;
import com.algaworks.brewer.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping("/estilos")
public class EstilosController {

	@Autowired
	private CadastroEstiloService cadastroEstiloService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Estilo estilo) {
		ModelAndView mv = new ModelAndView("estilo/CadastroEstilo");
		mv.addObject(estilo);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult errors, 
			RedirectAttributes redirectAttributes) {
		if (errors.hasErrors()) {
			return novo(estilo);
		}
		try {
			cadastroEstiloService.salvar(estilo);
		} catch (NomeEstiloJaCadastradoException e) {
			errors.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}
		/* no redirec o Model não funciona, para isto usamos o RedirectAttributes */
		redirectAttributes.addFlashAttribute("mensagem", "Estilo salvo com sucesso!!!");
		/* no redirect colocamos o url e não o nome da view */
		return new ModelAndView("redirect:/estilos/novo");
	}
	
	@PostMapping()
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody @Valid Estilo estilo, BindingResult errors) {
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(errors.getFieldError("nome").getDefaultMessage());
		}
		estilo = cadastroEstiloService.salvar(estilo);
		return ResponseEntity.ok(estilo);
	}
	
	@GetMapping
	public ModelAndView pesquisar(EstiloFilter filtro, BindingResult result, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("estilo/PesquisaEstilos");
		PageWrapper<Estilo> pagina = new PageWrapper<>(cadastroEstiloService.filtrar(filtro, pageable), httpServletRequest);
		mv.addObject("pagina", pagina);
		return mv;
	}


}
