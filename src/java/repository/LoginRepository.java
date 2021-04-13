package repository;

import dominio.Usuario;
import dominio.Pessoa;
import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author rosivaldo.adm
 */
public class LoginRepository implements Serializable {


    private final EntityManager em;

    public LoginRepository(EntityManager em) {
        this.em = em;
    }

    public Usuario getLoginByUsuario(String login) {
        try {
            String jpql = "select u from Usuario u  where  u.login = :login";
            Query query = em.createQuery(jpql, Usuario.class);
            query.setParameter("login", login);
            return (Usuario) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public Pessoa getPessoaByUsuario(String login) {
        try {
            String jpql = "SELECT p FROM Pessoa p JOIN p.usuario usuario WHERE usuario.login = :login";
            Query query = em.createQuery(jpql, Pessoa.class);
            query.setParameter("login", login);
            return (Pessoa) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
