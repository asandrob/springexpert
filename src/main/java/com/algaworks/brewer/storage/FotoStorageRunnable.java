package com.algaworks.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.FotoDTO;

public class FotoStorageRunnable implements Runnable {

	private MultipartFile[] files;
	private DeferredResult<FotoDTO> resultado;
	private FotoStorage fotoStorage;
	private String data;
	
	public FotoStorageRunnable(MultipartFile[] files, DeferredResult<FotoDTO> resultado, FotoStorage fotoStorage) {
		this.files = files;
		this.resultado = resultado;
		this.fotoStorage = fotoStorage;
	}
	public FotoStorageRunnable(String data, DeferredResult<FotoDTO> resultado, FotoStorage fotoStorage) {
		this.resultado = resultado;
		this.fotoStorage = fotoStorage;
		this.data = data;
	}

	@Override
	public void run() {
		String nomeFoto;
		String contentType;
		if (data != null) {
			nomeFoto = fotoStorage.salvarTemporariamente(this.data);
			contentType = "image/png";
		} else {
			nomeFoto = fotoStorage.salvarTemporariamente(this.files);
			contentType = files[0].getContentType();
		}
		resultado.setResult(new FotoDTO(nomeFoto, contentType));
	}

}
