package com.algaworks.brewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.FotoDTO;
import com.algaworks.brewer.storage.FotoStorage;
import com.algaworks.brewer.storage.FotoStorageRunnable;

@RestController
@RequestMapping("/fotos")
public class FotosController {

	@Autowired
	private FotoStorage fotoStorage;
	
	@PostMapping
	public DeferredResult<FotoDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<FotoDTO> resultado = new DeferredResult<>();
		Thread tarefa = new Thread(new FotoStorageRunnable(files, resultado, fotoStorage)); 
		tarefa.start();
		return resultado;
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public DeferredResult<FotoDTO> upload(@RequestBody FotoDTO fotoDTO) {
		DeferredResult<FotoDTO> resultado = new DeferredResult<>();
		//TODO criar uma validação para FotoDTO, pelo menos evitar um null pointer ao tentar extrair o string base64 da foto.
		String imageString = fotoDTO.getDataUrl().split(",")[1];
		Thread tarefa = new Thread(new FotoStorageRunnable(imageString, resultado, fotoStorage)); 
		tarefa.start();
		return resultado;
	}
	
	@GetMapping("/temp/{nome:.*}")/*o ':.*' é uma REGEX para pegar a extensão do arquivo, ex: .png*/
	public ResponseEntity<byte[]> recuperarFotoTemporaria(@PathVariable String nome) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
		return ResponseEntity.ok()
				.headers(headers)
				.body(fotoStorage.recuperarFotoTemporaria(nome));
	}

}
