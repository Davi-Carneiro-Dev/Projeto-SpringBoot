package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.PessoaEntrada;
import br.edu.faculdade.vagas.dto.PessoaResposta;
import br.edu.faculdade.vagas.exception.RecursoConflitanteException;
import br.edu.faculdade.vagas.exception.RecursoNaoEncontradoException;
import br.edu.faculdade.vagas.model.Pessoa;
import br.edu.faculdade.vagas.repository.CandidaturaRepository;
import br.edu.faculdade.vagas.repository.PessoaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class PessoaService {

    private final PessoaRepository repository;
    private final CandidaturaRepository candidaturaRepository;

    public PessoaService(PessoaRepository repository, CandidaturaRepository candidaturaRepository) {
        this.repository = repository;
        this.candidaturaRepository = candidaturaRepository;
    }

    @Transactional(readOnly = true)
    public List<PessoaResposta> listar() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"))
                .stream()
                .map(PessoaResposta::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public PessoaResposta buscarPorId(Long id) {
        return PessoaResposta.de(encontrar(id));
    }

    @Transactional
    public PessoaResposta criar(PessoaEntrada entrada) {
        String email = normalizarEmail(entrada.email());
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new RecursoConflitanteException("Já existe uma pessoa cadastrada com este e-mail.");
        }

        Pessoa pessoa = new Pessoa(entrada.nome().trim(), email, entrada.perfil());
        return PessoaResposta.de(repository.save(pessoa));
    }

    @Transactional
    public PessoaResposta atualizar(Long id, PessoaEntrada entrada) {
        Pessoa pessoa = encontrar(id);
        String email = normalizarEmail(entrada.email());
        if (repository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new RecursoConflitanteException("Já existe uma pessoa cadastrada com este e-mail.");
        }

        pessoa.atualizar(entrada.nome().trim(), email, entrada.perfil());
        return PessoaResposta.de(pessoa);
    }

    @Transactional
    public void excluir(Long id) {
        Pessoa pessoa = encontrar(id);
        if (candidaturaRepository.existsByPessoaId(id)) {
            throw new RecursoConflitanteException("A pessoa possui candidaturas e não pode ser excluída.");
        }
        repository.delete(pessoa);
    }

    private Pessoa encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa " + id + " não encontrada."));
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
