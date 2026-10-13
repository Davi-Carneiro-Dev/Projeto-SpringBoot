package br.edu.faculdade.vagas.dto;

import br.edu.faculdade.vagas.model.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PessoaEntrada(
        @NotBlank(message = "o nome é obrigatório")
        @Size(max = 120, message = "o nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "o e-mail é obrigatório")
        @Email(message = "informe um e-mail válido")
        @Size(max = 160, message = "o e-mail deve ter no máximo 160 caracteres")
        String email,

        @NotNull(message = "o perfil é obrigatório")
        Perfil perfil
) {
    public PessoaEntrada {
        if (nome != null) {
            nome = nome.trim();
        }
        if (email != null) {
            email = email.trim();
        }
    }
}
