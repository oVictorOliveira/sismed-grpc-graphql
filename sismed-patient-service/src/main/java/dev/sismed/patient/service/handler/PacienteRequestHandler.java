package dev.sismed.patient.service.handler;

import dev.sismed.patient.*;
import dev.sismed.patient.entity.Paciente;
import dev.sismed.patient.exception.PacienteNaoEncontradoException;
import dev.sismed.patient.repository.PacienteRepository;
import dev.sismed.patient.util.PacienteMessageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteRequestHandler {

    private final PacienteRepository repositorioPaciente;

    public PacienteRequestHandler(PacienteRepository repositorioPaciente) {
        this.repositorioPaciente = repositorioPaciente;
    }

    @Transactional
    public AdicionarPacienteResposta adicionarPaciente(AdicionarPacienteRequisicao requisicao) {
        String cpf = requisicao.getPaciente().getCpf();
        if (repositorioPaciente.existePorCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
        }
        Paciente paciente = PacienteMessageMapper.paraEntidade(requisicao);
        repositorioPaciente.save(paciente);
        return PacienteMessageMapper.paraRespostaAdicionar(paciente);
    }

    @Transactional(readOnly = true)
    public BuscarPacientePorIdResposta buscarPacientePorId(BuscarPacientePorIdRequisicao requisicao) {
        Paciente paciente = repositorioPaciente.findById(requisicao.getId())
                .orElseThrow(() -> new PacienteNaoEncontradoException(requisicao.getId()));
        return PacienteMessageMapper.paraRespostaBuscarPorId(paciente);
    }

    @Transactional(readOnly = true)
    public ListarPacientesResposta listarPacientes() {
        return PacienteMessageMapper.paraRespostaListar(repositorioPaciente.findAll());
    }

    @Transactional(readOnly = true)
    public BuscarHistoricoPacienteResposta buscarHistoricoPaciente(BuscarHistoricoPacienteRequisicao requisicao) {
        // Read-model populado na Fase 8 via eventos RabbitMQ. Retorna vazio até lá.
        return BuscarHistoricoPacienteResposta.newBuilder().build();
    }
}
