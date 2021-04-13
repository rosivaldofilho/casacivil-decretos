package security;

import dominio.Pessoa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repository.LoginRepository;
import util.JpaUtil;

public class AppUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EntityManager em = JpaUtil.getEntityManager();
        LoginRepository loginRep = new LoginRepository(em);

        Pessoa pessoa = loginRep.getPessoaByUsuario(login);

        UsuarioSistema user = null;

        if (pessoa != null) {
            user = new UsuarioSistema(pessoa, getPermissoes(pessoa));
        }

        em.clear();
        em.close();

        return user;
    }

    private Collection<? extends GrantedAuthority> getPermissoes(Pessoa pessoa) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        pessoa.getUsuario().getPermissoes().stream().forEach((permissao) -> {
            String PREFIXO = "ROLE_";
            authorities.add(new SimpleGrantedAuthority(PREFIXO + permissao.getDescricao().toUpperCase()));
        });

        return authorities;
    }

}
