package br.edu.faculdade.vagas.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginaResposta<T>(
        List<T> conteudo,
        int pagina,
        int tamanho,
        long totalDeElementos,
        int totalDePaginas,
        boolean primeira,
        boolean ultima
) {
    public static <T> PaginaResposta<T> de(Page<T> pagina) {
        return new PaginaResposta<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages(),
                pagina.isFirst(),
                pagina.isLast()
        );
    }
}
