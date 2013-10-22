package br.com.caiosousa.tenant.service.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class ListaTenantJSON extends ResourceSupport {

	private List<TenantJSON> lista;

	public void adicionaTenantJSON(TenantJSON tenantJSON) {
		this.getLista().add(tenantJSON);
	}

	public List<TenantJSON> getLista() {
		if (lista == null) {
			lista = new ArrayList<>();
		}
		return lista;
	}

}
