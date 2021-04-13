package dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Rosivaldo Souza
 * @param <T>
 */
public class DaoGenerico<T> implements Serializable {

    protected final Class classe;
    protected EntityManager em;

    DaoGenerico(Class classe, EntityManager em) {
        this.classe = classe;
        this.em = em;
    }

    public T addOrUpd(T u) {
        return em.merge(u);
    }

    public void persiste(T u) {
        em.persist(u);
    }

    public void del(T u) {
        em.remove(u);
    }

    public T findById(Integer id) {
        return (T) this.em.find(classe, id);
    }

    public List<T> listAll() {
        CriteriaQuery query = em.getCriteriaBuilder().createQuery(classe);
        query.from(classe);
        return em.createQuery(query).getResultList();
    }

    public List<T> listAllAtivos() {
        String jpql = "select a from " + classe.getSimpleName() + " a WHERE a.ativo = TRUE";
        return em.createQuery(jpql, classe).getResultList();
    }

    public List<T> jpqlLike(String busca, String campo) {

        String jpql = "select t from " + classe.getSimpleName()
                + " t  where  LOWER(t." + campo + ") like :pbusca";
        Query query = em.createQuery(jpql, classe);
        query.setParameter("pbusca", "%" + busca.toLowerCase() + "%");
        return query.getResultList();

    }

    public List<T> jpqlEqualsID(Integer busca, String campo) {

        String jpql = "select t from " + classe.getSimpleName()
                + " t  where  t." + campo + " = :pbusca";
        Query query = em.createQuery(jpql, classe);
        query.setParameter("pbusca", busca);
        return query.getResultList();

    }

    public List<T> jpqlEqualsID(Integer busca, String campo, String where) {

        String jpql = "select t from " + classe.getSimpleName()
                + " t  where  t." + campo + " = :pbusca AND " + where;
        Query query = em.createQuery(jpql, classe);
        query.setParameter("pbusca", busca);
        return query.getResultList();

    }
}
