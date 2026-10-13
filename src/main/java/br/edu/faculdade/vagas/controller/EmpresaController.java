package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.dto.EmpresaEntrada;
import br.edu.faculdade.vagas.dto.EmpresaResposta;
import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService service;

    public EmpresaController(EmpresaService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmpresaResposta> listar() {
        return service.listar();
    }

    @GetMapping("/{slug}")
    public EmpresaResposta buscarPorSlug(@PathVariable String slug) {
        return service.buscarPorSlug(slug);
    }

    @GetMapping("/{slug}/vagas")
    public List<VagaResposta> listarVagas(@PathVariable String slug) {
        return service.listarVagas(slug);
    }

    @PostMapping
    public ResponseEntity<EmpresaResposta> criar(@Valid @RequestBody EmpresaEntrada entrada,
                                                 UriComponentsBuilder uriBuilder) {
        EmpresaResposta resposta = service.criar(entrada);
        URI localizacao = uriBuilder.path("/empresas/{slug}")
                .buildAndExpand(resposta.slug())
                .toUri();
        return ResponseEntity.created(localizacao).body(resposta);
    }

    @PutMapping("/{slug}")
    public EmpresaResposta atualizar(@PathVariable String slug,
                                     @Valid @RequestBody EmpresaEntrada entrada) {
        return service.atualizar(slug, entrada);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> excluir(@PathVariable String slug) {
        service.excluir(slug);
        return ResponseEntity.noContent().build();
    }
}
