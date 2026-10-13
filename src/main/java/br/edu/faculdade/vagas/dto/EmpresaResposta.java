package br.edu.faculdade.vagas.dto;

import br.edu.faculdade.vagas.model.Empresa;

public record EmpresaResposta(
        Long id,
        String nome,
        String slug,
        String site,
        String descricao
) {
    public static EmpresaResposta de(Empresa empresa) {
        return new EmpresaResposta(
                empresa.getId(),
                empresa.getNome(),
                empresa.getSlug(),
                empresa.getSite(),
                empresa.getDescricao()
        );
    }
}
