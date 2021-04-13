package service;

import static com.google.common.hash.Hashing.md5;
import dominio.Pessoa;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import repository.PessoaRepository;
import util.FacesUtil;

/**
 *
 * @author rosivaldo.adm
 */
public class PessoaService implements Serializable {

    @Inject
    private PessoaRepository pessoaRep;

    @Transactional
    public Pessoa cadastrar(Pessoa pessoa) throws IOException {
        if (pessoa.getFoto().isEmpty()) {
            pessoa.setFoto("/resources/images/profiles/user/no-image.jpg");
        }

        pessoa.getUsuario().setLogin(pessoa.getDocumentoReceita());
        pessoa.getUsuario().setPermissoes(pessoaRep.permissoes());
        pessoa.getUsuario().setSenha(md5().hashString(pessoa.getUsuario().getSenha(), StandardCharsets.UTF_8).toString());
        pessoa.setDataCadastro(new Date());
        return pessoaRep.guardar(pessoa);

    }
    
    @Transactional
    public boolean atualizar(Pessoa pessoa) throws IOException {
        try {
            pessoaRep.guardar(pessoa);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Transactional
    public boolean alterarSenha(Pessoa pessoa) throws IOException {

        pessoa.getUsuario().setSenha(md5().hashString(pessoa.getUsuario().getSenha(), StandardCharsets.UTF_8).toString());
        try {
            pessoaRep.guardar(pessoa);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void remover(Pessoa pessoa) {
        pessoaRep.remover(pessoa);
    }

    @Transactional
    public Pessoa desativar(Pessoa pessoa) {
        pessoa.setAtivo(false);
        return pessoaRep.guardar(pessoa);
    }

    @Transactional
    public List<Pessoa> listarTodos() {
        return pessoaRep.listAllAtivos();
    }

    @Transactional
    public List<Pessoa> completaNome(String query) {
        return pessoaRep.completaNome(query);
    }
    
    @Transactional
    public Pessoa buscaPessoaPeloCPF(String query) throws NoResultException{
        return pessoaRep.getPessoabyCPF(query);
    }
    
    @Transactional
    public Integer numeroTotalDePessoas(){
        return pessoaRep.getNumeroTotalDePessoasAtivos();
    }
    
    public void retornaParaInfoPessoa(Pessoa pessoa) throws IOException {
        FacesUtil.redirecionarPara("/pessoa/informacao.xhtml?pessoa=" + pessoa.getId());
    }
    
    public void retornaParaInicio() throws IOException {
        FacesUtil.redirecionarPara("/tonacorrida");
    }
    
    public void retornaParaLogin() throws IOException {
        FacesUtil.redirecionarPara("login.xhtml");
    }
    
    public void encaminharSucesso() throws IOException {
        FacesUtil.redirecionarPara("/site/success.xhtml");
    }
    
    

}
