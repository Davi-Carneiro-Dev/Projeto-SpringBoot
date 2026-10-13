package br.edu.faculdade.vagas.dto;

import br.edu.faculdade.vagas.model.Perfil;
import br.edu.faculdade.vagas.model.Pessoa;

public record PessoaResposta(
        Long id,
        String nome,
        String email,
        Perfil perfil
) {
    public static PessoaResposta de(Pessoa pessoa) {
        return new PessoaResposta(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getEmail(),
                pessoa.getPerfil()
        );
    }
}
