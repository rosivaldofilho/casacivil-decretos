package conversores;

import dominio.Decreto;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import repository.DecretoRepository;
import util.FacesUtil;

/**
 *
 * @author rosivaldo.adm
 */
@FacesConverter("decretoNumeroConverter")
public final class DecretoNumeroConverter implements Converter {

    @Inject
    private DecretoRepository decretoRep;


    @PostConstruct
    public void init() {
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                Decreto decreto = decretoRep.getDecretobyNumero(value);
                return decreto;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Conversão", "Identificador de decreto inválido."));
            } catch (Exception e) {
                try {
                    FacesUtil.redirecionarPara("/erro");
                } catch (IOException ex) {
                    Logger.getLogger(DecretoNumeroConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            return null;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Decreto) object).getId());
        } else {
            return null;
        }
    }

}
