package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.dto.VagaResposta;
import br.edu.faculdade.vagas.model.Area;
import br.edu.faculdade.vagas.model.Senioridade;
import br.edu.faculdade.vagas.service.BuscaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vagas")
public class BuscaController {

    private final BuscaService service;

    public BuscaController(BuscaService service) {
        this.service = service;
    }

    @GetMapping("/busca")
    public List<VagaResposta> buscar(
            @RequestParam(required = false) Area area,
            @RequestParam(required = false) Senioridade senioridade,
            @RequestParam(required = false) Boolean aceitaIniciante) {
        return service.buscar(area, senioridade, aceitaIniciante);
    }
}
