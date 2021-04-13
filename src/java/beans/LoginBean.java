package beans;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import util.FacesUtil;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private FacesContext facesContext;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String usuario;

    private String tipoUsario = "ATLETA";

    private boolean atleta = true;

    public String isAtletaSelecionado() {
        if (isAtleta()) {
            return "btn-primary";
        } else {
            return "btn-dafault";
        }
    }
    public String isOrganizadorSelecionado() {
        if (!isAtleta()) {
            return "btn-primary";
        } else {
            return "btn-dafault";
        }
    }

    public void setaAtletaTrue() {
        usuario = null;
        this.tipoUsario = "ATLETA";
        this.atleta = true;
    }

    public void setaAtletaFalse() {
        this.tipoUsario = "ORGANIZADOR";
        usuario = null;
        this.atleta = false;
    }

    public void atualizaContexto() throws IOException {
        facesContext = FacesContext.getCurrentInstance();
        request = FacesUtil.getContextRequest();
        response = FacesUtil.getContextResponse();
    }

    public void preRender() throws IOException {
        atualizaContexto();
        // Se existir a variavel invalid na url de erro, lança uma mensagem
        if ("true".equals(request.getParameter("invalid"))) {
            FacesContext.getCurrentInstance().addMessage("msgLogin", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Usuário ou senha inválido!", "Usuário ou senha inválido!"));
        }
    }

    public void login() throws ServletException, IOException {

        // Atualizando request, response e context para multiplas tentativas com erro
        atualizaContexto();

        RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
        dispatcher.forward(request, response);
        facesContext.responseComplete();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the atleta
     */
    public boolean isAtleta() {
        return atleta;
    }

    /**
     * @param atleta the atleta to set
     */
    public void setAtleta(boolean atleta) {
        this.atleta = atleta;
    }

    /**
     * @return the tipoUsario
     */
    public String getTipoUsario() {
        return tipoUsario;
    }

    /**
     * @param tipoUsario the tipoUsario to set
     */
    public void setTipoUsario(String tipoUsario) {
        this.tipoUsario = tipoUsario;
    }

}
