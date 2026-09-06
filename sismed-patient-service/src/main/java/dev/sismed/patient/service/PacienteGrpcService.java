package dev.sismed.patient.service;

import dev.sismed.patient.*;
import dev.sismed.patient.ServicoPacienteGrpc.ServicoPacienteImplBase;
import dev.sismed.patient.service.handler.PacienteRequestHandler;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PacienteGrpcService extends ServicoPacienteImplBase {

    private final PacienteRequestHandler manipulador;

    public PacienteGrpcService(PacienteRequestHandler manipulador) {
        this.manipulador = manipulador;
    }

    @Override
    public void adicionarPaciente(AdicionarPacienteRequisicao requisicao,
                                   StreamObserver<AdicionarPacienteResposta> observador) {
        observador.onNext(manipulador.adicionarPaciente(requisicao));
        observador.onCompleted();
    }

    @Override
    public void listarPacientes(ListarPacientesRequisicao requisicao,
                                 StreamObserver<ListarPacientesResposta> observador) {
        observador.onNext(manipulador.listarPacientes());
        observador.onCompleted();
    }

    @Override
    public void buscarPacientePorId(BuscarPacientePorIdRequisicao requisicao,
                                     StreamObserver<BuscarPacientePorIdResposta> observador) {
        observador.onNext(manipulador.buscarPacientePorId(requisicao));
        observador.onCompleted();
    }

    @Override
    public void buscarHistoricoPaciente(BuscarHistoricoPacienteRequisicao requisicao,
                                         StreamObserver<BuscarHistoricoPacienteResposta> observador) {
        observador.onNext(manipulador.buscarHistoricoPaciente(requisicao));
        observador.onCompleted();
    }
}
