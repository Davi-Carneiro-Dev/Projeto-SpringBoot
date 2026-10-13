package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.VagaEntrada;
import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.exception.RecursoNaoEncontradoException;
import br.edu.faculdade.vagas.model.Empresa;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.EmpresaRepository;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VagaService {

    private final VagaRepository repository;
    private final EmpresaRepository empresaRepository;

    public VagaService(VagaRepository repository, EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public List<VagaResposta> listar() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "publicadaEm"))
                .stream()
                .map(VagaResposta::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public VagaResposta buscarPorId(Long id) {
        return VagaResposta.de(encontrar(id));
    }

    @Transactional
    public VagaResposta criar(VagaEntrada entrada) {
        Vaga vaga = novaVaga(entrada);
        return VagaResposta.de(repository.save(vaga));
    }

    @Transactional
    public VagaResposta atualizar(Long id, VagaEntrada entrada) {
        Vaga vaga = encontrar(id);
        Empresa empresa = encontrarEmpresa(entrada.empresaSlug());
        vaga.atualizar(
                entrada.titulo(),
                entrada.descricao(),
                entrada.area(),
                entrada.senioridade(),
                entrada.local(),
                entrada.aceitaIniciante(),
                empresa
        );
        return VagaResposta.de(vaga);
    }

    @Transactional
    public void excluir(Long id) {
        Vaga vaga = encontrar(id);
        repository.delete(vaga);
    }

    private Vaga encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga " + id + " não encontrada."));
    }

    private Vaga novaVaga(VagaEntrada entrada) {
        Empresa empresa = encontrarEmpresa(entrada.empresaSlug());
        return new Vaga(
                entrada.titulo(),
                entrada.descricao(),
                entrada.area(),
                entrada.senioridade(),
                entrada.local(),
                entrada.aceitaIniciante(),
                empresa
        );
    }

    private Empresa encontrarEmpresa(String slug) {
        return empresaRepository.findBySlug(slug)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Empresa com slug " + slug + " não encontrada."
                ));
    }
}
