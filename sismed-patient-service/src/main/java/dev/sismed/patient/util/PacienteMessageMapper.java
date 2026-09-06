package dev.sismed.patient.util;

import dev.sismed.patient.*;
import dev.sismed.patient.entity.Paciente;

import java.time.LocalDate;
import java.util.List;

public class PacienteMessageMapper {

    public static Paciente paraEntidade(AdicionarPacienteRequisicao requisicao) {
        PacienteProto proto = requisicao.getPaciente();
        LocalDate dataNascimento = proto.getDataNascimento().isBlank()
                ? null
                : LocalDate.parse(proto.getDataNascimento());
        return new Paciente(proto.getNome(), proto.getCpf(), proto.getEmail(),
                proto.getTelefone(), dataNascimento);
    }

    public static PacienteProto paraProto(Paciente p) {
        PacienteProto.Builder b = PacienteProto.newBuilder()
                .setId(p.getId())
                .setNome(p.getNome())
                .setCpf(p.getCpf())
                .setEmail(p.getEmail())
                .setAtivo(p.isAtivo());
        if (p.getTelefone() != null) b.setTelefone(p.getTelefone());
        if (p.getDataNascimento() != null) b.setDataNascimento(p.getDataNascimento().toString());
        return b.build();
    }

    public static AdicionarPacienteResposta paraRespostaAdicionar(Paciente p) {
        return AdicionarPacienteResposta.newBuilder()
                .setPacienteId(p.getId())
                .setMensagem("Paciente cadastrado com sucesso")
                .build();
    }

    public static BuscarPacientePorIdResposta paraRespostaBuscarPorId(Paciente p) {
        return BuscarPacientePorIdResposta.newBuilder()
                .setPaciente(paraProto(p))
                .build();
    }

    public static ListarPacientesResposta paraRespostaListar(List<Paciente> pacientes) {
        return ListarPacientesResposta.newBuilder()
                .addAllPacientes(pacientes.stream().map(PacienteMessageMapper::paraProto).toList())
                .build();
    }
}
