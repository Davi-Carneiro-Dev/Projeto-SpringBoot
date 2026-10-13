package br.edu.faculdade.vagas.dto;

import br.edu.faculdade.vagas.model.SituacaoCandidatura;
import jakarta.validation.constraints.NotNull;

public record SituacaoCandidaturaEntrada(
        @NotNull(message = "a situação é obrigatória")
        SituacaoCandidatura situacao
) {
}
