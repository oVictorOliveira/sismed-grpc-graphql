package dev.sismed.aggregator.security.servico;

import dev.sismed.aggregator.security.UsuarioAutenticado;
import dev.sismed.aggregator.security.repositorio.UsuarioRepositorio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ServicoUsuarioDetalhes implements UserDetailsService {

    private final UsuarioRepositorio repositorioUsuario;

    public ServicoUsuarioDetalhes(UsuarioRepositorio repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repositorioUsuario.findByLogin(login)
                .map(UsuarioAutenticado::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
    }
}
