package conversores;

import dao.DaoFactory;
import dominio.Pessoa;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author rosivaldo.adm
 */
@FacesConverter("pessoaConverter")
public final class PessoaConverter implements Converter {

    @Inject
    private EntityManager em;

    private DaoFactory dao;

    @PostConstruct
    public void init() {
        this.dao = new DaoFactory(em);
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                Pessoa pessoa = dao.getPessoaDao().findById(Integer.valueOf(value));
                return pessoa;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Conversão", "Identificador de usuário inválido."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Pessoa) object).getId());
        } else {
            return null;
        }
    }

    /**
     * @return the dao
     */
    public DaoFactory getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(DaoFactory dao) {
        this.dao = dao;
    }

}
