package br.edu.faculdade.vagas.dto;

import br.edu.faculdade.vagas.model.Candidatura;
import br.edu.faculdade.vagas.model.SituacaoCandidatura;

import java.time.LocalDateTime;

public record CandidaturaResposta(
        Long id,
        Long pessoaId,
        String pessoaNome,
        Long vagaId,
        String vagaTitulo,
        SituacaoCandidatura situacao,
        LocalDateTime enviadaEm
) {
    public static CandidaturaResposta de(Candidatura candidatura) {
        return new CandidaturaResposta(
                candidatura.getId(),
                candidatura.getPessoa().getId(),
                candidatura.getPessoa().getNome(),
                candidatura.getVaga().getId(),
                candidatura.getVaga().getTitulo(),
                candidatura.getSituacao(),
                candidatura.getEnviadaEm()
        );
    }
}
