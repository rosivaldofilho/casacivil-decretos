package security;

import dominio.Pessoa;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UsuarioSistema extends User {

    private static final long serialVersionUID = 1L;

    private final Pessoa pessoa;

    public UsuarioSistema(Pessoa pessoa, Collection<? extends GrantedAuthority> authorities) {
        super(pessoa.getUsuario().getLogin(), pessoa.getUsuario().getSenha(), authorities);
        this.pessoa = pessoa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

}
