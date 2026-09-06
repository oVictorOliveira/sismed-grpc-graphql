package dev.sismed.aggregator.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import io.grpc.StatusRuntimeException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

@Component
public class ResolvedorExcecaoGraphql extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof StatusRuntimeException grpcEx) {
            var codigo = grpcEx.getStatus().getCode();

            ErrorType tipoErro = switch (codigo) {
                case NOT_FOUND -> ErrorType.NOT_FOUND;
                case INVALID_ARGUMENT, FAILED_PRECONDITION -> ErrorType.BAD_REQUEST;
                case PERMISSION_DENIED, UNAUTHENTICATED -> ErrorType.FORBIDDEN;
                default -> ErrorType.INTERNAL_ERROR;
            };

            String mensagem = grpcEx.getStatus().getDescription();
            if (mensagem == null || mensagem.isBlank()) {
                mensagem = switch (codigo) {
                    case NOT_FOUND -> "Recurso não encontrado";
                    case INVALID_ARGUMENT -> "Argumento inválido";
                    case FAILED_PRECONDITION -> "Operação não permitida no estado atual";
                    case PERMISSION_DENIED -> "Acesso negado";
                    default -> "Erro interno";
                };
            }

            return GraphqlErrorBuilder.newError(env)
                    .message(mensagem)
                    .errorType(tipoErro)
                    .build();
        }
        return null;
    }
}
