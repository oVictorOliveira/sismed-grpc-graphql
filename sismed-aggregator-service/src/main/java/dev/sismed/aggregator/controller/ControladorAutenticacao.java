package dev.sismed.aggregator.controller;

import dev.sismed.aggregator.security.UsuarioAutenticado;
import dev.sismed.aggregator.security.dto.EntradaLoginDTO;
import dev.sismed.aggregator.security.dto.RespostaTokenDTO;
import dev.sismed.aggregator.security.servico.ServicoJwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class ControladorAutenticacao {

    private final AuthenticationManager gerenciadorAutenticacao;
    private final ServicoJwt servicoJwt;

    public ControladorAutenticacao(AuthenticationManager gerenciadorAutenticacao, ServicoJwt servicoJwt) {
        this.gerenciadorAutenticacao = gerenciadorAutenticacao;
        this.servicoJwt = servicoJwt;
    }

    @PostMapping("/login")
    public RespostaTokenDTO login(@RequestBody EntradaLoginDTO entrada) {
        var autenticacao = gerenciadorAutenticacao.authenticate(
                new UsernamePasswordAuthenticationToken(entrada.login(), entrada.senha()));
        var usuario = (UsuarioAutenticado) autenticacao.getPrincipal();
        String token = servicoJwt.gerarToken(usuario);
        return new RespostaTokenDTO(token, "Bearer", "8h");
    }
}
