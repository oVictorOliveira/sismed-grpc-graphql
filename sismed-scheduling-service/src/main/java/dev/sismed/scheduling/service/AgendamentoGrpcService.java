package dev.sismed.scheduling.service;

import dev.sismed.scheduling.*;
import dev.sismed.scheduling.ServicoAgendamentoGrpc.ServicoAgendamentoImplBase;
import dev.sismed.scheduling.service.handler.AgendamentoRequestHandler;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AgendamentoGrpcService extends ServicoAgendamentoImplBase {

    private final AgendamentoRequestHandler manipulador;

    public AgendamentoGrpcService(AgendamentoRequestHandler manipulador) {
        this.manipulador = manipulador;
    }

    @Override
    public void agendarConsulta(AgendarConsultaRequisicao requisicao,
                                 StreamObserver<AgendarConsultaResposta> observador) {
        observador.onNext(manipulador.agendarConsulta(requisicao));
        observador.onCompleted();
    }

    @Override
    public void buscarConsulta(BuscarConsultaRequisicao requisicao,
                                StreamObserver<BuscarConsultaResposta> observador) {
        observador.onNext(manipulador.buscarConsulta(requisicao));
        observador.onCompleted();
    }

    @Override
    public void atualizarConsulta(AtualizarConsultaRequisicao requisicao,
                                   StreamObserver<AtualizarConsultaResposta> observador) {
        observador.onNext(manipulador.atualizarConsulta(requisicao));
        observador.onCompleted();
    }

    @Override
    public void cancelarConsulta(CancelarConsultaRequisicao requisicao,
                                  StreamObserver<CancelarConsultaResposta> observador) {
        observador.onNext(manipulador.cancelarConsulta(requisicao));
        observador.onCompleted();
    }

    @Override
    public void confirmarConsulta(ConfirmarConsultaRequisicao requisicao,
                                   StreamObserver<ConfirmarConsultaResposta> observador) {
        observador.onNext(manipulador.confirmarConsulta(requisicao));
        observador.onCompleted();
    }

    @Override
    public void realizarConsulta(RealizarConsultaRequisicao requisicao,
                                  StreamObserver<RealizarConsultaResposta> observador) {
        observador.onNext(manipulador.realizarConsulta(requisicao));
        observador.onCompleted();
    }

    @Override
    public void listarConsultasPorPaciente(ListarConsultasPorPacienteRequisicao requisicao,
                                            StreamObserver<ListarConsultasPorPacienteResposta> observador) {
        observador.onNext(manipulador.listarConsultasPorPaciente(requisicao));
        observador.onCompleted();
    }

    @Override
    public void listarProximasConsultas(ListarProximasConsultasRequisicao requisicao,
                                         StreamObserver<ListarProximasConsultasResposta> observador) {
        observador.onNext(manipulador.listarProximasConsultas(requisicao));
        observador.onCompleted();
    }
}
