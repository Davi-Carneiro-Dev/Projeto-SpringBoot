package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.dto.CandidaturaEntrada;
import br.edu.faculdade.vagas.dto.CandidaturaResposta;
import br.edu.faculdade.vagas.dto.SituacaoCandidaturaEntrada;
import br.edu.faculdade.vagas.service.CandidaturaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    private final CandidaturaService service;

    public CandidaturaController(CandidaturaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CandidaturaResposta> listar(
            @RequestParam(required = false) Long pessoaId,
            @RequestParam(required = false) Long vagaId) {
        return service.listar(pessoaId, vagaId);
    }

    @GetMapping("/{id}")
    public CandidaturaResposta buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<CandidaturaResposta> criar(
            @Valid @RequestBody CandidaturaEntrada entrada,
            UriComponentsBuilder uriBuilder) {
        CandidaturaResposta resposta = service.criar(entrada);
        URI localizacao = uriBuilder.path("/candidaturas/{id}")
                .buildAndExpand(resposta.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(resposta);
    }

    @PatchMapping("/{id}/situacao")
    public CandidaturaResposta atualizarSituacao(
            @PathVariable Long id,
            @Valid @RequestBody SituacaoCandidaturaEntrada entrada) {
        return service.atualizarSituacao(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
