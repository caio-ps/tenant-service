package br.com.caiosousa.tenant.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import pessoa.service.exception.CamposInvalidosException;
import pessoa.service.exception.Mensagens;
import pessoa.service.exception.OperacaoNaoPermitidaException;
import pessoa.service.exception.RegistroNaoEncontradoException;
import tenant.enumeration.StatusTenant;
import tenant.enumeration.TipoTenant;
import tenant.model.ContadorTenants;
import tenant.model.Tenant;

@Component
public class TenantServico {

	@Autowired
	MongoOperations mongo;
	
	public void cria(Tenant tenant) throws CamposInvalidosException, OperacaoNaoPermitidaException {
		verificaSeTemPermissao();
		validaCamposObrigatoriosParaCriacao(tenant);
		tenant = adicionaValoresPadrao(tenant);
		mongo.save(tenant);
		// chama serviço de pessoa e cria os administradores do tenant
	}
	
	public Tenant atualiza(Tenant tenant)
			throws CamposInvalidosException, OperacaoNaoPermitidaException, RegistroNaoEncontradoException {
		
		// verificaSeTemPermissao();
		// final Pessoa pessoaJaExistente = validaAtualizacao(pessoa);
		// final Pessoa pessoaAtualizada = preencheCamposParaAtualizar(pessoaJaExistente, pessoa);
		// mongo.save(pessoaAtualizada);
		// return buscaPorEmail(pessoaAtualizada.getEmail());
		return null;
		
	}
	
	public List<Tenant> buscaTodos() throws RegistroNaoEncontradoException, OperacaoNaoPermitidaException {
		
		verificaSeTemPermissao();
		List<Tenant> tenants = mongo.findAll(Tenant.class);
		
		if (tenants == null || tenants.isEmpty()) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return tenants;
		
	}
	
	public Tenant buscaPorCodigo(Long codigoTenant) throws RegistroNaoEncontradoException, OperacaoNaoPermitidaException {
		
		verificaSeTemPermissao();
		Query tenantQuery = new Query(Criteria.where("codigoTenant").is(codigoTenant));
		Tenant tenant = mongo.findOne(tenantQuery, Tenant.class);
		
		if (tenant == null) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return tenant;
		
	}
	
	public void exclui(Long codigoTenant) throws RegistroNaoEncontradoException, OperacaoNaoPermitidaException {
		
		verificaSeTemPermissao();
		Tenant tenant = buscaPorCodigo(codigoTenant);
		tenant.setStatus(StatusTenant.INATIVO);
		mongo.save(tenant);
		// Chama o serviço de pessoa e exclui todas as pessoas do tenant
		
	}

	public void validaCamposObrigatoriosParaCriacao(Tenant tenant) throws CamposInvalidosException {
		
		final CamposInvalidosException camposInvalidos = 
				new CamposInvalidosException();
		
		if (tenant.getDescricao() == null || tenant.getDescricao().equals("")) {
			camposInvalidos.addCampoInvalido(Mensagens.CAMPO_DESCRICAO_OBRIGATORIO);
		}
		
		if (tenant.getAdministradores() == null || tenant.getAdministradores().isEmpty()) {
			camposInvalidos.addCampoInvalido(Mensagens.PELO_MENOS_UM_TENANT_OBRIGATORIO);
		}
		
		if (camposInvalidos.getCamposInvalidos().size() > 0) {
			throw camposInvalidos;
		}
		
	}

	private Tenant adicionaValoresPadrao(Tenant tenant) {
		final Long codigoDoProximoTenant = buscaCodigoProximoTenant();

		tenant.setCodigoTenant(codigoDoProximoTenant);
		tenant.setTipo(TipoTenant.FREE);
		tenant.setStatus(StatusTenant.ATIVO);
		tenant.setNumeroDePessoas(Long.valueOf(tenant.getAdministradores().size()));
		
		return tenant;
	}

	private Long buscaCodigoProximoTenant() {

		ContadorTenants contadorTenantsAtual = null;
		List<ContadorTenants> contadorTenants = mongo.findAll(ContadorTenants.class);
		
		if (contadorTenants == null) {
			contadorTenantsAtual = new ContadorTenants();
			contadorTenantsAtual.setCodigoUltimoTenant(1L);
		} else {
			contadorTenantsAtual = contadorTenants.get(0);
			contadorTenantsAtual.setCodigoUltimoTenant(contadorTenantsAtual.getCodigoUltimoTenant() + 1);
		}

		mongo.save(contadorTenantsAtual);
		return contadorTenantsAtual.getCodigoUltimoTenant();

	}

	private void verificaSeTemPermissao() throws OperacaoNaoPermitidaException {
		
	}
	
}
