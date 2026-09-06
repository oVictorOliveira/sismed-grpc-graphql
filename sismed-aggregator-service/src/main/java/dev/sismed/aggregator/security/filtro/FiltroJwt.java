package dev.sismed.aggregator.security.filtro;

import dev.sismed.aggregator.security.servico.ServicoJwt;
import dev.sismed.aggregator.security.servico.ServicoUsuarioDetalhes;
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
public class FiltroJwt extends OncePerRequestFilter {

    private final ServicoJwt servicoJwt;
    private final ServicoUsuarioDetalhes servicoUsuarioDetalhes;

    public FiltroJwt(ServicoJwt servicoJwt, ServicoUsuarioDetalhes servicoUsuarioDetalhes) {
        this.servicoJwt = servicoJwt;
        this.servicoUsuarioDetalhes = servicoUsuarioDetalhes;
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
        if (!servicoJwt.tokenValido(token)) {
            cadeia.doFilter(requisicao, resposta);
            return;
        }

        String login = servicoJwt.extrairLogin(token);
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails usuario = servicoUsuarioDetalhes.loadUserByUsername(login);
            var autenticacao = new UsernamePasswordAuthenticationToken(
                    usuario, null, usuario.getAuthorities());
            autenticacao.setDetails(new WebAuthenticationDetailsSource().buildDetails(requisicao));
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        cadeia.doFilter(requisicao, resposta);
    }
}
