package com.algaworks.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {
	
	public String salvarTemporariamente(MultipartFile[] files);
	public String salvarTemporariamente(String data);
	public byte[] recuperarFotoTemporaria(String nome);
	
}
