package br.edu.faculdade.vagas.dto;

import java.util.Map;

public record EstatisticasResposta(
        long totalDeEmpresas,
        long totalDeVagas,
        long vagasParaIniciantes,
        Map<String, Long> porArea,
        Map<String, Long> porSenioridade,
        Map<String, Long> porEmpresa
) {
}
