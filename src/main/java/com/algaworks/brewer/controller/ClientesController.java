package com.algaworks.brewer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.controller.page.PageWrapper;
import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.model.TipoPessoa;
import com.algaworks.brewer.repository.Estados;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.service.CadastroClienteService;
import com.algaworks.brewer.service.exception.CpfCnpjClienteJaCadastradoException;

@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@Autowired
	private Estados estados;
	
	@Autowired
	private CadastroClienteService cadastroClienteService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/CadastroCliente");
		mv.addObject("estados", estados.findAll());
		mv.addObject("tiposPessoa", TipoPessoa.values());
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView cadastrar(@Valid Cliente cliente, BindingResult result, 
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return novo(cliente);
		}
		try {
			cadastroClienteService.salvar(cliente);
		} catch (CpfCnpjClienteJaCadastradoException e) {
			result.rejectValue("cpfOuCnpj", e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		redirectAttributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!!!");
		return new ModelAndView("redirect:/clientes/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter filtro, BindingResult result, 
			@PageableDefault(size = 5 ) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cliente/PesquisaClientes");
		
		
//		mv.addObject("sabores", Sabor.values());
//		mv.addObject("origens", Origem.values());
//		mv.addObject("estilo", estilos.findAll());
		PageWrapper<Cliente> pagina = new PageWrapper<>(cadastroClienteService.filtrar(filtro, pageable), httpServletRequest);
		mv.addObject("pagina", pagina);
		return mv;
	}

}
