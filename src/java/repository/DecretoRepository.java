package repository;

import LazyLoading.FiltroBusca;
import dao.DaoFactory;
import dominio.Decreto;
import dominio.Permissao;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author rosivaldo.adm
 */
public class DecretoRepository implements Serializable {

    @Inject
    private EntityManager em;

    private DaoFactory dao;

    @PostConstruct
    public void init() {
        this.dao = new DaoFactory(em);
    }

    @SuppressWarnings("unchecked")
    public List<Decreto> filtrados(FiltroBusca filtro) {
        Criteria criteria = criarCriteriaParaFiltro(filtro);

        criteria.setFirstResult(filtro.getPrimeiroRegistro());
        criteria.setMaxResults(filtro.getQuantidadeRegistros());

        if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
            criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
        } else if (filtro.getPropriedadeOrdenacao() != null) {
            criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
        } else {
            criteria.addOrder(Order.desc("dataCadastro"));
        }

        return criteria.list();
    }

    public int quantidadeFiltrados(FiltroBusca filtro) {
        Criteria criteria = criarCriteriaParaFiltro(filtro);

        criteria.setProjection(Projections.rowCount());

        return ((Number) criteria.uniqueResult()).intValue();
    }

    private Criteria criarCriteriaParaFiltro(FiltroBusca filtro) {

        Map<String, Object> filters = filtro.getFilters();
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Decreto.class);

        for (String filterProperty : filters.keySet()) {
            Object filterValue = filters.get(filterProperty);
            String value = filterValue.toString();

            try {
                if (StringUtils.isNotEmpty(value)) {
                    if (filterProperty.equals("numero")) {
                        criteria.add(Restrictions.eq(filterProperty, Integer.valueOf(value)));
                    } else if (filterProperty.equals("conteudo")) {
                        criteria.add(Restrictions.ilike(filterProperty, value, MatchMode.ANYWHERE));
                    }
                }

            } catch (Exception e) {
                return null;
            }
        }
        return criteria;
    }

    public List<Decreto> criarCriteriaParaFiltroIndex(FiltroBusca filtro) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Decreto.class);
        boolean n = false, c = false, dt = false;

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {
            criteria.add(Restrictions.ilike("conteudo", filtro.getDescricao(), MatchMode.ANYWHERE));
            c = true;
        }

        if (filtro.getNumero() != null) {
            criteria.add(Restrictions.eq("numero", filtro.getNumero()));
            n = true;
        }
        
        
        if (filtro.getDataInicial() != null || filtro.getDataFinal()!= null) {
            criteria.add(Restrictions.between("dataDecreto", filtro.getDataInicial(), filtro.getDataFinal()));
            dt = true;
        }

        criteria.addOrder(Order.desc("dataDecreto"));
        
        if (c || n || dt) {
            return criteria.list();
        } else {
            return null;
        }
    }

    public Decreto guardar(Decreto decreto) {
        return em.merge(decreto);
    }

    public void remover(Decreto decreto) {
        dao.getDecretoDao().del(decreto);
    }

    public List<Permissao> permissoes() {
        return dao.getPermissaoDao().jpqlLike("decreto", "descricao");
    }

    public List<Decreto> listAll() {
        return dao.getDecretoDao().listAll();
    }

    public List<Decreto> listAllAtivos() {
        return dao.getDecretoDao().listAllAtivos();
    }

    public Decreto getDecretoById(Integer id) {
        return dao.getDecretoDao().findById(id);
    }

    public List<Decreto> completaNome(String query) {
        return dao.getDecretoDao().jpqlLike(query, "nome");
    }

    public Decreto getDecretobyNumero(String numero) {
        String jpql = "select d from Decreto d where d.numero = :numero";
        Query query = em.createQuery(jpql, Decreto.class);
        query.setParameter("numero", Integer.valueOf(numero));
        return (Decreto) query.getSingleResult();
    }

    public Decreto getUltimoDecreto() {
        String jpql = "SELECT d FROM Decreto d ORDER BY d.numero DESC";
        Query query = em.createQuery(jpql, Decreto.class);
        return (Decreto) query.getResultList().get(0);
    }

    public List<Decreto> getUltimosDecretos() {
        String jpql = "SELECT d FROM Decreto d ORDER BY d.dataDecreto DESC";
        Query query = em.createQuery(jpql, Decreto.class);
        return query.setMaxResults(10).getResultList();
    }

    public Integer getNumeroTotalDeDecretosAtivos() {
        String jpql = "select COUNT(a) from Decreto a WHERE a.ativo=TRUE";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        return Integer.valueOf(query.getSingleResult().toString());
    }

}
