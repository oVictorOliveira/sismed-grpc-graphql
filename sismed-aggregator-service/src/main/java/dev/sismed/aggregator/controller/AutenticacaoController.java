package dev.sismed.aggregator.controller;

import dev.sismed.aggregator.security.AuthenticatedUsuario;
import dev.sismed.aggregator.security.dto.LoginInputDTO;
import dev.sismed.aggregator.security.dto.TokenResponseDTO;
import dev.sismed.aggregator.security.servico.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AuthenticationManager gerenciadorAutenticacao;
    private final JwtService jwtService;

    public AutenticacaoController(AuthenticationManager gerenciadorAutenticacao, JwtService jwtService) {
        this.gerenciadorAutenticacao = gerenciadorAutenticacao;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody LoginInputDTO entrada) {
        var autenticacao = gerenciadorAutenticacao.authenticate(
                new UsernamePasswordAuthenticationToken(entrada.login(), entrada.senha()));
        var usuario = (AuthenticatedUsuario) autenticacao.getPrincipal();
        String token = jwtService.gerarToken(usuario);
        return new TokenResponseDTO(token, "Bearer", "8h");
    }
}
