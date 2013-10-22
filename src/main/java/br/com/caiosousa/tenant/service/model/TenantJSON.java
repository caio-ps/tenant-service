package br.com.caiosousa.tenant.service.model;

import org.springframework.hateoas.ResourceSupport;

import br.com.caiosousa.tenant.model.Tenant;

public class TenantJSON extends ResourceSupport {

	private Tenant content;

	public TenantJSON(Tenant content) {
		this.content = content;
	}

	public Tenant getContent() {
		return content;
	}

}
