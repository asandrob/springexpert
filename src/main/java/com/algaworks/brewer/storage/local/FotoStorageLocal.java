package com.algaworks.brewer.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.storage.FotoStorage;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

public class FotoStorageLocal implements FotoStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);

	private static final String THUMBNAIL_PREFIX = "thumbnail.";
	
	private Path local;

	private Path localTemporario;

	public FotoStorageLocal() {
		this.local = getDefault().getPath(System.getProperty("user.home"), "brewerfotos");
		this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
		criarPastas();
	}

	@Override
	public String salvarTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		if (files != null && files.length > 0) {
			MultipartFile arquivo = files[0];
			novoNome = renomearArquivo(arquivo.getOriginalFilename());
			try {
				arquivo.transferTo(arquivoTemporario(novoNome));
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException("Erro ao tentar criar salvar a foto na pasta temporária.", e);
			}
		}
		return novoNome;
	}

	@Override
	public String salvarTemporariamente(String data) {
		byte[] decodedBytes = Base64.getDecoder().decode(data);
		String novoNome = renomearArquivo("paste_img.png");
		try {
			FileUtils.writeByteArrayToFile(arquivoTemporario(novoNome), decodedBytes);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar criar salvar a foto na pasta temporária.", e);
		}
		return novoNome;
	}

	@Override
	public byte[] recuperarFotoTemporaria(String nomeFoto) {
		try {
			return Files.readAllBytes(this.localTemporario.resolve(nomeFoto));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto temporária", e);
		}
	}
	
	@Override
	public void salvar(String nomeFoto) {
		try {
			Files.move(this.localTemporario.resolve(nomeFoto), this.local.resolve(nomeFoto));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar transferir foto temporária para local definitivo", e);
		}
		try {
			Thumbnails.of(this.local.resolve(nomeFoto).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao gerar thumbnail", e);
		};
	}
	
	@Override
	public byte[] recuperarFoto(String nomeFoto) {
		try {
			return Files.readAllBytes(this.local.resolve(nomeFoto));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto", e);
		}
	}
	
	@Override
	public byte[] recuperarThumbnail(String fotoCerveja) {
		return recuperarFoto(THUMBNAIL_PREFIX + fotoCerveja);
	}

	@Override
	public void remover(String foto) {
		try {
			Files.deleteIfExists(this.local.resolve(foto));
			Files.deleteIfExists(this.local.resolve(THUMBNAIL_PREFIX + foto));
		} catch (IOException e) {
			LOGGER.warn(String.format("Erro apagando foto '%s'. Mensagem: %s", foto, e.getMessage()));
		}
	}

	private void criarPastas() {
		try {
			Files.createDirectories(this.local);
			Files.createDirectories(this.localTemporario);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pastas para salvar fotos foram criadas");
				LOGGER.debug("Pastas padrão: " + this.local.toAbsolutePath());
				LOGGER.debug("Pastas temporária: " + this.localTemporario.toString());
			}
		} catch (IOException e) {
			throw new RuntimeException("Erro ao tentar criar as pastas para salvar as fotos.", e);
		}
	}

	private File arquivoTemporario(String nome) {
		return new File(this.localTemporario.toAbsolutePath().toString() + getDefault().getSeparator() + nome);
	}

	private String renomearArquivo(String nomeOriginal) {
		String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Nome original: %s, novo nome: %s", nomeOriginal, novoNome));
		}
		return novoNome;
	}

}
