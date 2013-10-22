package br.com.caiosousa.tenant.service.startup;

import org.springframework.boot.web.SpringBootServletInitializer;

public class ServletLauncher extends SpringBootServletInitializer {

	@Override
	protected Class<?>[] getConfigClasses() {
		return new Class<?>[] {Launcher.class};
	}

}
