package com.algaworks.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstilosController {

	@GetMapping("/estilos/novo")
	public String novo() {
		return "estilo/CadastroEstilo";
	}
}
