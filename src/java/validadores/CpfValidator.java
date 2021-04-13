package validadores;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import dao.DaoFactory;
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
@FacesValidator("cpfValidator")
public class CpfValidator implements Validator {

    @Inject
    private EntityManager em;

    private DaoFactory dao;

    @PostConstruct
    public void init() {
        this.dao = new DaoFactory(em);
    }

    @Override
    public void validate(FacesContext context, UIComponent componente, Object value) throws ValidatorException {
        CPFValidator validator = new CPFValidator();
        try {
            validator.assertValid(value.toString());
        } catch (InvalidStateException e) { // exception lançada quando o documento é inválido
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "CPF inválido.", "CPF inválido!");
            throw new ValidatorException(msg);
        }
    }

}
