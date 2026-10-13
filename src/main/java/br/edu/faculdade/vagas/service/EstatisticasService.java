package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.EstatisticasResposta;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstatisticasService {

    private final VagaRepository repository;

    public EstatisticasService(VagaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public EstatisticasResposta calcular() {
        List<Vaga> vagas = repository.findAll();

        long vagasParaIniciantes = vagas.stream()
                .filter(vaga -> Boolean.TRUE.equals(vaga.getAceitaIniciante()))
                .count();

        Map<String, Long> porArea = contarPor(vagas, vaga -> vaga.getArea().name());
        Map<String, Long> porSenioridade = contarPor(vagas, vaga -> vaga.getSenioridade().name());

        return new EstatisticasResposta(vagas.size(), vagasParaIniciantes, porArea, porSenioridade);
    }

    private Map<String, Long> contarPor(List<Vaga> vagas,
                                        java.util.function.Function<Vaga, String> classificador) {
        return vagas.stream().collect(Collectors.groupingBy(
                classificador,
                LinkedHashMap::new,
                Collectors.counting()
        ));
    }
}
