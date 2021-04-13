package security;

import dominio.Pessoa;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import util.FacesUtil;

@RequestScoped
@ManagedBean(name = "segurancaBean")
public class Seguranca implements Serializable {

    public Pessoa getUsuario() {
        Pessoa usuario = null;
        UsuarioSistema usuarioLogado = getUsuarioLogado();
        if (usuarioLogado != null) {
            usuario = usuarioLogado.getPessoa();
        }
        return usuario;
    }

    private UsuarioSistema getUsuarioLogado() {
        UsuarioSistema usuario = null;
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (auth != null && auth.getPrincipal() != null) {
            usuario = (UsuarioSistema) auth.getPrincipal();
        }
        return usuario;
    }

    // REGRAS DE ACESSO AO MENU
    public boolean isMenuMembroPermitido() throws IOException {
        return FacesUtil.getExternalContext().isUserInRole("ROLE_ADMINISTRADOR")
                || FacesUtil.getExternalContext().isUserInRole("ROLE_SERVIDOR");
    }

    // END REGRAS DE ACESSO AO MENU
    public boolean isAdministrador() throws IOException {
        return FacesUtil.getExternalContext().isUserInRole("ROLE_ADMINISTRADOR");
    }

    public boolean isServidor() throws IOException {
        return FacesUtil.getExternalContext().isUserInRole("ROLE_SERVIDOR");
    }

    public boolean isLogout() throws IOException {
        return  getUsuario()==null;
    }

}
