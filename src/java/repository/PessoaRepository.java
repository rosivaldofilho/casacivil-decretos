package repository;

import dao.DaoFactory;
import dominio.Pessoa;
import dominio.Permissao;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author rosivaldo.adm
 */
public class PessoaRepository implements Serializable {

    @Inject
    private EntityManager em;

    private DaoFactory dao;

    @PostConstruct
    public void init() {
        this.dao = new DaoFactory(em);
    }

    public Pessoa guardar(Pessoa pessoa) {
        return em.merge(pessoa);
    }

    public void remover(Pessoa pessoa) {
        dao.getPessoaDao().del(pessoa);
    }

    public List<Permissao> permissoes() {
        return dao.getPermissaoDao().jpqlLike("pessoa", "descricao");
    }

    public List<Pessoa> listAll() {
        return dao.getPessoaDao().listAll();
    }

    public List<Pessoa> listAllAtivos() {
        return dao.getPessoaDao().listAllAtivos();
    }
    
    public Pessoa getPessoaById(Integer id) {
        return dao.getPessoaDao().findById(id);
    }

    public List<Pessoa> completaNome(String query) {
        return dao.getPessoaDao().jpqlLike(query, "nome");
    }

    public Pessoa getPessoabyCPF(String cpf) {
        String jpql = "select a from Pessoa a where a.documentoReceita = :cpf";
        Query query = em.createQuery(jpql, Pessoa.class);
        query.setParameter("cpf", cpf);
        return (Pessoa) query.getSingleResult();
    }
    
    public Integer getNumeroTotalDePessoasAtivos(){
        String jpql = "select COUNT(a) from Pessoa a WHERE a.ativo=TRUE";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        return Integer.valueOf(query.getSingleResult().toString());
    }

}
