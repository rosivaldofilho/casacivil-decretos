package dao;

import dominio.Decreto;
import dominio.Permissao;
import dominio.Pessoa;
import dominio.Usuario;
import java.io.Serializable;
import javax.persistence.EntityManager;

/**
 *
 * @author Rosivaldo Souza
 */
public class DaoFactory implements Serializable {

    private final EntityManager em;

    public DaoFactory(EntityManager em) {
        this.em = em;
    }

    public DaoGenerico<Permissao> getPermissaoDao() {
        return new DaoGenerico<>(Permissao.class, em);
    }

    public DaoGenerico<Pessoa> getPessoaDao() {
        return new DaoGenerico<>(Pessoa.class, em);
    }
    
    public DaoGenerico<Decreto> getDecretoDao() {
        return new DaoGenerico<>(Decreto.class, em);
    }

    public DaoGenerico<Usuario> getUsuarioDao() {
        return new DaoGenerico<>(Usuario.class, em);
    }


}
