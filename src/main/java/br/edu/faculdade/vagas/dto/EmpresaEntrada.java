package br.edu.faculdade.vagas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpresaEntrada(
        @NotBlank(message = "o nome é obrigatório")
        @Size(max = 120, message = "o nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "o slug é obrigatório")
        @Size(max = 100, message = "o slug deve ter no máximo 100 caracteres")
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "o slug deve conter apenas letras minúsculas, números e hífens")
        String slug,

        @Size(max = 255, message = "o site deve ter no máximo 255 caracteres")
        @Pattern(regexp = "^https?://.+$", message = "o site deve começar com http:// ou https://")
        String site,

        @NotBlank(message = "a descrição é obrigatória")
        @Size(max = 2000, message = "a descrição deve ter no máximo 2000 caracteres")
        String descricao
) {
    public EmpresaEntrada {
        nome = nome == null ? null : nome.trim();
        slug = slug == null ? null : slug.trim();
        site = site == null || site.isBlank() ? null : site.trim();
        descricao = descricao == null ? null : descricao.trim();
    }
}
