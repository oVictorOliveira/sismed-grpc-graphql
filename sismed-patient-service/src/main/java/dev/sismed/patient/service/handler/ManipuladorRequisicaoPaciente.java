package dev.sismed.patient.service.handler;

import dev.sismed.patient.*;
import dev.sismed.patient.entity.Paciente;
import dev.sismed.patient.exception.PacienteNaoEncontradoException;
import dev.sismed.patient.repository.PacienteRepositorio;
import dev.sismed.patient.util.MapadorEntidadeMensagem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManipuladorRequisicaoPaciente {

    private final PacienteRepositorio repositorioPaciente;

    public ManipuladorRequisicaoPaciente(PacienteRepositorio repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    @Transactional
    public AdicionarPacienteResposta adicionarPaciente(AdicionarPacienteRequisicao requisicao) {
        String cpf = requisicao.getPaciente().getCpf();
        if (repositorioPaciente.existePorCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
        }
        Paciente paciente = MapadorEntidadeMensagem.paraEntidade(requisicao);
        repositorioPaciente.save(paciente);
        return MapadorEntidadeMensagem.paraRespostaAdicionar(paciente);
    }

    @Transactional(readOnly = true)
    public BuscarPacientePorIdResposta buscarPacientePorId(BuscarPacientePorIdRequisicao requisicao) {
        Paciente paciente = repositorioPaciente.findById(requisicao.getId())
                .orElseThrow(() -> new PacienteNaoEncontradoException(requisicao.getId()));
        return MapadorEntidadeMensagem.paraRespostaBuscarPorId(paciente);
    }

    @Transactional(readOnly = true)
    public ListarPacientesResposta listarPacientes() {
        return MapadorEntidadeMensagem.paraRespostaListar(repositorioPaciente.findAll());
    }

    @Transactional(readOnly = true)
    public BuscarHistoricoPacienteResposta buscarHistoricoPaciente(BuscarHistoricoPacienteRequisicao requisicao) {
        // Read-model populado na Fase 8 via eventos RabbitMQ. Retorna vazio até lá.
        return BuscarHistoricoPacienteResposta.newBuilder().build();
    }
}
