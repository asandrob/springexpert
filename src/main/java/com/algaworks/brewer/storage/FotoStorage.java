package com.algaworks.brewer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {
	
	public String salvarTemporariamente(MultipartFile[] files);
	public String salvarTemporariamente(String data);
	public byte[] recuperarFotoTemporaria(String nomeFoto);
	public void salvar(String nomeFoto);
	public byte[] recuperarFoto(String nomeFoto);
	public byte[] recuperarThumbnail(String foto);
	public void remover(String foto);
	
}
