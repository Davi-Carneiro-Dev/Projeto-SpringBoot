package br.edu.faculdade.vagas.dto;

import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VagaEntrada(
        @NotBlank(message = "o título é obrigatório")
        @Size(max = 120, message = "o título deve ter no máximo 120 caracteres")
        String titulo,

        @NotBlank(message = "a descrição é obrigatória")
        @Size(max = 2000, message = "a descrição deve ter no máximo 2000 caracteres")
        String descricao,

        @NotNull(message = "a área é obrigatória")
        Area area,

        @NotNull(message = "a senioridade é obrigatória")
        Senioridade senioridade,

        @NotBlank(message = "o local é obrigatório")
        @Size(max = 120, message = "o local deve ter no máximo 120 caracteres")
        String local,

        @NotNull(message = "informe se a vaga aceita iniciantes")
        Boolean aceitaIniciante,

        @NotBlank(message = "a empresa é obrigatória")
        @Size(max = 100, message = "o slug da empresa deve ter no máximo 100 caracteres")
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "o slug da empresa deve conter apenas letras minúsculas, números e hífens")
        String empresaSlug
) {
}
