package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.dto.PessoaEntrada;
import br.edu.faculdade.vagas.dto.PessoaResposta;
import br.edu.faculdade.vagas.service.PessoaService;
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
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService service;

    public PessoaController(PessoaService service) {
        this.service = service;
    }

    @GetMapping
    public List<PessoaResposta> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public PessoaResposta buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<PessoaResposta> criar(@Valid @RequestBody PessoaEntrada entrada,
                                                UriComponentsBuilder uriBuilder) {
        PessoaResposta resposta = service.criar(entrada);
        URI localizacao = uriBuilder.path("/pessoas/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(localizacao).body(resposta);
    }

    @PutMapping("/{id}")
    public PessoaResposta atualizar(@PathVariable Long id,
                                    @Valid @RequestBody PessoaEntrada entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
