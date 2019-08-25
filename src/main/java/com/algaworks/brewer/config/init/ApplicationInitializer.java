package com.algaworks.brewer.config.init;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.algaworks.brewer.config.WebConfig;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		/* passamos um array com as classes que configuram o servlet */
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		/* é equivalente ao urlMapping no web.xml */
		return new String[] { "/" };
	}

}
