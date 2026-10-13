package br.edu.faculdade.vagas.dto;

import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.model.Vaga;

import java.time.LocalDateTime;

public record VagaResposta(
        Long id,
        String titulo,
        String descricao,
        Area area,
        Senioridade senioridade,
        String local,
        Boolean aceitaIniciante,
        String empresaSlug,
        LocalDateTime publicadaEm
) {
    public static VagaResposta de(Vaga vaga) {
        return new VagaResposta(
                vaga.getId(),
                vaga.getTitulo(),
                vaga.getDescricao(),
                vaga.getArea(),
                vaga.getSenioridade(),
                vaga.getLocal(),
                vaga.getAceitaIniciante(),
                vaga.getEmpresaSlug(),
                vaga.getPublicadaEm()
        );
    }
}
