package br.com.caiosousa.tenant.service;

import java.lang.reflect.Field;
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
import tenant.model.Tenant;

@Component
public class TenantServico {

	@Autowired
	MongoOperations mongo;
	
	public void cria(Tenant tenant) throws CamposInvalidosException, OperacaoNaoPermitidaException {
		verificaSeTemPermissao();
		validaCriacao(tenant);
		mongo.save(tenant);
	}
	
	public Tenant atualiza(Tenant tenant)
			throws CamposInvalidosException, OperacaoNaoPermitidaException, RegistroNaoEncontradoException {
		
		verificaSeTemPermissao();
		final Pessoa pessoaJaExistente = validaAtualizacao(pessoa);
		final Pessoa pessoaAtualizada = preencheCamposParaAtualizar(pessoaJaExistente, pessoa);
		mongo.save(pessoaAtualizada);
		return buscaPorEmail(pessoaAtualizada.getEmail());
		
	}
	
	public List<Pessoa> buscaTodos() throws RegistroNaoEncontradoException {
		
		List<Pessoa> pessoas = mongo.findAll(Pessoa.class);
		
		if (pessoas == null || pessoas.isEmpty()) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return pessoas;
		
	}
	
	public Pessoa buscaPorEmail(String email) throws RegistroNaoEncontradoException {
		
		Query pessoaQuery = new Query(Criteria.where("email").is(email));
		Pessoa pessoa = mongo.findOne(pessoaQuery, Pessoa.class);
		
		if (pessoa == null) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return pessoa;
		
	}
	
	public Pessoa validaLogin(String email, String senha) throws RegistroNaoEncontradoException, OperacaoNaoPermitidaException {
		
		verificaSeTemPermissao();
		Query pessoaQuery = new Query(Criteria.where("email").is(email).and("senha").is(senha));
		Pessoa pessoa = mongo.findOne(pessoaQuery, Pessoa.class);
		
		if (pessoa == null) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return pessoa;
		
	}
	
	public void exclui(String email) throws RegistroNaoEncontradoException, OperacaoNaoPermitidaException{
		
		verificaSeTemPermissao();
		Pessoa pessoa = buscaPorEmail(email);
		validaExclusao(pessoa);
		// Chama o serviço de controle de tenants e desativa a pessoa no tenant corrente.
		
	}

	public void validaCamposObrigatoriosParaCriacao(Tenant tenant) throws CamposInvalidosException {
		
		final CamposInvalidosException camposInvalidos = 
				new CamposInvalidosException();
		
		if (tenant.getEmail() == null || pessoa.getEmail().equals("")) {
			camposInvalidos.addCampoInvalido(Mensagens.CAMPO_EMAIL_OBRIGATORIO);
		}
		
		if (pessoa.getTenants() == null || pessoa.getTenants().isEmpty()) {
			camposInvalidos.addCampoInvalido(Mensagens.PELO_MENOS_UM_TENANT_OBRIGATORIO);
		}
		
		if (camposInvalidos.getCamposInvalidos().size() > 0) {
			throw camposInvalidos;
		}
		
	}

	public boolean pessoaJaExiste(Pessoa novaPessoa) {
		try {
			buscaPorEmail(novaPessoa.getEmail());
			return Boolean.TRUE;
		} catch (RegistroNaoEncontradoException e) {
			return Boolean.FALSE;
		}
		
	}
	
	private void validaCriacao(Tenant tenant) throws CamposInvalidosException, OperacaoNaoPermitidaException {
		validaCamposObrigatoriosParaCriacao(tenant);
		if (pessoaJaExiste(pessoa)) {
			throw OperacaoNaoPermitidaException.PESSOA_JA_EXISTE;
		}
	}
	
	private Pessoa validaAtualizacao(Pessoa pessoa) throws CamposInvalidosException, RegistroNaoEncontradoException {
		
		final CamposInvalidosException camposInvalidos = 
				new CamposInvalidosException();
		
		if (pessoa.getEmail() == null || pessoa.getEmail().equals("")) {
			camposInvalidos.addCampoInvalido(Mensagens.CAMPO_EMAIL_OBRIGATORIO);
		}
		
		return buscaPorEmail(pessoa.getEmail());
		
	}
	
	private void validaExclusao(Pessoa pessoa) throws OperacaoNaoPermitidaException {

		/*
		 * Valida se pode ser desativada do tenant em questão.
		 */

	}
	
	private Pessoa preencheCamposParaAtualizar(Pessoa pessoaJaExistente, Pessoa novosCampos) {
		
		/*
		 * E-mail não pode ser alterado, pois é a chave primária
		 */
		for (Field campo : Pessoa.class.getDeclaredFields()) {
			
			try {

				if (!campo.getName().equalsIgnoreCase("email")) {
					campo.setAccessible(Boolean.TRUE);
					campo.set(pessoaJaExistente, campo.get(novosCampos));
				}
				
			} catch (Exception e) {
			}
			
		}
		
		return pessoaJaExistente;
		
	}
	
	private void verificaSeTemPermissao() throws OperacaoNaoPermitidaException {
		
	}
	
}
