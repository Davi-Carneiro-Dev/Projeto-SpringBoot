package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.dto.VagaEntrada;
import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.service.VagaService;
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
@RequestMapping("/vagas")
public class VagaController {

    private final VagaService service;

    public VagaController(VagaService service) {
        this.service = service;
    }

    @GetMapping
    public List<VagaResposta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public VagaResposta buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<VagaResposta> criar(@Valid @RequestBody VagaEntrada entrada,
                                              UriComponentsBuilder uriBuilder) {
        VagaResposta resposta = service.criar(entrada);
        URI localizacao = uriBuilder.path("/vagas/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(localizacao).body(resposta);
    }

    @PutMapping("/{id}")
    public VagaResposta atualizar(@PathVariable Long id,
                                  @Valid @RequestBody VagaEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
