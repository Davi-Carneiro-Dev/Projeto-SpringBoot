package br.edu.faculdade.vagas.service;

import br.edu.faculdade.vagas.dto.VagaEntrada;
import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.dto.PaginaResposta;
import br.edu.faculdade.vagas.exception.RequisicaoInvalidaException;
import br.edu.faculdade.vagas.exception.RecursoNaoEncontradoException;
import br.edu.faculdade.vagas.model.Empresa;
import br.edu.faculdade.vagas.model.Vaga;
import br.edu.faculdade.vagas.repository.EmpresaRepository;
import br.edu.faculdade.vagas.repository.VagaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class VagaService {

    private static final Set<String> CAMPOS_ORDENAVEIS = Set.of(
            "id", "titulo", "area", "senioridade", "local", "publicadaEm"
    );

    private final VagaRepository repository;
    private final EmpresaRepository empresaRepository;

    public VagaService(VagaRepository repository, EmpresaRepository empresaRepository) {
        this.repository = repository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional(readOnly = true)
    public PaginaResposta<VagaResposta> listar(int pagina, int tamanho,
                                               String ordenarPor, String direcao) {
        validarPaginacao(pagina, tamanho, ordenarPor, direcao);
        Sort ordenacao = Sort.by(Sort.Direction.fromString(direcao), ordenarPor);
        Page<VagaResposta> resultado = repository.findAll(PageRequest.of(pagina, tamanho, ordenacao))
                .map(VagaResposta::de);
        return PaginaResposta.de(resultado);
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

    private void validarPaginacao(int pagina, int tamanho, String ordenarPor, String direcao) {
        if (pagina < 0) {
            throw new RequisicaoInvalidaException("A página não pode ser negativa.");
        }
        if (tamanho < 1 || tamanho > 100) {
            throw new RequisicaoInvalidaException("O tamanho deve estar entre 1 e 100.");
        }
        if (!CAMPOS_ORDENAVEIS.contains(ordenarPor)) {
            throw new RequisicaoInvalidaException("Campo de ordenação inválido: " + ordenarPor + ".");
        }
        if (!direcao.equalsIgnoreCase("asc") && !direcao.equalsIgnoreCase("desc")) {
            throw new RequisicaoInvalidaException("A direção deve ser asc ou desc.");
        }
    }
}
