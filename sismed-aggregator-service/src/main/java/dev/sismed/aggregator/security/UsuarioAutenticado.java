package dev.sismed.aggregator.security;

import dev.sismed.aggregator.security.entidade.PapelUsuario;
import dev.sismed.aggregator.security.entidade.SismedUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioAutenticado implements UserDetails {

    private final SismedUsuario usuario;

    public UsuarioAutenticado(SismedUsuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPapel().name()));
    }

    @Override
    public String getPassword() { return usuario.getSenha(); }

    @Override
    public String getUsername() { return usuario.getLogin(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public Long getPacienteId() { return usuario.getPacienteId(); }

    public PapelUsuario getPapel() { return usuario.getPapel(); }
}
