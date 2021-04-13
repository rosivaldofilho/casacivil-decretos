package util;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rosivaldo Souza
 */
public class FacesUtil {

    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }

    // Mensagens de topo de página
    public static void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage("mensagem", new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }

    public static void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage("mensagem", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
    }

    public static void addWornMessage(String message) {
        FacesContext.getCurrentInstance().addMessage("mensagem", new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }
    
    //Mensagens de Notificação
    public static void addInfoGrowl(String message) {
        FacesContext.getCurrentInstance().addMessage("notificacao", new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }
    
    public static void addErrorGrowl(String message) {
        FacesContext.getCurrentInstance().addMessage("notificacao", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
    }
    
    public static void addWornGrowl(String message) {
        FacesContext.getCurrentInstance().addMessage("notificacao", new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }

    public static void redirecionarPara(String caminho) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(caminho);
    }

    public static HttpServletRequest getContextRequest() throws IOException {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static HttpServletResponse getContextResponse() throws IOException {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    public static ExternalContext getExternalContext() throws IOException {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    /**
     * Limpa os dados dos componentes de edição e de seus filhos,
     * recursivamente. Checa se o componente é instância de EditableValueHolder
     * e 'reseta' suas propriedades.
     * <p>
     * Quando este método, por algum motivo, não funcionar, parta para
     * ignorância e limpe o componente assim:
     * <p>
     * <blockquote><pre>
     * 	component.getChildren().clear()
     * </pre></blockquote> :-)
     * @param component
     */
    public void cleanSubmittedValues(UIComponent component) {
        if (component instanceof EditableValueHolder) {
            EditableValueHolder evh = (EditableValueHolder) component;
            evh.setSubmittedValue(null);
            evh.setValue(null);
            evh.setLocalValueSet(false);
            evh.setValid(true);
        }
        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                cleanSubmittedValues(child);
            }
        }
    }

}
