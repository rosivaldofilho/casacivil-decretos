package testes;

import javax.persistence.EntityManager;
import util.JpaUtil;

/**
 *
 * @author rosivaldo
 */
public class CriarTabelas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();
        em.clear();
        em.close();
        System.exit(0);
    }
}