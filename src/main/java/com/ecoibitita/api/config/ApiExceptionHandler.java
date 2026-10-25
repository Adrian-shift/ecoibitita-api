package com.ecoibitita.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Padroniza os erros da API: {"status":400,"error":"Bad Request","message":"..."}.
 * Só trata erros previstos. O restante segue o comportamento padrão do Spring,
 * que não expõe detalhes internos.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    public record Erro(int status, String error, String message) {}

    // erros que o próprio serviço lança (404, 403, "máximo de 3 fotos"...)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Erro> status(ResponseStatusException e) {
        String mensagem = e.getReason() != null
                ? e.getReason()
                : "Não foi possível concluir a operação.";
        return resposta(HttpStatus.valueOf(e.getStatusCode().value()), mensagem);
    }

    // campos do formulário inválidos (@Valid)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Erro> validacao(BindException e) {
        String mensagem = e.getBindingResult().getAllErrors().stream()
                .map(erro -> {
                    if (erro instanceof FieldError campo && campo.isBindingFailure()) {
                        return "Valor inválido para o campo '" + campo.getField() + "'.";
                    }
                    return erro.getDefaultMessage();
                })
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining(" "));
        if (mensagem.isBlank()) mensagem = "Dados inválidos. Confira o formulário.";
        return resposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    // envio sem o campo "fotos"
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<Erro> parteAusente(MissingServletRequestPartException e) {
        String mensagem = "fotos".equals(e.getRequestPartName())
                ? "É obrigatório enviar pelo menos 1 foto"
                : "Faltou um dado obrigatório no envio.";
        return resposta(HttpStatus.BAD_REQUEST, mensagem);
    }

    // foto acima do limite do application.properties
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Erro> arquivoGrande(MaxUploadSizeExceededException e) {
        return resposta(HttpStatus.valueOf(413),
                "Arquivo muito grande. Cada foto pode ter até 5 MB (máximo de 15 MB no envio).");
    }

    private ResponseEntity<Erro> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status)
                .body(new Erro(status.value(), status.getReasonPhrase(), mensagem));
    }
}
