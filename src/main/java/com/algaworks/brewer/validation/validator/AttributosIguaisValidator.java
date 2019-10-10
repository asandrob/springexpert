package com.algaworks.brewer.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.apache.commons.beanutils.BeanUtils;

import com.algaworks.brewer.validation.AtributosIguais;

public class AttributosIguaisValidator implements ConstraintValidator<AtributosIguais, Object>{

	private String atributo;
	private String confirmacao;
	
	
	@Override
	public void initialize(AtributosIguais constraintAnnotation) {
		this.atributo = constraintAnnotation.atributo();
		this.confirmacao = constraintAnnotation.confirmacao();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean valido = false;
		try {
			Object atributo = BeanUtils.getProperty(value, this.atributo);
			Object confirmacao = BeanUtils.getProperty(value, this.confirmacao);
			valido = saoNull(atributo, confirmacao) || saoIguais(atributo, confirmacao);
		} catch (Exception e) {
			throw new RuntimeException("Erro recuperando valores dos atributos", e);
		} 
		
		if (!valido) {
			context.disableDefaultConstraintViolation();
			String mensagem = context.getDefaultConstraintMessageTemplate();
			ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(mensagem);
			violationBuilder.addPropertyNode(confirmacao).addConstraintViolation();
		}
		return valido;
	}

	private boolean saoIguais(Object atributo, Object confirmacao) {
		return atributo != null && atributo.equals(confirmacao);
	}

	private boolean saoNull(Object atributo, Object confirmacao) {
		return atributo == null && confirmacao == null;
	}

}
