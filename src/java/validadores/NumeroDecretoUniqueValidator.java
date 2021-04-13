
package validadores;

import dao.DaoFactory;
import dominio.Decreto;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author rosivaldo.adm
 */
@FacesValidator("numeroDecretoUniqueValidator")
public class NumeroDecretoUniqueValidator implements Validator {

    @Inject
    private EntityManager em;

    private DaoFactory dao;

    @PostConstruct
    public void init() {
        this.dao = new DaoFactory(em);
    }

    @Override
    public void validate(FacesContext context, UIComponent componente, Object value) throws ValidatorException {

        List<Decreto> decretos = dao.getDecretoDao().jpqlEqualsID(Integer.valueOf(value.toString()), "numero");
        if (!decretos.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Número já utilizado.", "Número já utilizado em outro decreto");
            throw new ValidatorException(msg);
        }
    }

}
