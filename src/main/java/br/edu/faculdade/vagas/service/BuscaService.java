package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.exception.RequisicaoInvalidaException;
import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class BuscaService {

    private static final Set<String> CAMPOS_ORDENAVEIS = Set.of(
            "titulo", "area", "senioridade", "local", "publicadaEm"
    );

    private final VagaRepository repository;

    public BuscaService(VagaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<VagaResposta> buscar(Area area, Senioridade senioridade, Boolean aceitaIniciante,
                                     String local, String empresaSlug,
                                     LocalDate dataInicio, LocalDate dataFim,
                                     String ordenarPor, String direcao) {
        validar(dataInicio, dataFim, ordenarPor, direcao);
        LocalDateTime publicadaDepoisDe = dataInicio == null ? null : dataInicio.atStartOfDay();
        LocalDateTime publicadaAntesDe = dataFim == null ? null : dataFim.plusDays(1).atStartOfDay();
        Sort ordenacao = Sort.by(Sort.Direction.fromString(direcao), ordenarPor);

        return repository.buscar(
                        area,
                        senioridade,
                        aceitaIniciante,
                        normalizar(local),
                        normalizar(empresaSlug),
                        publicadaDepoisDe,
                        publicadaAntesDe,
                        ordenacao
                )
                .stream()
                .map(VagaResposta::de)
                .toList();
    }

    private void validar(LocalDate dataInicio, LocalDate dataFim,
                         String ordenarPor, String direcao) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new RequisicaoInvalidaException("A data inicial não pode ser posterior à data final.");
        }
        if (!CAMPOS_ORDENAVEIS.contains(ordenarPor)) {
            throw new RequisicaoInvalidaException("Campo de ordenação inválido: " + ordenarPor + ".");
        }
        if (!direcao.equalsIgnoreCase("asc") && !direcao.equalsIgnoreCase("desc")) {
            throw new RequisicaoInvalidaException("A direção deve ser asc ou desc.");
        }
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
