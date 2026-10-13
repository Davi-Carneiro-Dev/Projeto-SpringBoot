package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.CandidaturaEntrada;
import br.edu.faculdade.vagas.dto.CandidaturaResposta;
import br.edu.faculdade.vagas.dto.SituacaoCandidaturaEntrada;
import br.edu.faculdade.vagas.exception.RecursoConflitanteException;
import br.edu.faculdade.vagas.exception.RecursoNaoEncontradoException;
import br.edu.faculdade.vagas.model.Candidatura;
import br.edu.faculdade.vagas.model.Pessoa;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.CandidaturaRepository;
import br.edu.faculdade.vagas.repository.PessoaRepository;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CandidaturaService {

    private final CandidaturaRepository repository;
    private final PessoaRepository pessoaRepository;
    private final VagaRepository vagaRepository;

    public CandidaturaService(CandidaturaRepository repository,
                              PessoaRepository pessoaRepository,
                              VagaRepository vagaRepository) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.vagaRepository = vagaRepository;
    }

    @Transactional(readOnly = true)
    public List<CandidaturaResposta> listar(Long pessoaId, Long vagaId) {
        return repository.buscar(pessoaId, vagaId)
                .stream()
                .map(CandidaturaResposta::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public CandidaturaResposta buscarPorId(Long id) {
        return CandidaturaResposta.de(encontrar(id));
    }

    @Transactional
    public CandidaturaResposta criar(CandidaturaEntrada entrada) {
        Pessoa pessoa = pessoaRepository.findById(entrada.pessoaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pessoa " + entrada.pessoaId() + " não encontrada."
                ));
        Vaga vaga = vagaRepository.findById(entrada.vagaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vaga " + entrada.vagaId() + " não encontrada."
                ));

        if (repository.existsByPessoaIdAndVagaId(pessoa.getId(), vaga.getId())) {
            throw new RecursoConflitanteException("Esta pessoa já se candidatou a esta vaga.");
        }

        return CandidaturaResposta.de(repository.save(new Candidatura(pessoa, vaga)));
    }

    @Transactional
    public CandidaturaResposta atualizarSituacao(Long id, SituacaoCandidaturaEntrada entrada) {
        Candidatura candidatura = encontrar(id);
        candidatura.atualizarSituacao(entrada.situacao());
        return CandidaturaResposta.de(candidatura);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(encontrar(id));
    }

    private Candidatura encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Candidatura " + id + " não encontrada."
                ));
    }
}
