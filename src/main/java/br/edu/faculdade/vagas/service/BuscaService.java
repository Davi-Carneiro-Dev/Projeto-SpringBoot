package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuscaService {

    private final VagaRepository repository;

    public BuscaService(VagaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<VagaResposta> buscar(Area area, Senioridade senioridade, Boolean aceitaIniciante) {
        return repository.buscar(area, senioridade, aceitaIniciante)
                .stream()
                .map(VagaResposta::de)
                .toList();
    }
}
