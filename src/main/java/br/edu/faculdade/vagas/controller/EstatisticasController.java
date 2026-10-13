package br.edu.faculdade.vagas.controller;

import br.edu.faculdade.vagas.dto.EstatisticasResposta;
import br.edu.faculdade.vagas.service.EstatisticasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatisticas")
public class EstatisticasController {

    private final EstatisticasService service;

    public EstatisticasController(EstatisticasService service) {
        this.service = service;
    }

    @GetMapping
    public EstatisticasResposta consultar() {
        return service.calcular();
    }
}
