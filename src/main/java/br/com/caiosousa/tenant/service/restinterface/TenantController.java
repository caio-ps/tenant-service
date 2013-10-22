package br.com.caiosousa.tenant.service.restinterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.caiosousa.exception.CamposInvalidosException;
import br.com.caiosousa.exception.OperacaoNaoPermitidaException;
import br.com.caiosousa.tenant.model.Tenant;
import br.com.caiosousa.tenant.service.TenantServico;
import br.com.caiosousa.tenant.service.model.ListaTenantJSON;
import br.com.caiosousa.tenant.service.model.TenantJSON;

@Controller
public class TenantController extends AbstractController<ListaTenantJSON, TenantJSON> {

	@Autowired
	TenantServico tenantServico;

//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET, value="/pessoa")
//    @ResponseStatus(value = HttpStatus.OK)
//    public HttpEntity<ListaPessoaJSON> getPessoas() throws OperacaoNaoPermitidaException, RegistroNaoEncontradoException {
//
//    	final ListaPessoaJSON pessoasJSON = converteListaParaJson(pessoaServico.buscaTodos());
//    	return new ResponseEntity<ListaPessoaJSON>(adicionaListaLinksPermitidos(pessoasJSON), HttpStatus.OK);
//        
//    }
//    
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET, value="/pessoa/email")
//    @ResponseStatus(value = HttpStatus.OK)
//    public HttpEntity<TenantJSON> getPessoaPorEmail(
//    		@RequestParam(value = "email", required = true) String email)
//    				throws OperacaoNaoPermitidaException, RegistroNaoEncontradoException {
//
//		final TenantJSON pessoaJSON = new TenantJSON(pessoaServico.buscaPorEmail(email));
//		return new ResponseEntity<TenantJSON>(adicionaLinksPermitidos(pessoaJSON), HttpStatus.OK);
//			
//    }
//    
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET, value="/pessoa")
//    @ResponseStatus(value = HttpStatus.OK)
//	public Pessoa validaLogin(Pessoa pessoa) throws OperacaoNaoPermitidaException, RegistroNaoEncontradoException {
//
//		final Pessoa pessoaLogada = pessoaServico.validaLogin(pessoa.getEmail(), pessoa.getSenha());
//		return pessoaLogada;
//        
//    }
//    
    @ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/tenant")
    @ResponseStatus(value = HttpStatus.CREATED)
	public HttpEntity<TenantJSON> criaPessoa(@RequestBody Tenant tenant)
    		throws OperacaoNaoPermitidaException, CamposInvalidosException {

		tenantServico.cria(tenant);
		return new ResponseEntity<TenantJSON>(adicionaLinksPermitidos(new TenantJSON(tenant)), HttpStatus.CREATED);
    	
    }
//    
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.PUT, value="/pessoa")
//    @ResponseStatus(value = HttpStatus.OK)
//    public HttpEntity<TenantJSON> atualizaPessoa(@RequestBody Pessoa pessoa)
//    		throws OperacaoNaoPermitidaException, CamposInvalidosException, RegistroNaoEncontradoException {
//
//    	Pessoa pessoaAtualizada = pessoaServico.atualiza(pessoa);
//    	adicionaLinksPermitidos(new TenantJSON(pessoaAtualizada));
//    	return new ResponseEntity<TenantJSON>(adicionaLinksPermitidos(new TenantJSON(pessoaAtualizada)), HttpStatus.OK);
//    	
//    }
//    
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.DELETE, value="/pessoa")
//    @ResponseStatus(value = HttpStatus.OK)
//    public HttpEntity<ListaPessoaJSON> excluiPessoa(@RequestBody Pessoa pessoa)
//    		throws OperacaoNaoPermitidaException, CamposInvalidosException, RegistroNaoEncontradoException {
//
//    	pessoaServico.exclui(pessoa.getEmail());
//    	return this.getPessoas();
//    	
//    }
//    
//    private ListaPessoaJSON converteListaParaJson(List<Pessoa> pessoas) {
//    	final ListaPessoaJSON pessoasJSON = new ListaPessoaJSON();
//
//    	for(Pessoa pessoa : pessoas) {
//    		pessoasJSON.adicionaPessoaJSON(new TenantJSON(pessoa));
//    	}
//    	
//    	return pessoasJSON;
//    }
//
	@Override
	protected ListaTenantJSON adicionaListaLinksPermitidos(ListaTenantJSON listaTenantJSON) {
		try {
    		
			for (TenantJSON pessoaJSON : listaTenantJSON.getLista()) {
				adicionaLinksPermitidos(pessoaJSON);
			}
			
			// listaPessoasJSON.add(linkTo(methodOn(PessoaController.class).getPessoas()).withRel("GET"));
			// listaPessoasJSON.add(linkTo(methodOn(PessoaController.class).criaPessoa(null)).withRel("POST"));
			
		} catch (Exception e) {
		}
		
		return listaTenantJSON;
		
	}

	@Override
	protected TenantJSON adicionaLinksPermitidos(TenantJSON listaTenantJSON) {
		try {
			// pessoaJSON.add(linkTo(methodOn(PessoaController.class).getPessoaPorEmail(null)).withRel("GET"));
			// pessoaJSON.add(linkTo(methodOn(PessoaController.class).atualizaPessoa(null)).withRel("PUT"));
			// pessoaJSON.add(linkTo(methodOn(PessoaController.class).excluiPessoa(null)).withRel("DELETE"));
		} catch (Exception e) {	
		}
		
		return listaTenantJSON;
	}

}
