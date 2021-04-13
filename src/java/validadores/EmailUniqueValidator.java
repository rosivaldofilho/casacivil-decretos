/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validadores;

import dao.DaoFactory;
import dominio.Pessoa;
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
@FacesValidator("emailUniqueValidator")
public class EmailUniqueValidator implements Validator {

    @Inject
    private EntityManager em;

    private DaoFactory dao;

    @PostConstruct
    public void init() {
        this.dao = new DaoFactory(em);
    }

    @Override
    public void validate(FacesContext context, UIComponent componente, Object value) throws ValidatorException {

        List<Pessoa> pessoas = dao.getPessoaDao().jpqlLike(value.toString(), "email");
        if (!pessoas.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Email já utilizado.", "Email já utilizado no sistema");
            throw new ValidatorException(msg);
        }
    }

}
