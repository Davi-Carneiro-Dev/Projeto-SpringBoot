package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.EmpresaEntrada;
import br.edu.faculdade.vagas.dto.EmpresaResposta;
import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.exception.RecursoConflitanteException;
import br.edu.faculdade.vagas.exception.RecursoNaoEncontradoException;
import br.edu.faculdade.vagas.model.Empresa;
import br.edu.faculdade.vagas.repository.EmpresaRepository;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final VagaRepository vagaRepository;

    public EmpresaService(EmpresaRepository empresaRepository, VagaRepository vagaRepository) {
        this.empresaRepository = empresaRepository;
        this.vagaRepository = vagaRepository;
    }

    @Transactional(readOnly = true)
    public List<EmpresaResposta> listar() {
        return empresaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"))
                .stream()
                .map(EmpresaResposta::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpresaResposta buscarPorSlug(String slug) {
        return EmpresaResposta.de(encontrar(slug));
    }

    @Transactional(readOnly = true)
    public List<VagaResposta> listarVagas(String slug) {
        encontrar(slug);
        return vagaRepository.findByEmpresa_SlugOrderByPublicadaEmDesc(slug)
                .stream()
                .map(VagaResposta::de)
                .toList();
    }

    @Transactional
    public EmpresaResposta criar(EmpresaEntrada entrada) {
        if (empresaRepository.existsBySlug(entrada.slug())) {
            throw new RecursoConflitanteException("Já existe uma empresa com o slug " + entrada.slug() + ".");
        }

        Empresa empresa = new Empresa(
                entrada.nome(),
                entrada.slug(),
                entrada.site(),
                entrada.descricao()
        );
        return EmpresaResposta.de(empresaRepository.save(empresa));
    }

    @Transactional
    public EmpresaResposta atualizar(String slugAtual, EmpresaEntrada entrada) {
        Empresa empresa = encontrar(slugAtual);
        if (empresaRepository.existsBySlugAndIdNot(entrada.slug(), empresa.getId())) {
            throw new RecursoConflitanteException("Já existe uma empresa com o slug " + entrada.slug() + ".");
        }

        empresa.atualizar(entrada.nome(), entrada.slug(), entrada.site(), entrada.descricao());
        return EmpresaResposta.de(empresa);
    }

    @Transactional
    public void excluir(String slug) {
        Empresa empresa = encontrar(slug);
        if (vagaRepository.existsByEmpresa_Slug(slug)) {
            throw new RecursoConflitanteException("A empresa possui vagas e não pode ser excluída.");
        }
        empresaRepository.delete(empresa);
    }

    private Empresa encontrar(String slug) {
        return empresaRepository.findBySlug(slug)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Empresa com slug " + slug + " não encontrada."
                ));
    }
}
