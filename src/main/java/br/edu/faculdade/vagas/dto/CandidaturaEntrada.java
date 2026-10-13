package br.edu.faculdade.vagas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CandidaturaEntrada(
        @NotNull(message = "a pessoa é obrigatória")
        @Positive(message = "o identificador da pessoa deve ser positivo")
        Long pessoaId,

        @NotNull(message = "a vaga é obrigatória")
        @Positive(message = "o identificador da vaga deve ser positivo")
        Long vagaId
) {
}
