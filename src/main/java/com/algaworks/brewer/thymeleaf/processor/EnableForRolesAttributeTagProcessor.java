package com.algaworks.brewer.thymeleaf.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class EnableForRolesAttributeTagProcessor extends AbstractAttributeTagProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnableForRolesAttributeTagProcessor.class);

	private static final String NOME_ATRIBUTO = "enableforroles";
	private static final int PRECEDENCIA = 1000;
	
	public EnableForRolesAttributeTagProcessor(String dialectPrefix) {
		super(TemplateMode.HTML, dialectPrefix, null, false, NOME_ATRIBUTO, true, PRECEDENCIA, true);
	}
	
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			AttributeName attributeName, String attributeValue, IElementTagStructureHandler structureHandler) {
		boolean temPermissao = false;
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		ArrayList<String> permissoesDoUsuario = authentication.
				getAuthorities().stream().map(a -> a.toString()).collect(Collectors.toCollection(ArrayList::new));
		ArrayList<String> permissoesRequeridas = new ArrayList<>();
		try {
			permissoesRequeridas = 
					Arrays.asList(attributeValue.split(",")).stream().map(a -> a.toString()).collect(Collectors.toCollection(ArrayList::new));;
		} catch (NullPointerException e) {
			LOGGER.error("Não foi possível ler as permissões do atributo enableforroles");
		}
		for (String permissaoDoUsuario : permissoesDoUsuario) {
			long achouPermissao = permissoesRequeridas.stream().
					filter(permissaoRequerida -> permissaoRequerida.equalsIgnoreCase(permissaoDoUsuario)).count();
			if (achouPermissao != 0L) {
				temPermissao = true;
				break;
			}
		}
		if (!temPermissao) {
			/*<button> insere um atributo disabled*/
			structureHandler.setAttribute("disabled", "");
			/*<a> remove o href ou th:ref*/
			structureHandler.removeAttribute("th:href");
			structureHandler.removeAttribute("href");
			/**/
			String stylesExistentes = tag.getAttributeValue("style");
			String classesExistentes = tag.getAttributeValue("class");
			structureHandler.setAttribute("style",
					stylesExistentes == null ? "cursor: not-allowed;pointer-events:auto;" :
					stylesExistentes + ";cursor: not-allowed;pointer-events:auto;");
			structureHandler.setAttribute("class", classesExistentes + " disabled");
		}
	}

}