package dev.sismed.aggregator.security.filtro;

import dev.sismed.aggregator.security.servico.JwtService;
import dev.sismed.aggregator.security.servico.UsuarioDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    public JwtFilter(JwtService jwtService, UsuarioDetailsService usuarioDetailsService) {
        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest requisicao,
                                     HttpServletResponse resposta,
                                     FilterChain cadeia) throws ServletException, IOException {
        String cabecalho = requisicao.getHeader("Authorization");

        if (cabecalho == null || !cabecalho.startsWith("Bearer ")) {
            cadeia.doFilter(requisicao, resposta);
            return;
        }

        String token = cabecalho.substring(7);
        if (!jwtService.tokenValido(token)) {
            cadeia.doFilter(requisicao, resposta);
            return;
        }

        String login = jwtService.extrairLogin(token);
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails usuario = usuarioDetailsService.loadUserByUsername(login);
            var autenticacao = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities());
            autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(requisicao));
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        cadeia.doFilter(requisicao, resposta);
    }
}
