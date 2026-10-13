package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.EstatisticasResposta;
import br.edu.faculdade.vagas.dto.ContagemPorArea;
import br.edu.faculdade.vagas.dto.ContagemPorEmpresa;
import br.edu.faculdade.vagas.dto.ContagemPorSenioridade;
import br.edu.faculdade.vagas.repository.EmpresaRepository;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EstatisticasService {

    private final VagaRepository repository;
    private final EmpresaRepository empresaRepository;

    public EstatisticasService(VagaRepository repository, EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public EstatisticasResposta calcular() {
        Map<String, Long> porArea = new LinkedHashMap<>();
        for (ContagemPorArea contagem : repository.contarPorArea()) {
            porArea.put(contagem.getArea().name(), contagem.getQuantidade());
        }

        Map<String, Long> porSenioridade = new LinkedHashMap<>();
        for (ContagemPorSenioridade contagem : repository.contarPorSenioridade()) {
            porSenioridade.put(contagem.getSenioridade().name(), contagem.getQuantidade());
        }

        Map<String, Long> porEmpresa = new LinkedHashMap<>();
        for (ContagemPorEmpresa contagem : repository.contarPorEmpresa()) {
            porEmpresa.put(contagem.getEmpresaSlug(), contagem.getQuantidade());
        }

        return new EstatisticasResposta(
                empresaRepository.count(),
                repository.count(),
                repository.contarVagasParaIniciantes(),
                porArea,
                porSenioridade,
                porEmpresa
        );
    }
}
